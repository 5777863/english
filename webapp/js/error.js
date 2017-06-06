window.onload = function() {

	// установливаем обработчик события resize
	$(window).resize(function() {
		var size = $(window).height();
		$(".start").css("min-height", size - 50);
	});

	// вызовем событие resize
	$(window).resize();

}