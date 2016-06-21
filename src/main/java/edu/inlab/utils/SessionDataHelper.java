package edu.inlab.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by inlab-dell on 2016/6/21.
 */
public class SessionDataHelper {
    public static Integer getTaskIdFromToken(HttpServletRequest request, String token){
        Integer retVal = null;
        if(request.getSession().getAttribute(Constants.KEY_UPLOAD_TOKEN_MAP) instanceof Map){
            Map tokenMap = (Map<String, Integer>) request.getSession().getAttribute(Constants.KEY_UPLOAD_TOKEN_MAP);
            if(tokenMap.containsKey(token)){
                retVal =  (Integer) tokenMap.get(token);
            }
        }
        return retVal;
    }

    public static void putTaskIdFormToken(HttpServletRequest request, String token, Integer id){
        Map tokenMap;
        if(request.getSession().getAttribute(Constants.KEY_UPLOAD_TOKEN_MAP) instanceof Map){
            tokenMap = (Map<String, Integer>) request.getSession().getAttribute(Constants.KEY_UPLOAD_TOKEN_MAP);
            tokenMap.put(token, id);
        } else {
            tokenMap = new HashMap();
        }
        request.getSession().setAttribute(Constants.KEY_UPLOAD_TOKEN_MAP, tokenMap);
    }

    public static void removeToken(HttpServletRequest request, String token){
        if(request.getSession().getAttribute(Constants.KEY_UPLOAD_TOKEN_MAP) instanceof Map){
            Map tokenMap = (Map<String, Integer>) request.getSession().getAttribute(Constants.KEY_UPLOAD_TOKEN_MAP);
            if(tokenMap.containsKey(token)){
                tokenMap.remove(token);
            }
        }
    }
}
