package edu.inlab.repo;

import edu.inlab.models.Task;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by inlab-dell on 2016/5/10.
 */
@Repository("taskRepo")
public class TaskRepoImpl extends AbstractDao<Integer, Task> implements TaskRepository {
    public Task getTaskById(int id) {
        return getByKey(id);
    }

    @SuppressWarnings("unchecked")
    public List<Task> getByOwnerId(int ownerId) {
        List<Task> tasks = getSession().createCriteria(Task.class)
                .add(Restrictions.eq("ownerId", ownerId))
                .addOrder(Order.desc("id"))
                .list();
        return tasks;
    }

    @SuppressWarnings("unchecked")
    public List<Task> getOpenTasks(int startId, int count) {
        List<Task> tasks = getSession().createCriteria(Task.class)
                .add(Restrictions.ge("id", startId))
                .setMaxResults(count)
                .list();
        return tasks;
    }

    @SuppressWarnings("unchecked")
    public List<Task> getPagedTasks(int pageNo, int pageSize){
        List<Task> tasks = getSession().createCriteria(Task.class)
                .addOrder(Order.desc("id"))
                .setFirstResult((pageNo-1) * pageSize)
                .setMaxResults(pageSize)
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

    public List<Task> getByIds(List<Integer> ids) {
        if(ids.isEmpty())
            return new ArrayList<>();
        Query query = getSession().createQuery("FROM Task WHERE id IN ( :ids )");
        query.setParameterList("ids", ids);
        return query.list();
    }
}
