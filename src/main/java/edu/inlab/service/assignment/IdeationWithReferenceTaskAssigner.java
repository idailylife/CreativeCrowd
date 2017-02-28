package edu.inlab.service.assignment;

import edu.inlab.models.*;
import edu.inlab.service.MicroTaskService;
import edu.inlab.service.TaskService;
import edu.inlab.service.UserMicrotaskService;
import edu.inlab.utils.BlobObjectConv;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Array;
import java.sql.Blob;
import java.util.*;

/**
 * Created by hebowei on 2017/1/19.
 * 可附带方案参考的创意设计方案提交任务
 *
 * params = {
 *     "showParent": true/false,                    (valid iff refSize>0)
 *     "refSize": size of reference item (set),     (valid iff refSize>0)
 *     "nonRefTaskAllocation": size of remaining non-ref tasks to allocate
 *              The assigning priority of such tasks are higher than referenced ones!
 *     "nonRefTaskTotalCnt": count of all non-ref tasks (first round, excluding not-claimed ones in nonRefTaskAllocation)
 *     "refColNames": "aaa,bbb,ccc"
 * }
 *
 *
 * 如果refSize > 0，则新建的UserMicrotask实例中，results属性会预先填入参考项
 *
 * results = {
 *     *refItems: [
 *          {
 *              umtId : 参考项的UserMicrotask ID,
 *              *parentUmtId: 参考项父节点的UserMicrotask ID (可选)
 *          }, ...
 *
 *     ],    //可选，如果有参考项
 *     selectedRefId: SOME_ID or this.id,   //如果没有参考项，则设置为自己的usermicrotask id
 *     ...
 * }
 *
 * Reference的解析使用渲染器里的方法渲染，此处只负责分发
 */
@Component
@ComponentScan(basePackages = "edu.inlab")
public class IdeationWithReferenceTaskAssigner extends MicroTaskAssigner {
    @Autowired
    TaskService taskService;

    @Autowired
    MicroTaskService microTaskService;

    @Autowired
    UserMicrotaskService userMicrotaskService;

    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public boolean supportAssignUserMicroTask() {
        return true;
    }

    @Override
    public Microtask assignNext(UserTask userTask) throws RuntimeException {
        return null;
    }

    @Override
    public Microtask assignCurrent(UserTask userTask) throws RuntimeException {
        return null;
    }

    @Override
    public UserMicroTask assignNextUMT(UserTask userTask) throws RuntimeException {
        Task task = taskService.findById(userTask.getTaskId());
        Microtask microtask = microTaskService.getUniqueByTask(task);

        JSONObject taskParams = new JSONObject(task.getParams());
        UserMicroTask userMicroTask = new UserMicroTask(userTask.getId(), microtask.getId());
        int refSize = taskParams.getInt("refSize");

        userMicroTask.setMicrotaskId(microTaskService.getUniqueByTask(task).getId());

        int nonRefTaskAllo = taskParams.getInt("nonRefTaskAllocation");
        if(nonRefTaskAllo > 0){
            taskParams.put("nonRefTaskAllocation", --nonRefTaskAllo);
            taskParams.put("nonRefTaskTotalCnt", taskParams.getInt("nonRefTaskTotalCnt")+1);
            task.setParams(taskParams.toString());
            taskService.updateTask(task);
            refSize = 0;
        }

        if(refSize > 0){
            // Reference enabled

            IdeationTaskConfig taskConfig = IdeationTaskConfig.ReadFromBlob(task.getConfigBlob());
            Set<Integer> topRankedUmtIds = taskConfig.getTopRankedItems(refSize);

            JSONObject umtResults = new JSONObject();
            JSONArray refAryJson = new JSONArray();
            for(Integer refUmtId:topRankedUmtIds){
                JSONObject refUmtSet = new JSONObject();
                refUmtSet.put("umtId", refUmtId);
                if(taskParams.getBoolean("showParent")){
                    UserMicroTask refUmt = userMicrotaskService.getById(refUmtId);
                    refUmtSet.put("parentUmtId", Integer.parseInt(refUmt.getMetaInfo()));
                }
                refAryJson.put(refUmtSet);
            }
            umtResults.put("refItems", refAryJson);
            userMicroTask.setResults(umtResults);
        }
        return userMicroTask;
    }

