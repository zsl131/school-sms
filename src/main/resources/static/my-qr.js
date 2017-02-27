$(document).ready(function() {
    var uuid = "";
    $.get("/qr/login/showQrGen", function(data) {
        var obj = eval("(" + data + ")");
        //设置该uuid值
        uuid = obj.uuid;
        //设置二维码图片地址
        $("#QrGen").attr("src", obj.img);
        //检查验证登录
        checkScan();
    });
    function checkScan() {
        setInterval(function() {
            $.get("/qr/login/checkScan?uuid=" + uuid,
                    function(data) {
                        if (data == "ok") {
                            //验证成功并重定向到welcome页面
                            window.location = "welcome.jsp";
                        }
                    });
        },4000)
    }
});