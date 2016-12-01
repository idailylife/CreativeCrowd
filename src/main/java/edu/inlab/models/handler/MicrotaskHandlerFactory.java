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

    public static MicrotaskPageRenderer getRenderer(String name){
        if(name.equals(RENDERER_SIMPLE)){
            return new SimplePageRenderer();
        } else {
            return null;
        }
    }


//    public static List<JstlCompatibleModel> parseMicrotaskToItemLists(JSONArray jsonArray){
//        List<JstlCompatibleModel> compatibleModels = new ArrayList<JstlCompatibleModel>();
//        int length = jsonArray.length();
//        for(int i=0; i<length; i++){
//            JSONObject rowObj = jsonArray.getJSONObject(i);
//            if(rowObj.keys().hasNext()){
//                JstlCompatibleModel model = new JstlCompatibleModel();
//                model.setTag(rowObj.keys().next());
//                JSONObject contentObj = rowObj.getJSONObject(model.getTag());
//                Map<String, Object> resultMap = new HashMap<String, Object>();
//                Iterator<String> keyIterator = contentObj.keys();
//                while(keyIterator.hasNext()){
//                    String key = keyIterator.next();
//                    if(key.startsWith("ary_")){
//                        List<String> array = new ArrayList<String>();
//                        JSONArray contentAry = contentObj.getJSONArray(key);
//                        for(int j=0; j<contentAry.length(); j++){
//                            array.add(contentAry.getString(j));
//                        }
//                        resultMap.put(key, array);
//                    } else {
//                        resultMap.put(key, contentObj.getString(key));
//                    }
//                }
////                String itemObjStr = rowObj.getJSONObject(model.getTag()).toString();
////                ObjectMapper objectMapper = new ObjectMapper();
////                Map<String, Object> resultMap = new HashMap<String, Object>();
////                try{
////                    resultMap = objectMapper.readValue(itemObjStr, new TypeReference<Map<String, String>>(){});
////                } catch (IOException e){
////                    e.printStackTrace();
////                }
//                model.setContents(resultMap);
//                compatibleModels.add(model);
//            }
//        }
//        return compatibleModels;
//    }
}