package edu.inlab.service.assignment;

import edu.inlab.models.Microtask;
import edu.inlab.models.Task;
import edu.inlab.models.UserTask;
import edu.inlab.service.MicroTaskService;
import edu.inlab.service.TaskService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * 按照顺序发放的Microtask分配器、
 * 会根据Microtask对象的nextId域分配下一项任务
 * Created by inlab-dell on 2016/6/16.
 */
@Component
@ComponentScan(basePackages = "edu.inlab")
public class SequenceMicroTaskAssigner implements MicroTaskAssigner {
    @Autowired
    MicroTaskService microTaskService;

    @Autowired
    TaskService taskService;

    @Override
    public Microtask assignNext(UserTask userTask) throws RuntimeException {
        Integer currMTaskId = userTask.getCurrUserMicrotaskId();
        Task task = taskService.findById(userTask.getTaskId());
        if(currMTaskId != null){
            Microtask currMTask = microTaskService.getById(currMTaskId);
//            if(currMTask.getNextId() != null)
//                return microTaskService.getById(currMTask.getNextId());
            JSONArray mTaskArray = new JSONArray(task.getParams());
            int i;
            for(i=0; i<mTaskArray.length(); i++){
                int mtId = mTaskArray.getInt(i);
                if(currMTask.getId().equals(mtId))
                    break;
            }
            if(i+1 < mTaskArray.length()){
                return microTaskService.getById(mTaskArray.getInt(i+1));
            }
        }
        return null;
    }

    @Override
    public void onMicrotaskDelete(Microtask microtask) {
        //删除params里面相关的id
        Task task = microtask.getTask();
        JSONArray paramJsonAry = new JSONArray(task.getParams());
        String strMTaskId = String.valueOf(microtask.getId());
        int i = -1;
        for(i=0; i<paramJsonAry.length(); i++){
            if(paramJsonAry.get(i) instanceof String){
                if(paramJsonAry.getString(i).equals(strMTaskId)){
                    break;
                }
            }
        }
        if(i > 0)
            paramJsonAry.remove(i);
        task.setParams(paramJsonAry.toString());
        taskService.updateTask(task);
    }

    @Override
    public void onMicrotaskCreate(Microtask microtask) {

    }
}
