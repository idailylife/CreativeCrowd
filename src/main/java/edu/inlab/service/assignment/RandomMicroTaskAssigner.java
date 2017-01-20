package edu.inlab.service.assignment;

import edu.inlab.models.Microtask;
import edu.inlab.models.Task;
import edu.inlab.models.UserMicroTask;
import edu.inlab.models.UserTask;
import edu.inlab.service.MicroTaskService;
import edu.inlab.service.TaskService;
import edu.inlab.web.exception.TaskAssignException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;

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

    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public Microtask assignCurrent(UserTask userTask) throws RuntimeException {
        return null;
    }

    @Override
    public boolean supportAssignUserMicroTask() {
        return false;
    }

    @Override
    public UserMicroTask assignNextUMT(UserTask userTask) throws RuntimeException {
        return null;
    }

    @Transactional
    public Microtask assignNext(UserTask userTask) {
        Task task = taskService.findById(userTask.getTaskId());
        //Integer microtaskSize = Integer.parseInt(task.getParams());
        List<Microtask> relatedMicrotasks = task.getRelatedMictorasks();
        Map<Integer, Microtask> mTaskIdMap = getMicrotaskMapFromList(relatedMicrotasks);

        RandomParams randomParams = new RandomParams(task.getParams());
        Set<Integer> microTaskIdsToAssign = getMicrotaskIdsFromList(relatedMicrotasks);
        if(randomParams.finishedCount < relatedMicrotasks.size()){
            //如果还有尚未分配的任务，可选的Id里去除已分配的
            Set<Integer> assignedMTaskIds = randomParams.getFinishedMTaskIds();
            if(assignedMTaskIds != null)
                microTaskIdsToAssign.removeAll(assignedMTaskIds);
        }

        if(microTaskIdsToAssign.isEmpty()){
            throw new TaskAssignException("[E01]Microtask resource exhausted for task #" + task.getId());
        }

        Random random = new Random();
        if(userTask.getCurrUserMicrotaskId() == null){
            //从备选id中分配新Microtask
            Integer nextId = microTaskIdsToAssign.iterator().next();
            return mTaskIdMap.get(nextId);
        } else {
            List<UserMicroTask> userMicroTasks = userTask.getRelatedUserMicrotasks();
            if(userMicroTasks.size() >= randomParams.getRandSize()){
                return null; //Task finished
            }
            Set<Integer> existingMTaskIds = getFinishedMicrotaskIds(userMicroTasks);
            if(existingMTaskIds != null)
                microTaskIdsToAssign.removeAll(existingMTaskIds);
            if(microTaskIdsToAssign.isEmpty()){
                throw new TaskAssignException("Microtask resource exhausted for task #" + task.getId() + "& UserTask #"
                        + userTask.getId());
            }
            int index = random.nextInt(microTaskIdsToAssign.size());
            Integer candidateId = (Integer)microTaskIdsToAssign.toArray()[index];
            return mTaskIdMap.get(candidateId);
        }

    }

    private Set<Integer> getFinishedMicrotaskIds(List<UserMicroTask> userMicroTasks){
        Set<Integer> microTaskIds = new HashSet<Integer>();
        for(UserMicroTask userMicroTask:userMicroTasks){
            microTaskIds.add(userMicroTask.getMicrotaskId());
        }
        return microTaskIds;
    }

    private Set<Integer> getMicrotaskIdsFromList(List<Microtask> microtasks){
        Set<Integer> ids = new HashSet<Integer>();
        for(Microtask microtask:microtasks){
            ids.add(microtask.getId());
        }
        return ids;
    }

    private Map<Integer, Microtask> getMicrotaskMapFromList(List<Microtask> microtaskList){
        Map<Integer, Microtask> map = new HashMap<>();
        for(Microtask mtask:microtaskList){
            map.put(mtask.getId(), mtask);
        }
        return map;
    }



    /**
     * Update task params when some user finishes some microtask
     * @param userMicroTask
     * @param task
     */
    @Override
    public void onUserMicrotaskSubmit(UserMicroTask userMicroTask, Task task) {
        RandomParams randomParams = new RandomParams(task.getParams());
        randomParams.finishedCount++;
        randomParams.finishedMTaskIds.add(userMicroTask.getMicrotaskId());
        task.setParams(randomParams.toString());
        taskService.updateTask(task);
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    private class RandomParams{
        int randSize;
        Set<Integer> finishedMTaskIds;
        int finishedCount;

        public RandomParams(String paramsStr){
            JSONObject paramObj = new JSONObject(paramsStr);
            randSize = paramObj.getInt("randSize");
            finishedCount = paramObj.getInt("finishedCount");
            finishedMTaskIds = new HashSet<>();
            if(finishedCount > 0){
                JSONArray finishedAry = paramObj.getJSONArray("finishedMTaskId");
                for(int i=0; i<finishedAry.length(); i++){
                    finishedMTaskIds.add(finishedAry.getInt(i));
                }
            }
        }

        @Override
        public String toString() {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("randSize", randSize);
            jsonObject.put("finishedCount", finishedCount);
            JSONArray itemAry = new JSONArray();
            if(finishedMTaskIds != null){
                finishedMTaskIds.forEach(itemAry::put); //Method reference!WOW~~~
            }
            jsonObject.put("finishedMTaskId", itemAry);
            return jsonObject.toString();
        }


        public int getRandSize() {
            return randSize;
        }

        public void setRandSize(int randSize) {
            this.randSize = randSize;
        }

        public Set<Integer> getFinishedMTaskIds() {
            return finishedMTaskIds;
        }

        public void setFinishedMTaskIds(Set<Integer> finishedMTaskIds) {
            this.finishedMTaskIds = finishedMTaskIds;
        }

        public int getFinishedCount() {
            return finishedCount;
        }

        public void setFinishedCount(int finishedCount) {
            this.finishedCount = finishedCount;
        }
    }
}
