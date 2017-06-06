<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page isErrorPage="false"%>
<%@ page isELIgnored="false"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/navbar.css" rel="stylesheet">
<title>Insert title here</title>
</head>
<body>
	<nav class='clear row'>
		<div class='navdiv'>
			<a href='index.html'>Главная</a>
		</div>
		<div class='navdiv'>
			<a class='anime' href='manual.html'>Инструкция</a>
		</div>
		<div class='navdiv'>
			<a href='course.html'>Курсы</a>
		</div>
		<div class='navdiv'>
			<a class='nav' id='feedback' href='#frm-feedback'>Обратная связь</a>
		</div>
		<c:choose>
			<c:when test="${empty sessionScope.user == false}">
				<div class='navdiv'>
					<a href='dictionary.html'>Мой словарь</a>
				</div>
				<div class='navdiv'>
					<a href='logout.html'>Выход</a>
				</div>
			</c:when>
			<c:otherwise>
				<div class='navdiv'>
					<a href='dictionary.html'>Тесты</a>
				</div>
				<div class='navdiv'>
					<a href='login.html'>Вход</a>
				</div>
			</c:otherwise>
		</c:choose>
	</nav>
</body>
</html>