$(document).ready(function () {

    function showSuccessToast(message) {
        Toastify({
            text: message,
            duration: 5000,
            gravity: "top",
            position: "right",
            style: {
                background: "#4CAF50",
            },
            stopOnFocus: true
        }).showToast();
    }

    function showErrorToast(message) {
        Toastify({
            text: message,
            duration: 5000,
            gravity: "top",
            position: "right",
            style: {
                background: "#f44336",
            },
            stopOnFocus: true
        }).showToast();
    }

    // URL của API
    const apiUrlGet = 'http://localhost:8080/nhan-vien/hien-thi';
    const apiUrlUpdate = 'http://localhost:8080/nhan-vien/update';
    const apiUrlAdd = 'http://localhost:8080/nhan-vien/add';

    const itemsPerPage = 10; // Số lượng bản ghi mỗi trang
    let currentPage = 1; // Trang hiện tại, mặc định là 1
    let totalPages; // Tổng số trang

    // Hàm để gọi API và hiển thị bảng
    function loadTable(page) {
        $.getJSON(apiUrlGet, function (data) {
            const startIndex = (page - 1) * itemsPerPage;
            const endIndex = startIndex + itemsPerPage;
            const employees = data.slice(startIndex, endIndex);

            let table = `

            <table class="table table-bordered" style="text-align: center">
            <thead>
              <tr>
                <th>STT</th>
                <th>Tên nhân viên</th>
                <th>Email</th>
                <th>Giới tính</th>
                <th>Số điện thoại</th>
                <th>Trạng thái</th>
                <th>Hành động</th>
              </tr>
            </thead>
            <tbody id="tableBody">`;
            console.log(employees)
            employees.forEach((employee, index) => {
                table += `<tr>
              <td>${index + 1}</td>
              <td>${employee.hoTen}</td>
              <td>${employee.email}</td>
              <td>${employee.gioiTinh ? "Nam" : "Nữ"}</td>
              <td>${employee.soDienThoai}</td>
              <td style="${employee.trangThai == "active" ? "color:green;font-weight:bold" : "color:red;font-style:italic"}">
                        ${employee.trangThai == "active" ? "Hoạt động" : "Ngừng hoạt động"}
                        </td>
              <td><button class="btn btn-warning action-button" data-employee='${JSON.stringify(employee)}'><i class="fas fa-edit edit-icon"></i></button></td>
            </tr>`;
            });

            table += `</tbody></table>
                   `;
            $('#tableContainer').html(table);

            $('.action-button').on('click', function () {
                const employee = $(this).data('employee');
                renderForm(employee);
            });
            $('.add-button').on('click', function () {
                // const employee = $(this).data('employee');
                renderAddForm();
            });

        });
    }

    function createPagination(totalPages) {
        let paginationHtml = '';

        // Tạo nút Back và Next
        paginationHtml += `
                <button class="btn btn-light" id="prevButton">Prev</button>
                <select class="form-control mx-2" id="pageSelect">
            `;

        // Thêm các option cho dropdown
        for (let i = 1; i <= totalPages; i++) {
            paginationHtml += `
                    <option value="${i}">${i}</option>
                `;
        }

        paginationHtml += `
                </select>
                <button class="btn btn-light" id="nextButton">Next</button>
            `;

        $('#pagination').html(paginationHtml);

        // Thêm sự kiện click cho nút Prev
        $('#prevButton').on('click', function () {
            if (currentPage > 1) {
                currentPage--;
                $('#pageSelect').val(currentPage);
                loadTable(currentPage);
            }
        });

        // Thêm sự kiện click cho nút Next
        $('#nextButton').on('click', function () {
            if (currentPage < totalPages) {
                currentPage++;
                $('#pageSelect').val(currentPage);
                loadTable(currentPage);
            }
        });

        // Thêm sự kiện change cho dropdown select
        $('#pageSelect').on('change', function () {
            const selectedPage = $(this).val();
            currentPage = parseInt(selectedPage); // Cập nhật trang hiện tại
            loadTable(currentPage); // Load lại dữ liệu cho trang mới
        });
    }

    // Gọi hàm loadTable khi trang được tải
    function init() {
        $.getJSON(apiUrlGet, function (data) {
            const totalItems = data.length;
            totalPages = Math.ceil(totalItems / itemsPerPage);
            createPagination(totalPages); // Tạo dropdown select phân trang và nút điều hướng
            loadTable(currentPage); // Load dữ liệu cho trang đầu tiên
            $('#linkAdd').hide();
            $('#linkUpdate').hide();
        });
    }

    init();
    $('#qlnv').on('click', function() {
        $('#formContainer').hide();

        init();
        $('#tableContainer').show();
        $('#searchForm').show();
        $('#actionForm').show();
        $('#pagination').show();

    });

    // Hàm render form
    function renderForm(employee) {
        $('#linkUpdate').show();
        let form = `<form id="employeeForm">
                <div class="row">
                    <div class="col-4">
                        <h2>Thông tin nhân viên</h2>
                        <div class="form-group">
                            <label for="maNhanVien">Mã nhân viên</label>
                            <input type="text" class="form-control" id="maNhanVien" name="maNhanVien" value="${employee.maNhanVien}" readonly>
                        </div>
                        <div class="form-group">
                            <label for="matKhau">Mật khẩu</label>
                            <input type="password" class="form-control" id="matKhau" name="matKhau" value="${employee.matKhau}" readonly>
                        </div>
                        <div class="form-group">
                            <label for="tenNhanVien">Tài khoản</label>
                            <input type="text" class="form-control" id="tenNhanVien" name="tenNhanVien" value="${employee.tenNhanVien}" readonly>
                        </div>
                    </div>
                    <div class="col-8">
                        <h2>Thông tin chi tiết</h2>
                        <div class="row">
                             <div class="form-group col-4">
                                <label for="hoTen">Tên nhân viên</label>
                                <input type="text" class="form-control" id="hoTen" name="hoTen" value="${employee.hoTen}">
                            </div>

                            <div class="form-group col-4">
                                <label for="email">Email</label>
                                <input type="email" class="form-control" id="email" name="email" value="${employee.email}">
                            </div>
                            <div class="form-group col-4">
                                <label for="diaChi">Địa chỉ</label>
                                <input type="text" class="form-control" id="diaChi" name="diaChi">
                            </div>
                        </div>
                         <div class="row">
                            <div class="form-group col-4">
                                <label for="gioiTinh">Giới tính</label>
                                    <select class="form-control" id="gioiTinh" name="gioiTinh">
                                        <option value="true" ${employee.gioiTinh ? "selected" : ""}>Nam</option>
                                        <option value="false" ${employee.gioiTinh ? "" : "selected"}>Nữ</option>
                                    </select>
                            </div>
                            <div class="form-group col-4">
                                <label for="soDienThoai">Số điện thoại</label>
                                <input type="text" class="form-control" id="soDienThoai" name="soDienThoai" value="${employee.soDienThoai}">
                            </div>
                            <div class="form-group col-4">
                                <label for="trangThai">Trạng thái</label>
                                    <select class="form-control" id="trangThai" name="trangThai">
                                        <option value="active" ${employee.trangThai == "active" ? "selected" : ""}>Hoạt động</option>
                                        <option value="inactive" ${employee.trangThai == "inactive" ? "selected" : ""}>Ngừng hoạt động</option>
                                    </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group col-4">
                                <label for="tinh">Tỉnh/Thành phố</label>
                                <select class="form-control" id="tinh" name="tinh">
                                    <option value="">Chọn Tỉnh/Thành phố</option>
                                </select>
                            </div>
                            <div class="form-group col-4">
                                <label for="huyen">Quận/Huyện</label>
                                <select class="form-control" id="huyen" name="huyen" disabled>
                                    <option value="">Chọn Quận/Huyện</option>
                                </select>
                            </div>
                            <div class="form-group col-4">
                                <label for="xa">Phường/Xã</label>
                                <select class="form-control" id="xa" name="xa" disabled>
                                    <option value="">Chọn Phường/Xã</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>



                <button type="button" class="btn btn-primary" id="updateButton">Update</button>
            </form>`;

        $('#tableContainer').hide();
        $('#searchForm').hide();
        $('#pagination').hide();
        $('#actionForm').hide();

        $('#formContainer').html(form).show();

        // Tách địa chỉ thành các phần tử
        let addressParts = employee.diaChi.split(", ");
        let specificAddress = addressParts[0];
        let xa = addressParts[1];
        let huyen = addressParts[2];
        let tinh = addressParts[3];

        // Gán địa chỉ cụ thể vào ô input
        $('#diaChi').val(specificAddress);

        $.getJSON('https://raw.githubusercontent.com/kenzouno1/DiaGioiHanhChinhVN/master/data.json', function (data) {
            let provinces = data;

            // Load provinces into the Tỉnh select
            provinces.forEach(function (province) {
                $('#tinh').append(new Option(province.Name, province.Id));
            });

            // Gán giá trị Tỉnh
            let selectedProvince = provinces.find(province => province.Name === tinh);
            if (selectedProvince) {
                $('#tinh').val(selectedProvince.Id);

                let districts = selectedProvince.Districts;

                // Load districts into the Huyện select
                districts.forEach(function (district) {
                    $('#huyen').append(new Option(district.Name, district.Id));
                });

                // Gán giá trị Huyện
                let selectedDistrict = districts.find(district => district.Name === huyen);
                if (selectedDistrict) {
                    $('#huyen').val(selectedDistrict.Id).prop('disabled', false);

                    let communes = selectedDistrict.Wards;

                    // Load communes into the Xã select
                    communes.forEach(function (commune) {
                        $('#xa').append(new Option(commune.Name, commune.Id));
                    });

                    // Gán giá trị Xã
                    let selectedCommune = communes.find(commune => commune.Name === xa);
                    if (selectedCommune) {
                        $('#xa').val(selectedCommune.Id).prop('disabled', false);
                    }
                }
            }

            // Event listener for Tỉnh select
            $('#tinh').change(function () {
                let selectedProvinceId = $(this).val();
                let selectedProvince = provinces.find(province => province.Id === selectedProvinceId);
                let districts = selectedProvince ? selectedProvince.Districts : [];

                $('#huyen').empty().append(new Option('Chọn Quận/Huyện', '')).prop('disabled', districts.length === 0);
                $('#xa').empty().append(new Option('Chọn Phường/Xã', '')).prop('disabled', true);

                // Load districts into the Huyện select
                districts.forEach(function (district) {
                    $('#huyen').append(new Option(district.Name, district.Id));
                });
            });

            // Event listener for Huyện select
            $('#huyen').change(function () {
                let selectedProvinceId = $('#tinh').val();
                let selectedDistrictId = $(this).val();
                let selectedProvince = provinces.find(province => province.Id === selectedProvinceId);
                let selectedDistrict = selectedProvince ? selectedProvince.Districts.find(district => district.Id === selectedDistrictId) : null;
                let communes = selectedDistrict ? selectedDistrict.Wards : [];

                $('#xa').empty().append(new Option('Chọn Phường/Xã', '')).prop('disabled', communes.length === 0);

                // Load communes into the Xã select
                communes.forEach(function (commune) {
                    $('#xa').append(new Option(commune.Name, commune.Id));
                });
            });
        });

        // Thêm sự kiện click cho nút Update
        $('#updateButton').on('click', function () {
            const tinh = $('#tinh option:selected').text();
            const huyen = $('#huyen option:selected').text();
            const xa = $('#xa option:selected').text();
            const dcgoc = $('#diaChi').val();
            const diaChi = `${$('#diaChi').val()}, ${xa}, ${huyen}, ${tinh}`;
            const hoten = $('#hoTen').val();
            const manv = $('#maNhanVien').val();
            const matkhau = $('#matKhau').val();
            const tennv = $('#tenNhanVien').val();
            const email = $('#email').val();
            const sdt = $('#soDienThoai').val();

            $('#hoTen').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#hoTenError').remove(); // Loại bỏ thông báo lỗi
                }
            });
            $('#email').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#emailError').remove(); // Loại bỏ thông báo lỗi
                }
            });
            $('#sdt').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#sdtError').remove(); // Loại bỏ thông báo lỗi
                }
            });
            $('#tinh').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#tinhError').remove(); // Loại bỏ thông báo lỗi
                }
            });
            $('#huyen').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#huyenError').remove(); // Loại bỏ thông báo lỗi
                }
            });
            $('#xa').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#xaError').remove(); // Loại bỏ thông báo lỗi
                }
            });
            $('#diaChi').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#diaChiError').remove(); // Loại bỏ thông báo lỗi
                }
            });

            function valid() {
                if (hoten === '') {
                    $('#hoTen').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#hoTenError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#hoTen').after('<div id="hoTenError" class="invalid-feedback">Vui lòng nhập Tên nhân viên.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }
                if (email === '') {
                    $('#email').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#emailError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#email').after('<div id="emailError" class="invalid-feedback">Vui lòng nhập email.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }

                const regex = /^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,7}$/;
                if (!regex.test(email)) {
                    $('#email').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#emailError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#email').after('<div id="emailError" class="invalid-feedback">Email sai định dạng! Vui lòng nhập lại.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }

                if (sdt === '') {
                    $('#soDienThoai').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#soDienThoaiError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#soDienThoai').after('<div id="soDienThoaiError" class="invalid-feedback">Vui lòng nhập số điện thoại.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }
                const regexSdt = /(84|0[3|5|7|8|9])+([0-9]{8})\b/g;
                if (!regexSdt.test(sdt)) {
                    $('#soDienThoai').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#soDienThoaiError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#soDienThoai').after('<div id="soDienThoaiError" class="invalid-feedback">Số điện thoại sai định dang! Vui lòng nhập lại.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }
                if (tinh === 'Chọn Tỉnh/Thành phố') {
                    $('#tinh').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#tinhError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#tinh').after('<div id="tinhError" class="invalid-feedback">Vui lòng chọn tỉnh thành.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }
                if (huyen === 'Chọn Quận/Huyện') {
                    $('#huyen').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#huyenError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#huyen').after('<div id="huyenError" class="invalid-feedback">Vui lòng chọn quận/huyện.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }
                console.log(xa);
                if (xa === 'Chọn Phường/Xã') {
                    $('#xa').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#xaError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#xa').after('<div id="xaError" class="invalid-feedback">Vui lòng chọn xã/phường.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }
                if (dcgoc === '') {
                    $('#diaChi').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#diaChiError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#diaChi').after('<div id="diaChiError" class="invalid-feedback">Vui lòng nhập địa chỉ.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }

                return true;
            }

            if (!valid()) {
                return;
            }


            const updatedEmployee = {
                id: employee.id,
                hoTen: hoten,
                maNhanVien: manv,
                matKhau: matkhau,
                tenNhanVien: tennv,
                email: email,
                diaChi: diaChi,
                gioiTinh: $('#gioiTinh').val(),
                soDienThoai: sdt,
                trangThai: $('#trangThai').val(),
            };
            console.log(updatedEmployee);
            $.ajax({
                url: apiUrlUpdate,
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(updatedEmployee),
                success: function (response) {
                    if (response === true) {
                        alert("caidẹov");
                        showSuccessToast('Cập nhật  thành công');
                        $('#formContainer').hide();

                        init();
                        $('#tableContainer').show();
                        $('#searchForm').show();
                        $('#actionForm').show();
                        $('#pagination').show();

                    } else {
                        showErrorToast('Không thể cập nhật trạng thái');
                    }
                },
                error: function () {
                    showErrorToast('Đã xảy ra lỗi, không thể cập nhật trạng thái');
                }
            });
        });
    }

    function renderAddForm() {
        $('#linkAdd').show();
        let form = `<form id="addEmployeeForm">
        <div class="row">
            <div class="form-group col-4">
                <label for="hoTen">Tên nhân viên</label>
                <input type="text" class="form-control" id="hoTen" name="hoTen" placeholder="Nhập tên nhân viên">
            </div>
            <div class="form-group col-4">
                <label for="email">Email</label>
                <input type="email" class="form-control" id="email" name="email" placeholder="Nhập email">
            </div>
            <div class="form-group col-4">
                <label for="diaChi">Địa chỉ</label>
                <input type="text" class="form-control" id="diaChi" name="diaChi" placeholder="Nhập địa chỉ">
            </div>
        </div>
        <div class="row">
        <div class="form-group col-4">
            <label for="soDienThoai">Số điện thoại</label>
            <input type="text" class="form-control" id="soDienThoai" name="soDienThoai" placeholder="Nhập số điện thoại">
        </div>
         <div class="form-group col-4">
            <label for="gioiTinh">Giới tính</label>
            <select class="form-control" id="gioiTinh" name="gioiTinh">
                <option value="true">Nam</option>
                <option value="false">Nữ</option>
            </select>
        </div>

        <div class="form-group col-4">
            <label for="trangThai">Trạng thái</label>
            <select class="form-control" id="trangThai" name="trangThai">
                <option value="active">Hoạt động</option>
                <option value="inactive">Ngừng hoạt động</option>
            </select>
        </div>
        </div>
        <div class="row">
        <div class="form-group col-4">
            <label for="tinh">Tỉnh/Thành phố</label>
            <select class="form-control" id="tinh" name="tinh">
                <option value="">Chọn Tỉnh/Thành phố</option>
            </select>
        </div>
        <div class="form-group col-4" >
            <label for="huyen">Quận/Huyện</label>
            <select class="form-control" id="huyen" name="huyen" disabled>
                <option value="">Chọn Quận/Huyện</option>
            </select>
        </div>
        <div class="form-group col-4">
            <label for="xa">Phường/Xã</label>
            <select class="form-control" id="xa" name="xa" disabled>
                <option value="">Chọn Phường/Xã</option>
            </select>
        </div>
        </div>

        <button type="button" class="btn btn-primary" id="addButton">Thêm mới</button>
    </form>`;

        $('#tableContainer').hide();
        $('#pagination').hide();
        $('#searchForm').hide();
        $('#actionForm').hide();
        $('#formContainer').html(form).show();

        // Fetch data from API and populate selects
        $.getJSON('https://raw.githubusercontent.com/kenzouno1/DiaGioiHanhChinhVN/master/data.json', function (data) {
            let provinces = data;

            // Load provinces into the Tỉnh select
            provinces.forEach(function (province) {
                $('#tinh').append(new Option(province.Name, province.Id));
            });

            // Event listener for Tỉnh select
            $('#tinh').change(function () {
                let selectedProvinceId = $(this).val();
                let selectedProvince = provinces.find(province => province.Id === selectedProvinceId);
                let districts = selectedProvince ? selectedProvince.Districts : [];

                $('#huyen').empty().append(new Option('Chọn Quận/Huyện', '')).prop('disabled', districts.length === 0);
                $('#xa').empty().append(new Option('Chọn Phường/Xã', '')).prop('disabled', true);

                // Load districts into the Huyện select
                districts.forEach(function (district) {
                    $('#huyen').append(new Option(district.Name, district.Id));
                });
            });

            // Event listener for Huyện select
            $('#huyen').change(function () {
                let selectedProvinceId = $('#tinh').val();
                let selectedDistrictId = $(this).val();
                let selectedProvince = provinces.find(province => province.Id === selectedProvinceId);
                let selectedDistrict = selectedProvince ? selectedProvince.Districts.find(district => district.Id === selectedDistrictId) : null;
                let communes = selectedDistrict ? selectedDistrict.Wards : [];

                $('#xa').empty().append(new Option('Chọn Phường/Xã', '')).prop('disabled', communes.length === 0);

                // Load communes into the Xã select
                communes.forEach(function (commune) {
                    $('#xa').append(new Option(commune.Name, commune.Id));
                });
            });
        });

        // Thêm sự kiện click cho nút Thêm mới
        $('#addButton').on('click', function () {
            const tinh = $('#tinh option:selected').text();
            const huyen = $('#huyen option:selected').text();
            const xa = $('#xa option:selected').text();
            const dcgoc = $('#diaChi').val();
            const diaChi = `${$('#diaChi').val()}, ${xa}, ${huyen}, ${tinh}`;
            const hoten = $('#hoTen').val();
            const email = $('#email').val();
            const sdt = $('#soDienThoai').val();

            $('#hoTen').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#hoTenError').remove(); // Loại bỏ thông báo lỗi
                }
            });

            $('#email').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#emailError').remove(); // Loại bỏ thông báo lỗi
                }
            });
            $('#sdt').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#sdtError').remove(); // Loại bỏ thông báo lỗi
                }
            });
            $('#tinh').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#tinhError').remove(); // Loại bỏ thông báo lỗi
                }
            });
            $('#huyen').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#huyenError').remove(); // Loại bỏ thông báo lỗi
                }
            });
            $('#xa').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#xaError').remove(); // Loại bỏ thông báo lỗi
                }
            });
            $('#diaChi').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#diaChiError').remove(); // Loại bỏ thông báo lỗi
                }
            });

            function valid() {
                if (hoten === '') {
                    $('#hoTen').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#hoTenError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#hoTen').after('<div id="hoTenError" class="invalid-feedback">Vui lòng nhập Tên nhân viên.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }

                if (email === '') {
                    $('#email').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#emailError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#email').after('<div id="emailError" class="invalid-feedback">Vui lòng nhập email.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }
                const regex = /^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,7}$/;
                if (!regex.test(email)) {
                    $('#email').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#emailError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#email').after('<div id="emailError" class="invalid-feedback">Email sai định dạng! Vui lòng nhập lại.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }
                if (sdt === '') {
                    $('#soDienThoai').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#soDienThoaiError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#soDienThoai').after('<div id="soDienThoaiError" class="invalid-feedback">Vui lòng nhập số điện thoại.</div>'); // Thêm thông báo lỗi mới

                    return false;
                }
                const regexSdt = /(84|0[3|5|7|8|9])+([0-9]{8})\b/g;
                if (!regexSdt.test(sdt)) {
                    $('#soDienThoai').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#soDienThoaiError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#soDienThoai').after('<div id="soDienThoaiError" class="invalid-feedback">Vui lòng nhập số điện thoại.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }
                if (tinh === 'Chọn Tỉnh/Thành phố') {
                    $('#tinh').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#tinhError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#tinh').after('<div id="tinhError" class="invalid-feedback">Vui lòng chọn tỉnh thành.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }
                if (huyen === 'Chọn Quận/Huyện') {
                    $('#huyen').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#huyenError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#huyen').after('<div id="huyenError" class="invalid-feedback">Vui lòng chọn quận/huyện.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }
                console.log(xa);
                if (xa === 'Chọn Phường/Xã') {
                    $('#xa').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#xaError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#xa').after('<div id="xaError" class="invalid-feedback">Vui lòng chọn xã/phường.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }
                if (dcgoc === '') {
                    $('#diaChi').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#diaChiError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#diaChi').after('<div id="diaChiError" class="invalid-feedback">Vui lòng nhập địa chỉ.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }

                return true;
            }

            if (!valid()) {
                return;
            }

            const newEmployee = {
                hoTen: hoten,
                email: email,
                diaChi: diaChi,
                gioiTinh: $('#gioiTinh').val(),
                soDienThoai: sdt,
                trangThai: $('#trangThai').val(),

            };
            console.log(newEmployee);
            $.ajax({
                url: apiUrlAdd,
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(newEmployee),
                success: function (response) {
                    if (response === true) {
                        showSuccessToast('Thêm thành công');
                        $('#formContainer').hide();
                        $('#searchForm').show();
                        init();
                        $('#actionForm').show();
                        $('#tableContainer').show();
                        $('#pagination').show();
                    } else {
                        showErrorToast('Thêm thất bại');
                    }
                },
                error: function () {
                    showErrorToast('Đã xảy ra lỗi, thêm không thành công');
                }
            });
        });
    }

});