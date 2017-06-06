$(document).ready(function() {

	// установливаем обработчик события resize
	$(window).resize(function() {
		var size = $(window).height();
		// $( "#result2" ).text( $( window ).height() );
		$(".start").css("min-height", size - 50);
	});

	// вызовем событие resize
	$(window).resize();

	// валидация форм
	jQuery.validator.setDefaults({
		debug : true,
		success : "valid"
	});

	// делаем окно фэнсибокс на резудьтат отправки пароля
	function fancyResult(t) {
		$('#result_send_txt').html(t);
		$.fancybox($('#result_send'), {
			topRatio : 0.2,
			autoResize : true,
			afterClose : function() {
				$('#result_send_txt').html('');

			}
		});
		setTimeout(function() {
			$.fancybox.close();

		}, 1000 * 5);

	}

	$("#formvalid").validate({
		/*
		 * success: function(label) { label.addClass("valid").text("Ok!") },
		 */
		// focusCleanup: true,
		rules : {
			email : {
				required : true,
				email : true
			}
		},
		messages : {
			email : {
				required : 'Заполните поле email*',
				email : 'Некорректно заполнено поле*'
			}
		},
		submitHandler : function(form) {
			var dataform = $(form).serialize();
			// alert(dataform);
			$('#email').val('');
			$.get({
				url : 'password.html',
				data : dataform + '&a=' + Math.random(),
				headers : {
					'Cache-Control' : 'no-cache',
				},
				success : function(rJson) {
					if (rJson.msg != null) {
						$('#result').text(rJson.msg);
						setTimeout(function() {
							window.location.href = 'index.html';
						}, 1000 * 4);
						return;
					}
					if (rJson.error != null) {
						$('#result').text(rJson.error);
						return;
					}
				}
			});
		}
	});

})
