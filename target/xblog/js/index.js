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
var tpl = '<div pid="{pid}" class="post" style="padding-bottom:3%;padding-left:5%"><div class="content" style="display:inline-block;height:100px;width:85%"><h3>{title}</h3><p><i class="fa-solid fa-book"></i> {content}</p><p><i class="fa-solid fa-user"></i> {author} | <i class="fa-solid fa-calendar-days"></i> {date}</p></div></div>';

$(document).ready(function () {
    getPosts();
})

function getPosts(){
    
    // alert("remove posts");
    $.ajax({
        type: "get",
        url: "./request",
        data: {
            "token": getCookie("token"),
            "request": "get_posts",
            "type":"published"
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

$(document).ready(function(){
    $("#searchBox").on("keyup", function() {
      var value = $(this).val().toLowerCase();
      $("#posts .post").filter(function() {
        $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
      });
    });
  });