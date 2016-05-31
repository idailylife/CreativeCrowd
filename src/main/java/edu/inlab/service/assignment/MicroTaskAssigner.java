package edu.inlab.service.assignment;

import edu.inlab.models.Microtask;

import edu.inlab.models.UserTask;
import org.springframework.stereotype.Service;

/**
 * Created by inlab-dell on 2016/5/31.
 * 该接口负责实时分配某一个task中的某个microtask给指定用户
 */


public interface MicroTaskAssigner {
    /**
     * 分配下一个microtask
     * @param userTask
     * @return 下一项microtask，如果用户任务结束，可以为null
     */
    Microtask assignNext(UserTask userTask) throws RuntimeException;
}
