var curtime = "";

function startTime() {
    const today = new Date();
    let h = today.getHours();
    let m = today.getMinutes();
    let s = today.getSeconds();
    m = checkTime(m);
    s = checkTime(s);
    document.getElementById('clock').innerHTML = h + ":" + m + ":" + s;
    setTimeout(startTime, 1000);
}

function checkTime(i) {
    if (i < 10) { i = "0" + i };  // add zero in front of numbers < 10
    return i;
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}
var posts = null;
var tpl = '<div pid="{pid}" class="post" style="padding-bottom:3%"><div class="checkbox" style="display:inline-block;height:100px;width:10%"><input type="checkbox" id="multi" name="multi" style="left:65%"></div><div class="content" style="display:inline-block;height:100px;width:85%"><h3>{title}</h3><p><i class="fa-solid fa-book"></i>{content}</p><p><i class="fa-solid fa-user"></i>{author} | <i class="fa-solid fa-calendar-days"></i>{date}</p><div class="btns" style="text-align:right"><button class="delete-btn" value="{pid}" name="delete" style="color:#eebcbc"><i class="fa-solid fa-trash"></i>删除</button> <button status="{status}" class="publish-btn" value="{status}" pid="{pid}" name="publish" style="color:#e9982f"><i class="fa-solid fa-eye"></i><b class="vis">发布</b></button> <button class="edit-btn" pid="{pid}" name="edit" style="color:#5555dc"><i class="fa-solid fa-pen"></i>修改</button></div></div></div>';

$(document).ready(function () {
    getPosts();
    initSave();
    startTime();
})