    @Override
    public void onUserMicrotaskSubmit(UserMicroTask userMicroTask, Task task) {
        //TODO: Update score in config file according to visiting count
        JSONObject results = userMicroTask.getResults();
        if(!results.has("selectedRefId")){
            results.put("selectedRefId", userMicroTask.getId()); //没有选择参考项，则设置参考项为自身
        }
        userMicroTask.setResults(results);
        userMicrotaskService.update(userMicroTask);
    }

    @Override
    public boolean supportConfigFile() {
        return true;
    }

    /**
     * Update config file
     * @param taskBlob Task.configBlob object
     * @param inputs only one item, a JSONArray object
     * @return Is update successful? : true/false
     */
    @Override
    public Blob updateConfigFile(Blob taskBlob, Object... inputs) {
        // inputs[0] is a JSONArray, the first row contains column info
        //TODO: Beware of task data sync problem!!!
        JSONArray data2DAry = (JSONArray)inputs[0];
        // First line, read column info
        if(data2DAry.length() < 1)
            return null;
        // Read column info
        JSONArray colRow = data2DAry.getJSONArray(0);
        int idColIndex = -1, scoreColIndex = -1;
        for(int col=0; col<colRow.length(); col++){
            String colName = colRow.getString(col);
            if(colName.equals("id")){
                idColIndex = col;
            } else if(colName.equals("score")){
                scoreColIndex = col;
            }
        }
        // Touch Blob data
        IdeationTaskConfig taskConfig = null;
        if(taskBlob == null){
            //create new config
            taskConfig = new IdeationTaskConfig();
        } else {
            taskConfig = IdeationTaskConfig.ReadFromBlob(taskBlob);
        }

        // Read data
        for(int row=1; row < data2DAry.length(); row++){
            JSONArray dataRow = data2DAry.getJSONArray(row);
            int umtId = dataRow.getInt(idColIndex);
            double score = dataRow.getDouble(scoreColIndex);
            taskConfig.setItem(umtId, score);
        }

        // Write data
        taskBlob = BlobObjectConv.ObjectToBlob(taskConfig);

        return taskBlob;
    }

    private JSONObject getTaskParams(Task task){
        return new JSONObject(task.getParams());
    }

    @Override
    public ByteArrayOutputStream exportConfigFile(Task task) {

        JSONObject taskParams = getTaskParams(task);
        List<String> headers = new ArrayList<>();
        headers.add("id");
        headers.add("refId");
        headers.add("score");   // 用来排序的分数

        String[] appendHeaders = {"imageDesc", "textDesc"};
        if(taskParams.has("refColNames")){
            appendHeaders = taskParams.getString("refColNames").split(",");
        }
        Collections.addAll(headers, appendHeaders);

        Blob configBlob = task.getConfigBlob();
        Map<Integer, Double> scoreMap = null;
        if(configBlob != null){
            IdeationTaskConfig ideationTaskConfig = IdeationTaskConfig.ReadFromBlob(configBlob);
            scoreMap = ideationTaskConfig.getScoreMap();
        }


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try(Writer out = new BufferedWriter(new OutputStreamWriter(baos));){
            CSVFormat csvFormat = CSVFormat.EXCEL.withHeader(headers.toArray(new String[headers.size()]));
            CSVPrinter csvPrinter = new CSVPrinter(out, csvFormat);
            List<UserMicroTask> userMicroTasks = userMicrotaskService.getByTaskId(task.getId());
            //http://blog.csdn.net/theonegis/article/details/49912633
            for(UserMicroTask umt : userMicroTasks){
                List<String> record = new ArrayList<>();
                JSONObject umtResults = umt.getResults();
                if(!umtResults.has("selectedRefId")){
                    continue;   //unfinished umt
                }

                for(String header : headers){
                    if(header.equals("id")){
                        record.add(umt.getId().toString());
                    } else if(header.equals("refId")){
                        if(umtResults.getString("selectedRefId").equals(String.valueOf(umt.getId())))
                            record.add("NONE");
                        else
                            record.add(umtResults.getString("selectedRefId"));
                    } else if(header.equals("score")){
                        //TODO: 读取配置文件信息
                        if(configBlob == null){
                            record.add("NO_CONFIG_FILE");
                        } else {
                            record.add(scoreMap.getOrDefault(umt.getId(), -1.0).toString());
                        }
                    } else {
                        record.add(umtResults.getString(header));
                    }
                }
                csvPrinter.printRecord(record);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return baos;
    }

}
