$(function() {
    $(".check-teacher").click(function() {
        var id = $(this).attr("teaId");
        var name = $(this).attr("name");
        var phone = $(this).attr("phone");
        var openid = $(this).attr("openid");
        $(".check-teacher").each(function() {
            $(this).removeClass("btn-info");
        });
        $(this).addClass("btn-info");

        $("input[name='masterId']").val(id);
        $("input[name='masterName']").val(name);
        $("input[name='masterPhone']").val(phone);
        $("input[name='masterOpenid']").val(openid);
    });
});