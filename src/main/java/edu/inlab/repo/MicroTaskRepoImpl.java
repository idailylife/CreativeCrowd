package edu.inlab.repo;

import edu.inlab.models.Microtask;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by hebowei on 16/5/14.
 */
@Transactional
@Repository("microTaskRepo")
public class MicroTaskRepoImpl extends AbstractDao<Integer, Microtask> implements MicroTaskRepository {
    public Microtask getById(int id) {
        return getByKey(id);
    }

    public void save(Microtask microtask) {
        persist(microtask);
    }

    public List<Microtask> getByTaskId(int taskId) {
        List<Microtask> microtasks = getSession().createCriteria(Microtask.class)
                .add(Restrictions.eq("taskId", taskId))
                .list();
        return microtasks;
    }

    @Override
    public void update(Microtask microtask) {
        saveOrUpdate(microtask);
    }

    @Override
    public void remove(Microtask microtask) {
        delete(microtask);
    }
}
