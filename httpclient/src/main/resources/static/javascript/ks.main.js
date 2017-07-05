$("#tabs-container-statistics").show();
$("#tabs-container-todo").hide();
$("#tabs-container-others").hide();

$("#nav-tabs-statistics").click(function() {
	$("#tabs-container-statistics").show();
	$("#tabs-container-todo").hide();
	$("#tabs-container-others").hide();
	$(".li-tabs-statistics").addClass("active");
	$(".li-tabs-todo").removeClass("active");
	$(".li-tabs-others").removeClass("active");
});

$("#nav-tabs-todo").click(function() {
	$("#tabs-container-statistics").hide();
	$("#tabs-container-todo").show();
	$("#tabs-container-others").hide();
	$(".li-tabs-statistics").removeClass("active");
	$(".li-tabs-todo").addClass("active");
	$(".li-tabs-others").removeClass("active");
});

$("#nav-tabs-others").click(function() {
	$("#tabs-container-statistics").hide();
	$("#tabs-container-todo").hide();
	$("#tabs-container-others").show();
	$(".li-tabs-statistics").removeClass("active");
	$(".li-tabs-todo").removeClass("active");
	$(".li-tabs-others").addClass("active");
});

$("#warning-alert").fadeTo(5000, 500).slideUp(500, function(){
    $("#warning-alert").alert('close');
});

$("#success-alert").fadeTo(5000, 500).slideUp(500, function(){
    $("#success-alert").alert('close');
});