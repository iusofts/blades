/**
 * 初始化图片上传组件
 * @param name
 */
function initUploadImage(name,directory) {
    var pickfilesName = name;
    if(!directory){
        directory='';
    }
    // Custom example logic 
    var uploader = new plupload.Uploader({
        runtimes: 'html5,flash,silverlight,html4',
        browse_button: 'upload_' + pickfilesName, // you can pass in id...    
        drop_element: 'container_' + pickfilesName,
        container: 'container_' + pickfilesName, // ... or DOM Element itself       
        url: ctx + "/file/",
        multi_selection: false,
        multipart_params: {directory:directory},
        unique_names: true,
        filters: {
            max_file_size: '10mb',
            mime_types: [
                {title: "图片类型", extensions: "jpg,gif,png,bmp,ico"}
                //,{title : "Zip files", extensions : "zip"}
            ]
        }, // Flash settings
        flash_swf_url: ctx + '/resource/cms/plugins/plupload-2.1.2/js/Moxie.swf',       // Silverlight settings
        silverlight_xap_url: ctx + '/resource/cms/plugins/plupload-2.1.2/js/Moxie.xap',
        init: {
            PostInit: function () {

            },
            FilesAdded: function (up, files) {
                plupload.each(files, function (file) {

                });
                //开始上传文件
                uploader.start();
            },
            UploadProgress: function (up, file) {
                $("#console_" + pickfilesName).show();
                $("#console_" + pickfilesName).html(file.percent + "%");
            },
            FileUploaded: function (uploader, file, responseObject) {
                var result = responseObject.response;

                if (result.indexOf("success:" == 0)) {
                    var fileId = result.replace("success:", "");
                    //$('#console').append("<br/>"+file.name+"上传成功，文件编号为："+fileId);
                    $("input[name=" + pickfilesName + "]").val("/file/" + fileId);
                    $("#pickfiles_" + pickfilesName).attr("src", ctx + "/file/" + fileId);
                    $("#upload_" + pickfilesName).text("更换图片");
                    $("#del_" + pickfilesName).removeClass("hidden");
                    $("#console_" + pickfilesName).hide();
                    return;
                }

                if (result == "-601") {
                    $("#console_" + pickfilesName).html("<font color='red'>&nbsp;禁止类型</font>");
                    $("#console_" + pickfilesName).show();
                    return;
                }
                if (result == "-100") {
                    $("#console_" + pickfilesName).html("<font color='red'>&nbsp;上传失败</font>");
                    $("#console_" + pickfilesName).show();
                    return;
                }
                if (result == "-600") {
                    $("#console_" + pickfilesName).html("<font color='red'>文件太大</font>");
                    $("#console_" + pickfilesName).show();
                    return;
                }
            },
            Error: function (up, err) {
                var errinfo = "上传失败";
                if (err.code == -600) {
                    errinfo = "最大10M";
                }
                if (err.code == -200) {
                    errinfo = "服务器繁忙";
                }
                if (err.code == -601) {
                    errinfo = "禁止类型";
                }
                $("#console_" + pickfilesName).html('<font color="red">' + errinfo + '</font>');
                $("#console_" + pickfilesName).show();
            }
        }
    });
    uploader.init();
    //删除按钮
    $("#del_" + pickfilesName).click(function () {
        $("input[name=" + pickfilesName + "]").val("");
        $("#pickfiles_" + pickfilesName).attr("src", ctx + "/resource/cms/dist/img/choice-img.png");
        $("#upload_" + pickfilesName).text("上传图片");
        $("#del_" + pickfilesName).addClass("hidden");
        $("#console_" + pickfilesName).hide();
    });
    
    //图片点击事件
    $("#pickfiles_" + pickfilesName).click(function () {
        if($(this).attr("src")!=ctx + "/resource/cms/dist/img/choice-img.png"){
            window.open($(this).attr("src"));
        }
    });
    
}

/**
 * 初始化图片上传组件UI
 * @param name
 */
function initUploadImageUI(name) {
    var html = '<div class="img-upload" id="container_' + name + '">';
    html += '    <input name="' + name + '" type="hidden"/>';
    html += '    <div class="img-upload-top">';
    html += '        <img id="pickfiles_' + name + '" src="' + ctx + '/resource/cms/dist/img/choice-img.png" title="点击查看原图" style="max-width: 150px;cursor: pointer;">';
    html += '    </div>';
    html += '    <div id="console_' + name + '" class="img-upload-console">0%</div>';
    html += '    <div class="img-upload-bottom">';
    html += '        <button type="button" id="upload_' + name + '" class="btn btn-xs bg-olive">上传图片</button>';
    html += '        <button type="button" id="del_' + name + '" class="btn btn-xs bg-maroon hidden">删除图片</button>';
    html += '    </div>';
    html += '</div>';
    return html;
}

/**
 * 初始化图片上传编辑
 * @param name
 * @param url
 */
function initUploadImageEdit(name,url) {
    $("input[name=" + name + "]").val(url);
    $("#pickfiles_" + name).attr("src", ctx + url);
    $("#upload_" + name).text("更换图片");
    $("#del_" + name).removeClass("hidden");
    $("#console_" + name).hide();
}

/**
 * 初始化文件上传组件
 * @param name
 */
