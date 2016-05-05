package edu.inlab.models;

/**
 * Created by inlab-dell on 2016/5/4.
 */
public class UserTask {

    private Integer id;
    private Integer userId;
    private Integer taskId;
    private Integer progressState;
    private Integer progressCount;

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public Integer getProgressState() {
        return progressState;
    }

    public Integer getProgressCount() {
        return progressCount;
    }
}
