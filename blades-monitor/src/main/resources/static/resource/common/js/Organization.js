/**
 * 组织机构选择器
 * 
 * @author Ivan
 * @date：2016年3月2日 下午2:07:20
 */
function Organization() {
	/* 选择标签ID */
	var chooseId;
	/* 回显标签ID */
	var labelId;
	/* 值域标签ID */
	var valueId;
	/* 回显属性 */
	var labelProperty="name";
	/* 值域属性 */
	var valueProperty="code";
	/* 标题 */
	var title = '请选择';
	/* 宽度高度 */
	var area=["420px","320px"];
	/* 根结点ID */
	var pid = '01';
	/* 项目路径 */
	var root;
	/* 复选框 */
	var checkbox = false;
	/* 允许选择的类型 */
	var allowType = "3,9";
	/* 选择的节点 */
	var checkNodes = new Map();
	/* 默认选中的节点 */
	var defaultCheck = "";

	var choose_index;

	var zTree;
	var setting;
	var zNodes = [];
	
	// 鼠标单击树时调用的函数
	function zTreeOnClick(event, treeId, treeNode) {
		if (checkbox) {// 复选
			zTree.checkNode(treeNode, true, true);
			zTreeOnCheck(event, treeId, treeNode);
		} else {// 单选
			var allowTypes = allowType.split(",");
			var isAllow = false;
			for (var i = 0; i < allowTypes.length; i++) {
				if (treeNode.type == allowTypes[i]) {
					isAllow = true;
				}
			}
			if (isAllow) {
				$("#" + labelId).html(
						treeNode.getParentNode().name + "[" + treeNode.name
								+ "]");
				$("#" + valueId).val(treeNode[valueProperty]);
				layer.style(choose_index, {
					display : 'none'
				});
			}
		}
	}

	// 复选框选择时调用
	function zTreeOnCheck(event, treeId, treeNode) {
		if (treeNode.checked) {
			if (treeNode.type == "3" || treeNode.type == "9") {
				checkNodes.put(treeNode[valueProperty], treeNode.getParentNode().name
						+ "[" + treeNode.name + "]");
			} else {
				checkNodes.put(treeNode[valueProperty], treeNode.name);
			}
		} else {
			checkNodes.remove(treeNode[valueProperty]);
		}
		var lbl = "";
		var val ="";
		checkNodes.keySet().forEach(function(key) {
			lbl += checkNodes.get(key) + ";";
			val += key + ",";
		})
		$("#" + labelId).html(lbl);
		$("#" + valueId).val(val);
	}

	/**
	 * 选择器初始化
	 * 
	 * @param chooseId:选择标签ID
	 * @param labelId:回显标签ID
	 * @param valueId:值域标签ID
	 * @param root:项目路径
	 */
	function initialise(params) {

		//填充初始化数据
		if(typeof params != "undefined"){
			if(typeof params.chooseId != "undefined"){
				chooseId=params.chooseId;
			}
			if(typeof params.labelId != "undefined"){
				labelId=params.labelId;
			}
			if(typeof params.valueId != "undefined"){
				valueId=params.valueId;
			}
			if(typeof params.root != "undefined"){
				root=params.root;
			}
			if(typeof params.title != "undefined"){
				title=params.title;
			}
			if(typeof params.valueProperty != "undefined"){
				valueProperty=params.valueProperty;
			}
			if(typeof params.checkbox != "undefined"){
				checkbox=params.checkbox;
			}
			if(typeof params.area != "undefined"){
				area=params.area;
			}
			if(typeof params.defaultCheck != "undefined"){
				defaultCheck=params.defaultCheck;
			}
		}else{
			return;
		}

		$("#" + chooseId)
				.on(
						"click",
						function() {
							if (choose_index == undefined) {
								choose_index = layer
										.open({
											type : 1,
											title : title,
											shade : 0,
											area : area,
											fix : false,
											offset : [
													$(this).offset().top
															- $(document)
																	.scrollTop()
															+ 20,
													$(this).offset().left ],
											content : '<div id="zTreeContainer" style="height: 92%;overflow: auto;"><ul id="ztreeData_'
													+ chooseId
													+ '" class="ztree"></ul></div>',
											cancel : function() {
												layer.style(choose_index, {
													display : 'none'
												});
												return false;
											},
											success : function() {
												setting = {
													check : {
														enable : checkbox,
														chkDisabledInherit : true
													},
													async : {
														enable : true,
														url : root
																+ "/sys/org/tree.do",
														autoParam : [ "id",
																"name" ],
														otherParam : [ "pid",
																pid,
																"allowType",
																allowType ,"defaultCheck",defaultCheck]
													},
													data : {
														simpleData : {
															enable : true
														}
													},
													callback : {
														onClick : zTreeOnClick,
														onCheck : zTreeOnCheck
													}
												};
												zTree = $.fn.zTree.init(
														$("#ztreeData_"
																+ chooseId),
														setting, zNodes);
											}
										});
							} else {
								layer.style(choose_index, {
									display : 'block'
								});
							}
						});
	}
	this.init = initialise;
}

function Map() {
	this.container = new Object();
}

Map.prototype.put = function(key, value) {
	this.container[key] = value;
}

Map.prototype.get = function(key) {
	return this.container[key];
}

Map.prototype.keySet = function() {
	var keyset = new Array();
	var count = 0;
	for ( var key in this.container) {
		// 跳过object的extend函数
		if (key == 'extend') {
			continue;
		}
		keyset[count] = key;
		count++;
	}
	return keyset;
}

Map.prototype.size = function() {
	var count = 0;
	for ( var key in this.container) {
		// 跳过object的extend函数
		if (key == 'extend') {
			continue;
		}
		count++;
	}
	return count;
}

Map.prototype.remove = function(key) {
	delete this.container[key];
}

Map.prototype.toString = function() {
	var str = "";
	for (var i = 0, keys = this.keySet(), len = keys.length; i < len; i++) {
		str = str + keys[i] + "=" + this.container[keys[i]] + ";\n";
	}
	return str;
}
