$(document).ready(function() {

	// установливаем обработчик события resize
	$(window).resize(function() {
		var size = $(window).height();
		// $( "#result2" ).text( $( window ).height() );
		$('.start').css('min-height', size - 50);

	});

	// вызовем событие resize
	$(window).resize();

	// валидация форм
	jQuery.validator.setDefaults({
		debug : true,
		success : "valid"
	});

	// переход на страницу password.html
	$('#getPass').click(function() {
		window.location.href = 'password.html';
	});

	$("#login").validate({
		/*
		 * success: function(label) { label.addClass("valid").text("Ok!") },
		 */
		// focusCleanup: true,
		rules : {
			email : {
				required : true,
				email : true
			},
			pass : {
				required : true,
				minlength : 6,
			}
		},
		messages : {
			email : {
				required : 'Заполните поле email*',
				email : 'Некорректно заполнено поле*'
			},
			pass : {
				required : 'Заполните поле пароль*',
				minlength : 'Минимальная длинна пароля 6 символов*'
			}
		},
		submitHandler : function(form) {
			var data = $(form).serialize();
			$.get('login.html', data + '&a=' + Math.random(), function(rJson) {
				if (rJson.msg != null) {
					// если учетные данные введены неверные
					$('#result').html(rJson.msg);
					$('#email').val('');
					$('#password').val('');
					$('#email').focus();
					return;
				}
				if (rJson.errorpg != null) {
					window.location.href = rJson.errorpg;
				} else {
					 window.location.href = 'index.html';
				}
			});
		}
	});
})
