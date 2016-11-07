<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link href="${pageContext.request.contextPath }/assets/css/guestbook.css" rel="stylesheet" type="text/css">
<script 
	type="text/javascript" 
	src="${pageContext.request.contextPath }/assets/js/jquery/jquery-1.9.0.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

	
<script>
var isEnd = false;
var page = 1;

var render = function(vo, mode){
	//
	// 현업에서는 이부분을 template library ex) ejs
	//
	var htmls = 
		"<li id = 'gb-" + vo.no + "'>" +
		"<strong>" + vo.name + "</strong>" +
		"<p>" + vo.content.replace(/\n/gi,"<br>") + "</p>" +
		"<strong>" + vo.regDate + "</strong>" +
		"<a href=''>삭제</a>" +
		"</li>";// js templete
		
		if(mode == true){ //add 일경우 prepend, list를 뿌릴 경우 append
			$("#list-guestbook").prepend(htmls);
		} else{
			$("#list-guestbook").append(htmls);
		}
}

var fetchList = function(){
	if(isEnd == true){
		return;
	}
	$.ajax({
		url: "${pageContext.request.contextPath }/api/guestbook?a=ajax-list&p=" + page++,
		type: "get",
		dataType: "json",
		data: "",
		success: function(response){	//response.result = "success" or "fail"
										//response.data = [{},{},{}...]
			if(response.result != "success"){
				console.error(response.message);
				isEnd = true;
				return;
			}
			
			//rendering
			$(response.data).each(function(index, vo){
				render(vo, false);
			});
			if(response.data.length < 5){
				isEnd = true;
			}
		},
		error: function(jqXHR, status, e){
			console.error(status +":"+e	);
		}
	});
}

$(function(){
	
	var dialog, form;
    var password = $("#password");

	$(window).scroll(function(){
		var $window = $(this);
		var scrollTop = $window.scrollTop();
		var windowHeight = $window.height();
		var documentHeight = $(document).height();
		
		if(scrollTop + windowHeight + 1 > documentHeight){
			fetchList();
		}
		//console.log(scrollTop + ":" + windowHeight);
	});
	
	// 1번째 리스트 가져오기
	fetchList();
    
	
	
	//
	$("#add-form").submit(function(event){
		event.preventDefault();
		
		var $inputs =   $("#add-form :input");
		var values={};
		$inputs.each(function(){
			values[this.name] = $(this).val();
		})
		
		if(values.name == ""){
			alert("이름이 비었습니다.");
		    $("#name").focus();
		    return;
		}
		      
		if(values.password == ""){
		    alert("비번이 비었습니다.");
		    $("#password").focus();
		    return;
		} 
		      
		if(values.content == ""){
		    alert("내용이 비었습니다.");
		    $("#content").focus();
		    return;
		}

		// ajax insert
		$.ajax({
			url: "${pageContext.request.contextPath }/api/guestbook",
			type: "post",
			dataType: "json",
			data: "a=ajax-add" + 
					"&name="+values.name+
					"&password="+values.password+
					"&content="+values.content,
			
			success: function(response){	//response.result = "success" or "fail"
											//response.data = [{},{},{}...]
				if(response.result != "success"){
					console.error(response.message);
					return;
				}	
			//rendering
			render(response.data, true);	
			},
			error: function(jqXHR, e){
				console.log("error: "+e);
			}
		});
	});
	
	
	dialog = $("#dialog-form").dialog({
		autoOpen : false,
		height : 400,
		width : 350,
		modal : true,
		buttons : {
			
			Cancel : function() {
				dialog.dialog("close");
			}
		},
		close : function() {
			//form[0].reset();
			//allFields.removeClass("ui-state-error");
		}
	});

	form = dialog.find("form").on("submit", function(event) {
		event.preventDefault();
		addUser();
	});
	
	//삭제버튼 클릭 이벤트
	$(document).on("click","#list-guestbook li a",function(event){
		event.preventDefault();
		console.log("여기서 비밀번호를 입력받는 팝업 modal dialog를 띄웁니다.")
		
		dialog.dialog( "open" );
	});
});


	
</script>
</head>
<body>

	<div id="dialog-form" title="방명록 삭제">
		<form>
			<fieldset>
				<label for="password">Password</label> <input type="password"
					name="password" id="password" placeholder="비밀번호입력"
					class="text ui-widget-content ui-corner-all">

				<!-- Allow form submission with keyboard without duplicating the dialog button -->
				<input type="submit" tabindex="-1"
					style="position: absolute; top: -1000px">
			</fieldset>
		</form>

	</div>

	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="guestbook">
				<h1>방명록</h1>
				<form id = "add-form"  action="" method="post">
					<input type="hidden" name="a" value="add">
					<input type="text" name="name" placeholder="이름">
					<input type="password" name="password" placeholder="비밀번호">
					<textarea name="content" placeholder="내용을 입력해 주세요."></textarea>
					<input type="submit" value="보내기" />
				</form>
				<ul id = "list-guestbook"></ul>
				<button style="margin-top:20px" id="btn-fetch">가져오기</button>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp">
			<c:param name="menu" value="guestbook-ajax"/>
		</c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
	
</body>
</html>