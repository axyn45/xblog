// $(function() {
//     var loginForm = $("#login-form form");
//     loginForm.submit(function(event) {
//     var studentId = $("#student-id").val();
//     var password = $("#password").val();

//     if (!isValidStudentId(studentId)) {
//         event.preventDefault();
//         $("#student-id").closest(".form-group").addClass("has-error");
//         $("#tip1").removeClass("hide");
//     } else if (!isValidPassword(password)) {
//         event.preventDefault();
//         $("#password").closest(".form-group").addClass("has-error");
//         $("#tip2").removeClass("hide");
//     }
//     });

//     // 输入框获得焦点时移除错误样式和提示信息
//     $("input").focus(function() {
//         $(this).closest(".form-group").removeClass("has-error");
//         $(this).siblings(".help-block").addClass("hide");
//     });
// });

function isValidStudentId(studentId) {
    return /^\d{10}$/.test(studentId);
}

function isValidPassword(password) {
    return /^(?=.*\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{6,16}$/.test(password);
}

// async function sha256(message) {
//     // encode as UTF-8
//     const msgBuffer = new TextEncoder().encode(message);                    

//     // hash the message
//     const hashBuffer = await crypto.subtle.digest('SHA-256', msgBuffer);

//     // convert ArrayBuffer to Array
//     const hashArray = Array.from(new Uint8Array(hashBuffer));

//     // convert bytes to hex string                  
//     const hashHex = hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
//     return hashHex;
// }

$(document).ready(function(){
    var ct=1;
    $("#login").click(function(){
        $.ajax({
            type:"get",
            url:"login.do",
            data:{
                "id":$("#uid").val(),
                "pass":sha256($("#password").val()),
                "captcha":$("#captcha").val(),
                "remember-me":$("#remember-me").is(":checked")
            },
            dataType:"text",
            success:function(response){
                console.log(response);
                if (response === "success") { // 登录成功
                    location.href="/xblog/index.html";
                } else { // 登录失败
                    alert(response);
                    $("[alt=captcha]").attr("src",$("[alt=captcha]").attr("src")+"?ct="+ct++);
                }
            },
            error: function(xhr) {
                console.log("Error: " + xhr.responseText);
            }
        });
    });
    $("[alt=captcha]").click(function(){
        $("[alt=captcha]").attr("src",$("[alt=captcha]").attr("src")+"?ct="+ct++);
    })
})
// $(document).ready(function(){
//     $.ajax({
//         type:"get",
//         url:"./login.do",
//         data:{
//             "user_id":2222222222,
//             "password":"Wddd2222",
//             "remember-me":true
//         },
//         dataType:"text",
//         success:function(response){
//             console.log(response);
//             if (response === "success") { // 登录成功
//                 console.log("111")
//             } else { // 登录失败
//                 alert(response);
//             }
//         },
//         error: function(xhr) {
//             console.log("Error: " + xhr.responseText);
//         }
//     })
// })