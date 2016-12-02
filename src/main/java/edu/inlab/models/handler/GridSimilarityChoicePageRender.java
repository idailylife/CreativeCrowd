package edu.inlab.models.handler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by inlab-dell on 2016/12/1.
 * Parse similarity judging microtask page which is a "choose K objects from N candidates" question.
 * Each object consists one text description and one image, all items and parameters are passed in as a JSON list string written as:
 * [
 *  {"N":N }, {"K":K},
 *  {"nRows": num_of_rows},  // #columns = N / num_of_rows
 *  {"ref_item": {"image":internal_url, "text":text_description}},
 *  //following N elements
 *  {"item": {"image":internal_url, "text":text_description} },
 *  ...
 * ]
 */
public class GridSimilarityChoicePageRender implements MicrotaskPageRenderer {
    private final static String TAG_PARAMS = "params";

    @Override
    public Object parseTemplateText(String templateStr) {
        JSONArray handlerContent = new JSONArray(templateStr);
        return parseTemplateJson(handlerContent);
    }

    private List<JstlCompatibleModel> parseTemplateJson(JSONArray jsonArray){
        List<JstlCompatibleModel> compactibleModels = new ArrayList<>();
        int length = jsonArray.length();

        JstlCompatibleModel paramModel = new JstlCompatibleModel(); //model to store parameters N, K, num_rows
        paramModel.setTag(TAG_PARAMS);
        Map<String, Object> paramContentMap = new HashMap<>();

        for(int i=0; i<length; i++){
            JSONObject rowObj = jsonArray.getJSONObject(i);

            if(rowObj.keys().hasNext()){
                String tag = rowObj.keys().next();
                if(!tag.equals("item") && !tag.equals("ref_item")){
                    //model.setSingleValue(rowObj.getInt(tag));
                    paramContentMap.put(tag, rowObj.getInt(tag));
                } else {
                    JstlCompatibleModel model = new JstlCompatibleModel();
                    model.setTag(tag);
                    JSONObject contentObj = rowObj.getJSONObject(tag);
                    Map<String,Object> contentMap = new HashMap<>();
                    if(contentObj.has("image"))
                        contentMap.put("image", contentObj.getString("image"));
                    if(contentObj.has("text"))
                        contentMap.put("text", contentObj.getString("text"));
                    model.setContents(contentMap);
                    compactibleModels.add(model);
                }
            }

        }

        paramModel.setContents(paramContentMap);
        compactibleModels.add(paramModel);

        return compactibleModels;
    }
}
