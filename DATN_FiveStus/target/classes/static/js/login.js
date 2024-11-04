$(document).ready(function () {

    $('#modaltoggleNC').modal({
        backdrop: 'static',
        keyboard: false
    });

    $('#btnNhanCa').click(function () {
        let formNC ={
            tienDauCa:$('#tienQuyDauCa').val(),
        }
        $.ajax({
            url: 'http://localhost:8080/giao-ca/add-row',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formNC),
            success: function (response) {
                if(response === true){
                    $('#checkBox').prop('checked',false);
                    $('#tienQuyDauCa').val('');
                    $('#tienQuyDauCa').prop('disabled',false);
                    let timerInterval;
                    Swal.fire({
                        title: "Nhận ca thành công!",
                        icon: "success",
                        timer: 2000,
                        timerProgressBar: true,
                        showConfirmButton:false,
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
                            window.location.href = '/dich-vu';
                        }
                    });

                } else {
                    Swal.fire({
                        title: "Lõi!",
                        text: 'Hệ thống xảy ra lỗi!',
                        icon: "error",
                        showConfirmButton: false,
                        showCancelButton: true,
                        cancelButtonText: 'Thoát!'
                    });
                }
            },
            error: function () {
                Swal.fire({
                    title: "Lõi!",
                    text: 'Đã xảy ra sự cố!',
                    icon: "error",
                    showConfirmButton: false,
                    showCancelButton: true,
                    cancelButtonText: 'Thoát!'
                });
            }
        });

    })

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
                if (roles.includes("ROLE_ADMIN") || roles.includes("ROLE_MANAGER")) {
                    window.location.href = '/dich-vu';
                } else if (roles.includes("ROLE_EMPLOYEE")) {
                    checkStatus(formData.username);
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

    function checkStatus(email) {
        $.ajax({
            url: 'http://localhost:8080/giao-ca/last-data',
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                let employee = response["nhanVien"];
                let emailNV = employee["email"];
                let status = response["trangThai"];
                redirectPage(email, emailNV, status,employee,response["tongTienMatThucTe"]);
            },
            error: function () {
            }
        });
    }

    function redirectPage(email, emailEmp, status,employee,tongTienMatThucTe) {
        const isEmployeeEmail = emailEmp === email;
        switch (status) {
            case true:
                if(isEmployeeEmail){
                    window.location.href = '/dich-vu';
                }else{
                    showModal(employee,tongTienMatThucTe);
                }
                break;
            case false:
                if (!isEmployeeEmail) {
                    showWarningMessage();
                } else {
                    showModal(employee,tongTienMatThucTe);
                }
                break;
            default:
                if(isEmployeeEmail){
                    window.location.href = '/dich-vu';
                }else{
                    showModal(employee,tongTienMatThucTe);
                }
                break;
        }
    }

    function showModal(employee,tongTienMatThucTe) {
        $('#modaltoggleNC').modal('show');
        $('#checkBox').change(function() {
            if ($(this).is(':checked')) {
                $('#tienQuyDauCa').val(tongTienMatThucTe);
                $('#tienQuyDauCa').attr('readonly', true);
                $('#input1').after(' <div id="input2" class="mb-3">\n' +
                    '                        <label for="nvBanGiao" class="form-label">Nhân viên bàn giao</label>\n' +
                    '                        <input type="text" class="form-control" id="nvBanGiao">\n' +
                    '                    </div>');
                $('#nvBanGiao').val(employee["hoTen"]);
            } else {
                $('#tienQuyDauCa').val('');
                $('#tienQuyDauCa').removeAttr('readonly');
                $('#input2').remove();
            }
        });

    }

    function showWarningMessage() {
        Swal.fire({
            title: "Cảnh báo!",
            text: 'Tài khoản nhân viên ca trước chưa đăng xuất!',
            icon: "warning",
            showConfirmButton: false,
            showCancelButton: true,
            allowOutsideClick: false,
            allowEscapeKey: false,
            cancelButtonText: 'Thoát!'
        });
    }

});