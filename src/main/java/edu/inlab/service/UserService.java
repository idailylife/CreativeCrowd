package edu.inlab.service;

import edu.inlab.models.User;

/**
 * Created by inlab-dell on 2016/5/5.
 * Deals with miscellaneous services related to user
 */

public interface UserService {
    User findById(int id);
    User findByEmail(String email);
    void saveUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
    boolean isUniqueEmail(String email, Integer id);
}
