$(document).ready(function() {

	// делаем окно фэнсибокс на фидбэк
	$('#feedback').fancybox({
		topRatio : 0.2,
		autoResize : true,
		afterClose : function() {
			$('#fb_result').text('');
			$('#fb_txtarea').val('');
			$('#frm_fb').show();
		},
	});

	// валидация форм
	jQuery.validator.setDefaults({
		debug : true,
		success : "valid"
	});

	// валидация формы фидбэк
	$("#frm_fb").validate({
		/*
		 * success: function(label) { label.addClass("valid").text("Ok!") },
		 */
		// focusCleanup: true,
		rules : {
			text : {
				required : true,
				maxlength : 300,
			}
		},
		messages : {
			text : {
				required : 'Заполните поле*',
				maxlength : 'максимально 300 символов*'
			}
		},
		submitHandler : function(form) {
			var dataform = $(form).serialize();
			// отправка формы feedback
			$.get({
				url : 'feedback.html',
				data : dataform,
				headers : {
					'Cache-Control' : 'no-cache',
				},
				success : function(rJson) {
					if (rJson.result != null) {
						$('#frm_fb').css('display', 'none');
						$('#fb_result').text(rJson.result);
						// плавное появление надписи
						$('#fb_result').css({
							color : 'black',
							transition : 'color 2s'
						});
					} else {
						if (rJson.errorpg != null) {
							window.location.href = rJson.errorpg;
						}
					}
				},
				dataType : 'json'
			});
		}
	});
})