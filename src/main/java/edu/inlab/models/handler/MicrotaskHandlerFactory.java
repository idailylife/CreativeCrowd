package edu.inlab.models.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * Created by hebowei on 16/5/14.
 */
public class MicrotaskHandlerFactory {
    public final static String RENDERER_SIMPLE = "simple";
    public final static String RENDERER_GRID_CHOICE = "grid_choice";

    public static MicrotaskPageRenderer getRenderer(String name){
        if(name.equals(RENDERER_SIMPLE)){
            return new SimplePageRenderer();
        } else if (name.equals(RENDERER_GRID_CHOICE)){
            return new GridSimilarityChoicePageRender();
        } else {
            return null;
        }
    }
}
