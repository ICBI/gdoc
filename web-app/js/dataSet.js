$(document).ready(function() {

	$("#dataSetType").change(function() {
		loadDataSets(this.value)
	});
	if($('#dataSetType').val())
		loadDataSets($('#dataSetType').val());

});

function loadDataSets(dataType) {
	$.ajax({
		url: "/gdoc/studyDataSource/selectDataType",
		data: "dataType=" + dataType,
		cache: false,
		success: function(html) {
 			$("#dataDiv").html(html);
		}
	});
}