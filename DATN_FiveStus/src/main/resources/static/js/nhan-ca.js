$(document).ready(function () {

    loadForm();

    function loadForm() {

        $.ajax({
            url: `http://localhost:8080/giao-ca/last-row`,
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
    }

    $('#maNVNC').on('keydown', function () {

        if (event.key === "Enter") {
            if ($(this).val().trim() !== '') {
                $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                $('#maNVNCError').remove(); // Loại bỏ thông báo lỗi
            }
            if ($('#maNVNC').val().trim()===''){
                $('#maNVNC').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                $('#maNVNCError').remove();
                $('#maNVNC').after('<div id="maNVNCError" class="invalid-feedback">Vui lòng nhập mã nhân viên.</div>');
                return;
            }
            let code = $("#maNVNC").val();

            $.ajax({
                url: `http://localhost:8080/nhan-vien/search-for-code/` + code,
                type: 'GET',
                dataType: 'json',
                success: function (response) {
                    if (response !== code ) {
                        $("#nameForCode").val(response["hoTen"])
                        confirm(code);
                        console.log(response)
                    } else if(response===code) {
                        console.log("vcc")
                    }
                },
                error: function (response) {
                     $("#nameForCode").val('')
                    $("#maNVNC").addClass('is-invalid');
                    $("#maNVNC").after('<div id="maNVNCError" class="invalid-feedback">Không tìm thấy nhân viên.</div>');
                }
            });

        }

    });

    $('#cancelGC').on('click', function () {
        window.location.href = "/loginPages";
    });


    function confirm(nv) {
        if (nv ===''){
            return;
        }
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
                        window.location.href = "/quan-ly-nhan-vien";
                    } else {

                    }
                },
                error: function (jqxhr, textStatus, error) {
                    console.log()
                }
            });
        })
    }


});