function initUploadFile(name) {
    // Custom example logic 
    var pickfilesName = name;
    var fileId;
    var uploader = new plupload.Uploader({
        runtimes: 'html5,flash,silverlight,html4',
        browse_button: 'pickfiles_' + pickfilesName, // you can pass in id...    
        drop_element: 'container_' + pickfilesName,
        container: 'container_' + pickfilesName, // ... or DOM Element itself           
        url: ctx + "/file/",
        multi_selection: false,
        multipart_params: {},
        unique_names: true,
        filters: {
            max_file_size: '5120mb',
            mime_types: [
                {title: "文件类型", extensions: "*"}
                //,{title : "Zip files", extensions : "zip"}
            ]
        }, // Flash settings
        flash_swf_url: ctx + 'plugins/plupload-2.1.2/js/Moxie.swf',       // Silverlight settings
        silverlight_xap_url: ctx + 'plugins/plupload-2.1.2/js/Moxie.xap',
        init: {
            PostInit: function () {
                $('#filelist_' + pickfilesName).html('');
            },
            FilesAdded: function (up, files) {
                if (fileId) {
                    var file = uploader.getFile(fileId);
                    if (file) {
                        uploader.removeFile(file);
                    }
                }
                plupload.each(files, function (file) {
                    var hstr = '<div class="filelist" id="' + file.id + '"><a href="#" id="fileNameLink_' + file.id + '">' + file.name + '</a> <span>(' + plupload.formatSize(file.size).toUpperCase() + ') </span>';
                    hstr += '<div class="iu-slider">';
                    hstr += '	<div id="slider_' + file.id + '" class="iu-slider-load"></div>';
                    hstr += '</div>';
                    hstr += '&nbsp;<b></b><a class="file-del" id="del_' + file.id + '" href="javascript:void(0)">删除</a></div>';
                    $('#filelist_' + pickfilesName).html(hstr);

                    //删除按钮
                    $("#del_" + file.id).click(function () {
                        $("#" + file.id).remove();
                        uploader.removeFile(uploader.getFile(file.id));
                    });
                    //$('#name').val(file.name);
                    fileId = file.id;
                });
                //开始上传文件
                uploader.start();
            },
            UploadProgress: function (up, file) {
                var bps = uploader.total.bytesPerSec / (1024 * 1024);
                $("#slider_" + file.id).parent().show();
                $("#" + file.id).find('b').html('<span>' + file.percent + "%(" + bps.toFixed(2) + "M/s)</span>");
                $("#slider_" + file.id).css("width", file.percent + "%");
                if (file.percent == 100) {
                    $("#slider_" + file.id).parent().hide();
                    $("#" + file.id).find('b').html("<font color='green'>&nbsp;完成</span>");
                }
            },
            FileUploaded: function (uploader, file, responseObject) {
                var result = responseObject.response;

                if (result.indexOf("success:" == 0)) {
                    var fileId = result.replace("success:", "");
                    $("input[name=testFile]").val(fileId);
                    $("#fileNameLink_" + file.id).attr("href", ctx + "/file/download/" + fileId);
                    //$('#console').append("<br/>"+file.name+"上传成功，文件编号为："+fileId);
                    return;
                }

                if (result == "-601") {
                    $("#" + file.id).find('b').html("<font color='red'>&nbsp;禁止类型</font>");
                    return;
                }
                if (result == "-100") {
                    $("#" + file.id).find('b').html("<font color='red'>&nbsp;上传失败</font>");
                    return;
                }
                if (result == "-600") {
                    $("#" + file.id).find('b').html("<font color='red'>文件太大</font>");
                    return;
                }
            },
            Error: function (up, err) {
                var errinfo = "上传失败";
                if (err.code == -600) {
                    errinfo = "最大5G";
                }
                if (err.code == -200) {
                    errinfo = "服务器繁忙";
                }
                if (err.code == -601) {
                    errinfo = "禁止类型";
                }
                var hstr = '<div class="filelist_error" id="' + err.file.id + '"><span>' + err.file.name + ' (' + plupload.formatSize(err.file.size).toUpperCase() + ') </span>';
                hstr += '<div class="iu-slider">';
                hstr += '	<div id="slider_' + err.file.id + '" class="iu-slider-load"></div>';
                hstr += '</div>';
                hstr += '&nbsp;<b><font color="red">' + errinfo + '</font></b><a class="file-del" id="del_' + err.file.id + '" href="javascript:void(0)" >删除</a></div>';
                $('#filelist_' + pickfilesName).html(hstr);

                //删除按钮
                $("#del_" + err.file.id).click(function () {
                    $("#" + err.file.id).remove();
                    var file = uploader.getFile(err.file.id);
                    if (file) {
                        uploader.removeFile(file);
                    }
                });
                //setTimeout("uploader.start()",500);
            }
        }
    });
    uploader.init();
}

/**
 * 初始化文件上传组件UI
 * @param name
 */
function initUploadFileUI(name) {
    var html = '<div id="container_'+name+'" class="file-container">';
    html += '	<input type="hidden" name="'+name+'"/>';
    html += '	<button class="btn btn-default btn-file" id="pickfiles_'+name+'">';
    html += '		<i class="fa fa-paperclip"></i> 选择文件';
    html += '	</button>';
    html += '	<br />';
    html += '	<div id="filelist_'+name+'" class="file-body">上传组件加载失败，您可能没有安装 Flash.</div>';
    html += '	<div id="console_'+name+'" style="width: 100%;float: left;display: none;"></div>';
    html += '</div>';
    return html;
}

$(function () {
    //解析标签
    $("upload[type=image]").each(function () {
        var name = $(this).attr("name");
        var directory = $(this).attr("directory");
        //初始化UI
        $(this).after(initUploadImageUI(name));
        //初始化上传
        initUploadImage(name,directory);
        //判断是否编辑
        var value = $(this).attr("value");
        if(value){
            initUploadImageEdit(name,value);
        }
        $(this).remove();
    });

    $("upload[type=file]").each(function () {
        var name = $(this).attr("name");
        //初始化UI
        $(this).after(initUploadFileUI(name));
        //初始化上传
        initUploadFile(name);
        $(this).remove();
    });

});