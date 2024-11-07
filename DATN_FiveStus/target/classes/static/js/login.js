$(document).ready(function () {

    $('#maNVNC').on('click', function () {
        $('#username').remove('is-invalid');
        $('#password').remove('is-invalid');
        $('#passwordError').remove();
    })

    $('#loginForm').on('submit', function (event) {
        event.preventDefault(); // Ngăn chặn việc gửi form mặc định

        let formData = {
            username: $('#username').val().trim(),
            password: $('#password').val().trim()
        };

        $.ajax({
            url: '/api/auth/sign-in',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response) {
                let tokenJWT = response.response["token"];
                // Lưu token vào cookie
                Cookies.set('authToken', tokenJWT, {path: '/', secure: true, sameSite: 'Strict'});
                $.ajaxSetup({
                    headers: {
                        'Authorization': 'Bearer ' + tokenJWT
                    }
                });
                let roles = response.response["roles"];
                if (roles.includes("ROLE_ADMIN") || roles.includes("ROLE_MANAGER")||roles.includes("ROLE_EMPLOYEE")) {
                    window.location.href = '/dich-vu';
                }
            },
            error: function () {
                $('#username').addClass('is-invalid');
                $('#password').addClass('is-invalid');
                $('#passwordError').remove();
                $('#password').after('<div id="passwordError" class="invalid-feedback">Tên tài khoản hoặc mật khẩu sai.</div>');
            }
        });
    });



});