package edu.inlab.service.assignment;

import edu.inlab.models.Microtask;
import edu.inlab.models.UserTask;
import edu.inlab.service.MicroTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * 按照顺序发放的Microtask分配器、
 * 会根据Microtask对象的nextId域分配下一项任务
 * Created by inlab-dell on 2016/6/16.
 */
@Component
@ComponentScan(basePackages = "edu.inlab")
public class SequenceMicroTaskAssigner implements MicroTaskAssigner {
    @Autowired
    MicroTaskService microTaskService;

    @Override
    public Microtask assignNext(UserTask userTask) throws RuntimeException {
        Integer currMTaskId = userTask.getCurrUserMicrotaskId();
        if(currMTaskId != null){
            Microtask currMTask = microTaskService.getById(currMTaskId);
            if(currMTask.getNextId() != null)
                return microTaskService.getById(currMTask.getNextId());
        }
        return null;
    }
}
