package edu.inlab.service;

import edu.inlab.models.UserTask;
import edu.inlab.repo.UserTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by inlab-dell on 2016/5/13.
 */
@Transactional
@Service(value = "userTaskService")
public class UserTaskServiceImpl implements UserTaskService {
    @Autowired
    UserTaskRepository userTaskRepository;

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

    public UserTask getUnfinishedByUserIdAndTaskId(int userId, int taskId) {
        return userTaskRepository.getUnfinished(userId, taskId);
    }
}
