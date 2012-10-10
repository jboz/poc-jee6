;
(function($) {

	$.fn.equalize = function(options) {
		var $containers = this, // this is the jQuery object
		reset = false, equalize, type;

		// when options are an object
		if ($.isPlainObject(options)) {
			equalize = options.equalize || 'height';
			reset = options.reset || false;
		} else { // otherwise, a string was passed in or default to height
			equalize = options || 'height';
		}

		if (!$.isFunction($.fn[equalize])) {
			return false;
		}

		// determine if the height or width is being equalized
		type = (equalize.indexOf('eight') > 0) ? 'height' : 'width';

		var max = 0; // reset for each container
		$containers.each(function() {
			var $element = $(this), value;
			if (reset) {
				$element.css(type, '');
			} // remove existing height/width dimension
			value = $element[equalize](); // call height(), outerHeight(), etc.
			if (value > max) {
				max = value;
			} // update max
		});
		$containers.css(type, (max + 1) +'px'); // add CSS to children
	};

}(jQuery));