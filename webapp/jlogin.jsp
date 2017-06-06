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
<meta http-equiv="Cache-Control" content="no-cache">
<meta charset="utf-8">
<meta http-equiv="content-language" content="ru">
<link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
<meta name=viewport content="width=device-width, initial-scale=1.0 ">

<link href="css/login.css" rel="stylesheet">
<link href="css/navbar.css" rel="stylesheet">
<link href="css/fonts.css" rel="stylesheet">
<link href="css/valid.css" rel="stylesheet">

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

<script type="text/javascript" src="js/login.js"></script>
<script type="text/javascript" src="js/fbfancy.js"></script>

</head>
<body>
	<div class='start'>
		<header>
			<div class='head1'>Учим слова вмете с нами</div>
			<div class='head2'>
				<img src='img/headerSmall.jpg' height='100%' width='100%' />
			</div>
		</header>
		<nav class='clear row'>
			<div class='navdiv'>
				<a href='index.html'>Главная</a>
			</div>
			<div class='navdiv'>
				<a href='manual.html'>Инструкция</a>
			</div>
			<div class='navdiv'>
				<a href='course.html'>Курсы</a>
			</div>
			<div class='navdiv'>
				<a class='nav' id='feedback' href='#frm_feedback'>Обратная связь</a>
			</div>
			<div class='navdiv'>
				<a href='dictionary.html'>Тесты</a>
			</div>
			<div class='navdiv'>
				<a href='registration.html'>Регистрация</a>
			</div>
		</nav>
		<div class='main'>
			<div class='unit'>
				<form id='login'>
					<div class='cntr clear'>
						<div class='result' id='result'></div>
						<input type='email' name='email' id='email'
							placeholder='Введите e-mail' required /> <br /> <input
							type='password' name='pass' id='password'
							placeholder='Введите пароль ' autocomplete='off' required /><br />
						<div class='btn clear'>
							<input id='btn_log' class='btm_big left' type='submit'
								value='Войти'> <input class='btm_big left' type='button'
								id='getPass' value='Забыли пароль?'>
						</div>
					</div>
				</form>
			</div>
		</div>
		<%@ include file="fbfancy.jsp"%>
	</div>
	<%@ include file="foot.jsp"%>
</body>
</html>