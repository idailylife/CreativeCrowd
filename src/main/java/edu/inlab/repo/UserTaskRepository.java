package edu.inlab.repo;

import edu.inlab.models.UserTask;

import java.util.List;

/**
 * Created by inlab-dell on 2016/5/13.
 */
public interface UserTaskRepository {
    void save(UserTask userTask);
    UserTask getById(int id);
    List<UserTask> getByUserId(int userId, int startTaskId, int size);
    List<UserTask> getByUserAndTaskId(int userId, int taskId);
    List<UserTask> getByMturkIdAndTaskId(String mturkId, int taskId);
    Number getUserClaimedCount(int userId);
    Number getUserFinishedCount(int userId);
    List<UserTask> getUserUnfinishedTasks(int userId);
    UserTask getUnfinished(int userId, int taskId);
    UserTask getUnfinished(String mturkId, int taskId);
}
