package edu.inlab.models.handler;

import java.util.Map;

/**
 * Created by hebowei on 16/5/21.
 * 自定义模板渲染用
 * 处理JSON对象到jstl可以兼容使用的组件
 */
public class JstlCompatibleModel {
    private String tag; //label,text,... See SimpleMicroTaskHandler
    private Map<String, Object> contents;
    private Object singleValue;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Map<String, Object> getContents() {
        return contents;
    }

    public Object getSingleValue() {
        return singleValue;
    }

    public int getSingleValueInt(){
        return (int)singleValue;
    }

    public void setSingleValue(Object singleValue) {
        this.singleValue = singleValue;
    }


    public void setContents(Map<String, Object> contents) {
        this.contents = contents;
    }
}
