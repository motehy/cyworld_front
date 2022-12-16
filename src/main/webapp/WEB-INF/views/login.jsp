<%--
  Created by IntelliJ IDEA.
  User: LEEHYUK
  Date: 2022-12-15
  Time: 오후 2:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script type="text/javascript" src="${_static_domain}/static/front/resources/web/js/jquery-3.6.0.min.js?v=${_jsversion}"></script>
    <script type="text/javascript" src="${_static_domain}/static/front/resources/web/js/jquery.easing.js?v=${_jsversion}"></script>
    <script type="text/javascript" src="${_static_domain}/static/front/resources/web/js/jquery-ui.min.js?v=${_jsversion}"></script>
    <script type="text/javascript" src="${_static_domain}/static/front/resources/web/js/swiper.min.js?v=${_jsversion}"></script>
    <script type="text/javascript" src="${_static_domain}/static/front/resources/web/js/jquery.lazyload.js?v=${_jsversion}"></script>
    <script type="text/javascript" src="${_static_domain}/static/front/resources/web/js/ScrollMagic.min.js?v=${_jsversion}"></script>
    <script type="text/javascript" src="${_static_domain}/static/front/resources/web/js/ui.js?v=${_jsversion}"></script>
    <script type="text/javascript" src="${_static_domain}/static/front/weblib/js/moment.min.js?v=${_jsversion}"></script>
    <script type="text/javascript" src="${_static_domain}/static/front/weblib/js/moment-timezone-with-data-2012-2022.js?v=${_jsversion}"></script>
    <script type="text/javascript" src="${_static_domain}/static/front/resources/web/js/jquery.form.js?v=${_jsversion}"></script>
    <title>Title</title>
</head>
<body>
    <form name="loginform" id="loginform">
        <input type="text" id="userid" name="userid" value=""/>
        <input type="password" id="userpw" name="userpw" value=""/>

        <button type="submit" onclick="exeLogin();" >로그인</button>
    </form>
</body>
<script>
    $(document).ready(function(){
       <%--if("${loginfl}" != "" && "${loginfl}" == "fail"){--%>
       <%--    alert("로그인에 실패했습니다. 다시 시도해 주세요.");--%>
       <%--}else{--%>
       <%--    alert(11);--%>

       <%--}--%>
       <%-- console.log("${login_fl}");--%>
    });
    function exeLogin(){
        var v_frm = document.loginform;
        v_frm.action = "/core/login";
        v_frm.method = "post";
        v_frm.submit();
    }

</script>
</html>
