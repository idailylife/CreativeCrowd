package edu.inlab.models.handler;

/**
 * Created by hebowei on 16/5/14.
 */
public class TaskHandlerFactory {
    public static MicroTaskHandler getHandler(String type, String baseUrl){
        if(type.equals("simple")){
            return new SimpleMicroTaskHandler(baseUrl);
        } else {
            //TODO: other types?
            return null;
        }
    }
}
