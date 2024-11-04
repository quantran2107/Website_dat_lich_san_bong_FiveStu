$(document).ready(function () {

    loadDataFilForm();

    function loadDataFilForm() {
        console.log("Load Data Fil Form");
        $.ajax({
            url: 'http://localhost:8080/giao-ca/ban-giao',
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                console.log(response)
                if (response !== null) {
                    eventInputElenment(response);
                }
            },
            error: function (response) {
            }
        });
    }

    function eventInputElenment(data) {
        $(`#tienDauCa`).val(data["tienMatDauCa"]);
        const $tongTienSB = $('#tongTienSanBong');
        const $tienMatSanBong = $(`#tienMatSanBong`);
        const $chuyenKhoanSB = $(`#chuyenKhoanSanBong`);
        const $datCoc = $('#datCoc');
        const $tienMatDatCoc = $(`#tienMatDatCoc`);
        const $chuyenKhoanDatCoc = $(`#chuyenKhoanDatCoc`);
        const $tongMatTienThu = $('#tongTienMatThu');
        const $tienDauCa = $('#tienDauCa');
        const $tienCuoiCa = $('#tienCuoiCa');
        const $tienThucTe = $('#tienThucTe');
        const $tienChenhLech = $('#tienChenhLech');
        const $tongtienBanGiao = $('#tongTienBanGiao');

        const $ghiChu = $('#ghiChuChenhLech');

        $tongTienSB.val(parseFloat(data["tienMatSB"]) + parseFloat(data["chuyenKhoanSB"]));
        $tienMatSanBong.val(data["tienMatSB"]);
        $chuyenKhoanSB.val(data["chuyenKhoanSB"]);
        $tongMatTienThu.val(parseFloat(data["tienMatSB"]) + parseFloat(data["tienMatDatCoc"]));
        $tienCuoiCa.val(parseFloat(data["tienMatSB"]) + parseFloat(data["tienMatDatCoc"]));
        $tongtienBanGiao.val(parseFloat(data["tienMatDauCa"]) + parseFloat(data["tienMatSB"]) + parseFloat(data["tienMatDatCoc"]));

        $tienThucTe.on('input', function () {
            $tienChenhLech.val(parseFloat($tienThucTe.val()) - parseFloat($tongtienBanGiao.val()));
        })
        $(`#confirmGC`).on('click', function () {
            confirmGC(data.id);
        })
    }



    function confirmGC(id) {
        const $chuyenKhoanSB = $(`#chuyenKhoanSanBong`);
        const $chuyenKhoanDatCoc = $(`#chuyenKhoanDatCoc`);
        const $tongMatTienThu = $('#tongTienMatThu');
        const $tienThucTe = $('#tienThucTe');
        const $tienChenhLech = $('#tienChenhLech');
        const $tongtienBanGiao = $('#tongTienBanGiao');
        const $ghiChu = $('#ghiChuChenhLech');
        let data = {
            id: id,
            tienMatTrongCa: $tongMatTienThu.val(),
            tienChuyenKhoan: parseFloat($chuyenKhoanSB.val()) + parseFloat($chuyenKhoanDatCoc.val()),
            tongTienTrongCa: parseFloat($tongtienBanGiao.val()),
            tongTienMatThucTe: parseFloat($tienThucTe.val()),
            tienPhatSinh: parseFloat($tienChenhLech.val()),
            ghiChu: parseFloat($ghiChu.val()),
        }
        $.ajax({
            url: 'giao-ca/change-gc',
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(data), // Chuyển đổi đối tượng thành JSON
            success: function (response) {
                if (response === true) {
                    let timerInterval;
                    Swal.fire({
                        title: "Lưu thành công!",
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
                        /* Read more about handling dismissals below */
                        if (result.dismiss === Swal.DismissReason.timer) {
                            window.location.href = "/client/logout"
                        }
                    });
                } else{
                    Swal.fire({
                        title: "Lưu thất bại! Có lỗi xảy ra",
                        icon: "error",
                        timer: 3000,
                        timerProgressBar: true,
                    })
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                Swal.fire({
                    title: "Lưu thất bại! Có lỗi xảy ra",
                    icon: "error",
                    timer: 3000,
                    timerProgressBar: true,
                })
            }
        });
    }
});