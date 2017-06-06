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
<meta name="description" content="Изуаем английские слова">
<meta http-equiv="Cache-Control" content="no-cache">
<meta charset="utf-8">
<meta http-equiv="content-language" content="ru">
<meta name=viewport content="width=device-width, initial-scale=1.0 ">
<link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">

<link href="css/password.css" rel="stylesheet">
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

<script type="text/javascript" src="js/password.js"></script>
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
		<!--navbar -->
		<%@ include file="navbar.jsp"%>
		<div class='main'>
			<div class='unit'>
				<div class='cntr'>
					<div class='result' id='result'></div>
					<form id='formvalid'>
						<input id='email' type='email' name='email'
							placeholder='Введите e-mail' autocomplete='off' required /> <br />
						<div class='btn'>
							<input id='btn_pass' class='btm_big' type='submit'
								value='Получить пароль'>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class='fb_hide div_result_send' id='result_send'>
			<span id='result_send_txt'></span>
		</div>
		<%@ include file="fbfancy.jsp"%>
	</div>
	<%@ include file="foot.jsp"%>
</body>
</html>