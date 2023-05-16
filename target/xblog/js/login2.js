function isValidStudentId(studentId) {
    return /^\d{10}$/.test(studentId);
}

function isValidPassword(password) {
    return /^(?=.*\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{6,16}$/.test(password);
}


$(document).ready(function(){
    $("#login").click(async function(){
        $.ajax({
            type:"get",
            url:"login.do",
            data:{
                "id":$("#uid").val(),
                "pass":sha256($("#password").val()),
                "remember-me":$("#remember-me").is(":checked")
            },
            dataType:"text",
            success:function(response){
                console.log(response);
                if (response === "success") { // 登录成功
                    location.href="/xblog/index.html";
                } else { // 登录失败
                    alert(response);
                }
            },
            error: function(xhr) {
                console.log("Error: " + xhr.responseText);
            }
        });
    });
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