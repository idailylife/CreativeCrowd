package edu.inlab.utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by inlab-dell on 2016/5/11.
 * Convert JSONObject to Map (String)
 * Ref: http://stackoverflow.com/questions/21720759/convert-a-json-string-to-a-hashmap
 */
public class JSON2Map {
    public static Map<String, String> convertJSONObjectToMap(JSONObject jsonObject){
        Map<String, String> map = new HashMap<String, String>();
        Iterator<String> keysItr = jsonObject.keys();
        while(keysItr.hasNext()){
            String key = keysItr.next();
            String value = jsonObject.getString(key);
            map.put(key, value);
        }
        return map;
    }
}
