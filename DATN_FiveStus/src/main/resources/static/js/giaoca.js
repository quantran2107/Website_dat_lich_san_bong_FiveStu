$(document).ready(function () {

    $("#logoutGC").click(function (event) {
        event.preventDefault();
        loadDataModalGC();
    });

    function loadDataModalGC() {
        let id = 2
        let idGC;

        $.ajax({
            url: `http://localhost:8080/nhan-vien/${id}`, // URL
            type: 'GET', // Phương thức HTTP
            dataType: 'json', // Định dạng dữ liệu mong muốn
            success: function (response) {
                if (response.status === 400) {
                    console.log(response);
                    return;
                }
                $("#maNVGC").text(response["maNhanVien"]);
                $("#tenNVGC").text(response["hoTen"]);
            },
            error: function (jqxhr, textStatus, error) {
                let err = textStatus + ", " + error;
                console.error("Request Failed: " + err);
            }
        });


        // Hàm đầu tiên: Lấy dữ liệu hóa đơn
        $.ajax({
            url: `http://localhost:8080/hoa-don/search-for-nv/${id}`,
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                if (response.status === 400) {
                    let newRow = `
                        <tr>
                            <td colspan="7" style="text-align: center; font-weight: bold;">
                                Không có dữ liệu
                            </td>
                        </tr>`;
                    $('#tableGiaoCa').append(newRow);
                }
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
                });
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
                idGC = response.id;
                $("#tienMatCaTruoc").val(response["tienMatCaTruoc"] || "0");
                $("#tienMatTrongCa").val(response["tienMatTrongCa"] || "0");
                $("#tienChuyenKhoanTrongCa").val(response["tienChuyenKhoanTrongCa"] || "0");
                $("#tongTienMatThucTe").val(response["tongTienTrongCa"] || "0");
                $("#tongTienPhatSinh").val(response["tongTienMatThucTe"] || "0");
                $("#ghiChuPhatSinh").val(response["tongTienPhatSinh"] || "0");

            },
            error: function (jqxhr, textStatus, error) {
                let err = textStatus + ", " + error;
                console.error("Request Failed: " + err);
            }
        });


        $("#btnConfirm").click(function (event) {
            Swal.fire({
                title: 'Xác nhận giao ca',
                text: "Giao ca?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Có',
                cancelButtonText: 'Không',
                customClass: {
                    container: 'swal-gc' // Thêm class tùy chỉnh
                }
            }).then((result) => {
                if (result.isConfirmed) {
                    let formConfirm ={
                        idNhanVien:'',
                        tienMatTrongCa :'',
                        tienChuyenKhoanTrongCa:'',
                        tongTienTrongCa:'',
                        tongTienMatThucTe:'',
                        tongTienPhatSinh:'',
                        ghiChu:'',
                        trangThai:'off'
                    }
                    $.ajax({
                        url: 'http://localhost:8080/giao-ca/change-gc/'+idGC,
                        type: 'PUT',
                        contentType: 'application/json',
                        data: JSON.stringify(formConfirm),
                        success: function(response) {
                            if (response){
                                window.location.pathname='/loginPages'
                            } else {
                                Swal.fire({
                                    title: 'Lỗi!',
                                    text: 'Đã xảy ra lỗi !',
                                    icon: 'error',
                                    confirmButtonText: 'OK',
                                    customClass: {
                                        container: 'swal-gc' // Thêm class tùy chỉnh
                                    }
                                });
                            }
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                            Swal.fire({
                                title: 'Lỗi!',
                                text: 'Đã xảy ra lỗi trong quá trình thanh toán.',
                                icon: 'error',
                                confirmButtonText: 'OK',
                                customClass: {
                                    container: 'swal-gc' // Thêm class tùy chỉnh
                                }
                            });
                            console.error('Error:', textStatus, errorThrown);
                        }
                    });
                }
            })
        });
    }
});