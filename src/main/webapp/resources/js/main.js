/**
 * Main, on document ready.
 */
$(function() {
	// enable nice scroll
	$("html").niceScroll({
		cursorwidth : "10",
		boxzoom : true,
		touchbehavior : false
	});
	$.scrollTo.defaults.axis = 'y';
	// TOC, shows how to scroll the whole window
	$('nav a').click(function() {// $.scrollTo works EXACTLY the same way, but scrolls the whole screen
		$.scrollTo(this.hash, 1500);
		return false;
	});

	// Take care of menu position.
	// don't calculate menu offset every time because it's slow
	var menu = $('#menu'), pos = menu.offset();
	$(window).scroll(function() {
		if ($(window).scrollTop() > pos.top && !menu.hasClass("fixed")) {
			// page scrolled => fixe the menu position
			menu.addClass("fixed");
		} else if ($(window).scrollTop() <= pos.top && menu.hasClass("fixed")) {
			// scroll to top
			menu.removeClass("fixed");
		}
	});

	// the element inside of which we want to scroll
	var $elem = $('#content');

	// show the buttons
	$('#nav_up').fadeIn('slow');
	$('#nav_down').fadeIn('slow');

	// whenever we scroll fade out both buttons
	$(window).bind('scrollstart', function() {
		$('#nav_up,#nav_down').stop().animate({
			'opacity' : '0.2'
		});
	});
	// ... and whenever we stop scrolling fade in both buttons
	$(window).bind('scrollstop', function() {
		$('#nav_up,#nav_down').stop().animate({
			'opacity' : '1'
		});
	});

	// clicking the "down" button will make the page scroll to the $elem's height
	$('#nav_down').click(function(e) {
		$('html, body').animate({
			scrollTop : $elem.height()
		}, 800);
	});
	// clicking the "up" button will make the page scroll to the top of the page
	$('#nav_up').click(function(e) {
		$('html, body').animate({
			scrollTop : '0px'
		}, 800);
	});
});