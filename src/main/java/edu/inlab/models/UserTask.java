package edu.inlab.models;

import javax.persistence.*;

/**
 * Created by inlab-dell on 2016/5/4.
 */
@Entity
@Table(name = "usertask")
public class UserTask {
    //public enum TaskState {CLAIMED, FINISHED, EXPIRED};
    public static int TYPE_CLAIMED = 0;
    public static int TYPE_FINISHED = 1;
    public static int TYPE_EXPIRED = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "task_id", nullable = false)
    private Integer taskId;

    @Column(name = "curr_usermicrotask_id")
    private Integer currUserMicrotaskId;

    @Column(name = "state", nullable = false)
    private Integer state;

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getTaskId() {
        return taskId;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getCurrUserMicrotaskId() {
        return currUserMicrotaskId;
    }

    public void setCurrUserMicrotaskId(Integer currUserMicrotaskId) {
        this.currUserMicrotaskId = currUserMicrotaskId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
