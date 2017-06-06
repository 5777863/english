$(document)
		.ready(
				function() {

					// установливаем обработчик события resize
					$(window).resize(function() {
						var size = $(window).height();
						$(".start").css("min-height", size - 50);
					});

					// вызовем событие resize
					$(window).resize();

					// валидация форм
					jQuery.validator.setDefaults({
						debug : true,
						success : "valid"
					});

					// показывает блок процесса выполнения загрузки
					function showLoader() {
						$('#loadingDiv').show();
					}

					// скрывает блок процесса выполнения загрузки
					function hideLoader() {
						$('#loadingDiv').hide();
					}

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
					}

					$("#registration")
							.validate(
									{
										/*
										 * success: function(label) {
										 * label.addClass("valid").text("Ok!") },
										 */
										// focusCleanup: true,
										rules : {
											name : {
												required : true,
												maxlength : 30,
											},
											email : {
												required : true,
												email : true
											},
											pass1 : {
												required : true,
												minlength : 6,
												maxlength : 30,
											},
											pass2 : {
												required : true,
												equalTo : "#pass1",
											}
										},
										messages : {
											name : {
												required : 'Заполните поле пароль*',
												maxlength : 'Максимпльная длинна пароля 30 символов*'
											},
											email : {
												required : 'Заполните поле email*',
												email : 'Некорректно заполнено поле*'
											},
											pass1 : {
												required : 'Заполните поле пароль*',
												minlength : 'Минимальная длинна пароля 6 символов*',
												maxlength : 'Максимпльная длинна пароля 30 символов*'
											},
											pass2 : {
												required : 'Заполните поле повторного ввода пароля*',
												equalTo : 'Пароль не совпадает*'
											}
										},
										submitHandler : function(form) {
											var data = $(form).serialize();
											showLoader();
											// alert(data);
											$("[type='text']").val('');
											$("[type='email']").val('');
											$("[type='password']").val('');
											$
													.get(
															'registration.html',
															data
																	+ '&a='
																	+ Math
																			.random(),
															function(rJson) {

																/*
																 * alert(JSON.stringify(rJson) +
																 * '1');
																 * alert(rJson.error +
																 * '2');
																 * alert('type'+typeof
																 * rJson.error);
																 */

																if (rJson.error != null) {
																	$('#result')
																			.text(
																					rJson.error)
																	hideLoader();
																} else {
																	if (rJson.result != null) {
																		hideLoader();
																		$(
																				'#result')
																				.text(
																						rJson.result)
																		setTimeout(
																				function() {
																					window.location.href = 'index.html';
																				},
																				1000 * 5);
																	} else {
																		window.location.href = 'index.html';
																	}
																}
															});
										}
									});
				})
