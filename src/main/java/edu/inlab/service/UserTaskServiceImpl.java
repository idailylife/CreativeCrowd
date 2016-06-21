package edu.inlab.service;

import edu.inlab.models.Task;
import edu.inlab.models.UserTask;
import edu.inlab.repo.TaskRepository;
import edu.inlab.repo.UserTaskRepository;
import edu.inlab.web.EchoController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by inlab-dell on 2016/5/13.
 */
@Transactional
@Service(value = "userTaskService")
public class UserTaskServiceImpl implements UserTaskService {
    @Autowired
    UserTaskRepository userTaskRepository;

    @Autowired
    TaskRepository taskRepository;

    public void saveUserTask(UserTask userTask) {
        userTaskRepository.save(userTask);
    }

    public void updateUserTask(UserTask userTask) {
        UserTask entity = userTaskRepository.getById(userTask.getId());
        if(entity != null){
            entity.setCurrUserMicrotaskId(userTask.getCurrUserMicrotaskId());
            entity.setState(userTask.getState());
            entity.setTaskId(userTask.getTaskId());
            entity.setUserId(userTask.getUserId());
            entity.setMturkId(userTask.getMturkId());
            entity.setUserType(userTask.getUserType());
            entity.setRefCode(userTask.getRefCode());
            entity.setStartTime(userTask.getStartTime());
            entity.setRemuneration(userTask.getRemuneration());
        }
    }

    public UserTask getById(int id) {
        return userTaskRepository.getById(id);
    }

    public List<UserTask> getByUserId(int userId, int size) {
        return userTaskRepository.getByUserId(userId, 0, size);
    }

    public List<UserTask> getByUserId(int userId, int startTaskId, int size) {
        return userTaskRepository.getByUserId(userId, startTaskId, size);
    }

    public List<UserTask> getByUserAndTaskId(int userId, int taskId) {
        return userTaskRepository.getByUserAndTaskId(userId, taskId);
    }

    public Long getFinishedCount(int userId) {
        return (Long)userTaskRepository.getUserFinishedCount(userId);
    }

    public Long getClaimedCount(int userId) {
        return (Long)userTaskRepository.getUserClaimedCount(userId);
    }

    public List<UserTask> getUnfinishedTasks(int userId) {
        return userTaskRepository.getUserUnfinishedTasks(userId);
    }

    private UserTask getValidatedUserTask(UserTask userTask, Task task){
        if(task.getTimeLimit() == null || userTask == null)
            return userTask;
        if(EchoController.getRemainingTime(task, userTask) <= 0){
            userTask.setState(UserTask.STATE_EXPIRED);
            updateUserTask(userTask);
            return null;
        }
        return userTask;
    }

    public UserTask getUnfinishedByUserIdAndTaskId(int userId, int taskId) {
        UserTask userTask =  userTaskRepository.getUnfinished(userId, taskId);
        //Check and maintain validity (expiration) of this task
        Task task = taskRepository.getTaskById(taskId);
        return getValidatedUserTask(userTask, task);
    }

    public UserTask getUnfinishedByMTurkIdAndTaskId(String mturkId, int taskId) {
        UserTask userTask = userTaskRepository.getUnfinished(mturkId, taskId);
        //Maintain validity
        Task task = taskRepository.getTaskById(taskId);
        return getValidatedUserTask(userTask, task);
    }

    public List<Integer> getTaskIds(List<UserTask> userTasks) {
        List<Integer> ids = new ArrayList<Integer>();
        for(UserTask userTask: userTasks){
            ids.add(userTask.getTaskId());
        }
        return ids;
    }

    public List<UserTask> getByMturkIdAndTaskId(String mturkId, int taskId) {
        return userTaskRepository.getByMturkIdAndTaskId(mturkId, taskId);
    }

    public List<UserTask> getFinishedOrExpired(String mturkId, int taskId) {
        return userTaskRepository.getFinishedOrExpired(mturkId, taskId);
    }
}
