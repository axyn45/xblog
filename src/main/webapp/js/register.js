// 学号验证函数
function isValidUname(uname) {
return /^([A-z,0-9]){4,10}$/.test(uname);
}

// 密码验证函数
function isValidPassword(password) {
return /^(?=.*\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,20}$/.test(password);
}

// 邮箱验证函数
function isValidEmail(email) {
return /\S+@\S+\.\S+/.test(email);
}

$(document).ready(function(){
    $("#register").click(function(event){
        var uname = $("#uname").val();
        var password = $("#password").val();
        var fname = $("#fname").val();
        var email = $("#email").val();
        var sname = $("#sname").val();

        if (!isValidUname(uname)) {
            event.preventDefault(); // 阻止表单提交
            $("#uname").closest(".form-group").addClass("has-error");
            $("#tip1").removeClass("hide");
        } else if (!isValidPassword(password)) {
            event.preventDefault();
            $("#password").closest(".form-group").addClass("has-error");
            $("#tip2").removeClass("hide");
        } else if (!isValidEmail(email)) {
            event.preventDefault();
            $("#email").closest(".form-group").addClass("has-error");
            $("#tip3").removeClass("hide");
        }
        $("input").focus(function() {
            $(this).closest(".form-group").removeClass("has-error");
            $(this).siblings(".help-block").addClass("hide");
        });

        $.ajax({
            type:"get",
            url:"register.do",
            data:{
                "uname":$("#uname").val(),
                "pass":sha256($("#password").val()),
                "email":$("#email").val(),
                "fname":$("#fname").val(),
                "sname":$("#sname").val()
            },
            dataType:"json",
            success:function(data){
                if(data.code==0){
                    alert("注册成功！");
                    window.location = "./login.html";
                }
                else{
                    alert(data.info+"("+data.code+")");
                }
            },
            error: function(data) {
                console.log("Error: " + data.responseText);
            }
        });
    });
})