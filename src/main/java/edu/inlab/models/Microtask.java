package edu.inlab.models;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by inlab-dell on 2016/5/4.
 */
@Entity
@Table(name = "microtask")
@TypeDef(name = "customJsonObject", typeClass = JSONObject.class)
public class Microtask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @Column(name = "task_id", nullable = false)
    private Integer taskId;

    @Column(name = "template", nullable = false)
    @Type(type = "customJsonObject")
    private JSONObject template;

    @Column(name = "handler")
    private String handlerType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public JSONObject getTemplate() {
        return template;
    }

    public void setTemplate(JSONObject template) {
        this.template = template;
    }

    public String getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
    }

    @Override
    public boolean equals(Object obj) {
        if(this.id != null && ((Microtask)obj).getId()!=null){
            return this.id.equals(((Microtask) obj).getId());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        if(this.id != null){
            return this.id;
        }
        return super.hashCode();
    }
}
