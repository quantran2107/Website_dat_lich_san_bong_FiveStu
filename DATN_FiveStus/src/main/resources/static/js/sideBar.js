$(document).ready(function () {

        loadSidebar();
        const url = window.location.href;
        console.log(url);

        function loadSidebar() {
            $.ajax({
                url: `http://localhost:8080/api/auth/get-role`,
                type: 'GET',
                dataType: 'json',
                success: function (response) {
                    if (!response.includes('ROLE_EMPLOYEE')) {
                        checkSideBar();
                    }

                    logout(response);

                },
                error:  () =>{
                    window.location.href = "/client/logout";
                }
            });
        }

        function logout(listRole) {
            if (url === 'http://localhost:8080/giao-ca'){
                return
            }
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
                            window.location.href = "/giao-ca";
                        }
                    });

                } else {
                    window.location.href = "/client/logout";
                }

            });
        }

        function checkSideBar() {
            const newUl = `
            <ul class="navbar-nav flex-fill w-100 mb-3 mt-3">
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
            <ul class="navbar-nav flex-fill w-100 mb-3">
                <li class="nav-item active">
                    <a class="nav-link pl-3" href="/quan-ly-lich-dat">
                        <i class="fe fe-calendar"></i>
                        <span class="ml-3 item-text">Quản lý lịch đặt</span>
                    </a>
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
            `;

            $(".navbar-nav").eq(4).after(newUl);

        }
    }
)
;