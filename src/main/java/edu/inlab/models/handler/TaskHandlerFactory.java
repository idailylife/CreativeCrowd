package edu.inlab.models.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hebowei on 16/5/14.
 */
public class TaskHandlerFactory {

    public static List<JstlCompatibleModel> parseMicrotaskToItemLists(JSONArray jsonArray){
        List<JstlCompatibleModel> compatibleModels = new ArrayList<JstlCompatibleModel>();
        int length = jsonArray.length();
        for(int i=0; i<length; i++){
            JSONObject rowObj = jsonArray.getJSONObject(i);
            if(rowObj.keys().hasNext()){
                JstlCompatibleModel model = new JstlCompatibleModel();
                model.setTag(rowObj.keys().next());
                String itemObjStr = rowObj.getJSONObject(model.getTag()).toString();
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> resultMap = new HashMap<String, Object>();
                try{
                    resultMap = objectMapper.readValue(itemObjStr, new TypeReference<Map<String, String>>(){});
                } catch (IOException e){
                    e.printStackTrace();
                }
                model.setContents(resultMap);
                compatibleModels.add(model);
            }
        }
        return compatibleModels;
    }
}
