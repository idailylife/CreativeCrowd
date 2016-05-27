package edu.inlab.models;

/**
 * Created by inlab-dell on 2016/5/27.
 */
public class ComboUserTask {
    private UserTask userTask;
    private Task task;

    public UserTask getUserTask() {
        return userTask;
    }

    public void setUserTask(UserTask userTask) {
        this.userTask = userTask;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
