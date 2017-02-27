$(function() {
    $(".check-user").click(function() {
        var userId = $(this).attr("userId");
        var username = $(this).attr("username");
        $(".check-user").each(function() {
            $(this).removeClass("btn-info");
        });
        $(this).addClass("btn-info");

        $("input[name='userId']").val(userId);
        $("input[name='username']").val(username);
    });
});