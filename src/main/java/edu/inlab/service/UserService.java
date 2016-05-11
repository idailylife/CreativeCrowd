package edu.inlab.service;

import edu.inlab.models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by inlab-dell on 2016/5/5.
 * Deals with miscellaneous services related to user
 */

public interface UserService {
    int ERR_NO_SUCH_USER = -1;
    int ERR_WRONG_PASSWORD = -2;
    int ERR_SALT_UNDEFINED = -3;
    int ERR_TOKEN_UNSET = -4;
    int ERR_TOKEN_INVALID = -5;
    int ERR_COOKIE_INVALID = -6;
    int ERR_TOKEN_EXPIRED = -7;
    int SUCC_LOGIN = 0;
    int NOT_LOGIN = -10;

    User findById(int id);
    User findByEmail(String email);
    void saveUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
    boolean isUniqueEmail(String email, Integer id);
    void setSaltPassword(User user);
    int verify(String email, String passwordInMD5);
    int verify(int uid, String token);
    int maintainLoginState(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
