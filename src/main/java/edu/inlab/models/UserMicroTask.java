package edu.inlab.models;

import org.json.JSONObject;

/**
 * Created by inlab-dell on 2016/5/4.
 */
public class UserMicroTask {
    private Integer id;
    private Integer taskId;
    private JSONObject results;

    public Integer getId() {
        return id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public JSONObject getResults() {
        return results;
    }
}
