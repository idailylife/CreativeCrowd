package edu.inlab.models;
import com.fasterxml.jackson.annotation.JsonView;


/**
 * Created by inlab-dell on 2016/5/6.
 */
public class AjaxResponseBody {
    @JsonView(JsonDummyView.Public.class)
    Integer state;

    @JsonView(JsonDummyView.Public.class)
    String message;

    @JsonView(JsonDummyView.Public.class)
    String content;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
