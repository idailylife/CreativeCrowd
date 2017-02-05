package edu.inlab.repo;

import edu.inlab.models.UserMicroTask;

import java.util.List;

/**
 * Created by hebowei on 16/5/14.
 */
public interface UserMicrotaskRepository {
    UserMicroTask getById(int id);
    void save(UserMicroTask userMicroTask);
    Number getCountByUserTaskId(int usertaskId);
    List<UserMicroTask> getByTaskId(int taskId);
}
