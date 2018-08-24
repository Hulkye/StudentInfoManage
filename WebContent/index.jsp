<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<meta http-equiv="Pragma" content="no-cache"> 
<meta http-equiv="Cache-Control" content="no-cache"> 
<meta http-equiv="Expires" content="0"> 
<title>学生信息管理系统登录</title> 
<script type="text/javascript">
	function resetValue() {
		document.getElementById("userName").value="";
		document.getElementById("password").value="";
	}	
</script>
<link href="css/login.css" type="text/css" rel="stylesheet"> 
</head> 
<body> 
<div class="login">
    <div class="message">学生信息管理系统登录</div>
    <div id="darkbannerwrap"></div>
    
    <form action="login" method="post">
    <table>
		<input name="action" value="login" type="hidden">
		<input type="text" value="${userName }" name="userName" id="userName" placeholder="用户名">
		<hr class="hr15">
		<input type="password" value="${password }" name="password" id="password" placeholder="密码">
		<hr class="hr15">
		<input value="登录" style="width:100%;" type="submit">
		<hr class="hr20">
		<input value="重置" style="width:100%;" type="button" onclick="resetValue()">
		<hr class="hr20">
		<font color="red">${error }</font>	
		</table>
	</form>
</div>
</body>
</html>