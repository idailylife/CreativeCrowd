package edu.inlab.models;

import edu.inlab.repo.usertype.JSONArrayUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by inlab-dell on 2016/5/4.
 */
@Entity
@Table(name = "microtask")
@TypeDef(name = "customJsonArray", typeClass = JSONArrayUserType.class)
public class Microtask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

//    @Column(name = "task_id", nullable = false)
//    private Integer taskId;

    @ManyToOne
    private Task task;


    /**
     * Template of a microtask
     * For the definition of simple handler, please refer to
     *   edu.inlab.models.handlers.SimpleMicroTaskHandler
     * Should be rendered by handler that declared in `handler` column
     */
    @Column(name = "template", nullable = false)
    @Type(type = "customJsonArray")
    private JSONArray template;

    /**
     * Defines the type of handler to render the template
     * A handler should be written at /webapp/WEB-INF/views/pages/json_handler/[HANDLER_TYPE].jsp
     */
    @Column(name = "handler")
    private String handlerType;

    @Column(name = "prev_id")
    private Integer prevId;

    @Column(name = "next_id")
    private Integer nextId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public Integer getTaskId() {
//        return taskId;
//    }
//
//    public void setTaskId(Integer taskId) {
//        this.taskId = taskId;
//    }

    public JSONArray getTemplate() {
        return template;
    }

    public void setTemplate(JSONArray template) {
        this.template = template;
    }

    public String getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Integer getPrevId() {
        return prevId;
    }

    public void setPrevId(Integer prevId) {
        this.prevId = prevId;
    }

    public Integer getNextId() {
        return nextId;
    }

    public void setNextId(Integer nextId) {
        this.nextId = nextId;
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
