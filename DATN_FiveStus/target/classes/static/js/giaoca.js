$(document).ready(function () {


    $("#logoutGC").click(function (event) {
        event.preventDefault();
        $.ajax({
            url: `http://localhost:8080/giao-ca/nvgc`, // URL
            type: 'GET', // Phương thức HT
            dataType: 'json', // Định dạng dữ liệu mong muốn
            success: function (response) {
                $("#maNVGC").text(response["maNhanVien"])
                loadDataModalGC(response.id);
            },
            error: function () {
                Swal.fire({
                    title: 'Lỗi!',
                    text: 'Đã xảy ra lỗi!',
                    icon: 'error',
                    confirmButtonText: 'OK',
                    customClass: {
                        container: 'swal-gc'
                    }
                });
            }
        });
        conFirm();
    });

    function conFirm() {
        $("#btnConfirm").click(function () {
            let tienMatCaTruoc = $("#tienMatCaTruoc").val();
            let tongTienMatThucTe = ($("#tongTienMatThucTe").val());
            let tongTienPhatSinh = ($("#tongTienPhatSinh").val());
            let ghiChuPhatSinh = $("#ghiChuPhatSinh").val();
            let tienMatTrongCa = ($("#tienMatTrongCa").val());
            let tienChuyenKhoanTrongCa = ($("#tienChuyenKhoanTrongCa").val());
            let tongTienTrongCa = $("#tongTien").text();

            let cacu = parseInt(tongTienTrongCa)+parseInt(tienMatCaTruoc)-parseInt(tongTienPhatSinh) -parseInt(tongTienMatThucTe)
            let caculator = parseInt(tienMatTrongCa) + parseInt(tienChuyenKhoanTrongCa) - parseInt(tongTienTrongCa);
            if (caculator !== 0) {
                Swal.fire({
                    title: 'Cảnh báo!',
                    text: 'Tiền chuyển khoản trong ca + tiền mặt trong ca = Tổng tiền!',
                    icon: 'warning',
                    showConfirmButton: true,
                    customClass: {
                        container: 'swal-gc'
                    }
                });
                return;
            }
            if (cacu !== 0) {
                Swal.fire({
                    title: 'Cảnh báo!',
                    text: 'Tổng tiền hóa đơn + tiền mặt ca trước = Tiền mặt thực tế + tiền phát sinh!',
                    icon: 'warning',
                    showConfirmButton: true,
                    customClass: {
                        container: 'swal-gc'
                    }
                });
                return;
            }
            let formConfirm = {
                tienMatTrongCa: tienMatTrongCa,
                tienChuyenKhoanTrongCa: tienChuyenKhoanTrongCa,
                tongTienTrongCa: tongTienTrongCa,
                tongTienMatThucTe: tongTienMatThucTe,
                tongTienPhatSinh: tongTienPhatSinh,
                ghiChu: ghiChuPhatSinh,
                trangThai: false,
            }
            $.ajax({
                url: 'http://localhost:8080/giao-ca/change-gc',
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(formConfirm),
                success: function () {
                    Swal.fire({
                        title: 'Thành công!',
                        text: 'Đã cập nhật giao ca thành công!',
                        icon: 'success',
                        showConfirmButton: false,
                        timer: 4000,
                        customClass: {
                            container: 'swal-gc'
                        }
                    });
                    window.location.href = "/client/logout"
                },
                error: function (xhr, status, error) {
                    console.error('Lỗi:', error);
                }
            });

        })
    }

    function loadDataModalGC(id) {
        if (id === 0) {
            Swal.fire({
                title: 'Lỗi!',
                text: 'Đã xảy ra lỗi!',
                icon: 'error',
                confirmButtonText: 'OK',
                customClass: {
                    container: 'swal-gc' // Thêm class tùy chỉnh
                }

            });
            return;
        }

        $('#tableGiaoCa').empty();


        $.ajax({
            url: `http://localhost:8080/hoa-don/search-for-nv/${id}`,
            type: 'GET',
            dataType: 'json',
            success: function (response) {

                let tongTien = 0;
                let hd = response[0];

                response.forEach((hoaDon, index) => {
                    let newRow = `
                    <tr>
                        <td>${index + 1}</td>
                        <td>${hoaDon["maHoaDon"]}</td>
                        <td>${hoaDon["maNhanVien"]}</td>
                        <td>${hoaDon["hoVaTenKhachHang"]}</td>
                        <td>${hoaDon["tienCoc"]} VND</td>
                        <td>${hoaDon["tongTien"]} VND</td>
                        <td>${hoaDon["trangThai"]}</td>
                    </tr>`;
                    $('#tableGiaoCa').append(newRow);
                    tongTien += hoaDon["tongTien"];
                });
                $("#tongTien").text(tongTien)
            },
            error: function () {
                let newRow = `
                        <tr>
                            <td colspan="7" style="text-align: center; font-weight: bold;">
                                Không có dữ liệu
                            </td>
                        </tr>`;
                $('#tableGiaoCa').append(newRow);
            }
        });
        $.ajax({
            url: `http://localhost:8080/giao-ca/for-nv/${id}`,
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                if (response !== null) {
                    $("#tienMatCaTruoc").val(response["tienMatCaTruoc"]);
                }
            },
            error: function (jqxhr, textStatus, error) {
                let err = textStatus + ", " + error;
                console.error("Request Failed: " + err);
            }
        });
    }
});