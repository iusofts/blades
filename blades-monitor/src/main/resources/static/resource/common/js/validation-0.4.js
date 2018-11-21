/**
 * 异步表单验证
 * @author Ivan
 * @date：2017年5月30日 下午2:07:20
 */
function Validation(){
	/* 表单ID */
	var formId;
	/* 提交按钮ID */
	var submitId;
	/* 验证单个属性(输入框焦点离开时触发验证) */
	var singleNames;
	/* 当前验证的属性 */
	var validateProperty=""
	/* 是否返回校验正确信息，默认不返回 */
	var returnRight = false;
	/* 返回校验通过的信息 */
	var rightInfo = "";
	/* 默认消息 */
	var msgDefult = new Map();
	/* 是否替换原有错误回调函数 */
	var replaceError = false;
	/* 是否异步 */
	var async = true;
	/* 其它参数 */
	var param = new Map();
	
	
	/** 
		校验器初始化
		@param
		formId:表单ID
		@param
		submitId:提交按钮ID
		@param
		singleName:验证单个属性(输入框焦点离开时触发验证)
		@param
		rightInfo:返回校验通过的信息(如:输入正确)
		@param
		right:单个属性校验通过回调函数
		@param
		success:表单校验通过回调函数
		@param
		error:表单校验失败回调函数
		@param
		replaceError:是否替换原有错误回调函数
		@param
		exception:表单校验异常回调函数
		@param
		before:表单校验前调用函数函数
		@param
		finish:表单校验完成调用函数
		@param
		async:是否异步
		@param
		param:附带参数
	*/
	function initialise(params){
		
		//填充初始化数据
		if(typeof params != "undefined"){
			if(typeof params.formId != "undefined"){
				formId=params.formId;
			}
			if(typeof params.submitId != "undefined"){
				submitId=params.submitId;
			}
			if(typeof params.singleName != "undefined"){
				addSingel(params.singleName);
			}
			if(typeof params.rightInfo != "undefined"){
				returnRight = true;
				rightInfo=params.rightInfo;
			}
			if(typeof params.returnRight != "undefined"){
				returnRight=params.returnRight;
			}
			if(typeof params.success != "undefined"){
				successDo=params.success;
			}
			if(typeof params.error != "undefined"){
				replaceError=params.replaceError;
				addErrorListener(params.error,params.replaceError);
			}
			if(typeof params.exception != "undefined"){
				exceptionsDo=params.exception;
			}
			if(typeof params.before != "undefined"){
				beforeDo=params.before;
			}
			if(typeof params.right != "undefined"){
				rightDo=params.right;
			}
			if(typeof params.finish != "undefined"){
				finishDo=params.finish;
			}
			if(typeof params.async != "undefined"){
				async=params.async;
			}
			if(typeof params.param != "undefined"){
				param=objToMap(params.param);
			}
		}

        $('#'+formId).submit(function(){
            	ajaxValid();
                return false;
            }
        );
		
		//为表单提交按钮绑定提交事件
		if(submitId){
            if($('#'+formId).find("#"+submitId).length>0 && $('#'+formId).find("#"+submitId).attr("type")!='submit'){
                $('#'+formId).find("#"+submitId).click(function(){
                    ajaxValid();
                });
            }
		}
		
		//加载默认消息
		$('#'+formId).find("[id^='msg_']").each(function(){
			msgDefult.put($(this).attr("id"),$(this).html());
		});
		
	}
	this.init=initialise;
	
	/** 
 	设置单个验证
 	@param
	newSingleNames:验证单个属性(输入框焦点离开时触发验证),多个属性用","隔开。
	 */
	function addSingel(singleName){
		//根据name绑定输入框离开事件
		for(var i=0;i<singleName.length;i++){
			if(singleName[i]!=''){
				if(singleName[i].indexOf(":")!=-1){
					singleName[i]=singleName[i].replace(":","\\:");
				}
				$('#'+formId).find("[name="+singleName[i]+"]").blur(function (){ajaxValid($(this).attr("name"))});
			}
		}
	}
	
	this.setSingel=addSingel;
	
	this.getInfo = function(){
		alert(formId + ", " +singleNames + ", " + returnRight+ ", " + rightInfo);
	}
	
	/** 校验成功回调函数 **/
	var successDo=function(){alert("校验通过！")};
	/** 校验失败回调函数 **/
	var errorDo=function(error){
		//遍历回显所有属性校验失败的错误信息
		for ( var p in error ){
			errorMsg(p,error[p]);
		}
	};
	/** 校验异常回调函数 **/
	var exceptionsDo=function(e){
        try {
            layer.msg(e.responseJSON.message);
        } catch (e) {
        	layer.msg('服务器繁忙');
        }
    };
	/** 校验前调用函数 **/
	var beforeDo=function(){};
	/** 校验完成调用函数 **/
	var finishDo=function(){};
	/** 校验正确调用函数 **/
	var rightDo=function(p){};
	
	
	/**
		绑定校验失败监听器
		@param fun:回调函数
		@param isRepalce:是否替换原有错误回显方法
	 */
	function addErrorListener(fun,isReplace){
		if(isReplace==true){
			//替换原有错误处理
			alert("替换");
			errorDo=fun;
		}else{
			//合并错误处理
			var errorDo2=errorDo;
			errorDo=function(error){
				fun(error);
				errorDo2(error);
			};
		}
	}
	
	/**
		异步校验
		@param property:单个校验属性的名称,不填或为空时校验整个表单。
	 */
	function ajaxValid(property){
		beforeDo();
		var data="";
		if(typeof property != "undefined"){
			var property2=property;
			if(property.indexOf(":")!=-1){
				//校验重复输入是否一致
				property2=property.replace(":","\\:");
				var propertyConfirm=property.split(":")[1];
				data=propertyConfirm+"="+$('#'+formId).find("[name="+propertyConfirm+"]").val()+"&";
				data=data+property+"="+$('#'+formId).find("[name="+property2+"]").val();
				data=data+"&validateProperty="+propertyConfirm;
			}else{
				data=property+"="+$('#'+formId).find("[name="+property+"]").val();
				data=data+"&validateProperty="+property;
			}
		}else{
			data=$('#'+formId).serialize();
			//校验信息还原默认值
			$('#'+formId).find("[v=false]").each(function(){
			    $(this).html(msgDefult.get($(this).attr("id")));
			  });
			$('#'+formId).find("[v2=false]").removeClass("input_validation_error");
		}
		//拼接其它参数
		param.keySet().forEach(function(key){
			if(data==""){
				data+=key+"="+param.get(key);
			}else{
				data+="&"+key+"="+param.get(key);
			}
		});
		
		//alert(data);
		$.ajax({
				type: "POST",
				url:$('#'+formId).attr("action"),
				data:data,
				async: async,
			    error: function(e) {
					if(e.status == 400 && e.responseJSON.code == 100){
                        //校验未通过，回显错误信息
                        var error=e.responseJSON.ext;
                        //单个属性验证返回校验失败，但无此属性错误信息返回，认为该属性校验成功
                        if(typeof property != "undefined" && typeof error[property]=="undefined"){
                            //单个属性校验通过，回显正确信息
                            rightMsg(property);
                        }else if(typeof property != "undefined"){
                            if(replaceError!=true){//是否替换原有错误回调函数
                                //单个属性校验失败，回显错误信息
                                errorMsg(property,error[property]);
                            }
                            errorDo(error);
                        }else{
                            //校验错误回调函数
                            errorDo(error);
                        }
					}else{
                        exceptionsDo(e);
                    }
			    	finishDo();
			    },
			    success: function() {
					if(typeof property == "undefined"){
						//提交整个表单校验通过，回调函数
						successDo();
					}else{
						//单个属性校验通过，回显正确信息
						rightMsg(property);
					}
			    	finishDo();
			    }
		});
	}
	
	this.ajaxValid=ajaxValid;
	
	/**
	 * 回显错误信息
	 * @param p:属性名
	 * @param error:错误信息
	 */
	function errorMsg(p,error){
		var p2=p;//只取冒号前面的名字
		 var p3=p;//转义冒号的名字
		 if(p.indexOf(":")!=-1){
			 p2=p.split(":")[0];
			 p3=p.replace(":","\\:");
		 }
		 $('#'+formId).find("#msg_"+p2).html("<font color='red'>"+error+"</font>");
		 $('#'+formId).find("#msg_"+p2).attr("v","false");
		 $('#'+formId).find("[name="+p3+"]").addClass("input_validation_error");
		 $('#'+formId).find("[name="+p3+"]").attr("v2","false");
	}
	
	/**
	 * 回显正确信息
	 * @param p:属性名
	 */
	function rightMsg(p){
		rightDo(p);
		var p2=p;//只取冒号前面的名字
		 var p3=p;//转义冒号的名字
		 if(p.indexOf(":")!=-1){
			 p2=p.split(":")[0];
			 p3=p.replace(":","\\:");
		 }
		if($('#'+formId).find("[name="+p3+"]").val()!=''){
			$('#'+formId).find("#msg_"+p2).html("<font color='green'>"+rightInfo+"</font>");
			$('#'+formId).find("#msg_"+p2).attr("v","true");
		}
		$('#'+formId).find("[name="+p3+"]").removeClass("input_validation_error");
	}
	
	function objToMap(obj){
		var map=new Map();
		for ( var p in obj ){ // 方法 
		  if ( typeof ( obj [ p ]) == " function " ){
			  obj [ p ]() ; 
		  } else { 
			 map.put(p,obj[p]);
		  }
		}
		return map;
	}
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
