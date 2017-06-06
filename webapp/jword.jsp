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

<link href="css/word.css" rel="stylesheet">
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
<script src="js/word.js"></script>
<script type="text/javascript" src='js/fbfancy.js'></script>
</head>
<body>
	<div id="loadingDiv" class="loader"></div>
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

			<div class='divWordAdd clear'>
				<form id='newordfrm' method='post' action='unitedit.html'>
					<div id='inputnewadd'>
						<div class=divnewadd>
							<div class='inputxt'>
								<input type='hidden' name='unitName' value='${unitName}'>
							</div>
							<div class='inputxt'>
								<input type="text" name="word" placeholder='английское слово'
									maxlength='50' required autocomplete='off'> <input
									type="text" name="translate" placeholder='перевод'
									maxlength='50' required autocomplete='off'>
							</div>
						</div>
					</div>
					<div class='btnadd'>
						<input class='btm_big' type="button" id="add" value='+'> <input
							class='btm_big' type="button" id="remove" value='-'> <input
							class='btm_big1Spec' id='newwordsend' type="submit"
							value='добавить'>
					</div>
				</form>
			</div>
			<div class='frm_unit'>
				<form id="words" method='get' action='unitedit.html'>
					<button class='btm_big1' type='reset'>сбросить</button>
					<button class='btm_big1' id='checked' type='button'>выбрать
						все</button>
					<button class='btm_big1 dsbl' id='copyWord' type='button' disabled>копировать</button>
					<button class='btm_big1 dsbl' id='moveword' name=delword
						type='submit' disabled value='delword'>удалить</button>
					<br>
					<div class='frm_in'>
						<p id='unitname'>${unitName}</p>
						<input type='hidden' name='unitname' value='${unitName}'>
						<c:forEach items="${resultListWord}" var="word">
							<div class='divWord clear'>
								<div class='oneWord'>
									<input type='checkbox' name='word' value='${word.id}'>
									<p class='word'>${word.word}-${word.translate}</p>
								</div>
								<div class='wordEditIn'>
									<a id='${word.word}' class='wordRename'
										name='${word.translate}' href='#'>изменить</a>
								</div>
							</div>

						</c:forEach>
					</div>
				</form>
				<!-- значение присваевается после выбора юнита -->
				<input type='hidden' id='unitforcopy' name='unitforcopy' value=''>
				<!-- список unitов пользователя возвращаемыйджесон массивом после нажатия кнопки копировать -->
				<div class='size hide left' id='spisokunit'>
					<div id='spisokunit2' class='spisokunit2'></div>
					<div class='center'>
						<button class='btm_big1 dsbl2' id='copyWordNext' type='button'
							disabled>Продолжить</button>
					</div>
				</div>
				<!-- подтверждение удаления слов -->
				<div class='hide size center' id='frmdel'>
					<!--форма удаления в fancybox-->
					<p>Для подтверждения удаления выбранных слов нажмите продолжить</p>
					<input id='del_sbm' class='btm_big2' type='submit'
						value='продолжить'>
				</div>
			</div>
		</div>
		<div class='hide size' id='frmrename'>
			<!--форма переименования в fancybox-->
			<p id='result' class='center'></p>
			<p id='msg' class='center'></p>
			<form id='frm_word_rnm' class='center'>
				<input type='hidden' name='unitname' value='${unitName}'> <input
					id='oldWord' type='hidden' name='oldWord' value=''> <input
					id='oldTranslate' type='hidden' name='oldTranslate' value=''>
				<input type='text' autocomplete='off' name='newWord' id='newWord'
					value=''> <input type='text' autocomplete='off'
					name='newTranslate' id='newTranslate' value=''> <input
					class='btm_big2' type='submit' value='применить'>
			</form>
		</div>
		<%@ include file="fbfancy.jsp"%>
	</div>
	<%@ include file="foot.jsp"%>
</body>
</html>