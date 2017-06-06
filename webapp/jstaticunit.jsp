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
<meta charset="utf-8">
<meta http-equiv="content-language" content="ru">
<meta http-equiv="Cache-Control" content="no-cache">
<meta name=viewport content="width=device-width, initial-scale=1.0 ">
<link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">

<link href="css/staticunit.css" rel="stylesheet">
<link href="css/fonts.css" rel="stylesheet">

<!-- Add jQuery library -->
<script src="js/jquery-3.1.1.min.js"></script>

<!-- Add fancyBox -->
<link rel="stylesheet" href="js/fancybox/source/jquery.fancybox.css"
	type="text/css" media="screen" />
<script type="text/javascript"
	src="js/fancybox/source/jquery.fancybox.pack.js"></script>

<script type="text/javascript"
	src="js/jquery-validation-1.15.0/dist/jquery.validate.min.js"></script>

<script src="js/staticunit.js"></script>
<script type="text/javascript" src='js/fbfancy.js'></script>
<!-- property for EL -->
<c:set var="irregularVerbs" scope="page" value='Неправильные глаголы' />
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
			<div class='frm_unit'>
				<c:choose>
					<c:when test="${empty sessionScope.user == false}">
						<p class='myName'>My name is ${sessionScope.user.uname}</p>
					</c:when>
				</c:choose>
				<div class='error' id='error'></div>

				<form id="unit" enctype="text/plain" action='test.html'>
					<div class='btvDiv'>
						<button class='btm_big1' id='checked' type='button'>выбрать
							все</button>
						<button class='btm_big1' type='reset'>сбросить</button>
						<button class='btm_big1 dsbl' id='startTest' type='sabmit'
							disabled>начать тест</button>
					</div>

					<div class='frm_in'>
						<c:choose>
							<c:when
								test="${requestScope.groupname == pageScope.irregularVerbs}">
								<!-- если тест на неправильные вариант теста по умолчанию вариант -->
								<div class='hide'>
									<input type='radio' name='test' value='variant' checked>Варианты
								</div>
							</c:when>
							<c:otherwise>
								<!-- если тест НЕ на неправильные вариант теста на выбор -->
								<p>Выберите вариант теста</p>
								<div class='typeRadio'>
									<input type='radio' name='test' value='variant' checked>
									<span>Варианты</span>
								</div>
								<div class='typeRadio'>
									<input type='radio' name='test' value='vvod'> <span>Ввод
										слова</span>
								</div>
							</c:otherwise>
						</c:choose>
						<p>Выберите направление перевода теста</p>
						<div class='typeRadio'>
							<input type='radio' name='lang' value='Ru' checked> <span>Ru-to-En</span>
						</div>
						<div class='typeRadio'>
							<input type='radio' name='lang' value='En'> <span>En-to-Ru</span>
						</div>
						<p>${requestScope.groupname}</p>
						<c:choose>
							<c:when test="${empty sessionScope.user == false}">
								<div class='unitdiv'>
									<c:forEach items="${resultListStaticRating}" var="staticrating">
										<div class='oneUnit'>
											<div>
												<input type='checkbox' name='idstatcat'
													value='${staticrating.staticcategory.id}'> <a
													class='unitName' name='${staticrating.staticcategory.id}'
													href='#'>${staticrating.staticcategory.unitname}</a>
											</div>
											<div class='wordsEdit clear'>
												<div class='wordEditIn'>
													<span class='${staticrating.id} rating'>результат:
														${staticrating.percent}%</span>
												</div>
												<div class='wordEditIn'>
													<a class='rating' name='${staticrating.id}' href='#'>обнулить
														результат</a>
												</div>
											</div>
											<!--Поле для вывода слов Unit -->
											<div class='${staticrating.staticcategory.id}word spisword'
												style="display: none;"></div>
										</div>
									</c:forEach>
								</div>
							</c:when>
							<c:otherwise>
								<div class='unitdiv'>
									<c:forEach items="${resultListStaticCat}" var="staticunits">
										<div class='oneUnit'>
											<div>
												<input type='checkbox' name='idstatcat'
													value='${staticunits.id}'> <a class='unitName'
													name='${staticunits.id}' href='#'>${staticunits.unitname}</a>
											</div>
											<!--Поле для вывода слов Unit -->
											<div class='${staticunits.id}word spisword'
												style="display: none;"></div>
										</div>
									</c:forEach>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</form>
			</div>
		</div>
		<%@ include file="fbfancy.jsp"%>
	</div>
	<%@ include file="foot.jsp"%>
</body>
</html>

