package edu.inlab.repo;

import edu.inlab.models.Task;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by inlab-dell on 2016/5/10.
 */
@Repository("taskRepo")
public class TaskRepoImpl extends AbstractDao<Integer, Task> implements TaskRepository {
    public Task getTaskById(int id) {
        return getByKey(id);
    }

    public List<Task> getByOwnerId(int ownerId) {
        List<Task> tasks = getSession().createCriteria(Task.class)
                .add(Restrictions.eq("owner_id", ownerId))
                .list();
        return tasks;
    }

    public List<Task> getOpenTasks(int startId, int count) {
        List<Task> tasks = getSession().createCriteria(Task.class)
                .add(Restrictions.ge("id", startId))
                .setMaxResults(count)
                .list();
        return tasks;
    }

    public void saveTask(Task task) {
        persist(task);
    }

    public void deleteTaskById(int id) {
        Query query = getSession().createSQLQuery("DELETE FROM Task WHERE id= :id");
        query.setInteger("id", id);
        query.executeUpdate();
    }
}
