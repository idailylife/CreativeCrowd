package edu.inlab.models;

import edu.inlab.repo.usertype.JSONObjectUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by inlab-dell on 2016/5/4.
 */
@Entity
@Table(name = "usermicrotask")
@TypeDef(name = "customJsonObject", typeClass = JSONObjectUserType.class)
public class UserMicroTask {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usertask_id", nullable = false)
    private Integer usertaskId;


    @Column(name = "microtask_id", nullable = false)
    private Integer microtaskId;

    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "results")
    @Type(type = "customJsonObject")
    private JSONObject results;

    @Column(name = "meta_info")
    private String metaInfo;

    @Column(name = "optlock")
    @Version
    private Integer version;

    public UserMicroTask (){

    }

    public UserMicroTask (Integer usertaskId, Integer microtaskId){
        this.usertaskId = usertaskId;
        this.microtaskId = microtaskId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getMicrotaskId() {
        return microtaskId;
    }

    public void setMicrotaskId(Integer microtaskId) {
        this.microtaskId = microtaskId;
    }

    public JSONObject getResults() {
        return results;
    }

    public void setResults(JSONObject results) {
        this.results = results;
    }

    public Integer getUsertaskId() {
        return usertaskId;
    }

    public void setUsertaskId(Integer usertaskId) {
        this.usertaskId = usertaskId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(String metaInfo) {
        this.metaInfo = metaInfo;
    }
}
