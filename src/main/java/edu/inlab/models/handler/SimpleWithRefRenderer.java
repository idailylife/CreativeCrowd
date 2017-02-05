package edu.inlab.models.handler;

import edu.inlab.models.UserMicroTask;
import edu.inlab.service.UserMicrotaskService;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hebowei on 2017/1/21.
 * If it contains no reference, it will behave the same as SimplePageRenderer
 *
 *
 */
@Component
public class SimpleWithRefRenderer extends SimplePageRenderer{

    private final static String[] DEFAULT_SHOW_FIELDS = {"image", "text"};

    @Autowired
    UserMicrotaskService userMicrotaskService;

    @Override
    public boolean requireUserMicrotask() {
        return true;
    }

    /**
     *
     * @param templateStr
     * @param params [userMicrotask, taskParams]
     * @return
     */
    @Override
    public Object parseTemplateText(String templateStr, Object... params) {
        //Render reference components
        if(params.length != 2 ||
                !(params[0] instanceof UserMicroTask) ||
                !(params[1] instanceof String)){
            System.err.println("Error: cannot parse template text, input params is invalid.");
            return null;
        }
        UserMicroTask userMicrotask = (UserMicroTask)params[0];
        JSONObject taskParams = new JSONObject((String)params[1]);
        String[] show_fields = DEFAULT_SHOW_FIELDS;
        if(taskParams.has("refColNames")){
            show_fields = taskParams.getString("refColNames").split(",");
        }

        List<JstlCompatibleModel> refContents = new ArrayList<>();
        if(userMicrotask.getResults() != null && userMicrotask.getResults().has("refItems")){
            JSONArray refItems = userMicrotask.getResults().getJSONArray("refItems");
            for(int i=0; i<refItems.length(); i++){
                JSONObject refItem = refItems.getJSONObject(i);
                UserMicroTask umt = userMicrotaskService.getById(refItem.getInt("umtId"));
                JstlCompatibleModel jstlCompatibleModel = new JstlCompatibleModel();
                jstlCompatibleModel.addMappedContent("umtId", umt.getId());
                for(String field : show_fields){
                    jstlCompatibleModel.addMappedContent(field, umt.getResults().getString(field));
                }
                jstlCompatibleModel.setTag("CHILD");
                refContents.add(jstlCompatibleModel);

                if(refItem.has("parentUmtId")){
                    // Contains parent
                    UserMicroTask parentUmt = userMicrotaskService.getById(refItem.getInt("parentUmtId"));
                    jstlCompatibleModel = new JstlCompatibleModel();
                    jstlCompatibleModel.addMappedContent("umtId", parentUmt.getId());
                    for(String field : show_fields){
                        jstlCompatibleModel.addMappedContent(field, parentUmt.getResults().getString(field));
                    }
                    jstlCompatibleModel.setTag("PARENT");
                    refContents.add(jstlCompatibleModel);
                }
            }
        }

        //Render basic components
        List<JstlCompatibleModel> renderedTemplate =
                (List<JstlCompatibleModel>) super.parseTemplateText(templateStr);

        return new Pair<>(refContents, renderedTemplate);
    }
}
