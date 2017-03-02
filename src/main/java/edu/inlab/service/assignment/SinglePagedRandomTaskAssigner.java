package edu.inlab.service.assignment;

import edu.inlab.models.Microtask;
import edu.inlab.models.Task;
import edu.inlab.models.UserMicroTask;
import edu.inlab.models.UserTask;
import edu.inlab.service.MicroTaskService;
import edu.inlab.service.TaskService;
import edu.inlab.service.UserMicrotaskService;
import edu.inlab.utils.GeneralUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by inlab-dell on 2016/12/2.
 * 单页随机任务分配器
 * 页面模板固定，但是内容物是随机分配的
 * 目前供给Grid-choice Render使用
 *
 * params = {
 *     "mtask_size": 一个任务包含的问题数量,
 *     "N": N个候选项,
 *     "K": 选出K个,
 *     "nRows": 显示的行数,
 *     "use_gold_std": 是否使用黄金标准问题,  //optional, default is false
 *     "candidates":
 *      [
 *          {"image": 内部图片文件名, "text":文字描述}, ...
 *      ],
 *      "gold_std_q":   //optional
 *      [
 *          ref: ID_REF_ITEM,
 *          cand: [ID_CANDIDATE,...],
 *          ans: [ID_ANSWER,...]
 *      ]
 * }
 *
 * params里的candidates生成的事情交给前端做，加个按钮. 读取csv或是excel.
 */
@Component
@ComponentScan(basePackages = "edu.inlab")
public class SinglePagedRandomTaskAssigner extends MicroTaskAssigner {
    @Autowired
    TaskService taskService;

    @Autowired
    MicroTaskService microTaskService;

    @Autowired
    UserMicrotaskService userMicrotaskService;

    static GoldenStandardQuestionCache gsQuestionCache = null;

    @Override
    public boolean isTransient() {
        return true;
    }

    @Override
    public boolean supportAssignUserMicroTask() {
        return false;
    }

    @Override
    public UserMicroTask assignNextUMT(UserTask userTask) throws RuntimeException {
        return null;
    }

    @Override
    public Microtask assignCurrent(UserTask userTask) throws RuntimeException {
        return assign(userTask, false);
    }

    @Override
    public Microtask assignNext(UserTask userTask) throws RuntimeException {
        return assign(userTask, true);
    }

    private Microtask assign(UserTask userTask, boolean assignNext){
        Task task = taskService.findById(userTask.getTaskId());
        JSONObject taskParams = new JSONObject(task.getParams());
        Long microtaskSizeLimit = taskParams.getLong("mtask_size");
        Long userMtaskCount = userMicrotaskService.getCountByUserTaskId(userTask.getId());

        if(assignNext && (userMtaskCount >= microtaskSizeLimit))
            return null;

        Microtask microtaskToRender = microTaskService.getUniqueByTask(task);
        //task.getRelatedMictorasks().get(0);

        if(!assignNext){
            JSONArray templateJson = new JSONArray();
            JSONObject tempObj = new JSONObject();
            tempObj.put("mtask_size", taskParams.getInt("mtask_size"));
            templateJson.put(tempObj);
            tempObj = new JSONObject();
            int param_N = taskParams.getInt("N");
            tempObj.put("N", param_N);

            templateJson.put(tempObj);
            tempObj = new JSONObject();
            tempObj.put("K", taskParams.getInt("K"));
            templateJson.put(tempObj);
            tempObj = new JSONObject();
            tempObj.put("nRows", taskParams.getInt("nRows"));
            templateJson.put(tempObj);

            if(taskParams.has("freeChoice")){
                tempObj = new JSONObject();
                tempObj.put("freeChoice", taskParams.getBoolean("freeChoice"));
                templateJson.put(tempObj);
            }

            tempObj = new JSONObject();
            float progress = 100*(userMtaskCount==0?1:userMtaskCount)/(float)microtaskSizeLimit;
            tempObj.put("progress", String.format("%.1f", progress)+"%");
            templateJson.put(tempObj);

            JSONArray candidates = taskParams.getJSONArray("candidates");
            if(taskParams.has("use_gold_std") && taskParams.getBoolean("use_gold_std")
                    && userMtaskCount.equals(microtaskSizeLimit)){
                //The last question should be gold standard question
                if(gsQuestionCache == null)
                    gsQuestionCache = new GoldenStandardQuestionCache();

                JSONArray goldStdQuestins = taskParams.getJSONArray("gold_std_q");
                int ind_gs_question = new Random().nextInt(goldStdQuestins.length());
                JSONObject gs_item = goldStdQuestins.getJSONObject(ind_gs_question);
                tempObj = new JSONObject();
                tempObj.put("ref_item", gsQuestionCache.getItem(task, gs_item.getString("ref")));
                templateJson.put(tempObj);

                JSONArray gs_candidates= gs_item.getJSONArray("cand");
                List<Integer> rand_indices = new ArrayList<>(param_N);
                for(int i=0; i<param_N; i++)
                    rand_indices.add(i);
                Collections.shuffle(rand_indices);

                for(int i=0; i<gs_candidates.length(); i++){
                    tempObj = new JSONObject();
                    tempObj.put("item", gsQuestionCache.getItem(task,
                            gs_candidates.getString(rand_indices.get(i))));
                    templateJson.put(tempObj);
                }
            } else {
                //Normal question
                List<Integer> selectedIndices = GeneralUtils.reserviorSample(candidates.length()
                        , param_N +1);    // 1 reference + N candidates
                tempObj = new JSONObject();
                tempObj.put("ref_item", candidates.getJSONObject(selectedIndices.get(0)));
                templateJson.put(tempObj);

                //templateJson.put("ref_item", candidates.getJSONObject(selectedIndices.get(0)));

                for(int i=1; i<selectedIndices.size(); i++){
                    tempObj = new JSONObject();
                    tempObj.put("item", candidates.getJSONObject(selectedIndices.get(i)));
                    templateJson.put(tempObj);
                }
            }


            /*
        * Push items into task template
        * */
            microtaskToRender.setTemplate(templateJson.toString());
        }


        if(assignNext && (userMtaskCount < microtaskSizeLimit - 1))
            microtaskToRender.setNextId(-1); //fake id
        else if((!assignNext) && (userMtaskCount < microtaskSizeLimit))
            microtaskToRender.setNextId(-1);

        return microtaskToRender;
    }

//    private List<Integer> reserviorSample(int N, int k){
//        List<Integer> results = new ArrayList<>(k);
//        Random random = new Random();
//        for(int i=0; i<N; i++){
//            if(i < k)
//                results.add(i);
//            else {
//                int next = random.nextInt(i+1);
//                if(next < k)
//                    results.set(next, i);
//            }
//        }
//        Collections.sort(results);
//        return results;
//    }

    @Override
    public void onUserMicrotaskSubmit(UserMicroTask userMicroTask, Task task) {

    }

    public class GoldenStandardQuestionCache {
        //Cache for golden standard questions
        private int taskId;
        private Map<String, JSONObject> cache;

        public GoldenStandardQuestionCache(){
            taskId = -1;
        }

        JSONObject getItem(Task task, String key){
            if (task.getId() != taskId){
                //Rebuild cache
                taskId = task.getId();
                JSONObject taskParams = new JSONObject(task.getParams());
                JSONArray items = taskParams.getJSONArray("candidates");
                cache = new HashMap<>();
                for(int i=0; i<items.length(); i++){
                    JSONObject item = items.getJSONObject(i);
                    cache.put(item.getString("image"), item);
                }
            }
            return cache.get(key);
        }
    }
}
