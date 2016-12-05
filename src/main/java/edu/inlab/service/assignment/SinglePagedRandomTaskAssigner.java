package edu.inlab.service.assignment;

import edu.inlab.models.Microtask;
import edu.inlab.models.Task;
import edu.inlab.models.UserMicroTask;
import edu.inlab.models.UserTask;
import edu.inlab.service.MicroTaskService;
import edu.inlab.service.TaskService;
import edu.inlab.service.UserMicrotaskService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
 *     "candidates":
 *      [
 *          {"image": 内部图片文件名, "text":文字描述}, ...
 *      ]
 * }
 *
 * params里的candidates生成的事情交给前端做，加个按钮. 读取csv或是excel.
 */
@Component
@ComponentScan(basePackages = "edu.inlab")
public class SinglePagedRandomTaskAssigner implements MicroTaskAssigner {
    @Autowired
    TaskService taskService;

    @Autowired
    MicroTaskService microTaskService;

    @Autowired
    UserMicrotaskService userMicrotaskService;

    @Override
    public boolean isTransient() {
        return true;
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
            JSONArray candidates = taskParams.getJSONArray("candidates");

            List<Integer> selectedIndices = reserviorSample(candidates.length()
                    , taskParams.getInt("N")+1);    // 1 reference + N candidates
            Collections.shuffle(selectedIndices);

            JSONArray templateJson = new JSONArray();
            JSONObject tempObj = new JSONObject();
            tempObj.put("mtask_size", taskParams.getInt("mtask_size"));
            templateJson.put(tempObj);
            tempObj = new JSONObject();
            tempObj.put("N", taskParams.getInt("N"));
            templateJson.put(tempObj);
            tempObj = new JSONObject();
            tempObj.put("K", taskParams.getInt("K"));
            templateJson.put(tempObj);
            tempObj = new JSONObject();
            tempObj.put("nRows", taskParams.getInt("nRows"));
            templateJson.put(tempObj);

            tempObj = new JSONObject();
            tempObj.put("ref_item", candidates.getJSONObject(selectedIndices.get(0)));
            templateJson.put(tempObj);

            //templateJson.put("ref_item", candidates.getJSONObject(selectedIndices.get(0)));
            for(int i=1; i<selectedIndices.size(); i++){
                tempObj = new JSONObject();
                tempObj.put("item", candidates.getJSONObject(selectedIndices.get(i)));
                templateJson.put(tempObj);
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

    private List<Integer> reserviorSample(int N, int k){
        List<Integer> results = new ArrayList<>(k);
        Random random = new Random();
        for(int i=0; i<N; i++){
            if(i < k)
                results.add(i);
            else {
                int next = random.nextInt(i+1);
                if(next < k)
                    results.set(next, i);
            }
        }
        return results;
    }

    @Override
    public void onUserMicrotaskSubmit(UserMicroTask userMicroTask, Task task) {

    }
}
