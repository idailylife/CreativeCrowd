package edu.inlab.service;

import edu.inlab.models.User;
import edu.inlab.repo.UserRepository;
import edu.inlab.utils.EncodeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by inlab-dell on 2016/5/5.
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {



    @Autowired
    UserRepository userRepository;

    public User findById(int id) {
        return userRepository.getUserById(id);
    }

    public User findByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public void saveUser(User user) {
        userRepository.saveUser(user);
    }

    /*
     * Since the method is running with Transaction, No need to call hibernate update explicitly.
     * Just fetch the entity from db and update it with proper values within transaction.
     * It will be updated in db once transaction ends.
     * See: http://websystique.com/springmvc/spring-4-mvc-and-hibernate4-integration-example-using-annotations/
     */
    public void updateUser(User user) {
        User entity = userRepository.getUserById(user.getId());
        if(entity != null){
            entity.setAcceptRate(user.getAcceptRate());
            entity.setAge(user.getAge());
            entity.setEmail(user.getEmail());
            entity.setGender(user.getGender());
            entity.setPassword(user.getPassword());
            entity.setPayAccount(user.getPayAccount());
            entity.setPayMethod(user.getPayMethod());
            entity.setPhoneNumber(user.getPhoneNumber());
            entity.setSalt(user.getSalt());
            entity.setTokenCookie(user.getTokenCookie());
            entity.setNickname(user.getNickname());
        }
    }

    public void deleteUser(int id) {
        userRepository.deleteUserById(id);
    }

    public boolean isUniqueEmail(String email, Integer id) {
        User user = findByEmail(email);
        if(user == null)
            return true;
        if(id != null && user.getId().equals(id))
            return true;
        return false;
    }

    /**
     * 设置加盐后的密码
     * @param user
     */
    public void setSaltPassword(User user) {
        if(user.getSalt() == null){
            user.setSalt(EncodeFactory.getSalt());
        }
        user.generateSaltPassword();
    }

    /**
     * 根据邮箱与密码验证用户
     * @param email
     * @param passwordInMD5 32bit MD5 lowercase un-salted
     * @return -1: Cannot find user with given email,
     *          -2: Wrong password,
     *          -3: Salt undefined,
     *          0:  Pass
     */
    public int verify(String email, String passwordInMD5) {
        User user = findByEmail(email);
        if(null == user){
            return ERR_NO_SUCH_USER;
        }
        String salt = user.getSalt();
        if(null == salt){
            return ERR_SALT_UNDEFINED;
        }
        String requestSaltPassword = EncodeFactory.getEncodedString(salt + passwordInMD5);
        if(requestSaltPassword.equals(user.getPassword())){
            return SUCC_LOGIN;
        } else {
            return ERR_WRONG_PASSWORD;
        }
    }

    /**
     * 根据cookie/session里的uid和token验证用户登录状态
     * @param uid
     * @param token
     * @return
     */
    public int verify(int uid, String token) {
        User user = findById(uid);
        if(null == user){
            return ERR_NO_SUCH_USER;
        }
        if(null == user.getTokenCookie()){
            return ERR_TOKEN_UNSET;
        }
        if(user.getTokenCookie().equals(token)){
            return SUCC_LOGIN;
        } else {
            return ERR_TOKEN_INVALID;
        }
    }
}
