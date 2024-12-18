$(document).ready(function () {
    $('#confirm-password').hide()
    $('#userNameCustomer').hide()
    $('#customerName').hide()
    $('#phoneNumber').hide()
    $('#otp').hide()
    showFogotPass()

    function showFogotPass() {
        $('#forgotPass').on('click', () => {
            $('#passCustomer').hide()
            $('#checkboxForm').hide();
            $('#forgotPass').hide();
            $('#otp').hide()
            $('#btnLogin').text('Gửi mã')
            $('#titleModal').text('Quên mật khẩu')
            $('#userEmailCustomer').attr('placeholder', 'Nhập email để nhận mã');
            removeError();
            removeForm();
        });
    }

    function removeError() {
        $('#userEmailCustomerError').remove();
        $('#passCustomerError').remove();
        $('#userNameCustomerError').remove();
        $('#confirm-passwordError').remove();
        $('#modalDKSDError').remove();
        $('#otpError').remove();
        $('#customerNameError').remove();
        $('#phoneNumberError').remove();
    }

    function removeForm() {
        $('#passCustomer').val('')
        $('#userEmailCustomer').val('')
        $('#userNameCustomer').val('')
        $('#confirm-password').val('')
        $('#remember-me').prop('checked', false);
        $('#otp').val('')
        $('#customerName').val('')
        $('#phoneNumber').val('');

    }
    function openModalDKSD(){
        $('#modalDKSD').on('click',(event)=>{
            event.preventDefault();
            $('#terms-modal').modal('show')
            $('#dongDKSD').on('click',(event)=>{
                event.preventDefault();
                $('#terms-modal').modal('hide')
            })
        })
    }

    $('#changeSign').on('click', (event) => {
        removeError();
        event.preventDefault();
        if ($('#changeSign').text() === 'Đăng ký') {
            removeForm();
            $('#otp').hide()
            $('#checkboxForm').show();
            $('#forgotPass').show();
            $('#userEmailCustomer').attr('placeholder', 'Nhập email');
            $('#passCustomer').show();
            $('#phoneNumber').show();
            $('#titleModal').text('Đăng ký')
            $('#changeSign').text('Đăng nhập')
            $('#textQuestion').text('Đã có tài khoản?')
            $('#forgotPass').remove();
            $('#btnLogin').text('Đăng ký');
            $('#checkboxForm').empty()
            $('#checkboxForm').append(`<input name="rememberme" type="checkbox" id="remember-me" value="forever"> Tôi đồng ý với <a href="#"  id="modalDKSD"
               style="text-decoration: underline ">
                Điều khoản sử dụng
            </a> `);

            openModalDKSD();

            $('#userNameCustomer').show()
            $('#confirm-password').show()
            $('#customerName').show();
        } else {
            removeForm();
            $('#otp').hide()
            $('#checkboxForm').show();
            $('#forgotPass').show();
            $('#userEmailCustomer').attr('placeholder', 'Nhập email');
            $('#passCustomer').show()
            $('#titleModal').text('Đăng nhập')
            $('#changeSign').text('Đăng ký')
            $('#textQuestion').text('Chưa có tài khoản?')
            $('.checkbox').append('<p class="lost-password" id="forgotPass">\n' +
                '                      <a style="text-decoration: underline" th:href="@{#}">Quên mật khẩu?</a>\n' +
                '                    </p>');
            showFogotPass()
            $('#btnLogin').text('Đăng nhập');
            $('#checkboxForm').empty()
            // $('#checkboxForm').append(`<!--<input name="rememberme" type="checkbox" id="remember-me" value="forever"> Ghi nhớ đăng nhập-->`);
            $('#userNameCustomer').hide()
            $('#confirm-password').hide()
            $('#customerName').hide();
            $('#phoneNumber').hide();
        }
    })

    $('#btnLogin').on('click', (e) => {
        e.preventDefault();
        $('#btnLogin').text() === 'Đăng nhập' ? login() : $('#btnLogin').text() === 'Đăng ký'? resgister(): $('#btnLogin').text() === 'Gửi mã'?  forgotPass() : confirmOtp();
    });

    function confirmOtp(){
        removeError();
        let formData = {
            otp: $('#otp').val().trim(),
            email: $('#userEmailCustomer').val().trim()
        };
        if (formData.email === '') {
            $('#userEmailCustomer').addClass('is-invalid');
            $('#userEmailCustomerError').remove();
            $('#userEmailCustomer').after('<div id="userEmailCustomerError" class="invalid-feedback">Chưa nhập email người dùng!</div>');
            return;
        }
        var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(formData.email)) {
            $('#userEmailCustomer').addClass('is-invalid');
            $('#userEmailCustomerError').remove();
            $('#userEmailCustomer').after('<div id="userEmailCustomerError" class="invalid-feedback">Email chưa đúng định dạng!</div>');
            return;
        }
        if (formData.otp === ''){
            $('#otp').addClass('is-invalid');
            $('#otpError').remove();
            $('#otp').after('<div id="otpError" class="invalid-feedback">Mời nhập mã otp!</div>');
        }
        $.ajax({
            url: 'http://localhost:8080/api/auth/check-otp',
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response) {
                if (response === true){
                    mess("Mật khẩu mới đã được gửi đến email của bạn!","/khach-hang/trang-chu")
                }else {
                        $('#otp').addClass('is-invalid');
                        $('#otpError').remove();
                        $('#otp').after('<div id="otpError" class="invalid-feedback">Mã không đúng!</div>');
                }
            },
            error: function (error) {
                messError(error.responseJSON.message)
            }
        });


    }

    function forgotPass(){
        removeError();
        let email =$('#userEmailCustomer').val().trim();
        if (email === '') {
            $('#userEmailCustomer').addClass('is-invalid');
            $('#userEmailCustomerError').remove();
            $('#userEmailCustomer').after('<div id="userEmailCustomerError" class="invalid-feedback">Chưa nhập email người dùng!</div>');
            return;
        }
        var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(email)) {
            $('#userEmailCustomer').addClass('is-invalid');
            $('#userEmailCustomerError').remove();
            $('#userEmailCustomer').after('<div id="userEmailCustomerError" class="invalid-feedback">Email chưa đúng định dạng!</div>');
            return;
        }

        $.ajax({
            url: 'http://localhost:8080/api/auth/otp?email='+email,
            type: 'GET',
            success: function (response) {
               if (response === true){
                   $('#btnLogin').text('Xác nhận');
                   $('#otp').show();
               }
            },
            error: function (error) {
               messError(error.responseJSON.message)
            }
        });
    }

    function login() {
        removeError()
        let formData = {
            username: $('#userEmailCustomer').val(),
            password: $('#passCustomer').val()
        };
        if (formData.username.trim() === '') {
            $('#userEmailCustomer').addClass('is-invalid');
            $('#userEmailCustomerError').remove();
            $('#userEmailCustomer').after('<div id="userEmailCustomerError" class="invalid-feedback">Chưa nhập email!</div>');
            return;
        }
        if (formData.password.trim() === '') {
            $('#passCustomer').addClass('is-invalid');
            $('#passCustomerError').remove();
            $('#passCustomer').after('<div id="passCustomerError" class="invalid-feedback">Chưa nhập mật khẩu!</div>');
            return;
        }
        $.ajax({
            url: '/api/auth/sign-in',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response) {
                if (response.response === null){
                    Swal.fire({
                        title: `${response.message}`,
                        icon: "error",
                        timer: 2000,
                        timerProgressBar: true,
                        showConfirmButton: false,
                        allowOutsideClick: false,
                        allowEscapeKey: false,
                    })
                    return;
                }
                let tokenJWT = response.response["token"];
                Cookies.set('authToken', tokenJWT, {path: '/', secure: true, sameSite: 'Strict'});
                $.ajaxSetup({
                    headers: {
                        'Authorization': 'Bearer ' + tokenJWT
                    }
                });
                let roles = response.response["roles"];
                if (roles.includes("ROLE_USER")) {
                    removeForm();
                    window.location.reload();
                }
            },
            error: function (response) {
                $('#userEmailCustomer').addClass('is-invalid');
                $('#passCustomer').addClass('is-invalid');
                $('#passCustomerError').remove();
                $('#passCustomer').after('<div id="passCustomerError" class="invalid-feedback">Tên tài khoản hoặc mật khẩu sai.</div>');
            }
        });
    }

    function resgister() {
        let confirmPassword = $('#confirm-password').val();
        var isChecked = $('#remember-me').prop('checked')
        let confirm = $('#confirm-password').val();
        let formData = {
            username: $('#userNameCustomer').val(),
            email: $('#userEmailCustomer').val(),
            password: $('#passCustomer').val(),
            name : $('#customerName').val(),
            phoneNumber: $('#phoneNumber').val(),
        };
        removeError()

        if (formData.name.trim() === '') {
            $('#customerName').addClass('is-invalid');
            $('#customerNameError').remove();
            $('#customerName').after('<div id="customerNameError" class="invalid-feedback">Vui lòng nhập họ và tên!</div>');
            return;
        }

        if (formData.username.trim() === '') {
            $('#userNameCustomer').addClass('is-invalid');
            $('#userNameCustomerError').remove();
            $('#userNameCustomer').after('<div id="userNameCustomerError" class="invalid-feedback">Vui lòng nhập tên người dùng!</div>');
            return;
        }
        if (formData.email.trim() === '') {
            $('#userEmailCustomer').addClass('is-invalid');
            $('#userEmailCustomerError').remove();
            $('#userEmailCustomer').after('<div id="userEmailCustomerError" class="invalid-feedback">Vui lòng nhập email người dùng!</div>');
            return;
        }
        if (formData.password.trim() === '') {
            $('#passCustomer').addClass('is-invalid');
            $('#passCustomerError').remove();
            $('#passCustomer').after('<div id="passCustomerError" class="invalid-feedback">Vui lòng nhập mật khẩu!</div>');
            return;
        }
        if (confirm.trim() === '') {
            $('#confirm-password').addClass('is-invalid');
            $('#confirm-passwordError').remove();
            $('#confirm-password').after('<div id="confirm-passwordError" class="invalid-feedback">Vui lòng xác nhận mật khẩu!</div>');
            return;
        }

        if (formData.phoneNumber === '') {
            $('#phoneNumber').addClass('is-invalid');
            $('#phoneNumberError').remove();
            $('#phoneNumber').after('<div id="phoneNumberError" class="invalid-feedback">Vui lòng nhập số điện thoại.</div>');
            return false;
        }
        if (!isChecked) {
            $('#modalDKSD').addClass('is-invalid');
            $('#modalDKSDError').remove();
            $('#modalDKSD').after('<div id="modalDKSDError" class="invalid-feedback">Vui lòng đồng ý điều khoản!</div>');
            return;
        }

        var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(formData.email)) {
            $('#userEmailCustomer').addClass('is-invalid');
            $('#userEmailCustomerError').remove();
            $('#userEmailCustomer').after('<div id="userEmailCustomerError" class="invalid-feedback">Email chưa đúng định dạng!</div>');
            return;
        }

        if (formData.password.length < 8) {
            $('#passCustomer').addClass('is-invalid');
            $('#passCustomerError').remove();
            $('#passCustomer').after('<div id="passCustomerError" class="invalid-feedback">Mật khẩu phải chứa độ dài hơn 8 ký tự!</div>');
            return;
        }
        const regexSdt = /(84|0[3|5|7|8|9])+([0-9]{8})\b/g;
        if (!regexSdt.test(formData.phoneNumber)) {
            $('#phoneNumber').addClass('is-invalid');
            $('#phoneNumberError').remove();
            $('#phoneNumber').after('<div id="phoneNumberError" class="invalid-feedback">Số điện thoại sai định dang! Vui lòng nhập lại.</div>');
            return false;
        }

        if (formData.password !== confirm) {
            $('#confirm-password').addClass('is-invalid');
            $('#confirm-passwordError').remove();
            $('#confirm-password').after('<div id="confirm-passwordError" class="invalid-feedback">Xác nhận mật khẩu không trùng khớp!</div>');
            return;
        }

        $.ajax({
            url: 'http://localhost:8080/api/auth/sign-up', // Địa chỉ API của bạn để xử lý đăng ký
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response) {

                if (response.status === "SUCCESS") {
                    mess("Đăng ký thành công!", "/khach-hang/trang-chu");
                    removeForm()
                }
            },
            error: function (response) {
                messError(response.responseJSON.message)
            }
        });
    }


    function mess(mess, location) {
        let timerInterval;
        Swal.fire({
            title: `${mess}`,
            icon: "success",
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
                window.location.href = location;
            }
        });
    }

    function messError(error) {
        let timerInterval;
        Swal.fire({
            title: `${error}`,
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
            }
        });
    }

});