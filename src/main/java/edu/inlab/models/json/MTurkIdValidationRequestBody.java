package edu.inlab.models.json;

import com.fasterxml.jackson.annotation.JsonView;

import javax.validation.constraints.NotNull;

/**
 * Created by inlab-dell on 2016/5/30.
 */
public class MTurkIdValidationRequestBody {
    @NotNull
    private String mturkId;
    @NotNull
    private String captcha;
    @NotNull
    private String taskId;

    public String getMturkId() {
        return mturkId;
    }

    public void setMturkId(String mturkId) {
        this.mturkId = mturkId;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "{mturkId:" + mturkId + ", captcha:" + captcha + ",taskId:" + taskId + "}";
    }
}
