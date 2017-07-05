$(document).ready(function() {
	var t_freetrail = $('#table-freetrail').DataTable({
		"columnDefs" : [ {
			"searchable" : false,
			"orderable" : false,
			"targets" : 0
		} ],
		"order" : [ [ 0, 'asc' ] ]
	});

	t_freetrail.on('order.dt search.dt', function() {
		t_freetrail.column(0, {
			search : 'applied',
			order : 'applied'
		}).nodes().each(function(cell, i) {
			cell.innerHTML = i + 1;
		});
	}).draw();

	var t_course = $('#table-course').DataTable({
		"columnDefs" : [ {
			"searchable" : false,
			"orderable" : false,
			"targets" : 0
		} ],
		"order" : [ [ 0, 'asc' ] ]
	});

	t_course.on('order.dt search.dt', function() {
		t_course.column(0, {
			search : 'applied',
			order : 'applied'
		}).nodes().each(function(cell, i) {
			cell.innerHTML = i + 1;
		});
	}).draw();

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

});