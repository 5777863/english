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

					// показывает блок процесса выполнения загрузки
					function showLoader() {
						$('#loadingDiv').show();
					}

					// скрывает блок процесса выполнения загрузки
					function hideLoader() {
						$('#loadingDiv').hide();
					}

					// click на -
					$('#remove').click(function() {
						if ($('.divnewadd').length == 1) {
							$('#remove').attr('disabled', 'true')
						} else {
							$('.divnewadd').last().remove();
						}
						if ($('.divnewadd').length < 20) {
							$('#add').removeAttr('disabled');
						}

					})

					// click на +
					$('#add')
							.click(
									function() {
										$('#inputnewadd')
												.append(
														"<div class=divnewadd><input type='text' name='word' placeholder='английское слово' maxlength='50' required autocomplete='off'> <input type='text' name='translate' placeholder='перевод' maxlength='50' required autocomplete='off'></div>");
										if ($('.divnewadd').length > 1) {
											$('#remove').removeAttr('disabled');
										}
										if ($('.divnewadd').length == 20) {
											$('#add').attr('disabled', 'true');
										}
									})

					// кнопка сбросить
					$("[type='reset']").click(function() {
						// делает кнопки копировать и удалить активными
						$('.dsbl').attr('disabled', 'true');
					})

					// делает кнопки копировать и удалить active/disbl при
					// нажатии на checkbox
					$("[type='checkbox']").click(function() {
						var counter = 0;
						var check = $("[type='checkbox']");
						for ( var i in check) {
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
						for ( var i in check) {
							check[i].checked = true;
						}
						$('.dsbl').removeAttr('disabled');
					})

					// click на moveword, удаление слов
					$('#moveword').click(function(e) {
						e.preventDefault();
						$.fancybox($('#frmdel'), {
							topRatio : 0.2,
							autoResize : true,
						});
					})

					// click на продолжить в окне удаление слов
					$('#del_sbm').click(
							function(e) {
								showLoader();
								$.get('json/unitedit.html', $('#words')
										.serialize()
										+ '&delword=delword'
										+ '&a='
										+ Math.random(), function(rJson) {
									hideLoader();
									if (rJson.redirect != null) {
										window.location.href = rJson.redirect;
									}
								});

							})

					// click на wordRename, изменть слово
					$('.wordRename').click(
							function(e) {
								e.preventDefault();
								var oldWord = $(this).attr('id');
								var oldTranslate = $(this).attr('name');
								$('#msg').text('');
								$('#result').html(
										'Введите новое название для  '
												+ oldWord + ' - '
												+ oldTranslate);
								$('#oldWord').val(oldWord);
								$('#oldTranslate').val(oldTranslate);
								$('#newWord').val(oldWord);
								$('#newTranslate').val(oldTranslate);
								$('#frm_word_rnm').attr('class', 'center');
								$.fancybox($('#frmrename'), {
									topRatio : 0.2,
									autoResize : true,
								});
							})

					// обработка кнопки копировать
					$('#copyWord').click(
							function() {
								$.get('json/unitedit.html', 'copy=copy' + '&a='
										+ Math.random(), function(rJson) {
									if (rJson.redirect != null) {
										window.location.href = rJson.redirect;
									} else {
										for (i in rJson) {
											$('#spisokunit2').append(
													"<div style='display:inline-block'><input type='radio' name='unit' value='"
															+ rJson[i].id
															+ "'>"
															+ rJson[i].unit
															+ '</div> <br>')
										}
										$.fancybox($('#spisokunit'), {
											topRatio : 0.2,
											autoResize : true,
											afterClose : function() {
												$('#spisokunit2').html('');
											},
										});
									}
								});

							})

					// делает кнопку продолжить в окне копирования слов активной
					$('#spisokunit2').on('click', "[type='radio']",
							function(e) {
								$('#unitforcopy').val($(this).val());
								$('.dsbl2').removeAttr('disabled');
							})

					// обработка кнопки продолжить в окне копирования слов
					$('#spisokunit').on(
							'click',
							'#copyWordNext',
							function() {
								showLoader();
								$.get('json/unitedit.html', $('#words')
										.serialize()
										+ '&unitforcopy='
										+ $('#unitforcopy').val()
										+ '&a='
										+ Math.random(), function(rJson) {
									if (rJson.redirect != null) {
										window.location.href = rJson.redirect;
										return;
									}
									if (rJson.msg != null) {
										$('#spisokunit2').html(rJson.msg);
										hideLoader();
									} else {
										$.fancybox.close();
									}
									var check = $("[type='checkbox']");
									for ( var i in check) {
										check[i].checked = false;
									}
									hideLoader();
								});
							})

					// обрабока формы изменить слово
					$('#frm_word_rnm')
							.validate(
									{
										rules : {
											newWord : {
												required : true
											},
											newTranslate : {
												required : true
											}
										},
										messages : {
											newUnit : {
												required : 'Заполните поле *'
											},
											newTranslate : {
												required : 'Заполните поле *'
											}
										},

										submitHandler : function(form) {
											// если новые данные отличаются от
											// старых
											if (($('#oldWord').val() != $(
													'#newWord').val())
													|| ($('#oldTranslate')
															.val() != $(
															'#newTranslate')
															.val())) {

												var data = $(form).serialize();
												$('#msg').text('');
												$
														.get(
																'json/unitedit.html',
																data
																		+ '&a='
																		+ Math
																				.random(),
																function(rJson) {
																	if (rJson.msg != null) {
																		$(
																				'#msg')
																				.html(
																						rJson.msg);
																		$(
																				'#msg')
																				.css(
																						{
																							color : 'black',
																							transition : 'color 2s'
																						});
																		return;
																	}
																	if (rJson.redirect != null) {
																		window.location.href = rJson.redirect;
																	}
																});
											} else {
												$.fancybox.close();
											}
										}
									})
				})