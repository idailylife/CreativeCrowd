package edu.inlab.models.handler;

import edu.inlab.utils.EncodeFactory;
import org.apache.commons.lang.StringEscapeUtils;
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
 *
 *  ///NOTICE: The following items are NOT part of user-defined file,
 *  /// they will be covered automatically by TaskAssigner
 *  /// according to parameters defined above !!
 *  {"N":N },
 *  {"K":K},
 *  {"freeChoice": true/false}
 *  {"nRows": num_of_rows},  // #columns = N / num_of_rows
 *  {"ref_item": {"image":internal_url, "text":text_description}},
 *  //following N elements
 *  {"item": {"image":internal_url, "text":text_description} },
 *  ...
 *
 *  ///
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
                    if(tag.equals("progress")){
                        paramContentMap.put(tag, rowObj.getString(tag));
                    } else if(tag.equals("freeChoice")){
                        paramContentMap.put(tag, rowObj.getBoolean(tag));
                    }else {
                        paramContentMap.put(tag, rowObj.getInt(tag));
                    }

                } else {
                    JstlCompatibleModel model = new JstlCompatibleModel();
                    model.setTag(tag);
                    JSONObject contentObj = rowObj.getJSONObject(tag);
                    Map<String,Object> contentMap = new HashMap<>();
                    if(contentObj.has("image")) {
                        String imgId = contentObj.getString("image");
                        contentMap.put("id", imgId);
                        contentMap.put("image", imgId);
                    } else {
                        contentMap.put("id", EncodeFactory.getEncodedString(contentObj.getString("text")));
                    }
                    if(contentObj.has("text"))
                        contentMap.put("text", StringEscapeUtils.escapeJavaScript(contentObj.getString("text")));

                    model.setContents(contentMap);
                    compactibleModels.add(model);
                }
            }

        }
        if(!paramContentMap.containsKey("freeChoice")){
            paramContentMap.put("freeChoice", false);
        }

        paramModel.setContents(paramContentMap);
        compactibleModels.add(paramModel);

        return compactibleModels;
    }
}
