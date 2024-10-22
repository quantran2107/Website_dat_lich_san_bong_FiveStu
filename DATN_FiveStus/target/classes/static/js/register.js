$(document).ready(function() {
    $('#formRegister').on('submit', function(e) {
        e.preventDefault(); // Ngăn chặn hành động mặc định của form

        let confirmPassword = $('#confirm-password').val();
        let terms = $('input[name="terms"]').is(':checked');
        // Lấy dữ liệu từ form
        let formData = {
            username: $('#username').val(),
            email: $('#email').val(),
            password: $('#password').val(),
        };

        if (formData.username.trim() === '') {
            alert('Username is required!');
            return;
        }
        if (formData.username.length < 3) {
            alert('Username must have at least 3 characters!');
            return;
        }
        if (formData.username.length > 20) {
            alert('Username can have at most 20 characters!');
            return;
        }
        var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (formData.email.trim() === '') {
            alert('Email is required!');
            return;
        }
        if (!emailPattern.test(formData.email)) {
            alert('Email is not in valid format!');
            return;
        }
        if (formData.password.trim() === '') {
            alert('Password is required!');
            return;
        }
        if (formData.password.length < 8) {
            alert('Password must have at least 8 characters!');
            return;
        }
        if (formData.password.length > 20) {
            alert('Password can have at most 20 characters!');
            return;
        }
        // Kiểm tra xác nhận mật khẩu
        if (formData.password !== confirmPassword) {
            alert('Mật khẩu và xác nhận mật khẩu không khớp.');
            return;
        }

        // Kiểm tra điều khoản sử dụng
        if (!terms) {
            alert('Bạn phải đồng ý với điều khoản sử dụng.');
            return;
        }

        // Gửi dữ liệu lên server
        $.ajax({
            url: 'http://localhost:8080/api/auth/sign-up', // Địa chỉ API của bạn để xử lý đăng ký
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function(response) {
                if(response.status === "SUCCESS"){
                    alert('Đăng ký thành công!');
                    window.location.href = '/login'; // Điều hướng đến trang đăng nhập
                }

            },
            error: function(xhr, status, error) {
                // Xử lý lỗi
                alert('Đăng ký thất bại. Vui lòng thử lại.');
            }
        });
    });
});