var currentStep = 1;
var excelJson = null;
var moduleObj = null;
var sourceType = 0;
var sourceObj = []; //从数据库中取数据的属性对象
var choiceParams=""; //选择的
var token = '';
$(function() {
    setNextStep(1);
    $(".module-panel .panel-body button").click(function() {
        //var moduleId = $(this).attr("moduleIid");
        $(this).parents(".panel-body").find("button").each(function() {
            $(this).removeClass("btn-info");
        });
        $(this).addClass("btn-info");
        setNextStep(2);
        moduleObj = this;
        setModuleHtml();
    });

    $(".single-temp").click(function() {
        setTempStyle(this);
    });

    $(".temp-2").click(function() {
        queryAllResource(this);
    });

    $(".submit-btn").click(function() {
        var thisObj = $(this);
        var mid = $(".module-panel").find(".panel-body").find("button.btn-info").attr("moduleIid");
        console.log("mid:"+mid+"|"+token+"|"+choiceParams+"|"+sourceType);
        if(!mid) {
            alert("请选择短信模板");
        } else if(token=='' || sourceType<=0) {
            alert("请选择数据源");
        } else if(choiceParams=='') {
            alert("请设置短信模板变量替换值");
        } else {
            var task = $("#task-check").is(":checked")?"1":"0";
            var time = $("#task-time").val();
            $(thisObj).css("display", "none");
            $.post("/admin/send/sendSms", {mid:mid, sourceType:sourceType, token:token, pars:choiceParams, task:task, time:time}, function(res) {
                if(res=='1') {
                    alert("发送成功，跳转至发送记录");
                    window.location.href = "/admin/sendRecord/list";
                } else {
                    alert("发送失败："+res);
                    $(thisObj).css("display", "block");
                }
            }, "json");
        }
    });

    $("#task-check").click(function() {
        var checked = $(this).is(":checked");
        if(checked) {
            $("#task-time").css("display", "block");
        } else {
            $("#task-time").css("display", "none");
        }
    });

    $("#task-time").jeDate({
        isinitVal:true, //初始化日期
        festival: true, //显示农历
        isClear:false,
        initAddVal:[0],
        minDate: $.nowDate(0),
        format: 'YYYY-MM-DD hh:mm:ss'
    });
})

function queryAllResource(obj) {
    var showObj = $(obj).parents(".single-source").find(".show-info");
    $.post("/admin/send/queryAllSource", {}, function(res) {
        if("0"==res.status) {
            var resObj = eval("("+res.msg+")");
            //alert(resObj.length);
            var html = '';
            for(var i=0;i<resObj.length;i++) {
                var singleObj = resObj[i];
                html += '<button class="btn btn-default" value="'+singleObj.packageName+'" onclick="queryAttr(this)">'+singleObj.showName+'</button>';
            }
            $(showObj).html(html);
        } else {
            $(showObj).html("获取失败："+res.msg);
        }
        //console.log(res);
    }, "json");
}

function queryAttr(obj) {
    var packageName = $(obj).val();
    token = packageName;
    $.post("/admin/send/querySourcePars", {packageName:packageName}, function(res) {
        if("0"==res.status) {
//            console.log(res);
            sourceObj = eval("("+res.msg+")");
            setModuleHtml();
            $(obj).parents(".show-info").find("button").each(function() {
                $(this).addClass("btn-default"); $(this).removeClass("btn-info");
            });
            $(obj).addClass("btn-info"); $(obj).removeClass("btn-default");
        } else {
            alert("获取对象属性失败："+res.msg);
        }
    }, "json");
}

function setTempStyle(obj) {
    var cls = $(obj).attr("class");
    //console.log(cls);
    $(obj).css({"display":"none"});
    if(cls.indexOf("temp-1")>=0) {
        sourceType = 1;
        $(".temp-2").css({"display":"block"});
    } else {
        sourceType = 2;
        $(".temp-1").css({"display":"block"});
    }

    var excelJson = null;
    var moduleObj = null;
    var sourceObj = []; //从数据库中取数据的属性对象
    setModuleHtml();
}

function buildReplace(str) {
    //str = '【昭通网】#name#，您的验#myPrice#证码是#code#。';
    var pattern = /\#(.*?)\#/g;
    var values = str.match(pattern);
    if(values==null) {values = [];}

    return values;
}

