<%@ page import="java.util.Set" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.cwk.crm.settings.domain.DicValue" %>
<%@ page import="java.util.List" %>
<%@ page import="com.cwk.crm.workbench.domain.Tran" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/";
	Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
	Set<String> set = pMap.keySet();

	List<DicValue> stageList = (List<DicValue>) application.getAttribute("stage");

	//阶段分界点
	int point = 0;
	for(int i = 0;i < stageList.size();i++){
		if("0".equals(pMap.get(stageList.get(i).getValue()))){
			point = i;
			break;
		}

	}
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />

<style type="text/css">
.mystage{
	font-size: 20px;
	vertical-align: middle;
	cursor: pointer;
}
.closingDate{
	font-size : 15px;
	cursor: pointer;
	vertical-align: middle;
}
</style>
	
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">
	var json = {
		<%
            for(String key:set){
                String value = pMap.get(key);
        %>
		"<%=key%>" : <%=value%>,
		<%
            }

        %>
	}


	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){
		var stage = "${t.stage}";
		var possibility = json[stage];
		$("#possibility").html(possibility);

		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});
		
		$(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});
		
		$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});
		
		$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});
		
		$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#E6E6E6");
		});
		
		
		//阶段提示框
		$(".mystage").popover({
            trigger:'manual',
            placement : 'bottom',
            html: 'true',
            animation: false
        }).on("mouseenter", function () {
                    var _this = this;
                    $(this).popover("show");
                    $(this).siblings(".popover").on("mouseleave", function () {
                        $(_this).popover('hide');
                    });
                }).on("mouseleave", function () {
                    var _this = this;
                    setTimeout(function () {
                        if (!$(".popover:hover").length) {
                            $(_this).popover("hide")
                        }
                    }, 100);
                });

		//在页面加载完毕。展现交易历史列表
		showHistoryList();
	});

	function showHistoryList() {
		$.ajax({
			url: "workbench/transaction/getHistoryListByTranId.do",
			data: {
				"tranId":"${t.id}"
			},
			type: "get",
			dataType: "json",
			success: function (data) {
				var html = "";
				$.each(data,function (i,n) {
					html += '<tr>';
					html += '<td>'+n.stage+'</td>';
					html += '<td>'+n.money+'</td>';
					html += '<td>'+json[n.stage]+'</td>';
					html += '<td>'+n.expectedDate+'</td>';
					html += '<td>'+n.createTime+'</td>';
					html += '<td>'+n.createBy+'</td>';
					html += '</tr>';
				})
				$("#historyBody").html(html);

			}
		})
	}

	function changeStage(stage,i) {
		$.ajax({
			url: "workbench/transaction/changeStage.do",
			data: {
				"id":"${t.id}",
				"stage":stage,
				"money":"${t.money}",
				"expectedDate":"${t.expectedDate}"
			},
			type: "post",
			dataType: "json",
			success: function (data) {
				if(data.success){
					$("#stage").html(data.t.stage);
					$("#possibility").html(json[data.t.stage]);
					$("#editTime").html(data.t.editTime);
					$("#editBy").html(data.t.editBy);
					changeIcon(stage,i);
					showHistoryList();
				}else {
					alert("修改阶段失败！");
				}

			}
		})

	}
	function changeIcon(stage,i) {
		//当前阶段
		var cp = $("#possibility").html();
		//当前阶段下标
		var index = i;
		//分界点
		var p = "<%=point%>";

		//当前阶段的可能性为0，前7个一定是黑圈，后两个一定是红叉、黑叉
		if(cp=="0"){
			//遍历前7个
			for(var j = 0;j<p;j++){
				//黑圈
				$("#"+j).removeClass();
				$("#"+j).addClass("glyphicon glyphicon-record mystage");
				$("#"+j).css("color","#000000");

			}

			//遍历后两个
			for(var j = p;j<<%=stageList.size()%>;j++){
				//如果是当前阶段
				if(j == index){
					//红叉
					$("#"+j).removeClass();
					$("#"+j).addClass("glyphicon glyphicon-remove mystage");
					$("#"+j).css("color","#FF0000");

				}else {//如果不是当前阶段
					//黑叉
					$("#"+j).removeClass();
					$("#"+j).addClass("glyphicon glyphicon-remove mystage");
					$("#"+j).css("color","#000000");

				}
			}
		}else {//当前阶段的可能性不为0，前7个包含绿圈、绿色标记、黑圈，后两个一定是黑叉
			//遍历前7个绿圈、绿色标记、黑圈
			for(var j = 0;j<p;j++){
				if(j == index){
					//绿色标记
					$("#"+j).removeClass();
					$("#"+j).addClass("glyphicon glyphicon-map-marker mystage");
					$("#"+j).css("color","#90F790");

				}else if(j<index){
					//绿圈
					$("#"+j).removeClass();
					$("#"+j).addClass("glyphicon glyphicon-ok-circle mystage");
					$("#"+j).css("color","#90F790");

				}else {
					//黑圈
					$("#"+j).removeClass();
					$("#"+j).addClass("glyphicon glyphicon-record mystage");
					$("#"+j).css("color","#000000");

				}

			}
			//遍历后两个
			for(var j = p;j<<%=stageList.size()%>;j++){
				//一定是黑叉
				$("#"+j).removeClass();
				$("#"+j).addClass("glyphicon glyphicon-remove mystage");
				$("#"+j).css("color","#000000");
			}
		}

	}
	
	
	
</script>