function getPosts() {

    // alert("remove posts");
    $.ajax({
        type: "get",
        url: "./request",
        data: {
            "token": getCookie("token"),
            "request": "get_posts",
            "type": "own"
        },
        dataType: "text",
        success: function (response) {
            response = JSON.parse(response);
            console.log(response);
            if (response.code == "0") { // 登录成功
                posts = response.posts;
                console.log(posts);
                $(".post").remove();
                $.each(posts, function (key, value) {
                    // console.log('hey here');
                    preNode = tpl.replaceAll("{pid}", key).replaceAll("{title}", value.title).replaceAll("{content}", value.content).replaceAll("{author}", value.author).replaceAll("{date}", value.date).replaceAll("{status}", value.status);
                    if (value.status == 1) {
                        preNode = preNode.replaceAll("发布", "转为草稿");
                        preNode = preNode.replaceAll("fa-eye", "fa-eye-slash")
                    }
                    var post = $.parseHTML(preNode);
                    $("#posts").append(post);
                    // alert("add 1 post");
                });
                initButtons();
                // initButtons();
            } else { // 登录失败
                alert("Get posts failed: " + response);
            }

        },
        error: function (xhr) {
            console.log("Error: " + xhr.responseText);
        }
    });
}

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
                    posts[$o.attr("pid")].status = target;
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

    $(".edit-btn").click(function () {
        $(".edit-panel").attr("action", "edit");
        $(".edit-panel").attr("pid", $(this).attr("pid"));
        document.querySelector(".edit-panel").style.width = "100%";
        // $(".close-btn").click(function(){$(".edit-panel").css("display","none")});
        var pid = $(this).attr("pid");
        // console.log(pid);
        $("#edit-title").val(posts[pid].title);
        $("#edit-content").val(posts[pid].content);
        $("#publish").prop('checked', posts[pid].status == 1)
        // $(".save-btn").click(function(){
        //     $.ajax({
        //         type: "get",
        //         url: "./request",
        //         data: {
        //             "token": getCookie("token"),
        //             "request": "update_post",
        //             "pid": pid,
        //             "title":$("#edit-title").val(),
        //             "content":$("#edit-content").val(),
        //             "publish":$("#publish").prop('checked')?1:0
        //         },
        //         dataType: "text",
        //         success: function (response) {
        //             response = JSON.parse(response);
        //             console.log(response);
        //             if (response.code == "0") { // 登录成功
        //                 console.log(response.info);
        //                 $(".edit-panel").css("display","none");
        //                 getPosts();
        //             } else { // 登录失败
        //                 alert("Delete failed: " + response.info);
        //             }
        //         },
        //         error: function (xhr) {
        //             console.log("Error: " + xhr.responseText);
        //         }
        //     });
        // })
    });

    $(".newpost-btn").click(function () {
        $(".edit-panel").attr("action", "new");
        $("#edit-title").val("");
        $("#edit-content").val("");
        $("#publish").prop('checked', false);
        document.querySelector(".edit-panel").style.width = "100%";

        // $(".save-btn").click(function(){
        //     $.ajax({
        //         type: "get",
        //         url: "./request",
        //         data: {
        //             "token": getCookie("token"),
        //             "request": "add_post",
        //             "title":$("#edit-title").val(),
        //             "content":$("#edit-content").val(),
        //             "publish":$("#publish").prop('checked')?1:0
        //         },
        //         dataType: "text",
        //         success: function (response) {
        //             response = JSON.parse(response);
        //             console.log(response);
        //             if (response.code == "0") {
        //                 console.log(response.info);
        //                 $(".edit-panel").css("display","none");
        //                 getPosts();
        //             } else {
        //                 alert("Delete failed: " + response.info);
        //             }
        //         },
        //         error: function (xhr) {
        //             console.log("Error: " + xhr.responseText);
        //         }
        //     });
        // })
    });

    $(".bulk-delete-btn").click(function () {
        var nodes = $('[id="multi"]:checked').parent().parent();
        if (nodes.length!=0) {
            $("#dialog-confirm").dialog({
                resizable: false,
                height: "auto",
                width: 400,
                modal: true,
                buttons: {
                    "确定": function () {
                        $.each(nodes, function (key, node) {
                            $.ajax({
                                type: "get",
                                url: "./request",
                                data: {
                                    "token": getCookie("token"),
                                    "request": "delete_post",
                                    "pid": node.getAttribute("pid")
                                },
                                dataType: "text",
                                success: function (response) {
                                    response = JSON.parse(response);
                                    console.log(response);
                                    if (response.code == "0") { // 登录成功

                                        console.log("delete pid " + node.getAttribute("pid"));

                                    } else { // 登录失败
                                        alert("Delete failed: " + response);
                                    }
                                },
                                error: function (xhr) {
                                    console.log("Error: " + xhr.responseText);
                                }
                            });
                            getPosts();
                        })
                        $(this).dialog("close");
                    },
                    Cancel: function () {
                        $(this).dialog("close");
                    }
                }
            });

        }else{
            $("#dialog-empty").dialog({
                resizable: false,
                height: "auto",
                width: 400,
                modal: true,
                buttons: {
                    "确定": function () {
                        $(this).dialog("close");
                    }
                }
            });
        }
    });
    $(".close-btn").click(function () { document.querySelector(".edit-panel").style.width = "0%"; });

    $("#selectall").click(function () {
        $('[id=multi]').prop('checked', this.checked);
    })
    console.log("initbuttons");
}

function initSave() {
    $(".save-btn").click(function () {
        var action = $(".edit-panel").attr("action");
        $.ajax({
            type: "get",
            url: "./request",
            data: {
                "token": getCookie("token"),
                "request": action == "edit" ? "update_post" : "add_post",
                "pid": $(".edit-panel").attr("pid"),
                "title": $("#edit-title").val(),
                "content": $("#edit-content").val(),
                "publish": $("#publish").prop('checked') ? 1 : 0
            },
            dataType: "text",
            success: function (response) {
                response = JSON.parse(response);
                console.log(response);
                if (response.code == "0") { // 登录成功
                    console.log(response.info);
                    document.querySelector(".edit-panel").style.width = "0%";
                    getPosts();
                } else { // 登录失败
                    alert("Delete failed: " + response.info);
                }
            },
            error: function (xhr) {
                console.log("Error: " + xhr.responseText);
            }
        });
    });
}

function bulkDelete() {



}