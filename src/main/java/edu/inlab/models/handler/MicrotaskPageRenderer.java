package edu.inlab.models.handler;

import edu.inlab.models.UserMicroTask;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by inlab-dell on 2016/6/22.
 */
public abstract class MicrotaskPageRenderer {
    public abstract Object parseTemplateText(String templateStr);

    public Object parseTemplateText(String templateStr, Object... params){
        return null;
    }

    public boolean requireUserMicrotask(){
        return false;
    }
}
