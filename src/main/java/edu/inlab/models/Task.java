package edu.inlab.models;

import org.json.JSONObject;

/**
 * Created by inlab-dell on 2016/5/4.
 */
public class Task {
    private Integer id;
    private Integer quota;
    private Integer finishedCount;
    private JSONObject descJson;
    private Integer mode;
    private Integer startTime;
    private Integer endTime;

    public Task(Integer quota, JSONObject descJson, Integer mode){
        this(null, quota, 0, descJson, mode, null, null);
    }

    public Task(Integer id, Integer quota, Integer finishedCount, JSONObject descJson, Integer mode,
                Integer startTime, Integer endTime){
        this.id = id;
        this.quota = quota;
        this.finishedCount = finishedCount;
        this.descJson = descJson;
        this.mode = mode;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Integer getId() {
        return id;
    }

    public Integer getQuota() {
        return quota;
    }

    public Integer getFinishedCount() {
        return finishedCount;
    }

    public JSONObject getDescJson() {
        return descJson;
    }

    public Integer getMode() {
        return mode;
    }
}
