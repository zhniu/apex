$(document).ready(function() {
	var widthIncrease = 30;
	var slideEaseTime = 300;
	var hoverIncreaseTime = 50;
	$(".placeholder").fadeIn( "slow");
	$(".banner").each(function(i) {
		var width = $(this).width();
		$(this).delay(i * 250).animate({
			'opacity' : 1,
			"margin-left" : "15px"
		}, slideEaseTime, function() {
			$(this).hover(function() {
				$(this).stop(true, false).animate({
					"width" : width + widthIncrease
				}, hoverIncreaseTime);
			}, function() {
				$(this).stop(true, false).animate({
					"width" : width
				}, hoverIncreaseTime);
			});
		})
	})
});