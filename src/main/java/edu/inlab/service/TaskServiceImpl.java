package edu.inlab.service;

import edu.inlab.models.Task;
import edu.inlab.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by inlab-dell on 2016/5/10.
 */
@Service("taskService")
@Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskRepository taskRepository;

    public Task findById(int id) {
        return taskRepository.getTaskById(id);
    }

    public List<Task> findByOwnerId(int ownerId) {
        return taskRepository.getByOwnerId(ownerId);
    }

    public List<Task> getOpenTasks(int startId, int count) {
        return taskRepository.getOpenTasks(startId, count);
    }

    public List<Task> getOpenTasks(int count) {
        return taskRepository.getOpenTasks(0, count);
    }

    public void saveTask(Task task) {
        taskRepository.saveTask(task);
    }

    public void updateTask(Task task) {
        Task entity = taskRepository.getTaskById(task.getId());
        if(entity != null){
            entity.setDescJson(task.getDescJson());
            entity.setTitle(task.getTitle());
            entity.setEndTime(task.getEndTime());
            entity.setFinishedCount(task.getFinishedCount());
            entity.setMode(task.getMode());
            entity.setOwnerId(task.getOwnerId());
            entity.setQuota(task.getQuota());
            entity.setStartTime(task.getStartTime());
        }
    }

    public void deleteTaskById(int id) {
        taskRepository.deleteTaskById(id);
    }
}
