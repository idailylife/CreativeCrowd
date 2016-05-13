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
    UserTask getByUserAndTaskId(int userId, int taskId);
}
