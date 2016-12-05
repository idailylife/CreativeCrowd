package edu.inlab.service;

import edu.inlab.models.Microtask;
import edu.inlab.models.Task;
import edu.inlab.models.UserTask;
import edu.inlab.repo.TaskRepository;
import edu.inlab.service.assignment.MicroTaskAssigner;
import edu.inlab.service.assignment.MicroTaskAssignerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by inlab-dell on 2016/5/10.
 */
@Service("taskService")
@Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    MicroTaskAssignerFactory microTaskAssignerFactory;

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

    public List<Task> getPagedTasks(int pageNo, int pageSize) {
        return taskRepository.getPagedTasks(pageNo, pageSize);
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
            entity.setRepeatable(task.getRepeatable());
            entity.setType(task.getType());
            entity.setParams(task.getParams());
            entity.setTag(task.getTag());
            entity.setRelatedMictorasks(entity.getRelatedMictorasks());
            entity.setTimeLimit(task.getTimeLimit());
            entity.setWage(task.getWage());
            entity.setWageType(task.getWageType());
            entity.setImage(task.getImage());
        }
    }

    public void deleteTaskById(int id) {
        taskRepository.deleteTaskById(id);
    }

    public Map<Integer, Task> findMapByIds(List<Integer> ids) {
        List<Task> tasks =  taskRepository.getByIds(ids);
        Map<Integer, Task> map = new HashMap<Integer, Task>();
        for(Task task: tasks){
            map.put(task.getId(), task);
        }
        return map;
    }
//
//    /**
//     * Inform microtask assigners the deletion of microtask
//     * @param microtask
//     */
//    @Override
//    public void onMicrotaskDelete(Microtask microtask, Task task) {
//        MicroTaskAssigner assigner = microTaskAssignerFactory.getAssigner(task.getMode());
//        assigner.onMicrotaskDelete(microtask);
//    }
//
//    @Override
//    public void onMicrotaskCreate(Microtask microtask, Task task) {
//        MicroTaskAssigner assigner = microTaskAssignerFactory.getAssigner(task.getMode());
//        assigner.onMicrotaskCreate(microtask);
//    }
}
