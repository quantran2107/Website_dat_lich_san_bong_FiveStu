$(document).ready(function () {

    $.ajax({
        url: 'http://localhost:8080/customer/get-customer',
        type: 'GET',
        dataType: 'json',
        success: function (response) {
            if (response.response != null) {
                $(`#customer`).html("Xin chào " + response.response.hoVaTen + " !");
                $(`#customer`).click(function () {
                    window.location.href = "/customer-details";
                })
                $(`#customerLog`).html("Đăng xuất");
                $(`#customerLog`).click(function () {
                    window.location.href = "/client/logout";
                })
            } else {
                // $(`#customer`).html("Chưa có tài khoản");
                $(`#customerLog`).html("Đăng nhập");
                $(`#customerLog`).click(function () {
                    window.location.href = "/login";
                })
            }

        },
        error: function (response) {
            $(`#customer`).html("Chưa có tài khoản !");
        }
    });


});