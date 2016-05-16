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

    @Column(name = "results")
    @Type(type = "customJsonObject")
    private JSONObject results;

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
}
