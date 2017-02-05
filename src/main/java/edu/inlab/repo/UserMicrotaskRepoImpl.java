package edu.inlab.repo;

import edu.inlab.models.UserMicroTask;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Override
    public Number getCountByUserTaskId(int usertaskId) {
        Number cnt = (Number) getSession().createCriteria(UserMicroTask.class)
                .add(Restrictions.eq("usertaskId", usertaskId))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        return cnt;
    }

    @Override
    public List<UserMicroTask> getByTaskId(int taskId) {
        return getSession().createCriteria(UserMicroTask.class)
                .add(Restrictions.eq("taskId", taskId))
                .list();
    }
}
