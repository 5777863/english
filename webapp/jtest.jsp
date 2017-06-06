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

<meta charset="utf-8">
<meta http-equiv="content-language" content="ru">
<meta http-equiv="Cache-Control" content="no-cache">
<meta name=viewport content="width=device-width, initial-scale=1.0 ">
<link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">

<link href="css/test.css" rel="stylesheet">
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

<script type="text/javascript" src="js/test.js"></script>
<script type="text/javascript" src='js/fbfancy.js'></script>

</head>
<body>
	<div class='start'>
		<header>
			<div class='head1'>Учим слова вмете с нами</div>
			<div class='head2'>
				<img src='img/headerSmall.jpg' height='100%' width='100%' />
			</div>
		</header>
		<nav>
			<%@ include file="navbar.jsp"%>
		</nav>
		<div class='main'>

			<div id='divkolvo' class='result'></div>
			<div id='error' class=''></div>

			<!-- блок для теста1 -->
			<div id='test1' class='hide divTest'>
				<div class='word'>
					<p id='question1'></p>
				</div>
				<div id='answers1'></div>
				<div id='er1'>неверно</div>
				<!-- >input type='button' value='Next'-->
				<div id='result1' class='divNext'></div>
				<p class='final_resilt'></p>
			</div>

			<!-- блок для теста2 -->
			<div id='test2' class='hide'>
				<input class='btm_big' id='lng' type='button' value='Ru'>
				<div class='divTest'>
					<div class='word'>
						<p id='question2'></p>
					</div>
					<div id='answers2'>
						<form>
							<input type='text' id='answer' name='answer'
								placeholder='Введите ответ ' autocomplete='off' required
								autofocus />
							<div class='hide'>
								<input class='btm_big next2' type='submit' value='ответить'>
							</div>
							<div id='er2'>неверно</div>
							<a href="#" class='next2'>далее</a>
						</form>
					</div>
					<div id='result2' class='divNext'></div>
					<p class='final_resilt'></p>
				</div>
			</div>
		</div>
		<%@ include file="fbfancy.jsp"%>
	</div>
	<%@ include file="foot.jsp"%>
</body>
</html>
