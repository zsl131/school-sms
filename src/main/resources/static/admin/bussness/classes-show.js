$(function() {
    //导入学生
    $(".upload-add-stu-btn").click(function() {
        var claId = $(this).attr("claId");
        var msg = '<form method="post" enctype="multipart/form-data">'+
                    '<div class="form-group form-group-lg">' +
                        '<div class="input-group">'+
                            '<div class="input-group-addon">选择需要导入学生信息的Excel文件：</div>'+
                            '<input name="file" type="file" class="form-control" onchange="uploadFile(this)"/>'+
                            '<input name="status" type="hidden" value="0"/>'+
                            '<input name="claId" type="hidden" value="'+claId+'"/>'+
                        '</div>'+
                        '<div class="message" style="max-height:200px; overflow: auto; padding-top:8px;"></div>'+
                    '</div></form>';
        var myDialog = confirmDialog(msg, "上传需要导入的学生信息Excel文件", function() {
            var formObj = $(myDialog).find("form");
            var status = $(formObj).find("input[name='status']").val();
            var fileObj = $(formObj).find("input[name='file']");
            if('1'==status) {
                var formData = new FormData();
                formData.append("claId", claId);
                formData.append("file", $(fileObj)[0].files[0]);
                $.ajax({
                    url: '/admin/student/importStu',
                    type: 'POST',
                    data: formData,
                    processData : false,
                    // 告诉jQuery不要去设置Content-Type请求头
                    contentType : false,
                    success: function(res) {
                        successProcess(res, formObj);
                    },
                    error: function(res) {
                        alert("提交出错:"+res);
                        cleanFile(fileObj); //清空file内容
                    }
                });

                return false;
            } else {
                alert("请先选择需要上传的Excel文件");
            }
        });
    });

    $(".upload-remove-stu-btn").click(function() {
        var claId = $(this).attr("claId");
        var msg = '<form method="post" enctype="multipart/form-data">'+
                    '<div class="form-group form-group-lg">' +
                        '<div class="input-group">'+
                            '<div class="input-group-addon">选择需要删除的学生的Excel文件：</div>'+
                            '<input name="file" type="file" class="form-control" onchange="uploadFile(this)"/>'+
                            '<input name="status" type="hidden" value="0"/>'+
                            '<input name="claId" type="hidden" value="'+claId+'"/>'+
                        '</div>'+
                        '<div class="message" style="max-height:200px; overflow: auto; padding-top:8px;"></div>'+
                    '</div></form>';

        var myDialog = confirmDialog(msg, "上传需要删除的学生的Excel文件", function() {
            var formObj = $(myDialog).find("form");
            var status = $(formObj).find("input[name='status']").val();
            var fileObj = $(formObj).find("input[name='file']");
            if('1'==status) {
                var formData = new FormData();
                formData.append("claId", claId);
                formData.append("file", $(fileObj)[0].files[0]);
                $.ajax({
                    url: '/admin/student/removeStu',
                    type: 'POST',
                    data: formData,
                    processData : false,
                    // 告诉jQuery不要去设置Content-Type请求头
                    contentType : false,
                    success: function(res) {
                        successProcess(res, formObj);
                    },
                    error: function(res) {
                        alert("提交出错:"+res);
                        cleanFile(fileObj); //清空file内容
                    }
                });
            }
        });
    });
});

function successProcess(res, formObj) {
    var html = '';
    if(res.status=='0') {
        html += "处理完成！";
        html += "\n信息如下：";
        var resJson = eval('(' + res.msg + ')');
        html += "共【"+resJson.total+"】条数据，成功<b style='color: blue'>【"+resJson.suc+"】</b>条，失败<b style='color:#F00;'>【"+resJson.err+"】</b>条。\n具体信息：\n"+resJson.msg;
    }
    else {html += "<p style='color:#F00'>处理出错！</p>"+res.msg;}

    html = replaceAll(html, "\n", "<br/>");
    $(formObj).find(".message").html(html);

    cleanFile($(formObj).find('input[name="file"]')); //清空file内容
}

function uploadFile(obj) {
    var strs = $(obj).val().split('.');
    var suffix = strs [strs .length - 1];

    if (suffix != 'xls' && suffix != 'xlsx') {
        alert("你选择的不是Excel文件，请重新选择");
        obj.outerHTML = obj.outerHTML; //这样清空，在IE8下也能执行成功
        $(obj).parents("form").find("input[name='status']").val("0");
    } else {
        $(obj).parents("form").find("input[name='status']").val("1");
    }
}

function cleanFile(obj) {
    obj.outerHTML = obj.outerHTML; //这样清空，在IE8下也能执行成功
    $(obj).val("");
}