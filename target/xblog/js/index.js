var curtime = ""; 
// 更新当前时间并更新页面显示的时间
function updateTime() {
    var now = new Date(); // 获取当前时间
    // 格式化当前时间（年-月-日 时:分:秒）
    var year = now.getFullYear();
    var month = now.getMonth() + 1;
    var day = now.getDate();
    var hour = now.getHours();
    var minute = now.getMinutes();
    var second = now.getSeconds();
    var formattedTime = `${year}-${month}-${day} ${hour}:${minute}:${second}`;
    curtime = `${year}/${month}/${day}-${hour}:${minute}:${second}`;
    // 更新页面中的时间显示
    document.getElementById('current-time').innerText = formattedTime;
}
// 初次加载页面时调用一次 updateTime() 函数
updateTime();
// 每隔一秒更新一次时间
setInterval(updateTime, 1000);

$(document).ready(function(){

    setTimeout(function() {
        $('#welcome-message').fadeOut('slow');
    }, 1000);

    $("button").click(function(){
        var action = $(this).attr("name");
        if (action == "collect"){
            if ($(this).css("color") == "rgb(238, 188, 188)"){
                $(this).css("color","rgb(230, 89, 89)");
            }
            else{
                $(this).css("color","rgb(238, 188, 188)");
            }
        }
        alert("操作成功");
        $.ajax({
            type:"get",
            url:"log.do",
            data:{
                "user_id":"id",
                "action":action,
                "item_id":$(this).parent().attr("name"),
                "time":curtime
            },
            dataType:"text",
            success:function(data){
                console.log(data);
            }
        });
    });
})