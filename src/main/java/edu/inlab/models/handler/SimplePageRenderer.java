package edu.inlab.models.handler;

import edu.inlab.models.UserMicroTask;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by inlab-dell on 2016/6/22.
 */
@Component
public class SimplePageRenderer extends MicrotaskPageRenderer {

    @Override
    public Object parseTemplateText(String templateStr) {
        JSONArray handlerContent = new JSONArray(templateStr);
        return parseMicrotaskToItemLists(handlerContent);
    }

    private List<JstlCompatibleModel> parseMicrotaskToItemLists(JSONArray jsonArray){
        List<JstlCompatibleModel> compatibleModels = new ArrayList<JstlCompatibleModel>();
        int length = jsonArray.length();
        for(int i=0; i<length; i++){
            JSONObject rowObj = jsonArray.getJSONObject(i);
            if(rowObj.keys().hasNext()){
                JstlCompatibleModel model = new JstlCompatibleModel();
                model.setTag(rowObj.keys().next());
                JSONObject contentObj = rowObj.getJSONObject(model.getTag());
                Map<String, Object> resultMap = new HashMap<String, Object>();
                Iterator<String> keyIterator = contentObj.keys();
                while(keyIterator.hasNext()){
                    String key = keyIterator.next();
                    if(key.startsWith("ary_")){
                        List<String> array = new ArrayList<String>();
                        JSONArray contentAry = contentObj.getJSONArray(key);
                        for(int j=0; j<contentAry.length(); j++){
                            array.add(contentAry.getString(j));
                        }
                        resultMap.put(key, array);
                    } else {
                        resultMap.put(key, contentObj.getString(key));
                    }
                }
//                String itemObjStr = rowObj.getJSONObject(model.getTag()).toString();
//                ObjectMapper objectMapper = new ObjectMapper();
//                Map<String, Object> resultMap = new HashMap<String, Object>();
//                try{
//                    resultMap = objectMapper.readValue(itemObjStr, new TypeReference<Map<String, String>>(){});
//                } catch (IOException e){
//                    e.printStackTrace();
//                }
                model.setContents(resultMap);
                compatibleModels.add(model);
            }
        }
        return compatibleModels;
    }
}
