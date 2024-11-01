$(document).ready(function () {

    $('#btnLogin').on('click', () =>{

        let formData = {
            username: $('#userEmailCustomer').val(),
            password: $('#passCustomer').val()
        };
        $.ajax({
            url: '/api/auth/sign-in',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response) {

                let tokenJWT = response.response["token"];
                Cookies.set('authToken', tokenJWT, {path: '/', secure: true, sameSite: 'Strict'});
                $.ajaxSetup({
                    headers: {
                        'Authorization': 'Bearer ' + tokenJWT
                    }
                });
                let roles = response.response["roles"];
                if (roles.includes("ROLE_USER")) {
                    window.location.reload();
                }
            },
            error: function () {
                $('#userEmailCustomer').addClass('is-invalid');
                $('#passCustomer').addClass('is-invalid');
                $('#passCustomerError').remove();
                $('#passCustomer').after('<div id="passCustomerError" class="invalid-feedback">Tên tài khoản hoặc mật khẩu sai.</div>');
            }
        });
    });

});