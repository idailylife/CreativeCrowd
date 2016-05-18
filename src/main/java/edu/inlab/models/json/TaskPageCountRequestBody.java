package edu.inlab.models.json;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Created by inlab-dell on 2016/5/18.
 */
public class TaskPageCountRequestBody {
    @JsonView(JsonDummyView.Public.class)
    private Integer page;

    @JsonView(JsonDummyView.Public.class)
    private Integer count;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
