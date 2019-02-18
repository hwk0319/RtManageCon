var TaskOperation = function(taskId) {
	var task;
	var init= function() {
		$.ajax({
			url : getContextPath() + "/taskmanage/taskById",
			type : "POST",
			datatype : "json",
			async : false,
			data : {
				"id" : taskId,
			},
			success : function(data) {
				if ('success' != data.status) {
					alert("data.value");
				} else {
					task= data.value[0];
				}
			}
		});
	}
	var o = init();
	return task;
}