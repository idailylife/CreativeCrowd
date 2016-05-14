package edu.inlab.service;

import edu.inlab.models.UserMicroTask;

/**
 * Created by hebowei on 16/5/14.
 */
public interface UserMicrotaskService {
    UserMicroTask getById(int id);
    void save(UserMicroTask userMicroTask);
    void update(UserMicroTask userMicroTask);
}
