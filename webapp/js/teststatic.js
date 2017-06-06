$(document)
		.ready(
				function() {

					if (sessionStorage.words == undefined
							|| sessionStorage.words == 'undefined') {
						window.location.href = 'dictionary.html';
					}
					// установливаем обработчик события resize
					$(window).resize(function() {
						var size = $(window).height();
						// $( "#result2" ).text( $( window ).height() );
						$(".start").css("min-height", size - 50);
					});

					// вызовем событие resize
					$(window).resize();

					// сортировка для слов
					function sortFunction(a, b) {
						if (a.id < b.id)
							return -1 // Или любое число, меньшее нуля
						if (a.id > b.id)
							return 1 // Или любое число, большее нуля
						return 0
					}

					// направление языка который был выбран
					var test = sessionStorage.test;
					var lang = sessionStorage.lng;
					var jsonResult = JSON.parse(sessionStorage.result);
					var jsonWords = JSON.parse(sessionStorage.words);

					// метод обробатывающий блок с тестом
					function nextWord(test, jsonWords, lang, jsonResult) {
						// вставляем знаение по кол-ву слов
						$('#divkolvo').text(
								'всего: ' + jsonResult.all + ' осталось: '
										+ jsonResult.ost + ' правильных: '
										+ jsonResult.res);
						if (test == 'variant') {
							var lngthStroki = 0;
							// объект слова, который спрашивается в тесте
							var wordObject = jsonWords[jsonWords.length - 1];
							// если тест для неправильных глаголов
							if ((wordObject.form2 != null)
									&& (wordObject.form3 != null)) {
								word = wordObject.word + ' - '
										+ wordObject.form2 + ' - '
										+ wordObject.form3
							} else {
								word = wordObject.word;
							}
							translate = wordObject.translate;
							unitid = wordObject.idstatcat;
							$('#answers1').text('')
							if (lang == 'En') {
								$('#question1').text(word);
								jsonWords.sort(sortFunction);
								for ( var i in jsonWords) {
									if (jsonWords[i].translate.length > lngthStroki) {
										lngthStroki = jsonWords[i].translate.length;
									}
									$('#answers1').append(
											'<a class="answer" href="#">'
													+ jsonWords[i].translate
													+ '</a>');
								}
								// задаем длинну строки ответа
								if (lngthStroki > 25) {
									$('.answer').css(
											'max-width',
											300 + (lngthStroki - 25) * 10
													+ 'px');
								} else {
									$('.answer').css('max-width', 300 + 'px');
								}
							} else {
								if (lang == 'Ru') {
									$('#question1').text(translate);
									jsonWords.sort(sortFunction);
									for ( var i in jsonWords) {
										// если тест для неправильных глаголов
										if ((jsonWords[i].form2 != null)
												&& (jsonWords[i].form3 != null)) {
											var xxx = jsonWords[i].word + ' - '
													+ jsonWords[i].form2
													+ ' - '
													+ jsonWords[i].form3
										} else {
											var xxx = jsonWords[i].word;
										}
										if (word.length > lngthStroki) {
											lngthStroki = word.length;
										}
										$('#answers1').append(
												'<a class="answer" href="#">'
														+ xxx + '</a>');
									}
									// задаем длинну сроки ответа
									if (lngthStroki > 25) {
										$('.answer').css(
												'max-width',
												300 + (lngthStroki - 25) * 10
														+ 'px');
									} else {
										$('.answer').css('max-width',
												300 + 'px');
									}
								}
							}
							// делаем блок видимым
							$('#test1').attr('class', 'divTest');
						} else {
							var lngthStroki = 0;
							if (test == 'vvod') {
								// объект слова, который спрашивается в тесте
								var wordObject = jsonWords[0];
								word = wordObject.word;
								translate = wordObject.translate;
								unitid = wordObject.idstatcat;
								if (lang == 'En') {
									// устанавливаем значения нопки переключения
									// языка
									$('#lng').val('En');
									$('#question2').text(wordObject.word);
									if (jsonWords[0].translate.length > lngthStroki) {
										lngthStroki = jsonWords[0].translate.length;
									}
								} else {
									if (lang == 'Ru') {
										// устанавливаем значения нопки
										// переключения языка
										$('#lng').val('Ru');
										$('#question2').text(
												wordObject.translate);
										if (jsonWords[0].word.length > lngthStroki) {
											lngthStroki = jsonWords[0].word.length;
										}
									}
								}
								// задаем длинну сроки ответа
								if (lngthStroki > 25) {
									$('#answer').css(
											'max-width',
											300 + (lngthStroki - 25) * 10
													+ 'px');
								} else {
									$('.answer').css('max-width', 300 + 'px');
								}
								// делаем блок видимым
								$('#test2').attr('class', '');
							}
						}
					}

					nextWord(test, jsonWords, lang, jsonResult);

					// отключаем соытие по умолчанию, что бы ссылка не
					// перезагружала страницу
					$('#answers1').on('click', '.disable', function(e) {
						e.preventDefault();
					})

					// обработка ответа тест1
					var otvet = true;
					// хранит ответ
					var question;
					$('#answers1')
							.on(
									'click',
									'.answer',
									function(e) {
										e.preventDefault();
										if (lang == 'En') {
											var answer = translate;
										} else {
											var answer = word;
										}
										if (otvet) {
											question = $(this).text();
											otvet = false;
										}
										// если ответ НЕ верный
										if (answer != $(this).text()) {
											$(this).css('background',
													'rgb(227, 204, 52)');
											$('#er1').show();

										} else {
											// если ответ верный
											$(this).css('background',
													'rgb(105, 217, 30)');
											// очищаем поле с сообщением о
											// неправильном ответе
											// $('#result1').text('');
											// обнуляем тригер
											otvet = true;
											$('#er1').hide();
											// после правильного ответа,
											// варианты не активными
											$('.answer').attr('class',
													'disable');
											// выводит кнопку далее
											$('#result1')
													.html(
															'<a  href="#" id="next" class="next">далее</a>');
											data1 = 'test=' + test + '&answer='
													+ answer + '&question='
													+ question + '&unitid='
													+ unitid;
										}
									});

					$('#test1').on(
							'click',
							'#next',
							function(e) {
								e.preventDefault();
								// сбрасываем значение ответа
								question = null;
								$.get('staticunit.html', data1 + '&a='
										+ Math.random(), function(rJson) {
									// если пришел
									// редирект
									if (rJson.redirect != null) {
										window.location.href = rJson.redirect;
										return;
									}
									// если с
									// сервера
									// пришла
									// обшибка
									if (rJson.errorpg != null) {
										window.location.href = rJson.errorpg;
										$('#test1').attr('class',
												'divTest hide');
										$('#answers1').text('');
										$('#question1').text('');
										$('#divkolvo').text('');
										return;
									}
									// Если тест
									// закончен,
									// пришел
									// резальтат
									if (rJson.end != null) {
										$('#result1').html('');
										$('.final_resilt').text(
												'Ваш результат '
														+ rJson.end.res
														+ ' из '
														+ rJson.end.all);
										$('#answers1').text('');
										$('#question1').text('');
										$('#divkolvo').text('');
										sessionStorage.clear();
										return;
									}
									sessionStorage.words = JSON
											.stringify(rJson.arraywords);
									sessionStorage.result = JSON
											.stringify(rJson.result);
									$('#result1').html('');
									nextWord(test, rJson.arraywords, lang,
											rJson.result);
								});
							})

					// обработка кнопки lng
					$('#lng').click(function() {
						var lng = $(this).val();
						if (lng == 'Ru') {
							lang = 'En';
							$(this).val('En');
							$('#question2').text(word);
						} else {
							if (lng == 'En') {
								lang = 'Ru';
								$(this).val('Ru');
								$('#question2').text(translate);
							}
						}
					});

					// Обработка ответа тест2
					$('.next2')
							.click(
									function(e) {
										e.preventDefault();
										if (lang == 'En') {
											var answer = translate;
										} else {
											var answer = word;
										}
										if (otvet) {
											question = $('#answer').val();
											otvet = false;
										}
										// если ответ НЕ верный
										if (answer != $('#answer').val()) {
											// обнуляем значени поля ввода
											// ответа
											$('#answer').val('');
											// сообщение что ошиблись в ответе
											$('#er2').css('opacity', '1');
										} else {
											// если ответ верный
											// очищаем поле с сообщением о
											// неправильном ответе
											$('#er2').css('opacity', '0');
											$('#answer').val('');
											// обнуляем тригер
											otvet = true;
											var data2 = 'test=' + test
													+ '&answer=' + answer
													+ '&question=' + question
													+ '&unitid=' + unitid;
											// сбрасываем значение ответа
											question = null;
											$
													.get(
															'staticunit.html',
															data2
																	+ '&a='
																	+ Math
																			.random(),
															function(rJson) {
																// если пришел
																// редирект
																if (rJson.redirect != null) {
																	window.location.href = rJson.redirect;
																	return;
																}
																// если с
																// сервера
																// пришла
																// обшибка
																if (rJson.errorpg != null) {
																	window.location.href = rJson.errorpg;
																	$('#test2')
																			.attr(
																					'class',
																					'divTest hide');
																	$(
																			'#answers2')
																			.text(
																					'');
																	$(
																			'#question2')
																			.text(
																					'');
																	$(
																			'#divkolvo')
																			.text(
																					'');
																	$('#lng')
																			.attr(
																					'class',
																					'hide');
																	return;
																}
																// Если тест
																// закончен,
																// пришел
																// резальтат
																if (rJson.end != null) {
																	$(
																			'.final_resilt')
																			.text(
																					'Ваш результат '
																							+ rJson.end.res
																							+ ' из '
																							+ rJson.end.all);

																	$(
																			'#answers2')
																			.text(
																					'');
																	$(
																			'#question2')
																			.text(
																					'');
																	$(
																			'#divkolvo')
																			.text(
																					'');
																	$('#lng')
																			.attr(
																					'class',
																					'hide');
																	sessionStorage
																			.clear();
																	return;
																}

																sessionStorage.words = JSON
																		.stringify(rJson.arraywords);
																sessionStorage.result = JSON
																		.stringify(rJson.result);
																nextWord(
																		test,
																		rJson.arraywords,
																		lang,
																		rJson.result);

															});
										}

									});
				})
