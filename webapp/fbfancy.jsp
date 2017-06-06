<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page isErrorPage="false"%>
<%@ page isELIgnored="false"%>
<html>
<head>
<link href="css/fbfancy.css" rel="stylesheet">
<link href="css/valid.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
	<div class='fb_hide fb_size' id='frm-feedback'>
		<p id='fb_result' class='fb_center'></p>
		<form id='frm_fb' action='feedback.html' method='get'
			class='fb_center' autocomplete='off' enctype="text/plain">
			<textarea name='text' maxlength='301' required cols='50' rows='6'
				class='fb_txtarea' id='fb_txtarea'
				placeholder='Тут Вы можете оставить свой отзыв или комментарий, так же адрес электронной почты, в случае, если Вы не авторизировались'></textarea>
			<input class='fb_btm_big' type='submit' value='отправить'>
		</form>
	</div>
</body>
</html>