package edu.inlab.repo;

import edu.inlab.models.UserMicroTask;
import org.springframework.stereotype.Repository;

/**
 * Created by hebowei on 16/5/14.
 */
@Repository("userMicrotaskRepo")
public class UserMicrotaskRepoImpl extends AbstractDao<Integer, UserMicroTask>
        implements UserMicrotaskRepository {
    public UserMicroTask getById(int id) {
        return getByKey(id);
    }

    public void save(UserMicroTask userMicroTask) {
        persist(userMicroTask);
    }
}
