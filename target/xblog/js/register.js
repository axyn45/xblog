// 学号验证函数
function isValidStudentId(studentId) {
return /^\d{10}$/.test(studentId);
}

// 密码验证函数
function isValidPassword(password) {
return /^(?=.*\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{6,16}$/.test(password);
}

// 邮箱验证函数
function isValidEmail(email) {
return /\S+@\S+\.\S+/.test(email);
}

$(document).ready(function(){
    $("#register").click(function(event){
        var studentId = $("#student-id").val();
        var password = $("#password").val();
        var nickname = $("#nickname").val();
        var email = $("#email").val();
        var major = $("#major").val();

        if (!isValidStudentId(studentId)) {
            event.preventDefault(); // 阻止表单提交
            $("#student-id").closest(".form-group").addClass("has-error");
            $("#tip1").removeClass("hide");
        } else if (!isValidPassword(password)) {
            event.preventDefault();
            $("#password").closest(".form-group").addClass("has-error");
            $("#tip2").removeClass("hide");
        } else if (nickname.trim() === "") {
            $("#nickname").closest(".form-group").addClass("has-error");
            event.preventDefault();
        } else if (!isValidEmail(email)) {
            event.preventDefault();
            $("#email").closest(".form-group").addClass("has-error");
            $("#tip3").removeClass("hide");
        } else if (major === "") {
            alert("请选择专业！");
            event.preventDefault();
        }

        $("input").focus(function() {
            $(this).closest(".form-group").removeClass("has-error");
            $(this).siblings(".help-block").addClass("hide");
        });

        $.ajax({
            type:"get",
            url:"register.do",
            data:{
                "userid":$("#student-id").val(),
                "password":$("#password").val(),
                "nickname":$("#nickname").val(),
                "email":$("#email").val(),
                "major":$("#major").val()
            },
            dataType:"json",
            success:function(data){
                if(data === "success"){
                    alert("注册成功！");
                }
            },
            error: function(xhr) {
                if (xhr.responseText === "success"){
                    alert("注册成功！");
                }
                console.log("Error: " + xhr.responseText);
            }
        });
    });
})