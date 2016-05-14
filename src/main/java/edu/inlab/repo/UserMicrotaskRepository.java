package edu.inlab.repo;

import edu.inlab.models.UserMicroTask;

/**
 * Created by hebowei on 16/5/14.
 */
public interface UserMicrotaskRepository {
    UserMicroTask getById(int id);
    void save(UserMicroTask userMicroTask);
}
