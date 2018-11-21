//跳转页码
function jumpPageNum(){
	var pageNum=$("#jumpPageNum").val();
	var reg = /^\d*\d+\d*$/;
	if(reg.test(pageNum)){
		page(pageNum,10);
	}else{
		layer.msg("输入的页码不合法！");
		$("#jumpPageNum").val("");
	}
}

/*
 *跨页选中 
 *inputName:隐藏域的name值。
 *checkboxName:checkbox的name值。
 */
function selectBoxs(inputName,checkboxName) {
	var idValues = "";

	//已选中数组
	var idArray = $("input[name='"+inputName+"']").val().split(",");

	//已选中的checkbox的有效性
	for (var i = 0; i < idArray.length; i++) {
		$(":checkbox[value='" + idArray[i] + "']").each(function() {
			var idVal = $(this).val();
			//若已选中的checkbox中出现了取消选中，将其ID从数组中移除
			if (!$(this).is(":checked")) {
				idArray.splice($.inArray(idVal, idArray), 1);
			}
		});
	}

	$(":checkbox[name='"+checkboxName+"']:checked").each(function() {
		var id = $(this).val();
		//若当前选中的ID不存在于idArray,将其加入到已选择
		if ($.inArray(id, idArray) == -1) {
			idArray.push(id);
		}
	});

	//将id数组组合成字符串放入隐藏域中
	for (var i = 0; i < idArray.length; i++) {
		if (idValues == "") {
			idValues = idArray[i];
		} else {
			idValues = idValues + "," + idArray[i];
		}
	}
	$("input[name='"+inputName+"']").val(idValues);
}

/*
 * 全选、全不选
 * allName:全选框的name值
 * boxName:checkbox的name值。
 */
function allCheckOrNot(allName,boxName){
	$(":checkbox[name='"+allName+"']").click(function() {
		//判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型
		if ($(this).is(":checked")) {
			$(":checkbox[name='"+boxName+"']").prop("checked", true);
		} else {
			$(":checkbox[name='"+boxName+"']").prop("checked", false);
		}
	});
}
/*
 *初始化已选中的checkbox
 *inputName:隐藏域的name值。
 *boxName:checkbox的name值。
 */
function initCheckBox(inputName,boxName){
	var checkedArr = $("input[name='"+inputName+"']").val().split(",");
	$(":checkbox[name='"+boxName+"']").each(function() {
		var idVal = $(this).val();
		if ($.inArray(idVal, checkedArr) >= 0) {
			$(":checkbox[value='" + idVal + "']").prop("checked", true);
		}
	});
}

/*
 * 批量删除
 * ids:要删除的ID数组.
 * $uri:ajax请求地址.
 */
function doDeletes(ids,$uri) {
	$.ajax({
		type : "POST",
		url : $uri,
		//dataType : "json",由于basecontroller返回不是json数据，所以去掉
		contentType : "application/json",
		data : JSON.stringify(ids),
		async : false,
		error : function(request) {
			layer.msg("服务器繁忙！");
		},
		success : function() {
			layer.msg("删除成功");
			jumpPageNum();
		}
	});
}
