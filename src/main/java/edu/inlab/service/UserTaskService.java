package edu.inlab.service;

import edu.inlab.models.User;
import edu.inlab.models.UserTask;

import java.util.List;

/**
 * Created by inlab-dell on 2016/5/13.
 */
public interface UserTaskService {
    void saveUserTask(UserTask userTask);
    void updateUserTask(UserTask userTask);
    UserTask getById(int id);
    List<UserTask> getByUserId(int userId, int size);
    List<UserTask> getByUserId(int userId, int startTaskId, int size);
    List<UserTask> getByUserAndTaskId(int userId, int taskId);
    Long getFinishedCount(int userId);
    Long getClaimedCount(int userId);
    List<UserTask> getUnfinishedTasks(int userId);
    UserTask getUnfinishedByUserIdAndTaskId(int userId, int taskId);
}
