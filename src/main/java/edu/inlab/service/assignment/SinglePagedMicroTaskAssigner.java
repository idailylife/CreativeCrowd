package edu.inlab.service.assignment;

import edu.inlab.models.Microtask;
import edu.inlab.models.Task;
import edu.inlab.models.UserMicroTask;
import edu.inlab.models.UserTask;
import edu.inlab.service.TaskService;
import edu.inlab.service.UserMicrotaskService;
import edu.inlab.service.UserTaskService;
import edu.inlab.web.exception.TaskAssignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by inlab-dell on 2016/5/31.
 * 最简单的、只有一页的分配器
 */
@Component
public class SinglePagedMicroTaskAssigner implements MicroTaskAssigner {

    @Autowired
    TaskService taskService;


    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public Microtask assignCurrent(UserTask userTask) throws RuntimeException {
        return null;
    }

    @Transactional
    public Microtask assignNext(UserTask userTask) {
        if(userTask.getState() == UserTask.STATE_CLAIMED
                && userTask.getCurrUserMicrotaskId() == null){
            Task task = taskService.findById(userTask.getTaskId());
            if(task.getRelatedMictorasks() != null &&
                    task.getRelatedMictorasks().size() == 1){
                Microtask nextMicrotask = task.getRelatedMictorasks().get(0);
                return nextMicrotask;
            } else {
                throw new TaskAssignException();
            }
        }
        return null;
    }


    @Override
    public void onUserMicrotaskSubmit(UserMicroTask userMicroTask, Task task) {

    }
}
