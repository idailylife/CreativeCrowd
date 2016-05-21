package edu.inlab.models.handler;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.*;

/**
 * Created by inlab-dell on 2016/5/12.
 * Deals with simple microtasks.
 * Input: the templateStr is a `stringified` jsonArray, which contains multiple lines of jsonObjects
 *     each jsonObject has a key indicates its type, which can be a (lower-cased, * means optional):
 *     [
 *      {label,     {id: ID, text: CONTENT, *for: FOR_ID}} :=> labels (for some input FOR_ID)
 *      {text,      {id: ID, multiline: true/false, *placeholder: TEXT}}           :=> text input
 *      {choice,    {id: ID, *type: single/multiple, items:[ITEM_1, ITEM_2, ...]}
 *              :=> single(default)/multiple choice box
 *      {image,     {src: internal_url}}    :=> image
 *      {button,    {*id:ID, type: int/ext, *url: EXTERNAL_URL, *target: prev/next/submit, *text: TEXT}}
 *                  :=> buttons
 *                  internal buttons will have specified ids: btn_prev, btn_submit or btn_next
 *                  external buttons can have customized id
 *      {file,      {accept: ALLOWED_TYPES, *text: TEXT_DESCRIPTION}}      :=> file
 *     ]
 * Output:
 */

@Deprecated
public class SimpleMicroTaskHandler implements MicroTaskHandler {
    //TODO: [IMPORTANT]Filter illegal scripts inside the text (XSS)

    static final String KEY_LABEL = "label";
    static final String KEY_TEXT = "text";
    static final String KEY_CHOICE = "choice";
    static final String KEY_IMAGE = "image";
    static final String KEY_BUTTON = "button";
    static final String KEY_FILE = "file";

    private String baseUrl;

    SimpleMicroTaskHandler(String baseUrl){
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }


    public String parseMicrotaskToHtml(String templateStr) {
        JSONArray inputJsonAry = new JSONArray(templateStr);
        StringBuilder retStr = new StringBuilder();
        int length = inputJsonAry.length();
        retStr.append("<form id=\"form-mtask\" enctype=\"multipart/form-data\">");
        for(int i=0; i<length; i++){
            retStr.append("<div class=\"row row-with-gap\"><div class=\"col-md-12");
            JSONObject rowObject = inputJsonAry.getJSONObject(i);
            Iterator<String> keys = rowObject.keys();
            if(keys.hasNext()){
                String keyType = keys.next();
                JSONObject itemObj = rowObject.getJSONObject(keyType);
                if(keyType.equals(KEY_BUTTON)){
                    retStr.append(" text-right");
                }
                retStr.append("\">");

                if(keyType.equals(KEY_LABEL)){
                    retStr.append(parseLabel(itemObj));
                } else if(keyType.equals(KEY_TEXT)){
                    retStr.append(parseText(itemObj));
                } else if(keyType.equals(KEY_IMAGE)){
                    retStr.append(parseImage(itemObj));
                } else if(keyType.equals(KEY_BUTTON)){
                    retStr.append(parseButton(itemObj));
                } else if(keyType.equals(KEY_CHOICE)){
                    //TODO: impl
                } else if(keyType.equals(KEY_FILE)){
                    retStr.append(parseFile(itemObj));
                }
            }
            retStr.append("</div></div>");
            retStr.append("\n");
        }
        retStr.append("</form>");
        return retStr.toString();
    }

    public String parseUserMicrotaskResults(Object object) {
        if(object instanceof JSONObject){
            return object.toString();
        }
        return null;
    }

    public Class nextHandler(Object... params) {
        return null;
    }

    public Class prevHandler(Object... params) {
        return null;
    }

    private String parseLabel(JSONObject labelObj){
        StringBuilder retStrBuilder = new StringBuilder();
        retStrBuilder.append("<label id=\"ud_")
                     .append(labelObj.getString("id"))
                     .append("\" ");
        if(labelObj.has("for")){
            retStrBuilder.append("class = \"control-label\" ");
            retStrBuilder.append("for=\"ud_")
                    .append(labelObj.getString("for"))
                    .append("\" ");
        }
        retStrBuilder.append(">")
                .append(labelObj.getString("text"))
                .append("</label>");
        return retStrBuilder.toString();
    }

    private String parseText(JSONObject textObj){
        StringBuilder retStrBuilder = new StringBuilder();
        if(textObj.has("multiline") && textObj.getString("multiline").equals("true")){
            //textarea
            retStrBuilder.append("<textarea class=\"form-control\" ");
            retStrBuilder.append("id=\"ud_").append(textObj.getString("id")).append("\" ");
            retStrBuilder.append("rows=\"5\" ");
            if(textObj.has("placeholder")){
                retStrBuilder.append("placeholder=\"").append(textObj.getString("placeholder")).append("\" ");
            }
            retStrBuilder.append("></textarea>");
        } else {
            //text input
            retStrBuilder.append("<input type=\"text\" class=\"form-control\" ");
            retStrBuilder.append("id=\"ud_").append(textObj.getString("id")).append("\" ");
            if(textObj.has("placeholder")){
                retStrBuilder.append("placeholder=\"").append(textObj.getString("placeholder")).append("\" ");
            }
            retStrBuilder.append(">");
        }
        return retStrBuilder.toString();
    }

    private String parseImage(JSONObject imageObj){
        StringBuilder retStrBuilder = new StringBuilder();
        retStrBuilder.append("<img id=\"ud_")
                    .append(imageObj.getString("id"))
                    .append("\" ");
        retStrBuilder.append("class=\"img-responsive center-block img-thumbnail\" ");
        retStrBuilder.append("src=\"").append(baseUrl).append(imageObj.getString("src")).append("\" ");
        retStrBuilder.append(">");
        return retStrBuilder.toString();
    }

    private String parseButton(JSONObject buttonObj){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<input type=\"button\" ");
        if(buttonObj.getString("type").equals("ext")){
            //External link
            //TODO: TO BE SUPPORTED
            stringBuilder.append("id=\"ud_")
                    .append(buttonObj.getString("id"))
                    .append(" value=\"")
                    .append(buttonObj.getString("text"))
                    .append("\"/>");
        } else if(buttonObj.getString("type").equals("int")){
            //Internal link
            stringBuilder.append("id=\"btn_")
                    .append(buttonObj.getString("target"))
                    .append("\" ");
            String target = buttonObj.getString("target");
            if(target.equals("prev")){
                stringBuilder.append("class=\"btn btn-default\" value=\"")
                        .append("上一项");
            } else if(target.equals("next")){
                stringBuilder.append("class=\"btn btn-default\" value=\"")
                        .append("下一项");
            } else if(target.equals("submit")){
                stringBuilder.append("class=\"btn btn-primary\" value=\"")
                        .append("提交");
            }
            stringBuilder.append("\"/>");
        }
        return stringBuilder.toString();
    }

    private String parseFile(JSONObject fileObj){

        StringBuilder stringBuilder = new StringBuilder();
        if(fileObj.has("text")){
            stringBuilder.append("<label for=\"btn_file\" class=\"control-label\">");
            stringBuilder.append(fileObj.getString("text"));
            stringBuilder.append("</label>");
        }
        stringBuilder.append("<input type=\"file\" id=\"btn_file\" class=\"btn btn-default\" ");
        if(fileObj.has("accept")){
            stringBuilder.append("accept=\"" + fileObj.getString("accept") + "\" ");
        }
        stringBuilder.append(">");

        return stringBuilder.toString();
    }
}

