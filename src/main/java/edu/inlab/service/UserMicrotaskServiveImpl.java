package edu.inlab.service;

import edu.inlab.models.UserMicroTask;
import edu.inlab.repo.UserMicrotaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by hebowei on 16/5/14.
 */
@Service("userMicrotaskService")
@Transactional
public class UserMicrotaskServiveImpl implements UserMicrotaskService {
    @Autowired
    UserMicrotaskRepository userMicrotaskRepository;

    public UserMicroTask getById(int id) {
        return userMicrotaskRepository.getById(id);
    }

    public void save(UserMicroTask userMicroTask) {
        userMicrotaskRepository.save(userMicroTask);
    }

    public void update(UserMicroTask userMicroTask) {
        UserMicroTask entity = userMicrotaskRepository.getById(userMicroTask.getId());
        if(entity != null){
            entity.setUsertaskId(userMicroTask.getUsertaskId());
            entity.setMicrotaskId(userMicroTask.getMicrotaskId());
            entity.setResults(userMicroTask.getResults());
            entity.setMetaInfo(userMicroTask.getMetaInfo());
        }
    }

    @Override
    public Long getCountByUserTaskId(int usertaskId) {
        return (Long)userMicrotaskRepository.getCountByUserTaskId(usertaskId);
    }

    @Override
    public List<UserMicroTask> getByTaskId(int taskId) {
        return userMicrotaskRepository.getByTaskId(taskId);
    }
}
