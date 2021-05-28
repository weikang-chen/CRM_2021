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
