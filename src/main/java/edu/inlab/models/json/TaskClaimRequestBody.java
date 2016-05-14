package edu.inlab.models.json;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Created by hebowei on 16/5/14.
 */
public class TaskClaimRequestBody {
    @JsonView(JsonDummyView.Public.class)
    Integer taskId;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }
}
