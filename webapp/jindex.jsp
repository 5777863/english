<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page isErrorPage="false"%>
<%@ page isELIgnored="false"%>
<html>
<head>
<title>Учим английский вместе</title>
<meta name="keywords"
	content="английские слова, тест английский, английский язык">
<meta name="description" content="Изучаем английские слова">
<meta charset="utf-8">
<meta http-equiv="content-language" content="ru">
<meta http-equiv="Cache-Control" content="no-cache">
<meta name=viewport
	content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
<link href="css/index.css" rel="stylesheet">
<link href="css/fonts.css" rel="stylesheet">

<!-- Important Owl stylesheet -->
<link rel="stylesheet" href="js/owl-carousel/owl.carousel.css">

<!-- Default Theme -->
<link rel="stylesheet" href="js/owl-carousel/owl.theme.css">

<!-- Add jQuery library -->
<script src="js/jquery-3.1.1.min.js"></script>

<!-- Add mousewheel plugin (this is optional) -->
<script type="text/javascript"
	src="js/fancybox/lib/jquery.mousewheel-3.0.6.pack.js"></script>
<script type="text/javascript"
	src="js/jquery-validation-1.15.0/dist/jquery.validate.min.js"></script>

<!-- Add fancyBox -->
<link rel="stylesheet" href="js/fancybox/source/jquery.fancybox.css"
	type="text/css" media="screen" />
<script type="text/javascript"
	src="js/fancybox/source/jquery.fancybox.pack.js"></script>

<!-- Include js plugin -->
<script src="js/owl-carousel/owl.carousel.js"></script>

<script type="text/javascript" src="js/index.js"></script>
<script type="text/javascript" src='js/fbfancy.js'></script>


</head>
<body>
	<div class='start'>
		<header>
			<div class='head1'>Учим слова вмете с нами</div>
			<div class='head2'>
				<img src='img/header.jpg' height='100%' width='100%' />
			</div>
		</header>
		<!--navbar -->
		<%@ include file="navbar.jsp"%>

		<!--	
	<div class='sizet clear row'>
		<div class='sizein'>1</div>
		<div class='sizein'>2</div>
		<div class='sizein'>3</div>
		</div>
		<div class='sizet clear row'>
		<div class='sizein'>1</div>
		<div class='sizein'>
			<p class='inner'>2</p>
			<p class='inner'>22</p>
			<p class='inner'>2</p>
			<p class='inner'>22</p>
		</div>
		<div class='sizein'>3</div>
		</div>
	-->
		<div class='main'>
			<div class='head center'>Наш ресурс предназначен помочь вам в
				изучении английских слов, предлогов, устойчивых выражений и много
				другого через прохождение тестов. вы сможете использовать как
				встроенные тесты так и создавать собственные словари с возможностью
				тестирования по ним.</div>
			<div id="owl-example" class="owl-carousel">
				<c:forEach items="${facts}" var="facts">
					<div class='ar'>
						<div class='div_carousel'>${facts.fact}</div>
					</div>
				</c:forEach>
			</div>
			<div class='tests'>
				<div class='head center'>Случайные тесты</div>
				<c:forEach items="${groupname}" var="groupname">
					<div class=divtst>
						<a class='smalla' href='staticunit.html?groupname=${groupname}'>${groupname}</a>
					</div>
				</c:forEach>
			</div>
		</div>
		<%@ include file="fbfancy.jsp"%>
	</div>
	<%@ include file="foot.jsp"%>
</body>