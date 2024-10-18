$(document).ready(function () {


    $("#logoutGC").click(function (event) {
        event.preventDefault();
        $.ajax({
            url: `http://localhost:8080/giao-ca/nvgc`, // URL
            type: 'GET', // Phương thức HT
            dataType: 'json', // Định dạng dữ liệu mong muốn
            success: function (response) {
                loadDataModalGC(response);
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
            let tongTienMatThucTe = ($("#tongTienMatThucTe").val());
            let tongTienPhatSinh =  ($("#tongTienPhatSinh").text());
            let ghiChuPhatSinh = ($("#ghiChuPhatSinh").text());
            let tienMatTrongCa = ($("#tienMatTrongCa").val());
            let tienChuyenKhoanTrongCa = ($("#tienChuyenKhoanTrongCa").val());
            let tongTienTrongCa = ($("#tongTien").text().trim().slice(0, -2));


            // if ( parseFloat(tongTienMatThucTe)<0 ) {
            //     $('#tongTienMatThucTe').addClass('is-invalid');
            //     $('#tongTienMatThucTeError').remove();
            //     $('#tongTienMatThucTe').after('<div id="tongTienMatThucTeError" class="invalid-feedback">Mời điền thông tin.</div>');
            //     return;
            // }
            // if (parseFloat(tongTienPhatSinh)<0) {
            //     $('#tongTienPhatSinh').addClass('is-invalid');
            //     $('#tongTienPhatSinhError').remove();
            //     $('#tongTienPhatSinh').after('<div id="tongTienPhatSinhError" class="invalid-feedback">Mời điền thông tin.</div>');
            //     return;
            // }
            // // if (ghiChuPhatSinh.trim()==='') {
            // //     $('#ghiChuPhatSinh').addClass('is-invalid');
            // //     $('#ghiChuPhatSinhError').remove();
            // //     $('#ghiChuPhatSinh').after('<div id="ghiChuPhatSinhError" class="invalid-feedback">Mời điền thông tin.</div>');
            // //     return;
            // // }
            // if ( parseFloat(tienMatTrongCa)) {
            //     $('#tienMatTrongCa').addClass('is-invalid');
            //     $('#tienMatTrongCaError').remove();
            //     $('#tienMatTrongCa').after('<div id="tienMatTrongCaError" class="invalid-feedback">Mời điền thông tin.</div>');
            //     return;
            // }
            // if (parseFloat(tienChuyenKhoanTrongCa)) {
            //     $('#tienChuyenKhoanTrongCa').addClass('is-invalid');
            //     $('#tienChuyenKhoanTrongCaError').remove();
            //     $('#tienChuyenKhoanTrongCa').after('<div id="tienChuyenKhoanTrongCaError" class="invalid-feedback">Mời điền thông tin.</div>');
            //     return;
            // }
            let caculator = parseInt(tienMatTrongCa)+parseInt(tienChuyenKhoanTrongCa) -parseInt(tongTienTrongCa);
            if (caculator!==0){
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
                        timer: 2000,
                        customClass: {
                            container: 'swal-gc'
                        }
                    });
                    window.location.href = "/login"
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



        $.ajax({
            url: `http://localhost:8080/hoa-don/search-for-nv/${id}`,
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                if (response === null) {
                    let newRow = `
                        <tr>
                            <td colspan="7" style="text-align: center; font-weight: bold;">
                                Không có dữ liệu
                            </td>
                        </tr>`;
                    $('#tableGiaoCa').append(newRow);
                    return;
                }
                let tongTien = 0;
                let hd =  response[0];
                $("#maNVGC").text(hd["maNhanVien"])
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
                $("#tongTien").text(tongTien + " ₫")
            },
            error: function (jqxhr, textStatus, error) {
                let err = textStatus + ", " + error;
                console.error("Request Failed: " + err);
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