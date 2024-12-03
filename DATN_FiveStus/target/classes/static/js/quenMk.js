$(document).ready(function () {
    $('#otpClass').hide();
    $('#changePass').hide();


    $('#toEmail').on('click',()=>{
        removeError();
        let email = $('#email').val();
        if (email === '') {
            $('#email').addClass('is-invalid');
            $('#emailError').remove();
            $('#email').after('<div id="emailError" class="invalid-feedback">Chưa nhập email người dùng!</div>');
            return;
        }
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(email)) {
            $('#email').addClass('is-invalid');
            $('#emailError').remove();
            $('#email').after('<div id="emailError" class="invalid-feedback">Email chưa đúng định dạng!</div>');
            return;
        }
        $.ajax({
            url: 'http://localhost:8080/api/auth/otp?email='+email,
            type: 'GET',
            success: function (response) {
                if (response === true){
                    $('#otpClass').show();
                    $('#changePass').show();
                    $('#toEmail').hide();

                }
            },
            error: function (error) {
                messError(error.responseJSON.message)
            }
        });
    })
    function removeError(){
        $('#emailError').remove();
        $('#otpError').remove();
    }

    $('#changePass').on('click',()=>{
        removeError();
        let email = $('#email').val();
        if (email === '') {
            $('#email').addClass('is-invalid');
            $('#emailError').remove();
            $('#email').after('<div id="emailError" class="invalid-feedback">Chưa nhập email người dùng!</div>');
            return;
        }
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(email)) {
            $('#email').addClass('is-invalid');
            $('#emailError').remove();
            $('#email').after('<div id="emailError" class="invalid-feedback">Email chưa đúng định dạng!</div>');
            return;
        }
        let formData = {
            otp: $('#otp').val().trim(),
            email: $('#email').val().trim()
        };
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
                    $('#otpClass').hide();
                    $('#changePass').hide();
                    $('#toEmail').show();
                    mess("Mật khẩu mới đã được gửi đến email của bạn!","/login")
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
    })
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
});