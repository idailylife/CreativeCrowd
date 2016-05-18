package edu.inlab.repo;

import edu.inlab.models.Task;

import java.util.List;

/**
 * Created by inlab-dell on 2016/5/10.
 */
public interface TaskRepository {
    Task getTaskById(int id);
    List<Task> getByOwnerId(int ownerId);
    List<Task> getOpenTasks(int startId, int count);
    List<Task> getPagedTasks(int pageNo, int pageSize);
    void saveTask(Task task);
    void deleteTaskById(int id);
}
