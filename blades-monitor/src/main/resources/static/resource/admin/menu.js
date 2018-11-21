/**
 * Created by Ivan on 2017/5/29.
 */
function choiceMenu(code) {
    //选中菜单
    var menuNode = $("li[code="+code+"]");
    $(menuNode).addClass("active");

    var pid = $(menuNode).attr("pid");
    while(pid){
        $("#"+pid).addClass("active");
        pid = $("#"+pid).attr("pid");
    }
}
