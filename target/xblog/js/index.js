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
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}
var posts = null;
var tpl='<div pid="{pid}" class="post" style="padding-bottom:3%"><div class="checkbox" style="display:inline-block;height:100px;width:10%"><input type="checkbox" id="multi" name="multi" style="left:65%"></div><div class="content" style="display:inline-block;height:100px;width:85%"><h3>{title}</h3><p><i class="fa-solid fa-book"></i>{content}</p><p><i class="fa-solid fa-user"></i>{author} |<i class="fa-solid fa-calendar-days"></i>{date}</p><div class="btns" style="text-align:right"><button class="delete-btn" value="{pid}" name="delete" style="color:#eebcbc"><i class="fa-solid fa-trash"></i>删除</button><button class="publish-btn" value="{pid}" name="publish" style="color:#e9982f"><i class="fa-solid fa-eye"></i>发布</button><button class="modify-btn" value="{pid}" name="modify" style="color:#5555dc"><i class="fa-solid fa-pen"></i>修改</button></div></div></div>';

$(document).ready(function () {
    $.ajax({
        type: "get",
        url: "./request",
        data: {
            "token": getCookie("token"),
            "request": "get_posts"
        },
        dataType: "text",
        success: function (response) {
            response=JSON.parse(response);
            console.log(response);
            if (response.code == "0") { // 登录成功
                posts = response.posts;
            } else { // 登录失败
                alert("Get posts failed: " + response);
            }
            $.each( posts, function( key, value ) {
                console.log('hey here');
                preNode=tpl.replaceAll("{pid}",key).replaceAll("{title}",value.title).replaceAll("{content}",value.title).replaceAll("{author}",value.author).replaceAll("{date}",value.date);
                var post=$.parseHTML(preNode);
                $("#posts").append(post)
            });
        },
        error: function (xhr) {
            console.log("Error: " + xhr.responseText);
        }
    });
    $(".delete-btn").click(function(){
        console.log($(this).parent().parent().parent());
        var pid=$(this).attr("value");
        $.ajax({
            type: "get",
            url: "./request",
            data: {
                "token": getCookie("token"),
                "request": "delete_post",
                "pid":pid
            },
            dataType: "text",
            success: function (response) {
                response=JSON.parse(response);
                console.log(response);
                if (response.code == "0") { // 登录成功

                    $("[pid={pid}]".replace("{pid}",pid)).remove();
                } else { // 登录失败
                    alert("Delete failed: " + response);
                }
            },
            error: function (xhr) {
                console.log("Error: " + xhr.responseText);
            }
        });
    })

})