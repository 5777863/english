$(document).ready(function() {
	// установливаем обработчик события resize

	$(window).resize(function() {
		var size = $(window).height();
		// $( "#result2" ).text( $( window ).height() );
		$(".start").css("min-height", size - 50);
	});

	// вызовем событие resize
	$(window).resize();

	// click на на unit, разворачивающийся список
	$('.unitName').click(function(e) {// [type=button]
		e.preventDefault();
		var attr = $(this).attr('name');
		var iddiv = 'div.' + attr + 'word';
		var data = 'staticcategoryId=' + attr;
		$.get('staticunit.html', data + '&a=' + Math.random(), function(rJson) {
			if (rJson.errorpg != null) {
				window.location.href = rJson.errorpg;
			} else {
				var spisok = '';
				for (i in rJson) {
					spisok = spisok + rJson[i] + '<br>';
				}
				$(iddiv).html(spisok);
				$(iddiv).slideToggle(1000, function() {
				});
			}
		}, 'json');
	})

	// click на сбросить оценку
	$('.rating').click(function(e) {// 
		e.preventDefault();
		var attr = $(this).attr('name');
		var data = 'ratingreset=' + attr;
		$.get('json/staticunit.htm', data + '&a=' + Math.random(), function(rJson) {
			if (rJson.redirect != null) {
				window.location.href = rJson.redirect;
			} else {
				if (rJson.result != null) {
					$('span.' + attr).text('результат: ' + rJson.result + '%');
				}
			}
		}, 'json');
	})

	// кнопка сбросить
	$("[type='reset']").click(function() {
		// function dsbl() {
		// делает кнопку начать тест НЕактивной
		$('.dsbl').attr('disabled', 'true');
	})

	// делает кнопки начать тест active/disbl при
	// нажатии на checkbox
	$("[type='checkbox']").click(function() {
		var counter = 0;
		var check = $("[type='checkbox']");
		for (var i = 0; i < check.length; i++) {
			if (check[i].type == 'checkbox') {
				if (check[i].checked == true) {
					counter++
				}
			}
			if (counter > 0) {
				$('.dsbl').removeAttr('disabled');
			} else {
				$('.dsbl').attr('disabled', 'true');
			}
		}
	})

	// кнопка выбрать все
	$('#checked').click(function() {
		// var check = document.getElementsByTagName('input');
		var check = $("[type='checkbox']");
		for (var i = 0; i < check.length; i++) {
			if (check[i].type == 'checkbox') {
				check[i].checked = true;
			}
		}
		$('.dsbl').removeAttr('disabled');
	})

	// обработка формы выбора уроков
	$("#startTest").click(function(e) {
		e.preventDefault();
		var data = $('#unit').serialize();
		$.get('staticunit.html', data + '&a=' + Math.random(), function(rJson) {
			if (rJson.redirect != null) {
				window.location.href = rJson.redirect;
			} else {
				if (rJson.msg != null) {
					$('#error').html(rJson.msg);
				} else {
					var words = JSON.stringify(rJson.arraywords);
					var result = JSON.stringify(rJson.resultToPage);
					sessionStorage.words = words;
					sessionStorage.result = result;
					// alert(words);
					sessionStorage.lng = $('input[name=lang]:checked').val();
					sessionStorage.test = $('input[name=test]:checked').val();
					window.location.href = 'teststatic.html';
				}
			}

		});
	});
});