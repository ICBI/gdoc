$(document).ready(function() {

	$("#dataSetType").change(function() {
		loadDataSets(this.value)
	});
	if($('#dataSetType').val())
		loadDataSets($('#dataSetType').val());

});

function loadDataSets(dataType) {
	var appName =  window.location.pathname.split("\/")[1];
	$.ajax({
		url: "/" + appName + "/studyDataSource/selectDataType",
		data: "dataType=" + dataType,
		cache: false,
		success: function(html) {
 			$("#dataDiv").html(html);
		}
	});
}