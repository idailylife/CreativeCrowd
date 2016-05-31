package edu.inlab.service.assignment;

import edu.inlab.models.Microtask;
import edu.inlab.models.UserTask;
import org.springframework.stereotype.Component;

/**
 * Created by inlab-dell on 2016/5/31.
 * 随机分配
 */

@Component
public class RandomMicroTaskAssigner implements MicroTaskAssigner {
    public Microtask assignNext(UserTask userTask) {
        return null;
    }
}
