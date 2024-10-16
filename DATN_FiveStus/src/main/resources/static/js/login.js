$(document).ready(function() {

    $('#loginForm').on('submit', function(event) {
        event.preventDefault(); // Ngăn chặn việc gửi form mặc định

        // Lấy dữ liệu từ form
        let formData = {
            username: $('#username').val(),
            password: $('#password').val()
        };
        // Gửi yêu cầu đăng nhập và nhận token từ server
        $.ajax({
            url: '/api/auth/sign-in',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function(response) {

                let tokenJWT = response.response["token"];
                // Lưu token vào cookie
                Cookies.set('authToken', tokenJWT, { path: '/', secure: true, sameSite: 'Strict' });
                $.ajaxSetup({
                    headers: {
                        'Authorization': 'Bearer ' + tokenJWT
                    }
                });
                window.location.href ='/quan-ly-nhan-vien';
            },
            error: function() {
                alert("Tài khoản hoặc mật khẩu sai!")
            }
        });
    });
});