</head>
<body>
	
	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>${t.name} <small>￥${t.money}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" onclick="window.location.href='edit.html';"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>

	<!-- 阶段状态 -->
	<div style="position: relative; left: 40px; top: -50px;">
		阶段&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%
			String curStage = ((Tran)request.getAttribute("t")).getStage();
			//当前阶段可能性
			String curPossibility = pMap.get(curStage);

			if("0".equals(curPossibility)){
				for(int i = 0;i < stageList.size();i++){
					DicValue dv = stageList.get(i);
					String perStage = dv.getValue();
					String perPossibility = pMap.get(perStage);
					if("0".equals(perPossibility)){
						if(perStage.equals(curStage)){//红叉
		%>
						<span id="<%=i%>" onclick="changeStage('<%=perStage%>','<%=i%>')"
							  class="glyphicon glyphicon-remove mystage"
							  data-toggle="popover" data-placement="bottom"
							  data-content="<%=dv.getText()%>" style="color: #FF0000;"></span>
						-----------
		<%
						}else {//黑叉
		%>
						<span id="<%=i%>" onclick="changeStage('<%=perStage%>','<%=i%>')"
							  class="glyphicon glyphicon-remove mystage"
							  data-toggle="popover" data-placement="bottom"
							  data-content="<%=dv.getText()%>" style="color: #000000;"></span>
						-----------
		<%
						}
					}else {//黑圈
		%>
						<span id="<%=i%>" onclick="changeStage('<%=perStage%>','<%=i%>')"
							  class="glyphicon glyphicon-record mystage"
							  data-toggle="popover" data-placement="bottom"
							  data-content="<%=dv.getText()%>" style="color: #000000;"></span>
						-----------
		<%
					}
				}

			}else {
				//当前阶段的下标
				int index = 0;
				for(int i = 0;i < stageList.size();i++){
					String perStage = stageList.get(i).getValue();
					if(perStage.equals(curStage)){
						index = i;
						break;
					}
				}
				for(int i = 0;i < stageList.size();i++) {
					DicValue dv = stageList.get(i);
					String perStage = dv.getValue();
					String perPossibility = pMap.get(perStage);
					if("0".equals(perPossibility)){//黑叉
		%>
					<span id="<%=i%>" onclick="changeStage('<%=perStage%>','<%=i%>')"
						  class="glyphicon glyphicon-remove mystage"
						  data-toggle="popover" data-placement="bottom"
						  data-content="<%=dv.getText()%>" style="color: #000000;"></span>
					-----------
		<%
					}else {
						if(i == index){//绿标
		%>
					<span id="<%=i%>" onclick="changeStage('<%=perStage%>','<%=i%>')"
						  class="glyphicon glyphicon-map-marker mystage"
						  data-toggle="popover" data-placement="bottom"
						  data-content="<%=dv.getText()%>" style="color: #90F790;"></span>
					-----------
		<%
						}else if(i<index){//绿圈
		%>
					<span id="<%=i%>" onclick="changeStage('<%=perStage%>','<%=i%>')"
						  class="glyphicon glyphicon-ok-circle mystage"
						  data-toggle="popover" data-placement="bottom"
						  data-content="<%=dv.getText()%>" style="color: #90F790;"></span>
					-----------
		<%
						}else {//黑圈
		%>
						<span id="<%=i%>" onclick="changeStage('<%=perStage%>','<%=i%>')"
							  class="glyphicon glyphicon-record mystage"
							  data-toggle="popover" data-placement="bottom"
							  data-content="<%=dv.getText()%>" style="color: #000000;"></span>
						-----------
		<%
						}

					}
				}

			}
		%>

	</div>
	
	<!-- 详细信息 -->
	<div style="position: relative; top: 0px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${t.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">金额</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${t.money}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${t.name}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">预计成交日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${t.expectedDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">客户名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${t.customerId}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">阶段</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="stage">${t.stage}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">类型</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${t.type}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">可能性</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="possibility"></b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">来源</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${t.source}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">市场活动源</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${t.activityId}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">联系人名称</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${t.contactsId}</b></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 60px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${t.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${t.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 70px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b id="editBy">${t.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;" id="editTime">${t.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 80px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${t.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 90px;">
			<div style="width: 300px; color: gray;">联系纪要</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					&nbsp;${t.contactSummary}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 100px;">
			<div style="width: 300px; color: gray;">下次联系时间</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>&nbsp;${t.nextContactTime}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div style="position: relative; top: 100px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>
		
		<!-- 备注1 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>哎呦！</h5>
				<font color="gray">交易</font> <font color="gray">-</font> <b>动力节点-交易01</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		
		<!-- 备注2 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>呵呵！</h5>
				<font color="gray">交易</font> <font color="gray">-</font> <b>动力节点-交易01</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary">保存</button>
				</p>
			</form>
		</div>
	</div>
	
	<!-- 阶段历史 -->
	<div>
		<div style="position: relative; top: 100px; left: 40px;">
			<div class="page-header">
				<h4>阶段历史</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table id="activityTable" class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>阶段</td>
							<td>金额</td>
							<td>可能性</td>
							<td>预计成交日期</td>
							<td>创建时间</td>
							<td>创建人</td>
						</tr>
					</thead>
					<tbody id="historyBody">
						<%--<tr>
							<td>资质审查</td>
							<td>5,000</td>
							<td>10</td>
							<td>2017-02-07</td>
							<td>2016-10-10 10:10:10</td>
							<td>zhangsan</td>
						</tr>--%>

					</tbody>
				</table>
			</div>
			
		</div>
	</div>
	
	<div style="height: 200px;"></div>
	
</body>
</html>