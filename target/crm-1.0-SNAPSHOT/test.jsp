<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/";
%>

<base href="<%=basePath%>">

$.ajax({
url: "",
data: {},
type: "post",
dataType: "json",
success: function (data) {

}
})

String createTime = DateTimeUtil.getSysTime();
String createBy = ((User)request.getSession().getAttribute("user")).getName();

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