window.onload = function() {
	var suggest_count = 0;
	var input_initial_value = '';
	var suggest_selected = 0;

	// метод определяет раскладку ввода
	function wtfLang(text) {
		if (/[а-я]/i.test(text)) {
			return 'ru';
		} else {
			return 'en';
		}
	}
	// читаем ввод с клавиатуры
	$("#search_box").keyup(
			function(I) {
				// определяем какие действия нужно
				// делать при нажатии на клавиатуру
				switch (I.keyCode) {
				// игнорируем нажатия на эти клавишы
				case 13: // enter
				case 27: // escape
				case 37: // стрелка влево
				case 38: // стрелка вверх
				case 39: // стрелка вправо
				case 40: // стрелка вниз
					break;
				default:

					// очищаем и скрываем поле с результатом если
					// очищаем поле ввода
					if ($(this).val() == "") {
						$('#final').html("").hide();
						suggest_count = null;
					}

					// производим поиск только при вводе
					// более 2х символов
					if ($(this).val().length > 1) {
						input_initial_value = $(this).val();
						$.get("search.html", 'search=' + $(this).val()
								+ '&lang=' + wtfLang($(this).val()), function(
								jsnArray) {
							// если пришел
							// редирект
							if (jsnArray.redirect != null) {
								window.location.href = jsnArray.redirect;
								return;
							}
							if (jsnArray.error != null) {
								window.location.href = jsnArray.error;
							} else {
								suggest_count = jsnArray.length;
								if (suggest_count > 0) {
									$("#search_advice_wrapper").html("")
									for ( var i in jsnArray) {
										if (jsnArray[i] != '') {
											$('#search_advice_wrapper').append(
													'<div class="advice_variant">'
															+ jsnArray[i]
															+ '</div>');
										}
									}
									$("#search_advice_wrapper").show(500);
								}
							}
						}, 'json');
					}
					break;
				}
			});

	// считываем нажатие клавишь, уже после вывода подсказки
	$("#search_box").keydown(function(I) {
		switch (I.keyCode) {
		// по нажатию клавишь прячем подсказку
		case 27: // escape
			$('#search_advice_wrapper').hide(500);
			return false;
			break;
		case 13: // enter
			I.preventDefault();
			// запрос на слова
			search($("#search_box").val());
			$('#search_advice_wrapper').hide(500);
			return false;
			break;
		// делаем переход по подсказке стрелочками клавиатуры
		case 38: // стрелка вверх
		case 40: // стрелка вниз
			I.preventDefault();
			if (suggest_count) {
				// делаем выделение пунктов в слое, переход по стрелочкам
				key_activate(I.keyCode - 39);
			}
			break;
		}
	});

	// делаем обработку клика по подсказке
	$('body').on('click', 'div.advice_variant', function() {
		// ставим текст в input поиска
		$('#search_box').val($(this).text());
		search($("#search_box").val());
		// прячем слой подсказки
		$('#search_advice_wrapper').fadeOut(350).html('');
	});

	// если кликаем в любом месте сайта, нужно спрятать подсказку
	$('html').click(function() {
		$('#search_advice_wrapper').hide(500);
	});
	// если кликаем на input и есть пункты подсказки, то показываем скрытый слой
	$('#search_box').click(function(event) {
		// alert(suggest_count);
		if (suggest_count)
			$('#search_advice_wrapper').show(500);
		event.stopPropagation();
	});

	function key_activate(n) {
		$('#search_advice_wrapper div').eq(suggest_selected - 1).removeClass(
				'active');
		if (n == 1 && suggest_selected < suggest_count) {
			suggest_selected++;
		} else if (n == -1 && suggest_selected > 0) {
			suggest_selected--;
		}
		if (suggest_selected > 0) {
			$('#search_advice_wrapper div').eq(suggest_selected - 1).addClass(
					'active');
			$("#search_box").val(
					$('#search_advice_wrapper div').eq(suggest_selected - 1)
							.text());
		} else {
			$("#search_box").val(input_initial_value);
		}
	}

	// запрос возвращает весь списко, куда входит выбранное слово
	function search(data) {
		$.get('search.html', 'words=' + data, function(jsnArray) {
			// если пришел
			// редирект
			if (jsnArray.redirect != null) {
				window.location.href = jsnArray.redirect;
				return;
			}
			if (jsnArray.error != null) {
				window.location.href = jsnArray.error;
			} else {
				kolvo = jsnArray.length;
				console.log(kolvo);
				if (kolvo > 0) {
					$('#final').html("").show();
					for ( var i in jsnArray) {
						if (jsnArray[i] != '') {
							$('#final').append(jsnArray[i] + '<br>');
						}
					}
				}
			}
		});
	}

}