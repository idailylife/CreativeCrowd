package edu.inlab.repo;

import edu.inlab.models.User;

/**
 * Created by inlab-dell on 2016/5/4.
 */
public interface UserRepository {
    User getUserById(int id);
    User getUserByEmail(String email);
    void saveUser(User user);
    void deleteUserById(int id);
}
