$(document).ready(function () {

    loadForm();
    function loadForm() {

        $.ajax({
            url: `http://localhost:8080/giao-ca/check-nhan-ca`,
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                if (response !== null) {
                    let NVnhan = response["nhanVienNhan"];
                    $("#NVcaTruoc").text(NVnhan["hoTen"]);
                    $("#tienMatCaTruoc").text(response["tongTienMatThucTe"]);
                    $("#notes").text("ghiChu")
                }
            },
            error: function (jqxhr, textStatus, error) {
                let err = textStatus + ", " + error;
                console.error("Request Failed: " + err);
            }
        });

        $.ajax({
            url: `http://localhost:8080/giao-ca/getNV`,
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                if (response !== null) {
                    $('#maNVNC').val(response["maNhanVien"])
                    $('#nameForCode').val(response["hoTen"])
                    confirm(response["maNhanVien"])
                }
            },
            error: function (jqxhr, textStatus, error) {
                let err = textStatus + ", " + error;
                console.error("Request Failed: " + err);
            }
        });

    }



    $('#cancelGC').on('click', function () {
        window.location.href = "/login";
    });

    function confirm(nv){
        $('#confirmGC').on('click', function () {

            let giaoCa = {
                codeNhanVien: nv,
                tienMat: $("#tienMatCaTruoc").text()
            }
            $.ajax({
                url: `http://localhost:8080/giao-ca/add-row`,
                type: 'POST',
                contentType: 'application/json', // Định dạng dữ liệu gửi đi
                data: JSON.stringify(giaoCa), // Chuyển đổi dữ liệu thành JSON
                success: function (response) {
                    if (response === true) {
                        Swal.fire({
                            title: 'Thành công!',
                            text: 'Đã cập nhật giao ca thành công!',
                            icon: 'success',
                            showConfirmButton: false, // Ẩn nút xác nhận
                            timer: 3000 // Thời gian tự động đóng thông báo (2000ms = 2 giây)
                        });
                        window.location.href = "/quan-ly-nhan-vien";
                    }
                },
                error: function () {
                    Swal.fire({
                        icon: 'error',
                        title: 'Thất bại!',
                        text: 'Có lỗi xảy ra !',
                    });
                }
            });

        })

    }


});