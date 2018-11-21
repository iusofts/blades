/**
 * 获取URL参数
 * @param name
 * @returns {null}
 * @constructor
 */
function getQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

/**
 * 字符串截图
 * @param str 被截取字符串
 * @param len 截取长度
 * @returns {*}
 */
function cutString(str, len) {
    var s = "";
    if(str){
        //length属性读出来的汉字长度为1
        if(str.length*2 <= len) {
            return str;
        }
        var strlen = 0;
        for(var i = 0;i < str.length; i++) {
            s = s + str.charAt(i);
            if (str.charCodeAt(i) > 128) {
                strlen = strlen + 2;
                if(strlen >= len){
                    return s.substring(0,s.length-1) + "...";
                }
            } else {
                strlen = strlen + 1;
                if(strlen >= len){
                    return s.substring(0,s.length-2) + "...";
                }
            }
        }
    }
    return s;
}

/**
 * 表单数据转对象
 * @param form
 * @returns {{}}
 */
function getFormJson(form) {
    var o = {};
    var a = $(form).serializeArray();
    $.each(a, function () {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
}

/**
 * 对象属性复制
 * @param obj
 * @param target
 * @returns {*}
 */
function copy(obj,target){
    //如果obj不是对象，那么直接返回值就可以了
    if(obj === null || typeof obj !== "object"){
        return obj;
    }

    var i,value;
    for(i in obj){
        value = obj[i];
        target[i] = value;
    }
    return target;
}