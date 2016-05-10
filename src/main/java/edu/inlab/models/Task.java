package edu.inlab.models;

import edu.inlab.repo.JSONUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.json.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.Min;

/**
 * Created by inlab-dell on 2016/5/4.
 */
@Entity
@Table(name = "task")
@TypeDef(name = "customJsonObject", typeClass = JSONUserType.class)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "quota", nullable = false)
    @Min(value = 0)
    private Integer quota;

    @Column(name = "finished_count", nullable = false)
    @Min(value = 0)
    private Integer finishedCount;

    @Column(name = "desc_json")
    @Type(type = "customJsonObject")
    private JSONObject descJson;

    @Column(name = "mode", nullable = false)
    private Integer mode;

    @Column(name = "start_time")
    private Integer startTime;

    @Column(name = "end_time")
    private Integer endTime;

    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;

    public Task(){
        this(null, null, null);
    }

    public Task(Integer quota, JSONObject descJson, Integer mode){
        this(null, quota, 0, descJson, mode, null, null, null);
    }

    public Task(Integer id, Integer quota, Integer finishedCount, JSONObject descJson, Integer mode,
                Integer startTime, Integer endTime, Integer ownerId){
        this.id = id;
        this.quota = quota;
        this.finishedCount = finishedCount;
        this.descJson = descJson;
        this.mode = mode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ownerId = ownerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public Integer getFinishedCount() {
        return finishedCount;
    }

    public void setFinishedCount(Integer finishedCount) {
        this.finishedCount = finishedCount;
    }

    public JSONObject getDescJson() {
        return descJson;
    }

    public void setDescJson(JSONObject descJson) {
        this.descJson = descJson;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public int hashCode() {
        if(this.id != null){
            return this.id;
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this.id != null && ((Task)obj).getId() != null){
            return this.id.equals(((Task) obj).getId());
        }
        return super.equals(obj);
    }
}
