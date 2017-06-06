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
<meta name=viewport content="width=device-width, initial-scale=1.0 ">
<link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">

<link href="css/dictionary.css" rel="stylesheet">
<link href="css/search.css" rel="stylesheet">
<link href="css/fonts.css" rel="stylesheet">
<link href="css/valid.css" rel="stylesheet">
<link href="css/typeFile.css" rel="stylesheet">

<!-- Add jQuery library -->
<script src="js/jquery-3.1.1.min.js"></script>

<!-- Add fancyBox -->
<link rel="stylesheet" href="js/fancybox/source/jquery.fancybox.css"
	type="text/css" media="screen" />
<script type="text/javascript"
	src="js/fancybox/source/jquery.fancybox.pack.js"></script>

<script type="text/javascript"
	src="js/jquery-validation-1.15.0/dist/jquery.validate.min.js"></script>

<script src="js/dictionary.js"></script>
<script src="js/search.js"></script>
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
			<div class='unit clear'>

				<div class='unitAdd'>
					<button class='btm_font' id='unitAdd' type='button'>Добавить
						словарь</button>
					<div class='addhide clear'>
						<div class='frmfileAdd clear'>
							<p>Загрузка словаря файлом</p>
							<!-- форма загрузки файла -->
							<form id='formFile' name="sendform" enctype="multipart/form-data"
								action="engl" method="post">
								<div class="file-upload">
									<label> <input id="file_v" class='fileAdd' type="file"
										name="fileName"> <span class='file'>Выберите
											файл</span>
									</label>
								</div>
								<input class='btmFile btm_font' id='file' type="submit"
									value="отправить">
							</form>
						</div>
						<div class='formHand clear'>
							<p>Создание словаря вводом</p>
							<!-- форма добавления нового урока -->
							<form id='frmAdd'>
								<input id='inpuUnitAdd' type='text' name='newUnit'
									placeholder='введите название словаря'> <input
									class='btm_font' type='submit' value='добавить'>
							</form>
						</div>
					</div>

				</div>

				<div class='search'>
					<div class="search_area">
						<form>
							<input type="text" name="query" id="search_box" value=""
								autocomplete="off" placeholder='найти слово в моем словаре'>
						</form>
						<div id="search_advice_wrapper"></div>
						<div id="final"></div>
					</div>
				</div>

			</div>

			<div class='frm_unit'>
				<p class='myName'>My name is ${sessionScope.user.uname}</p>
				<p>Мои тесты</p>
				<p>Количество словарей: ${fn:length(resultListUnit)}</p>
				<div class='error' id='error'></div>
				<!-- Отображаем форму с тестами, только если тесты существуют -->
				<c:choose>
					<c:when test="${fn:length(resultListUnit)!=0}">
						<form id="unit" enctype="text/plain" action='test.html'>
							<div class='btvDiv'>
								<button class='btm_big1' id='checked' type='button'>выбрать
									все</button>
								<button class='btm_big1' type='reset'>сбросить</button>
								<button class='btm_big1 dsbl' id='startTest' type='sabmit'
									disabled>начать тест</button>
							</div>

							<div class='frm_in'>
								<p>Выберите вариант теста</p>
								<div class='typeRadio'>
									<input type='radio' name='test' value='variant' checked>
									<span>Варианты</span>
								</div>
								<div class='typeRadio'>
									<input type='radio' name='test' value='vvod'> <span>Ввод
										слова</span>
								</div>
								<p>Выберите направление перевода теста</p>
								<div class='typeRadio'>
									<input type='radio' name='lang' value='Ru' checked> <span>Ru-to-En</span>
								</div>
								<div class='typeRadio'>
									<input type='radio' name='lang' value='En'> <span>En-to-Ru</span>
								</div>

								<div class='unitdiv'>
									<c:forEach items="${resultListUnit}" var="units">
										<div class='oneUnit'>
											<div>
												<input type='checkbox' name='unit' value='${units.unit}'>
												<a class='unitName' name='${units.id}' href='#'>${units.unit}</a>
											</div>
											<div class='wordsEdit clear'>
												<div class='wordEditIn'>
													<a class='unitRename' name='${units.unit}' href='#'>переименовать</a>
												</div>
												<div class='wordEditIn'>
													<a class='' name='${units.id}'
														href='unitedit.html?unit=${units.id}'>редакитровать</a>
												</div>
												<div class='wordEditIn'>
													<a class='unitDel' name='${units.unit}' href='#'>удалить</a>
												</div>
												<div class='wordEditIn'>
													<span class='${units.id} rat'>результат:
														${units.percent}%</span>
												</div>
												<div class='wordEditIn'>
													<a class='rating' name='${units.id}' href='#'>обнулить
														результат</a>
												</div>
											</div>
											<!--Поле для вывода слов Unit -->
											<div class='${units.unit}word spisword'
												style="display: none;"></div>
										</div>
									</c:forEach>
								</div>
							</div>

						</form>
					</c:when>
				</c:choose>

			</div>
			<div class='statictest'>
				<p>Встроенные тесты</p>
				<c:forEach items="${resultListGroupname}" var="groupname">
					<div>
						<a class='staticUnit'
							href='staticunit.html?groupname=${groupname}'>${groupname}</a>
					</div>
				</c:forEach>
			</div>
			<div class='hide size frmrnm'>
				<!--форма переименования в fancybox-->
				<p id='resultRnm' class='center'></p>
				<form id='frm_unit_rnm' class='center'>
					<input id='unitOldName' type='hidden' name='oldUnit' value=''>
					<input class='rnmText' type='text' autocomplete='off'
						name='newUnit' id='newUnit' placeholder='максимум 40 сиволов '
						value=''> <input class='btm_big2' type='submit'
						value='применить'>
				</form>
			</div>
			<div class='hide size frmdel'>
				<!--форма удаления в fancybox-->
				<p id='resultDel' class='center'></p>
				<form id='frm_unit_del' class='center' action='dictionary.html'
					method='get'>
					<input id='unitDel' type='hidden' name='unitdel' value=''>
					<input id='del_sbm' class='btm_big2' type='submit'
						value='продолжить'>
				</form>
			</div>
			<div class='fb_hide div_result_send' id='result_send'>
				<span id='result_send_txt'></span>
			</div>
			<%@ include file="fbfancy.jsp"%>
		</div>
	</div>
	<%@ include file="foot.jsp"%>
</body>
</html>
