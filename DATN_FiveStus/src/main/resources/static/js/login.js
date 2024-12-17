$(document).ready(function () {

    $('#username').on('click', function () {
        removeError();
    })
    $('#password').on('click', function () {
        removeError();
    })

    function removeError() {
        $('#username').remove('is-invalid');
        $('#password').remove('is-invalid');
        $('#passwordError').remove();
        $('#usernameError').remove();
    }

    $('#loginForm').on('submit', function (event) {
        event.preventDefault(); // Ngăn chặn việc gửi form mặc định
        $('#loginForm .is-invalid').removeClass('is-invalid');
        removeError();
        let username = $('#username').val().trim();
        let password= $('#password').val().trim()
        if (username === "") {
            $('#username').addClass('is-invalid');
            $('#username').after('<div id="usernameError" class="invalid-feedback">Chưa nhập email người dùng!</div>');
            return;
        }
        if (password === '') {
            $('#password').addClass('is-invalid');
            $('#password').after('<div id="usernameError" class="invalid-feedback">Chưa nhập mật khẩu!</div>');
            return;
        }
        var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(username)) {
            $('#username').addClass('is-invalid');
            $('#username').after('<div id="usernameError" class="invalid-feedback">Email không đúng định dạng!</div>');
            return;
        }

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
                if (response.response === null) {
                    let timerInterval;
                    Swal.fire({
                        title: `${response.message}`,
                        icon: "error",
                        timer: 2000,
                        timerProgressBar: true,
                        showConfirmButton: false,
                        allowOutsideClick: false,
                        allowEscapeKey: false,
                        didOpen: () => {
                            timerInterval = setInterval(() => {
                            }, 100);
                        },
                        willClose: () => {
                            clearInterval(timerInterval);
                        }
                    }).then((result) => {
                        if (result.dismiss === Swal.DismissReason.timer) {
                            window.location.href = '/login';
                        }
                    });
                    return;
                }
                let tokenJWT = response.response["token"];
                // Lưu token vào cookie
                Cookies.set('authToken', tokenJWT, {path: '/', secure: true, sameSite: 'Strict'});
                $.ajaxSetup({
                    headers: {
                        'Authorization': 'Bearer ' + tokenJWT
                    }
                });
                let roles = response.response["roles"];
                if (roles.includes("ROLE_ADMIN") || roles.includes("ROLE_MANAGER") || roles.includes("ROLE_EMPLOYEE")) {
                    window.location.href = '/dich-vu';
                } else if (roles.includes("ROLE_USER")) {
                    let timerInterval;
                    Swal.fire({
                        title: "Bạn không có quyền truy cập trang này!",
                        icon: "warning",
                        timer: 2000,
                        timerProgressBar: true,
                        showConfirmButton: false,
                        allowOutsideClick: false,
                        allowEscapeKey: false,
                        didOpen: () => {
                            timerInterval = setInterval(() => {
                            }, 100);
                        },
                        willClose: () => {
                            clearInterval(timerInterval);
                        }
                    }).then((result) => {
                        if (result.dismiss === Swal.DismissReason.timer) {
                            Cookies.remove('authToken', {path: '/'});
                            window.location.href = '/khach-hang/trang-chu';
                        }
                    });
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