package edu.inlab.models.json;

import com.fasterxml.jackson.annotation.JsonView;

import javax.validation.constraints.NotNull;

/**
 * Created by inlab-dell on 2016/6/2.
 */
public class UserEchoRequestBody {
    @NotNull
    @JsonView(JsonDummyView.Public.class)
    private Integer taskType;

    @JsonView(JsonDummyView.Public.class)
    private Integer userId;

    @JsonView(JsonDummyView.Public.class)
    private String mturkId;

    @NotNull
    @JsonView(JsonDummyView.Public.class)
    private Integer taskId;

    @JsonView(JsonDummyView.Public.class)
    private String token;

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMturkId() {
        return mturkId;
    }

    public void setMturkId(String mturkId) {
        this.mturkId = mturkId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
