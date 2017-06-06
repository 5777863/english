$(document).ready(function() {

	// установливаем обработчик события resize
	$(window).resize(function() {
		var size = $(window).height();
		// $( "#result2" ).text( $( window ).height() );
		$(".start").css("min-height", size - 50);
	});


	// сравниваем размеры div блоков в овл карусели и всем ставим максимальную
	// высоту
	function sizeblock() {
		var sizeowl = 0;
		var array = $('.div_carousel');
		for (var a = 0; a < 8; a++) {
			if ($(array[a]).height() > sizeowl) {
				sizeowl = $(array[a]).height();
			}
		}
		$('div.div_carousel').css("min-height", sizeowl);
	}

	// вызовем событие resize
	$(window).resize();

	$("#owl-example").owlCarousel({
		items : 4, // 10 items above 1000px browser width
		itemsDesktop : [ 1199, 4 ], // 5 items between 1000px
		// and 901px
		itemsDesktopSmall : [ 900, 3 ], // betweem 900px and
		// 601px
		itemsTablet : [ 600, 2 ], // 2 items between 600 and 0
		itemsMobile : [ 479, 1 ], // itemsMobile disabled -
		// inherit
		// from
		loop : true,
		autoPlay : true, // itemsTablet option
		stopOnHover : true,

	});

	sizeblock()

})