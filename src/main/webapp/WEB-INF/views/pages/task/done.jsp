<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: inlab-dell
  Date: 2016/5/23
  Time: 18:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="container">
<c:choose>
    <c:when test="${empty refCode}">
        <h2>任务完成,即将跳转至主页...</h2>
        <p>任务酬金等进度请访问个人中心查看.</p>
    </c:when>
    <c:otherwise>
        <h2>Congratulations!</h2>
        <p>Your have successfully finished the task.</p>
        <h4 class="bg-primary">Please, copy and paste the following reference code back into the task page of MTurk.</h4>
        <pre><h4 id="refCode" style="display:inline; line-height: 2">${refCode}</h4><button type="button" id="btn-copy" class="btn btn-default pull-right">Copy to Clipboard</button></pre>
        <p>For your task status and remuneration, please refer to the MTurk site.</p>
    </c:otherwise>
</c:choose>
</div>
<script>
    document.getElementById("btn-copy").addEventListener("click", function () {
        copyToClipboard(document.getElementById("refCode"));
        this.innerHTML = "Copied";
    });
    function copyToClipboard(elem) {
        // create hidden text element, if it doesn't already exist
        var targetId = "_hiddenCopyText_";
        var isInput = elem.tagName === "INPUT" || elem.tagName === "TEXTAREA";
        var origSelectionStart, origSelectionEnd;
        if (isInput) {
            // can just use the original source element for the selection and copy
            target = elem;
            origSelectionStart = elem.selectionStart;
            origSelectionEnd = elem.selectionEnd;
        } else {
            // must use a temporary form element for the selection and copy
            target = document.getElementById(targetId);
            if (!target) {
                var target = document.createElement("textarea");
                target.style.position = "absolute";
                target.style.left = "-9999px";
                target.style.top = "0";
                target.id = targetId;
                document.body.appendChild(target);
            }
            target.textContent = elem.textContent;
        }
        // select the content
        var currentFocus = document.activeElement;
        target.focus();
        target.setSelectionRange(0, target.value.length);

        // copy the selection
        var succeed;
        try {
            succeed = document.execCommand("copy");
        } catch(e) {
            succeed = false;
        }
        // restore original focus
        if (currentFocus && typeof currentFocus.focus === "function") {
            currentFocus.focus();
        }

        if (isInput) {
            // restore prior selection
            elem.setSelectionRange(origSelectionStart, origSelectionEnd);
        } else {
            // clear temporary content
            target.textContent = "";
        }
        return succeed;
    }
</script>