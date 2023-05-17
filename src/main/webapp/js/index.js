var curtime = "";
// 更新当前时间并更新页面显示的时间
// function updateTime() {
//     var now = new Date(); // 获取当前时间
//     // 格式化当前时间（年-月-日 时:分:秒）
//     var year = now.getFullYear();
//     var month = now.getMonth() + 1;
//     var day = now.getDate();
//     var hour = now.getHours();
//     var minute = now.getMinutes();
//     var second = now.getSeconds();
//     var formattedTime = `${year}-${month}-${day} ${hour}:${minute}:${second}`;
//     curtime = `${year}/${month}/${day}-${hour}:${minute}:${second}`;
//     // 更新页面中的时间显示
//     document.getElementById('current-time').innerText = formattedTime;
// }
// 初次加载页面时调用一次 updateTime() 函数
// updateTime();
// 每隔一秒更新一次时间
// setInterval(updateTime, 1000);
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}
var posts = null;
var tpl = '<div pid="{pid}" class="post" style="padding-bottom:3%"><div class="checkbox" style="display:inline-block;height:100px;width:10%"><input type="checkbox" id="multi" name="multi" style="left:65%"></div><div class="content" style="display:inline-block;height:100px;width:85%"><h3>{title}</h3><p><i class="fa-solid fa-book"></i>{content}</p><p><i class="fa-solid fa-user"></i>{author} |<i class="fa-solid fa-calendar-days"></i>{date}</p><div class="btns" style="text-align:right"><button class="delete-btn" value="{pid}" name="delete" style="color:#eebcbc"><i class="fa-solid fa-trash"></i>删除</button> <button status="{status}" class="publish-btn" value="{status}" pid="{pid}" name="publish" style="color:#e9982f"><i class="fa-solid fa-eye"></i><b class="vis">发布</b></button> <button class="edit-btn" pid="{pid}" name="edit" style="color:#5555dc"><i class="fa-solid fa-pen"></i>修改</button></div></div></div>';

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
            response = JSON.parse(response);
            console.log(response);
            if (response.code == "0") { // 登录成功
                posts = response.posts;
                $.each(posts, function (key, value) {
                    console.log('hey here');
                    preNode = tpl.replaceAll("{pid}", key).replaceAll("{title}", value.title).replaceAll("{content}", value.content).replaceAll("{author}", value.author).replaceAll("{date}", value.date).replaceAll("{status}", value.status);
                    if (value.status == 1) {
                        preNode = preNode.replaceAll("发布", "转为草稿");
                        preNode = preNode.replaceAll("fa-eye", "fa-eye-slash")
                    }
                    var post = $.parseHTML(preNode);
                    $("#posts").append(post)
                });
                initButtons();
            } else { // 登录失败
                alert("Get posts failed: " + response);
            }

        },
        error: function (xhr) {
            console.log("Error: " + xhr.responseText);
        }
    });


})

function initButtons() {
    $(".delete-btn").click(function () {
        console.log($(this).parent().parent().parent());
        var pid = $(this).attr("value");
        $("#dialog-confirm").dialog({
            resizable: false,
            height: "auto",
            width: 400,
            modal: true,
            buttons: {
                "确定": function () {
                    $.ajax({
                        type: "get",
                        url: "./request",
                        data: {
                            "token": getCookie("token"),
                            "request": "delete_post",
                            "pid": pid
                        },
                        dataType: "text",
                        success: function (response) {
                            response = JSON.parse(response);
                            console.log(response);
                            if (response.code == "0") { // 登录成功

                                $('.post[pid="{pid}"]'.replace("{pid}", pid)).remove();

                            } else { // 登录失败
                                alert("Delete failed: " + response);
                            }
                        },
                        error: function (xhr) {
                            console.log("Error: " + xhr.responseText);
                        }
                    });
                    $(this).dialog("close");
                },
                Cancel: function () {
                    $(this).dialog("close");
                }
            }
        });


    });
    $(".publish-btn").click(function () {
        console.log("publish hit");
        var target = 0;
        var text = "发布";
        var fa_class = "svg-inline--fa fa-eye";
        if ($(this).attr("value") == 0) {
            target = 1;
            text = "转为草稿";
            fa_class = "svg-inline--fa fa-eye-slash";
        } else if ($(this).attr("value") == 1) {
            target = 0;
            text = "发布";
            fa_class = "svg-inline--fa fa-eye";
        }
        // var pid=$(this).attr("pid");
        var $o = $(this);
        $.ajax({
            type: "get",
            url: "./request",
            data: {
                "token": getCookie("token"),
                "request": "set_status",
                "pid": $o.attr("pid"),
                "goal": target
            },
            dataType: "text",
            success: function (response) {
                response = JSON.parse(response);
                console.log(response);
                if (response.code == "0") { // 登录成功
                    // console.log($o.attr("class"));
                    console.log(response.info);
                    console.log('button[pid="' + $o.attr("pid") + '"]');
                    $('button[pid="' + $o.attr("pid") + '"]').attr("value", target);
                    // console.log(response.info);
                    $o.children(".vis").text(text);
                    // console.log(response.info);
                    $o.children(".svg-inline--fa").attr("class", fa_class);
                    // console.log($o.children(".svg-inline--fa").attr("class"));
                } else { // 登录失败
                    alert("Delete failed: " + response);
                }
            },
            error: function (xhr) {
                console.log("Error: " + xhr.responseText);
            }

        });
    });

    $(".edit-btn").click(function(){
        $(".edit-panel").css("display","block");
        $(".close-btn").click(function(){$(".edit-panel").css("display","none")});
        var pid=$(this).attr("pid");
        console.log(pid);
        $("#edit-title").val(posts[pid].title);
        $("#edit-content").val(posts[pid].content);
        $("#publish").prop('checked',posts[pid].status==1)
    })

    $("#selectall").click(function () {
        $('[id=multi]').prop('checked', this.checked);
    })
}