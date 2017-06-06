$(document)
		.ready(
				function() {
					// установливаем обработчик события resize
					$(window).resize(function() {
						var size = $(window).height();
						// $( "#result2" ).text( $( window ).height() );
						$(".start").css("min-height", size - 50);
					});

					// вызовем событие resize
					$(window).resize();

					// click на unit, разворачивающийся список
					$('.unitName')
							.click(
									function(e) {// [type=button]
										e.preventDefault();
										var attr = $(this).attr('name');
										var iddiv = 'div.' + $(this).text()
												+ 'word';
										var data = 'unitId=' + attr;
										$
												.get(
														'wordarray.html',
														data + '&a='
																+ Math.random(),
														function(rJson) {
															if (rJson.redirect != null) {
																window.location.href = rJson.redirect;
															} else {
																var spisok = '';
																for (i in rJson) {
																	spisok = spisok
																			+ rJson[i]
																			+ '<br>';
																}
																$(iddiv).html(
																		spisok);
																$(iddiv)
																		.slideToggle(
																				1000,
																				function() {
																				});
															}
														});
									})

					// click на на unitRename, переименовать unit
					$('.unitRename').click(
							function(e) {
								e.preventDefault();
								var attr = $(this).attr('name');
								$('#resultRnm').text(
										'Введите новое название для урока '
												+ attr);
								$('#unitOldName').val(attr);
								$.fancybox($('.frmrnm'), {
									topRatio : 0.2,
									autoResize : true,
									afterClose : function() {
										$('#resultRnm').text('');
										$('#newUnit').val('');
									},
								});
							})

					// click на unitDel, удалить unit
					$('.unitDel')
							.click(
									function(e) {
										e.preventDefault();
										var attr = $(this).attr('name');
										$('#resultDel')
												.text(
														'При удалении '
																+ attr
																+ ', все его содержимое будет удалено');
										$('#unitDel').val(attr);
										$.fancybox($('.frmdel'), {
											topRatio : 0.2,
											autoResize : true,
										});
									})

					// click на кнопку подтверждения удаления урока
					$('#del_sbm').click(
							function(e) {
								e.preventDefault();
								var data = $('#frm_unit_del').serialize();
								$.get('json/dictionary.html', data + '&a='
										+ Math.random(), function(rJson) {
									if (rJson.redirect != null) {
										window.location.href = rJson.redirect;
										return;
									}
									if (rJson.msg != null) {
										$('#resultDel').text(rJson.msg);
										$('#del_sbm').hide();
									}
								})
							})

					// click на сбросить оценку
					$('.rating').click(
							function(e) {// 
								e.preventDefault();
								var attr = $(this).attr('name');
								var data = 'ratingreset=' + attr;
								$.get('json/dictionary.html', data + '&a='
										+ Math.random(), function(rJson) {
									if (rJson.redirect != null) {
										window.location.href = rJson.redirect;
									} else {
										if (rJson.result != null) {
											$('span.' + attr).text(
													'результат: '
															+ rJson.result
															+ '%');
										}
									}
								}, 'json');
							})

					// расскрытие поля добавл. нов.урока
					$(".addhide").hide();
					$("#unitAdd").click(function() {
						$(".addhide").slideToggle();
					})

					// валидация форм
					jQuery.validator.setDefaults({
						debug : true,
						success : "valid"
					});

					// click на применить вокне переименования
					// !!!! делая валиацию формы в окне фэнсибокс, важно
					// что бы кнопка сабмит была под полями, а не справо,
					// иначе нужно нажимать дважды
					$('#frm_unit_rnm')
							.validate(
									{
										rules : {
											newUnit : {
												required : true,
												maxlength : 40,
											}
										},
										messages : {
											newUnit : {
												required : 'Заполните поле *',
												maxlength : 'Максимальная длина имени 40 символов *',
											}
										},

										submitHandler : function(form) {
											var data = $(form).serialize();
											// $('#unitOldName').val('');
											$
													.get(
															'unitrename.html',
															data
																	+ '&a='
																	+ Math
																			.random(),
															function(rJson) {
																if (rJson.redirect != null) {
																	window.location.href = rJson.redirect;
																	return;
																}

																if (rJson.msg != null) {
																	$(
																			'#resultRnm')
																			.text(
																					rJson.msg);
																	return;
																}
															});
										}
									})

					// валидация формы нового урока
					$('#frmAdd')
							.validate(
									{
										rules : {
											newUnit : {
												required : true,
												maxlength : 40,
											}
										},
										messages : {
											newUnit : {
												required : 'Заполните поле *',
												maxlength : 'Максимальная длина имени 40 символов *',
											}
										},

										submitHandler : function(form) {
											var data = $(form).serialize();
											$('#inpuUnitAdd').val('');
											$
													.get(
															'unitadd.html',
															data
																	+ '&a='
																	+ Math
																			.random(),
															function(rJson) {
																if (rJson.redirect != null) {
																	window.location.href = rJson.redirect;
																	return;
																}
																if (rJson.msg != null) {
																	$(
																			'#intAddResult')
																			.html(
																					rJson.msg);
																	$(
																			'#intAddResult')
																			.css(
																					{
																						color : 'black',
																						transition : 'color 1s'
																					});
																}
															});
										}
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
					// валидация формы загрузки файла
					$('#file')
							.click(
									function(event) {
										event.preventDefault();
										var filesExt = [ 'txt' ]; // массив
										// расширений

										var parts = $('input[type=file]').val()
												.split('.');
										var file_size = $('input[type=file]')[0].files[0].size;
										// alert($('input[type=file]')[0].files[0].lastModified);
										var formData = new FormData();
										jQuery.each($('#file_v')[0].files,
												function(i, file) {
													// fileName-имя фала для
													// обработки в сервлете
													formData.append('fileName',
															file);
												});
										if (filesExt.join().search(
												parts[parts.length - 1]) != -1) {
											if (file_size < 1 * 1024 * 1025) {
												showLoader();
												$
														.post({
															url : 'upload',
															data : formData,
															cache : false,
															contentType : false,
															processData : false,
															headers : {
																'Cache-Control' : 'no-cache',
															},
															success : function(
																	rJson) {
																hideLoader();
																if (rJson.redirect != null) {
																	window.location.href = rJson.redirect;
																}
															}
														});

											} else {
												fancyResult('Превышен максимальный размер файла в 1Мб');
											}
										} else {
											fancyResult('Неверный формат файла');
										}
									});

					// обработка формы выбора уроков
					$("#startTest").click(
							function(e) {
								e.preventDefault();
								var data = $('#unit').serialize();
								$.get('json/test.html', data + '&a='
										+ Math.random(), function(rJson) {
									if (rJson.redirect != null) {
										window.location.href = rJson.redirect;
									}
									if (rJson.msg != null) {
										$('#error').html(rJson.msg);
									} else {
										var words = JSON
												.stringify(rJson.arraywords);
										var result = JSON
												.stringify(rJson.resultToPage)
										sessionStorage.words = words;
										sessionStorage.result = result;
										// alert(words);
										sessionStorage.lng = $(
												'input[name=lang]:checked')
												.val();
										sessionStorage.test = $(
												'input[name=test]:checked')
												.val();
										window.location.href = 'test.html';
									}
								});
							});
				});