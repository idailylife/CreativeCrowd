package edu.inlab.service;

import edu.inlab.models.Task;
import edu.inlab.models.UserTask;

import java.util.List;
import java.util.Map;

/**
 * Created by inlab-dell on 2016/5/10.
 */
public interface TaskService {
    Task findById(int id);
    Map<Integer, Task> findMapByIds(List<Integer> ids);
    List<Task> findByOwnerId(int ownerId);
    List<Task> getOpenTasks(int startId, int count);
    List<Task> getOpenTasks(int count);
    List<Task> getPagedTasks(int pageNo, int pageSize);
    void saveTask(Task task);
    void updateTask(Task task);
    void deleteTaskById(int id);
}
