package edu.inlab.service.assignment;

import edu.inlab.models.Microtask;
import edu.inlab.models.Task;
import edu.inlab.models.UserMicroTask;
import edu.inlab.models.UserTask;
import edu.inlab.service.MicroTaskService;
import edu.inlab.service.TaskService;
import edu.inlab.web.exception.TaskAssignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by inlab-dell on 2016/5/31.
 * 随机分配
 */

@Component
@ComponentScan(basePackages = "edu.inlab")
public class RandomMicroTaskAssigner implements MicroTaskAssigner {

    @Autowired
    TaskService taskService;

    @Autowired
    MicroTaskService microTaskService;

    @Transactional
    public Microtask assignNext(UserTask userTask) {
        Task task = taskService.findById(userTask.getTaskId());
        Integer microtaskSize = Integer.parseInt(task.getParams());
        List<Microtask> relatedMicrotasks = task.getRelatedMictorasks();

        Random random = new Random();
        int index = random.nextInt(relatedMicrotasks.size());
        if(userTask.getCurrUserMicrotaskId() == null){
            //New task assignment
            return relatedMicrotasks.get(index);
        } else {
            List<UserMicroTask> userMicroTasks = userTask.getRelatedUserMicrotasks();
            if(userMicroTasks.size() >= microtaskSize){
                return null; //Task finished
            }

            Set<Integer> existingMTaskIds = getFinishedMicrotaskIds(userMicroTasks);
            Set<Integer> allMTaskIds = getMicrotaskIdsFromList(relatedMicrotasks);
            allMTaskIds.removeAll(existingMTaskIds);    //Set A - B
            index -= existingMTaskIds.size();
            if(allMTaskIds.isEmpty()){
                throw new TaskAssignException("Microtask resource exhausted for task #" + task.getId() + "& UserTask #"
                + userTask.getId());
            }
            //return (Microtask) allMTaskIds.toArray()[index];
            Integer candidateMTaskId = (Integer) allMTaskIds.toArray()[index];
            return microTaskService.getById(candidateMTaskId);
        }

    }

    Set<Integer> getFinishedMicrotaskIds(List<UserMicroTask> userMicroTasks){
        Set<Integer> microTaskIds = new HashSet<Integer>();
        for(UserMicroTask userMicroTask:userMicroTasks){
            microTaskIds.add(userMicroTask.getMicrotaskId());
        }
        return microTaskIds;
    }

    Set<Integer> getMicrotaskIdsFromList(List<Microtask> microtasks){
        Set<Integer> ids = new HashSet<Integer>();
        for(Microtask microtask:microtasks){
            ids.add(microtask.getId());
        }
        return ids;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}
