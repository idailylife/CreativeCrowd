package edu.inlab.repo;

import edu.inlab.models.Microtask;
import edu.inlab.models.Task;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
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

    @Override
    public void update(Microtask microtask) {
        saveOrUpdate(microtask);
    }

    @Override
    public void remove(Microtask microtask) {
        delete(microtask);
    }

    @Override
    public Microtask getFirstMtaskByTask(Task task) {
        StatelessSession session = getStatelessSession();

        Microtask retVal = (Microtask) session.createCriteria(Microtask.class)
                .add(Restrictions.eq("task", task))
                .uniqueResult();

        session.close();

        return retVal;
    }
}
