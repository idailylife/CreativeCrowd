<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
    N选k 图片+文字描述的设计方案相似度判断
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
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    var candidate_items = [];
<c:forEach items="${handlerContent}" var="model">
    <%-- instance of JstlCompatibleModel --%>
    <c:choose>
        <c:when test="${model.tag eq 'params'}">
            var grid_params = {
                "N":${model.contents['N']},
                "K":${model.contents['K']},
                "nRows":${model.contents['nRows']}
            };
        </c:when>
        <c:when test="${model.tag eq 'ref_item'}">
            var ref_item = {
                "image": '${model.contents['image']}',
                "text" : '${model.contents['text']}'
            };
        </c:when>
        <c:when test="${model.tag eq 'item'}">
            candidate_items.push({
                "image": '${model.contents['image']}',
                "text" : '${model.contents['text']}'
            });
        </c:when>
    </c:choose>
</c:forEach>
</script>