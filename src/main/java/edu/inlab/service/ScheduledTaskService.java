package edu.inlab.service;

import edu.inlab.models.Task;
import edu.inlab.models.UserTask;
import edu.inlab.repo.UserTaskRepository;
import edu.inlab.web.EchoController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by inlab-dell on 2016/6/22.
 */
@Service
public class ScheduledTaskService {
    @Autowired
    UserTaskService userTaskService;

    @Autowired
    TaskService taskService;

    final static Logger logger = Logger.getLogger(ScheduledTaskService.class);

    /**
     * Clear unfinished and over-timed usertasks
     */

    @Scheduled(fixedDelay = 600000)
    @Transactional
    public void clearOvertimedUsertasks(){
        logger.info("---------------------------->Start cleaning overtimed usertasks...");
        List<UserTask> unfinishedTasks = userTaskService.getAllUnfinished();
        Map<Integer, Task> taskMap = new HashMap<>();
        long count = 0;
        for(UserTask userTask : unfinishedTasks){
            Integer taskId = userTask.getTaskId();
            Task task;
            if(taskMap.containsKey(taskId)){
                task = taskMap.get(taskId);
            } else {
                task = taskService.findById(taskId);
                taskMap.put(taskId, task);
            }
            long remainingTime =  EchoController.getRemainingTime(task, userTask);
            if(remainingTime < 0){
                //exipred
                userTask.setState(UserTask.STATE_EXPIRED);
                userTaskService.updateUserTask(userTask);
                task.setClaimedCount(task.getClaimedCount() - 1);
                taskService.updateTask(task);
                count++;
            }
        }
        logger.info("---------------------------->Finished cleaning overtimed usertasks, " + count + " cleared.");
    }
}