function setModuleHtml() {
    var parObj = $(".value-panel").find(".params-div");
    $(parObj).html("");
    buildSingleModuleHtml(parObj, "手机号码", "phone");
    var values = [];
    if(moduleObj!=null) {
        var str = $(moduleObj).html();
        //console.log(str);
        values = buildReplace(str);
        $(".value-panel").find(".selected-module").html("已选择模板："+str);
    }
    for(var i=0; i<values.length; i++) {
        buildSingleModuleHtml(parObj, values[i], values[i]);
    }
    //console.log(sourceType+"=="+sourceObj);
}

function checkParam() {
    var isOk = true;
    var parValues = '';
    $(".param-select").each(function() {
        var val = $(this).val(); var field = $(this).attr("param");
        if(val==null || ""==val) {
            isOk = false;
        } else {
            parValues += field+"="+val+"&";
        }
    });

    choiceParams = parValues;

    if(isOk) {
        setNextStep(4);
    }
}

function buildSingleModuleHtml(obj, val, field) {
    var html = '<div class="form-group form-group-lg"><div class="input-group">' +
                    '<div class="input-group-addon">'+val+'替换为：</div>' +
                    '<select param="'+field+'" class="param-select form-control" onchange="checkParam()">' +
                        '<option value="">==请选择==</option>' ;
    if(sourceType==1 && excelJson!=null && "0"==excelJson.status) {
        var myJson = eval('(' + excelJson.msg + ')');
        token = myJson.token;
        var headList = myJson.headDtoList;
        for(var i=0;i<headList.length;i++) {
            var headObj = headList[i];
            html += '<option value="'+headObj.no+'">'+headObj.field+'</option>';
        }
        setNextStep(3);
    } else if(sourceType==2) {
        for(var i=0;i<sourceObj.length;i++) {
            var sObj = sourceObj[i];
            html += '<option value="'+sObj.packageName+'.'+sObj.className+'">'+sObj.showName+'</option>';
        }
        setNextStep(3);
    }

     html += '</select>' +
                '</div></div>';
//    return html;
    $(obj).append(html)
}

function uploadFile(obj) {
    var strs = $(obj).val().split('.');
    var suffix = strs [strs .length - 1];

    if (suffix != 'xls' && suffix != 'xlsx') {
        alert("你选择的不是Excel文件，请重新选择");
        obj.outerHTML = obj.outerHTML; //这样清空，在IE8下也能执行成功
    } else {
//        var fileObj = $("input[name='file']");
        var formData = new FormData();
        formData.append("file", $(obj)[0].files[0]);
        $.ajax({
            url: '/admin/send/uploadExcel',
            type: 'POST',
            data: formData,
            processData : false,
            // 告诉jQuery不要去设置Content-Type请求头
            contentType : false,
            success: function(res) {
                console.log(res);
                excelJson = res;
                showExcelInfo(obj);
                setNextStep(3);
                setModuleHtml();
            },
            error: function(res) {
                alert("提交出错:"+res);
            }
        });
    }
}

function showExcelInfo(obj) {
    var showObj = $(obj).siblings(".show-info");
    var html = "";
    var myJson = eval('(' + excelJson.msg + ')');
    if("0"==excelJson.status) {
        var headList = myJson.headDtoList;
        var rowList = myJson.rowDtoList;
        html = "Excel解析成功：表头【"+headList.length+"】项，数据【"+rowList.length+"】条";
    } else {
        html = "Excel解析失败："+excelJson.msg;
    }
    //console.log(html);
    $(showObj).html(html);
}

function setNextStep(step) {
    currentStep = step;
    if(step>=currentStep) {
        setTopStyle(step);
        setArrowStyle(step);
        setPanelStyle(step);
    }
}

function setArrowStyle(step) {
    $(".arrow-div").find(".arrow-up").each(function() {
        var cls = $(this).attr("class");
        if(cls.indexOf("arrow-hidden")<0) {
            $(this).addClass("arrow-hidden");
        }
    });
    $((".arrow-"+step)).find(".arrow-up").removeClass("arrow-hidden");
}

function setTopStyle(step) {
    for(var i=1; i<=step; i++) {
        $((".step-"+i)).addClass("current-step");
    }
}

function setPanelStyle(step) {
    $(".send-content").find(".panel").each(function() {
        $(this).removeClass("panel-info");
        $(this).addClass("panel-default");
    });

    $((".panel-"+step)).removeClass("panel-default");
    $((".panel-"+step)).addClass("panel-info");
}