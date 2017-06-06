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
<link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="content-language" content="ru">
<meta charset="utf-8">
<meta name=viewport
	content="width=device-width, initial-scale=1, maximum-scale=1">

<link href="css/cours.css" rel="stylesheet">
<link href="css/fonts.css" rel="stylesheet">

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

<script type="text/javascript" src="js/course.js"></script>
<script type="text/javascript" src="js/fbfancy.js"></script>


</head>
<body>
	<div class='start'>
		<header>
			<div class='head1'>Учим слова вмете с нами</div>
			<div class='head2'>
				<img src='img/header.jpg' height='100%' width='100%' />
			</div>
		</header>
		<%@ include file="navbar.jsp"%>
		<div class='main'>
			<div class='head'>
				<div class='yarov'>
					<div class=yarov_txt>
						<div class='love_img'>
							<img src="img/yarov.jpg" alt="" />
						</div>
						<div class='yar_txt'>
							Для изучения английского языка, как самостоятельно, так и с
							репетитором мы рекоммендуем использовать авторский видео курс
							английского языка, разработанный руководителем языковой школы
							LoveLanguages, <br> Ольгой Яровой.
						</div>
					</div>
					<div class='social'>
						<a href='https://vk.com/yarovayaschool_adm' title='vk.com'><img
							class='img_social' src='img/social/vk.png' height='10%'
							width='10%' /></a> <a
							href='http://www.youtube.com/user/yarovayaschoolvideo'
							title='youtube.com'><img class='img_social'
							src='img/social/ytube.png' height='10%' width='10%' /></a> <a
							href='https://www.facebook.com/love.languages'
							title='facebook.com'><img class='img_social'
							src='img/social/face.png' height='10%' width='10%' /></a> <a
							href='https://ok.ru/profile/575112909222' title='ok.ru'><img
							class='img_social' src='img/social/ok.png' height='10%'
							width='10%' /></a> <a href='skype:love.languages2?chat'
							title='Skype: love.languages2'><img class='img_social'
							src='img/social/skype.png' height='10%' width='10%' /></a>
					</div>
				</div>
				<div class='cours'>Что такое видео курс "Нескучной громматики"
					?</div>
				<div class='cours1'>
					<p>Видео курс Нескучной грамматики разрабатывался как
						вспомогательный материал для студентов школы Love Languages. Но к
						нашей радости он пришелся по душе очень многим, и мы очень рады,
						что наши видео уроки помогают многим людям разобраться в
						английской грамматике. Все видеоролики доступны абсолютно
						бесплатно. Подготовленный материал будет хорошим подспорьем, как
						для тех, кто изучает английский язык на групповых занятиях, так и
						для тех, кто занимается самостоятельно. Благодаря емким и разумно
						продуманным видео урокам, вы сможете очень быстро понять принципы,
						заложенные в основу английской грамматики, и полностью разобраться
						в её многочисленных нюансах.</p>
				</div>
				<div class='cours'>Практическая часть курса "Нескучной
					громматики"</div>
				<div class='cours1'>
					<p>
						В отработке и закреплении полученных знаний в видео уроках поможет
						<span class='bigText'>Сборник упражнений по грамматике и
							Ключи к нему</span> . Сборник содержит грамматические упражнения по
						основным темам грамматики английского языка и является логическим
						дополнением к видео курсу Ольги Яровой. Задача сборника - помочь
						учащимся познакомиться с грамматическим строем языка, приобрести
						дополнительный словарный запас, необходимый на данном уровне
						изучения языка, а также приобрести навыки устной и письменной
						речи, перевода с английского языка на русский и с русского языка
						на английский. Упражнения, подобранные к каждой теме, помогут
						усвоить и закрепить материал. Грамматика английского языка очень
						простая и понятная. Просто вам никогда еще ее так не объясняли.
						Совмещайте работу с книгой и проверку себя с ключами и все
						получится. Подробные инструкции самостоятельного изучения языка по
						книге и видео курсу вы найдете на канале Ольги Яровой в YouTube
					</p>
				</div>
				<div class='cours'>Что такое Языковая школа Ольги Яровой
					LoveLanguages?</div>
				<div class='cours1'>
					<p>Вы до сих пор не говорите на английском языке свободно? - Вы
						просто не знаете как! Я и команда моих преподавателей не только
						научим вас легко говорить и правильно писать, но и докажем вам,
						что изучение иностранного языка может быть веселым и легким.
						Просто все должно быть комфортным для вас: темп, методика и
						атмосфера. Мы поможем вам подтянуть знания, разговориться или
						выучить язык с нуля, приходите, вы точно будете довольны. Языковая
						школа Ольги Яровой LoveLanguages предлагает:
					<ul>
						<li>индивидуальные занятия</li>
						<li>занятия в группах</li>
						<li>скайп уроки</li>
						<li>подготовка к IELTS, TOEFL и Marlins</li>
						<li>подготовка к ЗНО и ДПА</li>
						<li>курс нескучной грамматики</li>
						<li>много ярких и интересных мероприятий по выходным дням</li>
					</ul>
					Звоните, приходите, оставайтесь! <br> Ждем Вас в нашей школе!<br>
					С уважением, Ольга Яровая
					</p>
				</div>
			</div>
			<%@ include file="fbfancy.jsp"%>
		</div>
		<%@ include file="foot.jsp"%>
	</div>
</body>
</html>