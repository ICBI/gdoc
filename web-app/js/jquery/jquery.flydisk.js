/*Flydisk 1.0 - jQuery plugin, which provides shuttle functionality to any two select lists(Developed by s.ramkumar.mca@gmail.com-GNU General Public License v2) */
jQuery.fn.flydisk=function(options){
	var settings=jQuery.extend({
		selectedColor:'white',
		left_disk:0,
		right_disk:0,
		add_button:0,
		remove_button:0,
		up_button:0,
		down_button:0,
		move_all_button:0,
		remove_all_button:0,
		move_top_button:0,
		move_bottom_button:0
	}, options);
	var left_disk=settings['left_disk'];
	var right_disk=settings['right_disk'];
	var add_button=settings['add_button'];
	var remove_button=settings['remove_button'];
	var up_button=settings['up_button'];
	var down_button=settings['down_button'];
	var move_all_button=settings['move_all_button'];
	var remove_all_button=settings['remove_all_button'];
	var move_top_button=settings['move_top_button'];
	var move_bottom_button=settings['move_bottom_button'];
		$.removeDupes('#'+left_disk, '#'+right_disk)
	$('#'+left_disk).dblclick(function(){
		return!$('#'+left_disk+' option:selected').remove().appendTo('#'+right_disk)
	});
	$('#'+right_disk).dblclick(function(){
		return!$('#'+right_disk+' option:selected').remove().appendTo('#'+left_disk)
	});
	if(left_disk&&right_disk){
		if(add_button&&remove_button){
			$('#'+add_button).click(function(){
				$('#'+left_disk+' option:selected').css("background",settings['selectedColor']);
				$('#'+left_disk+' option:selected').remove().appendTo('#'+right_disk);
				$.sortOptions('#'+left_disk);
				$.sortOptions('#'+right_disk);
				return false;
			});
			$('#'+remove_button).click(function(){
				$('#'+right_disk+' option:selected').css("background","");
				$('#'+right_disk+' option:selected').remove().appendTo('#'+left_disk);
				$.sortOptions('#'+left_disk);
				$.sortOptions('#'+right_disk);
				return false;
			})

		}else{
			alert('Please provide a valid ids for add_button & remove_button(mandatory)')
		}
		if(up_button){
			$('#'+up_button).click(function(){
				return!$('#'+right_disk+' option:selected').insertBefore($('#'+right_disk+' option:selected:first').prev())
			})
		}
		if(down_button){
			$('#'+down_button).click(function(){
				return!$('#'+right_disk+' option:selected').insertAfter($('#'+right_disk+' option:selected:last').next())
			})
		}
	}else{
		alert('Please provide a valid ids for left_disk & right_disk(mandatory)')
	}
	if(move_all_button){
		$('#'+move_all_button).click(function(){
			return!$('#'+left_disk+' option').remove().appendTo('#'+right_disk)
		})
	}
	if(remove_all_button){
		$('#'+remove_all_button).click(function(){
			return!$('#'+right_disk+' option').remove().appendTo('#'+left_disk)
		})
	}
	if(move_top_button){
		$('#'+move_top_button).click(function(){
			return!$('#'+right_disk+' option:selected').insertBefore($('#'+right_disk+' option:first'))
		})
	}
	if(move_bottom_button){
		$('#'+move_bottom_button).click(function(){
			return!$('#'+right_disk+' option:selected').insertAfter($('#'+right_disk+' option:last'))
		})
	}

};
jQuery.sortOptions=function(selectToSort) {
	$(selectToSort).find('option').sort(function(a,b){ 
        return a.value > b.value ? 1 : -1; 
    }).remove().appendTo(selectToSort);
}
jQuery.removeDupes=function(options1, options2) {
	$(options2).find('option').each(function() {
		$(options1).find('option[value=' + this.value + ']').remove();
	});
}