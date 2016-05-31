package edu.inlab.models;

import javax.persistence.*;

/**
 * Created by inlab-dell on 2016/5/4.
 */
@Entity
@Table(name = "usertask")
public class UserTask {
    //public enum TaskState {CLAIMED, FINISHED, EXPIRED};
    public static int STATE_CLAIMED = 0;
    public static int STATE_FINISHED = 1;
    public static int STATE_EXPIRED = 2;

    public static int USERTYPE_NORMAL = 0;
    public static int USERTYPE_MTURK = 1;

    public UserTask(){

    };

    public UserTask(int userId, int taskId){
        this.userId = userId;
        this.taskId = taskId;
        this.state = STATE_CLAIMED;
        this.userType = USERTYPE_NORMAL;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_type")
    private Integer userType;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "mturk_id")
    private String mturkId;

    @Column(name = "task_id", nullable = false)
    private Integer taskId;

    @Column(name = "curr_usermicrotask_id")
    private Integer currUserMicrotaskId;

    @Column(name = "state", nullable = false)
    private Integer state;

    @Column(name = "ref_code")
    private String refCode;

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

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getMturkId() {
        return mturkId;
    }

    public void setMturkId(String mturkId) {
        this.mturkId = mturkId;
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    /**
     * Generate a random reference code (for MTurk use)
     */
    public void generateRefCode(){
        refCode = java.util.UUID.randomUUID().toString();
    }
}
