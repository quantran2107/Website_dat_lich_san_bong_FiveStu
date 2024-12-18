$(document).ready(function () {
    loadSidebar();
    $('#modaltoggleNC').modal({
        backdrop: 'static',
        keyboard: false
    });

    function loadSidebar() {
        $.ajax({
            url: `http://localhost:8080/api/auth/get-role`,
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                if (!response.includes('ROLE_EMPLOYEE')) {
                    checkSideBar();
                    logout(response);
                    return;
                }
                checkSideBar2()
                checkStatus(response);
            },
            error: () => {
                window.location.href = "/admin/logout";
            }
        });
    }

    function checkStatus(listRole) {
        $.ajax({
            url: 'http://localhost:8080/giao-ca/check-gc',
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                switch (response.status) {
                    case 'OTHER_STAFF_ON_SHIFT':
                        showWarningMessage(response.response["nhanVien"]);
                        break;
                    case 'START_WORKING':
                        nhanCa(response.response);
                        logout(listRole);
                        break;
                    case 'FAIL':
                        window.location.href = '/admin/logout';
                        break;
                    default:
                        logout(listRole);
                        break;
                }
            },
            error: function () {
            }
        });
    }

    function nhanCa(giaoCa) {
        console.log(giaoCa)
        if (giaoCa !== null) {
            $('#checkBox').prop('disabled', true)
        }

        $('#modaltoggleNC').modal('show');
        $('#btnNhanCa').click(function () {

            const tienMat = $('#tienMatDauCa').val();
            const tienChuyenKhoan = $('#tienChuyenKhoanDauCa').val();

            if (!tienMat || !tienChuyenKhoan || parseFloat(tienMat) < 0 || parseFloat(tienChuyenKhoan) < 0) {
                Swal.fire({
                    title: "Cảnh báo!",
                    text: `Số tiền bạn nhập vào không được rỗng hoặc âm!`,
                    icon: "warning",
                    showConfirmButton: false,
                    showCancelButton: true,
                    allowOutsideClick: false,
                    allowEscapeKey: false,
                    cancelButtonText: 'Thoát!'
                });
                return;
            }

            let formNC = {
                tienMatDauCa: null,
                tienChuyenKhoanDauCa: null,
                tienMatCaTruoc: null
            }
            if ($('#checkBox').is(':checked')) {
                formNC.tienMatDauCa = $('#tienMatDauCa').val();
                formNC.tienChuyenKhoanDauCa = $('#tienChuyenKhoanDauCa').val();
                formNC.tienMatCaTruoc = 0;
            } else if (giaoCa === null) {
                if (!$('#checkBox').is(':checked')){
                    Swal.fire({
                        title: "Cảnh báo!",
                        text: `Số tiền bạn nhập vào không khớp với dữ liệu ca trước!`,
                        icon: "warning",
                        showConfirmButton: false,
                        showCancelButton: true,
                        allowOutsideClick: false,
                        allowEscapeKey: false,
                        cancelButtonText: 'Thoát!'
                    });
                    return;
                }
                formNC.tienMatDauCa = $('#tienMatDauCa').val();
                formNC.tienChuyenKhoanDauCa = $('#tienChuyenKhoanDauCa').val();
                formNC.tienMatCaTruoc = 0;
            } else if (giaoCa["tienMatTrongCa"].toString() !== ($('#tienMatDauCa').val()) || giaoCa["tienChuyenKhoanTrongCa"].toString() !== ($('#tienChuyenKhoanDauCa').val())) {
                Swal.fire({
                    title: "Cảnh báo!",
                    text: `Số tiền bạn nhập vào không khớp với dữ liệu ca trước!`,
                    icon: "warning",
                    showConfirmButton: false,
                    showCancelButton: true,
                    allowOutsideClick: false,
                    allowEscapeKey: false,
                    cancelButtonText: 'Thoát!'
                });
                return;
            } else {
                formNC.tienMatDauCa = $('#tienMatDauCa').val();
                formNC.tienChuyenKhoanDauCa = $('#tienChuyenKhoanDauCa').val();
                formNC.tienMatCaTruoc = giaoCa["tienMatCaTruoc"]
            }

            $.ajax({
                url: 'http://localhost:8080/giao-ca/add-row',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(formNC),
                success: function (response) {
                    if (response === true) {
                        $('#checkBox').prop('checked', false);
                        $('#tienQuyDauCa').val('');
                        $('#tienQuyDauCa').prop('disabled', false);
                        let timerInterval;
                        Swal.fire({
                            title: "Nhận ca thành công!",
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
                                $('#modaltoggleNC').modal('hide');
                            }
                        });

                    } else {
                        Swal.fire({
                            title: "Lỗi!",
                            text: 'Hệ thống xảy ra lỗi!',
                            icon: "error",
                            showConfirmButton: false,
                            showCancelButton: true,
                            cancelButtonText: 'Thoát!'
                        });
                    }
                },
                error: function () {
                    Swal.fire({
                        title: "Lỗi!",
                        text: 'Đã xảy ra sự cố!',
                        icon: "error",
                        showConfirmButton: false,
                        showCancelButton: true,
                        cancelButtonText: 'Thoát!'
                    });
                }
            });

        })
    }


    function logout(listRole) {
        $("#logoutGC").click(() => {
            if (listRole.includes('ROLE_EMPLOYEE')) {
                Swal.fire({
                    title: 'Xác nhận',
                    text: "Bạn có kết thúc ca làm không?",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Có',
                    cancelButtonText: 'Không'
                }).then((result) => {
                    if (result.isConfirmed) {
                        giaoCa();
                    }
                });
            } else {
                window.location.href = "/admin/logout";
            }

        });
    }


    function giaoCa() {
        $.ajax({
            url: 'http://localhost:8080/giao-ca/ban-giao',
            type: 'PUT',
            success: function (response) {
                console.log(response)
                if (response.response === true) {
                    let timerInterval;
                    Swal.fire({
                        title: `Kết thúc ca làm thành công`,
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
                            window.location.href = "/admin/logout";
                        }
                    });
                } else {
                    Swal.fire({
                        title: "Lưu thất bại! Có lỗi xảy ra",
                        icon: "error",
                        timer: 2500,
                        timerProgressBar: true,
                    })
                }
            },
            error: function () {
                Swal.fire({
                    title: "Lưu thất bại! Có lỗi xảy ra",
                    icon: "error",
                    timer: 2500,
                    timerProgressBar: true,
                })
            }
        });
    }

    function checkSideBar() {
        const newUl = `
            <ul class="navbar-nav flex-fill w-100 mb-3 mt-3">
                <li class="nav-item dropdown">
                    <a class="nav-link pl-3" href="/thong-ke">
                        <i class="fe fe-dollar-sign"></i>
                        <span class="ml-3 item-text">Thống kê </span>
                    </a>
                </li>
            </ul>
            <ul class="navbar-nav flex-fill w-100 mb-3">
                <li class="nav-item dropdown">
                    <a class="nav-link pl-3" href="/dich-vu">
                        <i class="fe fe-shopping-cart"></i>
                        <span class="ml-3 item-text">Quản lý dịch vụ</span>
                    </a>
                </li>
            </ul>

            <ul class="navbar-nav flex-fill w-100 mb-3">
                <li class="nav-item dropdown">
                    <a class="nav-link pl-3" href="/dat-lich-tai-quay">
                        <i class="fe fe-clipboard"></i>
                        <span class="ml-3 item-text">Đặt lịch</span>
                    </a>
                </li>
            </ul>
            <ul class="navbar-nav flex-fill w-100 mb-3">
                <li class="nav-item active">
                    <a class="nav-link pl-3" href="/list-san-ca">
                        <i class="fe fe-codepen"></i>
                        <span class="ml-3 item-text">Quản lý sân ca</span>
                    </a>
                </li>
            </ul>
           
            <ul class="navbar-nav flex-fill w-100 mb-3">
                <li class="nav-item active">
                    <a class="nav-link pl-3" href="/phieu-giam-gia">
                        <i class="fe fe-tag"></i>
                        <span class="ml-3 item-text">Quản lý phiếu giảm giá </span>
                    </a>
                </li>
            </ul>
            <ul class="navbar-nav flex-fill w-100 mb-3">
                <li class="nav-item dropdown">
                    <a class="nav-link pl-3" href="/quan-ly-khach-hang">
                        <i class="fe fe-users"></i>
                        <span class="ml-3 item-text">Quản lý khách hàng </span>
                    </a>
                </li>
            </ul>
            <ul class="navbar-nav flex-fill w-100 mb-3">
                <li class="nav-item dropdown">
                    <a class="nav-link pl-3" href="/web-hoa-don">
                        <i class="fe fe-file-minus"></i>
                        <span class="ml-3 item-text">Quản lý hóa đơn </span>
                    </a>
                </li>
            </ul>
<!--            <ul class="navbar-nav flex-fill w-100 mb-3">-->
<!--                <li class="nav-item active">-->
<!--                    <a class="nav-link pl-3" href="/quan-ly-lich-dat">-->
<!--                        <i class="fe fe-calendar"></i>-->
<!--                        <span class="ml-3 item-text">Quản lý lịch đặt</span>-->
<!--                    </a>-->
<!--                </li>-->
<!--            </ul>-->
            <ul  class="navbar-nav flex-fill w-100 mb-3">
                <li class="nav-item dropdown">
                    <a aria-expanded="false" class="dropdown-toggle nav-link pl-3" data-toggle="collapse" href="#pages">
                        <i class="fe fe-user"></i>
                        <span class="ml-3 item-text">Quản lý nhân viên</span>
                    </a>
                    <ul class="collapse list-unstyled pl-4 w-100" id="pages">
                        <li class="nav-item">
                            <a class="nav-link pl-4" href="/quan-ly-nhan-vien">
                                <span class="ml-1 item-text">Quản lý nhân viên</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link pl-4" href="/lich-lam-viec">
                                <span class="ml-1 item-text">Quản lý lịch làm việc</span>
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
            <ul class="navbar-nav flex-fill w-100 mb-3">
                <li class="nav-item dropdown">
                    <a class="nav-link pl-3" href="/listSanBong">
                        <i class="fe fe-columns"></i>
                        <span class="ml-3 item-text">Quản lý sân và giá </span>
                    </a>
                </li>
            </ul>
            <ul class="navbar-nav flex-fill w-100 mb-3">
                <li class="nav-item dropdown">
                    <a class="nav-link pl-3" href="/listThamSo">
                        <i class="fe fe-tool"></i>
                        <span class="ml-3 item-text">Quản lý tham số</span>
                    </a>
                </li>
            </ul>
             <ul class="navbar-nav flex-fill w-100 mb-3">
                <li class="nav-item active">
                    <a class="nav-link pl-3" href="#" id="logoutGC" >
                        <i class="fas fa-sign-out-alt"></i>
                        <span class="ml-3 item-text">Đăng xuất</span>
                    </a>
                </li>
            </ul>
            `;
        $(".w-100.d-flex").after(newUl);
    }

    function checkSideBar2() {
        const newUl = `
             <ul class="navbar-nav flex-fill w-100 mb-3">
                <li class="nav-item dropdown">
                    <a class="nav-link pl-3" href="/dich-vu">
                        <i class="fe fe-shopping-cart"></i>
                        <span class="ml-3 item-text">Quản lý dịch vụ</span>
                    </a>
                </li>
            </ul>

            <ul class="navbar-nav flex-fill w-100 mb-3">
                <li class="nav-item dropdown">
                    <a class="nav-link pl-3" href="/dat-lich-tai-quay">
                        <i class="fe fe-clipboard"></i>
                        <span class="ml-3 item-text">Đặt lịch</span>
                    </a>
                </li>
            </ul>
            <ul class="navbar-nav flex-fill w-100 mb-3">
                <li class="nav-item active">
                    <a class="nav-link pl-3" href="/list-san-ca">
                        <i class="fe fe-codepen"></i>
                        <span class="ml-3 item-text">Quản lý sân ca</span>
                    </a>
                </li>
            </ul>
            <ul class="navbar-nav flex-fill w-100 mb-3">
                <li class="nav-item active">
                    <a class="nav-link pl-3" href="#" id="logoutGC" >
                        <i class="fas fa-sign-out-alt"></i>
                        <span class="ml-3 item-text">Đăng xuất</span>
                    </a>
                </li>
            </ul>
        `;
        $(".w-100.d-flex").after(newUl);
    }

    function showWarningMessage(nv) {
        let hoTen = nv["hoTen"];
        Swal.fire({
            title: "Cảnh báo!",
            text: `Tài khoản nhân viên ${hoTen} chưa đăng xuất!`,
            icon: "warning",
            showConfirmButton: true,
            allowOutsideClick: false,
            allowEscapeKey: false,
            confirmButtonText: 'Đăng xuất',
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = "/admin/logout"
            }
        });
    }

    $(window).on("load", function () {
        $("#preloader").fadeOut("slow", function () {
            $("body").css("visibility", "visible");
        });
    });
});