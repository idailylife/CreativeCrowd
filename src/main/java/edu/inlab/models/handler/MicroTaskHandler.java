package edu.inlab.models.handler;


/**
 * Created by inlab-dell on 2016/5/12.
 * 解决MicroTask中HTML渲染及结果回收的问题
 */
public interface MicroTaskHandler {
    /**
     * Parses template string contents to html string
     * @param templateStr template string in MicroTask
     * @return
     */
    String parseMicrotaskToHtml(String templateStr);

    /**
     * Parse an object (i.e. JsonObject) to UserMicroTask results string
     * @param object
     * @return
     */
    String parseUserMicrotaskResults(Object object);

    /**
     * Get the class of next microtask handler, can be null
     * @return
     */
    Class nextHandler(Object... params);

    /**
     * Get the class of previous microtask handler, can be null
     * @return
     */
    Class prevHandler(Object... params);

    /**
     * Example usage of prev/nextHandler() :
     * - Cycled: Pass the same class type to both prev/nextHandler till the end
     * - Single-paged: prevHandler->null, nextHandler->null
     */
}
