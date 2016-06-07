package edu.inlab.service.wage;

import edu.inlab.models.Task;
import edu.inlab.models.UserTask;

/**
 * Created by inlab-dell on 2016/6/7.
 * 工资发配接口
 */
public interface WageAssigner {
    Double assignWage(Task task, UserTask userTask);
    Double assignWage(Task task);
}
