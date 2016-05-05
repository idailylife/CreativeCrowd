package edu.inlab.models;

import org.json.JSONObject;

/**
 * Created by inlab-dell on 2016/5/4.
 */
public class Microtask {
    public Integer id;
    public Integer taskId;
    public JSONObject template;

    public Integer getId() {
        return id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public JSONObject getTemplate() {
        return template;
    }
}
