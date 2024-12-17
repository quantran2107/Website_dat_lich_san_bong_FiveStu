$(document).ready(function () {
    let apiGetAll = 'http://localhost:8080/nhan-vien/hien-thi';
    let currentPage = 1; // Trang hiện tại
    let recordsPerPage = 5; // Số lượng bản ghi trên mỗi trang
    let totalPages = 0; // Tổng số trang
    let list = []; // Danh sách dữ liệu
    let imageData = '';

    $('#closeQRmodal').on('click', () => {
        $('#qrCodeModal').modal('hide');
    })

    $('#load').on('click',()=>{
        $('#searchInput').val('')
        loadTable(apiGetAll, '', currentPage, recordsPerPage);
    })

    $('#uploadButton').on('click', function () {
        // Tạo input file động
        const fileInput = $('<input type="file" style="display: none;">');
        $('body').append(fileInput); // Thêm vào DOM

        // Gắn sự kiện khi chọn file
        fileInput.on('change', function () {
            const file = this.files[0];
            if (file) {
                const formData = new FormData();
                formData.append('file', file);

                $.ajax({
                    type: 'POST',
                    enctype: 'multipart/form-data',
                    url: 'nhan-vien/upload',
                    data: formData,
                    processData: false,
                    contentType: false,
                    cache: false,
                    timeout: 1000000,
                    success: function () {
                        showSuccessToast("Tải lên file thành công!");
                    },
                    error: function () {
                        showErrorToast("Tải lên file thất bại!");
                    }
                });
            }
            fileInput.remove();
            loadTable(apiGetAll, '', currentPage, recordsPerPage);
        });
        fileInput.click();

    });

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

    $('#btnExcelMau').on('click', function () {
        let url = 'https://docs.google.com/spreadsheets/d/1bDAR1kH1wQIMQ6JLkgmmLLQlrMioKFYY/export?format=xlsx';
        let $a = $('<a></a>').attr('href', url).attr('download', 'file.xlsx').appendTo('body');
        $a[0].click();
        $a.remove();
    });


    // code Qr
    const btnScanQR = document.getElementById('btn-scan-qr');

    const qrCodeModal = $('#qrCodeModal');
    let html5QrCode;

    btnScanQR.onclick = () => {
        qrCodeModal.modal('show');
    };

    qrCodeModal.on('shown.bs.modal', function () {
        html5QrCode = new Html5Qrcode("qr-reader");
        html5QrCode.start(
            {facingMode: "environment"},
            {
                fps: 10,
                qrbox: {width: 250, height: 250}
            },
            (qrCodeMessage) => {

                let dataParts = qrCodeMessage.split('|');

                let hoTen = dataParts[2];
                $(`#hoTen`).val(hoTen);

                let ngaySinhRaw = dataParts[3];

                function formatDate1(dateStr) {
                    return `${dateStr.substring(0, 2)}/${dateStr.substring(2, 4)}/${dateStr.substring(4)}`;
                }

                function formatDate(dateStr) {
                    let parts = dateStr.split('/');
                    if (parts.length !== 3) return null; // Xử lý lỗi nếu chuỗi không hợp lệ

                    let formattedDate = `${parts[2]}-${parts[1]}-${parts[0]}`; // Chuyển đổi sang "yyyy-MM-dd"
                    return formattedDate;
                }

                let ngaySinh = new Date(formatDate(formatDate1(ngaySinhRaw)));
                if (!isNaN(ngaySinh.getTime())) {
                    let formattedNgaySinh = `${ngaySinh.getFullYear()}-${(ngaySinh.getMonth() + 1).toString().padStart(2, '0')}-${ngaySinh.getDate().toString().padStart(2, '0')}`;
                    $('#ngaySinh').val(formattedNgaySinh);
                }

                let gioiTinh = dataParts[4];
                if (gioiTinh === 'Nam') {
                    $(`#gioiTinhNam`).prop('checked', true);
                } else {
                    $(`#gioiTinhNu`).prop('checked', true);
                }

                let diaChi = dataParts[5];
                let addressParts = diaChi.split(", ");
                let specificAddress = addressParts[0];
                $(`#diaChi`).val(specificAddress);

                let xa = addressParts[1];
                let huyen = addressParts[2];
                let tinh = addressParts[3];

                $.getJSON('https://raw.githubusercontent.com/kenzouno1/DiaGioiHanhChinhVN/master/data.json', function (data) {
                    let provinces = data;

                    // Load provinces into the Tỉnh select
                    provinces.forEach(function (province) {
                        $('#tinh').append(new Option(province.Name, province.Id));
                    });

                    // Gán giá trị Tỉnh
                    let selectedProvince = provinces.find(province => province.Name.toLowerCase().includes(tinh.toLowerCase()));
                    if (selectedProvince) {
                        $('#tinh').val(selectedProvince.Id);

                        let districts = selectedProvince.Districts;

                        // Load districts into the Huyện select
                        districts.forEach(function (district) {
                            $('#huyen').append(new Option(district.Name, district.Id));
                        });

                        // Gán giá trị Huyện
                        let selectedDistrict = districts.find(district => district.Name.toLowerCase().includes(huyen.toLowerCase()));
                        if (selectedDistrict) {
                            $('#huyen').val(selectedDistrict.Id).prop('disabled', false);

                            let communes = selectedDistrict.Wards;

                            // Load communes into the Xã select
                            communes.forEach(function (commune) {
                                $('#xa').append(new Option(commune.Name, commune.Id));
                            });

                            // Gán giá trị Xã
                            let selectedCommune = communes.find(commune => commune.Name.toLowerCase().includes(xa.toLowerCase()));
                            if (selectedCommune) {
                                $('#xa').val(selectedCommune.Id).prop('disabled', false);
                            }
                        }
                    }
                });


                html5QrCode.stop().then(() => {
                    qrCodeModal.modal('hide');
                }).catch((err) => {
                    // Handle stop error
                });
            },
            (errorMessage) => {
                // Handle scan error
            }
        ).catch((err) => {
            // Handle start error
        });
    });

    qrCodeModal.on('hidden.bs.modal', function () {
        if (html5QrCode) {
            html5QrCode.stop().catch((err) => {
                // Handle stop error
            });
        }
    });


    // sự kiện click thêm nhân viên ở màn table
    $('#btnAdd').off('click').click(function () {
        $('#previewImage').attr('src', 'https://cellphones.com.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg');
        renderAddForm();

    });


    $('#fileInput').change(function (event) {
        let file = event.target.files[0];
        let reader = new FileReader();

        reader.onload = function (e) {
            $('#previewImage').attr('src', e.target.result);
            imageData = e.target.result.split(',')[1]; // Lưu dữ liệu ảnh vào biến toàn cục
        };

        reader.readAsDataURL(file); // Đọc nội dung của tệp dưới dạng URL base64
    });
    $('#fileInputU').change(function (event) {
        let file = event.target.files[0];
        let reader = new FileReader();
        reader.onload = function (e) {
            $('#previewImageU').attr('src', e.target.result);
            imageData = e.target.result.split(',')[1]; // Lưu dữ liệu ảnh vào biến toàn cục
        };
        reader.readAsDataURL(file); // Đọc nội dung của tệp dưới dạng URL base64
    });


    // Gọi loadTable khi trang vừa load
    loadTable(apiGetAll, '', currentPage, recordsPerPage);
    $(`#formAdd`).hide();
    $(`#formUpdate`).hide();


    $('#qlnv').on('click', function () {
        $('.is-invalid').removeClass('is-invalid');
        removeErrorClass();
        $(`#tableNhanVien`).show()
        loadTable(apiGetAll, '', currentPage, recordsPerPage);
        $(`#formAdd`).hide();
        $(`#formUpdate`).hide();

    });

    function removeErrorClass() {
        $('#ngaySinhError').remove();
        $('#emailError').remove();
        $('#soDienThoaiError').remove();
        $('#tinhError').remove();
        $('#huyenError').remove();
        $('#xaError').remove();
        $('#diaChiError').remove();
        $('#hoTenError').remove();

        $('#ngaySinhUError').remove();
        $('#emailUError').remove();
        $('#soDienThoaiUError').remove();
        $('#tinhUError').remove();
        $('#huyenUError').remove();
        $('#xaUError').remove();
        $('#diaChiUError').remove();
        $('#hoTenUError').remove();

    }

    $('#status-select .dropdown-item').click(function (event) {
        event.preventDefault();
        let selectedStatus = $(this).text().trim();
        let apiGetAll = '';
        $('#actionMenuButton3').text(selectedStatus);

        if (selectedStatus === 'Trạng thái') {
            apiGetAll = 'http://localhost:8080/nhan-vien/hien-thi';
        } else if (selectedStatus === 'Hoạt động') {
            apiGetAll = 'http://localhost:8080/nhan-vien/active';
        } else if (selectedStatus === 'Không hoạt động') {
            apiGetAll = 'http://localhost:8080/nhan-vien/inactive';
        }

        currentPage = 1;
        loadTable(apiGetAll, '', currentPage, recordsPerPage);
    });


    // Xử lý sự kiện khi nhập vào ô tìm kiếm
    $('#searchInput').on('input', function () {
        let keysearch = $(this).val().trim(); // Lấy giá trị từ ô input search
        currentPage = 1; // Reset lại trang về trang đầu tiên khi tìm kiếm
        loadTable(apiGetAll, keysearch, currentPage, recordsPerPage); // Gọi lại hàm loadTable với từ khóa tìm kiếm
    });


    // Hàm loadTable với phân trang và chức năng prev, next
    function loadTable(api, keysearch = '', page, limit) {
        $(`#qlnv`).hide();
        $(`#linkAdd`).hide();
        $(`#linkUpdate`).hide();
        let tbody = '';
        $.getJSON(api, function (data) {
            list = data;

            // Lọc dữ liệu nếu có từ khóa tìm kiếm
            if (keysearch !== '') {
                list = list.filter(employee =>
                    employee.hoTen.toLowerCase().includes(keysearch.toLowerCase()) || // Tìm theo tên
                    employee.maNhanVien.toLowerCase().includes(keysearch.toLowerCase()) || // Tìm theo mã nhân viên
                    employee.email.toLowerCase().includes(keysearch.toLowerCase()) || // Tìm theo email
                    employee.soDienThoai.toLowerCase().includes(keysearch.toLowerCase()) // Tìm theo số điện thoại
                );
            }

            totalPages = Math.ceil(list.length / limit);

            // Xác định chỉ số bắt đầu và kết thúc của dữ liệu hiển thị trên trang hiện tại
            let startIndex = (page - 1) * limit;
            let endIndex = startIndex + limit;
            let paginatedData = list.slice(startIndex, endIndex);

            // Render dữ liệu vào tbody
            paginatedData.forEach((employee, index) => {
                const statusClass = employee.trangThai === 'active' ? 'custom-4' : 'custom-3';
                const statusText = employee.trangThai === 'active' ? 'Hoạt động' : 'Đã nghỉ';

                tbody += ` <td class="special-td">${startIndex + index + 1}</td>
                <td class="special-td">${employee.maNhanVien === null ? "Không có" : employee.maNhanVien}</td>
                <td class="special-td">${employee["hoTen"] === null ? "Không có" : employee["hoTen"]}</td>
                <td class="special-td">${employee.email === null ? "Không có" : employee.email}</td>
                <td class="special-td">${employee["soDienThoai"] === null ? "Không có" : employee["soDienThoai"]}</td>
                <td class="special-td">${employee.gioiTinh ? "Nam" : "Nữ"}</td>
               <td>
                    <span class="${statusClass}">
                        <span>${statusText}</span>
                    </span>
                </td>
            <td><button title="Chi tiết" class="btn btn-outline-success action-button" data-employee='${JSON.stringify(employee)}'><span class="fe fe-edit-3"></span></button>           
            </tr>`;
            });

            $('#tbodyContainer').html(tbody);
            $('.action-button').off('click').on('click', function () {
                let employeeData = $(this).data('employee');
                $('#btnSubmitUpdate').hide();
                $('#btnChangeUpdate').show();
                renderUpdateForm(employeeData)

            });
            // Tạo các nút phân trang
            let pagination = `<div class="pagination" id="pagination">
                                <button title="Trước" style="height: 30px; display: flex; align-items: center; justify-content: center;" class="btn btn-success prev-page" ${currentPage === 1 ? 'hidden' : ''}><</button>
                                <select  class="custom-select page-select">`;
            for (let i = 1; i <= totalPages; i++) {
                pagination += `<option value="${i}" ${i === currentPage ? 'selected' : ''}>Trang ${i}</option>`;
            }
            pagination += `</select>
                            <button title="Sau" style="height: 30px; display: flex; align-items: center; justify-content: center;"  class="btn btn-success next-page" ${currentPage === totalPages ? 'hidden' : ''}>></button>
                           </div>`;

            $('#pagination').html(pagination);

            // Xử lý sự kiện khi chọn trang từ select
            $('.page-select').change(function () {
                currentPage = parseInt($(this).val());
                loadTable(api, keysearch, currentPage, limit);
            });

            // Xử lý sự kiện khi nhấn vào nút Prev
            $('.prev-page').off('click').click(function () {
                if (currentPage > 1) {
                    currentPage--;
                    loadTable(api, keysearch, currentPage, limit);
                }
            });

            // Xử lý sự kiện khi nhấn vào nút Next
            $('.next-page').off('click').click(function () {
                if (currentPage < totalPages) {
                    currentPage++;
                    loadTable(api, keysearch, currentPage, limit);
                }
            });
        });

    }


    function renderAddForm() {

        $('#tableNhanVien').hide();
        $('#formAdd').show();
        $('#qlnv').show();
        $(`#formUpdate`).hide()
        $(`#linkAdd`).show();
        $(`#linkUpdate`).hide();

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

        //Hàm xử lý sự kiện click btn btnSubmitAdd
        $('#btnSubmitAdd').off('click').click(function (e) {
            e.preventDefault(); // Ngăn chặn hành động mặc định của form

            // Lấy các giá trị từ các trường input
            let hoTen = $('#hoTen').val();
            let ngaySinh = $('#ngaySinh').val();
            let email = $('#email').val();
            let soDienThoai = $('#soDienThoai').val();
            let diaChiCT = $('#diaChi').val();
            let tinh = $('#tinh option:selected').text();
            let huyen = $('#huyen option:selected').text();
            let xa = $('#xa option:selected').text();
            let diaChi = `${diaChiCT}, ${xa}, ${huyen}, ${tinh}`
            let gender;
            if ($(`#gioiTinhNam`).is(`:checked`)) {
                gender = true;
            }
            if ($(`#gioiTinhNu`).is(`:checked`)) {
                gender = false;
            }

            $('#ngaySinh').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid');
                    $('#ngaySinhError').remove();
                }
            });

            $('#email').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid');
                    $('#emailError').remove();
                }
            });
            $('#soDienThoai').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid');
                    $('#soDienThoaiError').remove();
                }
            });
            $('#tinh').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid');
                    $('#tinhError').remove();
                }
            });
            $('#huyen').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid');
                    $('#huyenError').remove();
                }
            });
            $('#xa').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid');
                    $('#xaError').remove();
                }
            });
            $('#diaChi').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid');
                    $('#diaChiError').remove();
                }
            });
            $('#hoTen').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid');
                    $('#hoTenError').remove();
                }
            });

            function valid() {
                if (ngaySinh === '') {
                    $('#ngaySinh').addClass('is-invalid');
                    $('#ngaySinhError').remove();
                    $('#ngaySinh').after('<div id="ngaySinhError" class="invalid-feedback">Vui lòng nhập ngày sinh.</div>');
                    return false;
                }
                if (hoTen === '') {
                    $('#hoTen').addClass('is-invalid');
                    $('#hoTenError').remove();
                    $('#hoTen').after('<div id="hoTenError" class="invalid-feedback">Vui lòng nhập Tên nhân viên.</div>');
                    return false;
                }
                if (email === '') {
                    $('#email').addClass('is-invalid');
                    $('#emailError').remove();
                    $('#email').after('<div id="emailError" class="invalid-feedback">Vui lòng nhập email.</div>');
                    return false;
                }

                const regex = /^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,7}$/;
                if (!regex.test(email)) {
                    $('#email').addClass('is-invalid');
                    $('#emailUError').remove();
                    $('#email').after('<div id="emailError" class="invalid-feedback">Email sai định dạng! Vui lòng nhập lại.</div>');
                    return false;
                }

                if (soDienThoai === '') {
                    $('#soDienThoai').addClass('is-invalid');
                    $('#soDienThoaiError').remove();
                    $('#soDienThoai').after('<div id="soDienThoaiError" class="invalid-feedback">Vui lòng nhập số điện thoại.</div>');
                    return false;
                }
                const regexSdt = /(84|0[3|5|7|8|9])+([0-9]{8})\b/g;
                if (!regexSdt.test(soDienThoai)) {
                    $('#soDienThoai').addClass('is-invalid');
                    $('#soDienThoaiError').remove();
                    $('#soDienThoai').after('<div id="soDienThoaiError" class="invalid-feedback">Số điện thoại sai định dang! Vui lòng nhập lại.</div>');
                    return false;
                }
                if (tinh === 'Chọn Tỉnh/Thành phố') {
                    $('#tinh').addClass('is-invalid');
                    $('#tinhError').remove();
                    $('#tinh').after('<div id="tinhError" class="invalid-feedback">Vui lòng chọn tỉnh thành.</div>');
                    return false;
                }
                if (huyen === 'Chọn Quận/Huyện') {
                    $('#huyen').addClass('is-invalid');
                    $('#huyenError').remove();
                    $('#huyen').after('<div id="huyenUError" class="invalid-feedback">Vui lòng chọn quận/huyện.</div>');
                    return false;
                }

                if (xa === 'Chọn Phường/Xã') {
                    $('#xa').addClass('is-invalid');
                    $('#xaError').remove();
                    $('#xa').after('<div id="xaError" class="invalid-feedback">Vui lòng chọn xã/phường.</div>');
                    return false;
                }
                if (diaChiCT === '') {
                    $('#diaChi').addClass('is-invalid');
                    $('#diaChiError').remove();
                    $('#diaChi').after('<div id="diaChiError" class="invalid-feedback">Vui lòng nhập địa chỉ.</div>');
                    return false;
                }

                return true;
            }

            if (!valid()) {
                return;
            }
            Swal.fire({
                title: 'Xác nhận',
                text: 'Xác nhận thêm mới nhân viên ?',
                icon: 'question',
                showCancelButton: true,
                confirmButtonText: 'Yes',
                cancelButtonText: 'Cancel'
            }).then((result) => {
                if (result.isConfirmed) {
                    // Tạo đối tượng dữ liệu nhân viên
                    let employeeData = {
                        imageNV: imageData,
                        hoTen: hoTen,
                        ngaySinh: ngaySinh,
                        email: email,
                        soDienThoai: soDienThoai,
                        diaChi: diaChi,
                        gioiTinh: gender,
                    };

                    // Gọi API để thêm nhân viên
                    $.ajax({
                        url: 'http://localhost:8080/nhan-vien/add',
                        type: 'POST',
                        contentType: 'application/json',
                        data: JSON.stringify(employeeData),
                        success: function (response) {
                            if (response === true) {
                                showSuccessToast('Thêm thành công ');
                                $(`#tableNhanVien`).show();
                                loadTable(apiGetAll, '', currentPage, recordsPerPage);

                                $(`#formAdd`).hide();
                                $(`#formUpdate`).hide();
                                $('#imageNVdetail').attr('src', 'https://cellphones.com.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg');
                                imageData = '';
                                $('#formAdd').find('input').val('');
                            } else if (response==="Email đã tồn tại!"){
                                showErrorToast('Email đã tồn tại!');
                            }else {
                                showErrorToast('Thêm thất bại');
                            }
                        },
                        error: function (xhr, status, error) {
                            showErrorToast("Thêm thất bại")
                        }
                    });
                } else if (result.dismiss === Swal.DismissReason.cancel) {

                }
            });


        });
    }


    function renderUpdateForm(nhanV) {
        $('#tableNhanVien').hide();
        $('#formAdd').hide();
        $(`#formUpdate`).show()
        $(`#qlnv`).show()
        $(`#linkAdd`).hide();
        $(`#linkUpdate`).show();
        if (nhanV["imageNV"]) {
            imageData = nhanV["imageNV"]
            $('#previewImageU').attr('src', imageData);
        } else {
            $('#previewImageU').attr('src', 'https://cellphones.com.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg');

        }

        $(`#hoTenU`).val(nhanV["hoTen"]);
        $(`#ngaySinhU`).val(nhanV["ngaySinh"]);
        $(`#createAtU`).val(nhanV["createdAt"].split('T')[0]);
        $(`#tenNhanVienU`).val(nhanV["tenNhanVien"]);
        $(`#maNhanVienU`).val(nhanV["maNhanVien"]);

        $(`#emailU`).val(nhanV["email"]);
        $(`#soDienThoaiU`).val(nhanV["soDienThoai"]);
        if (nhanV["gioiTinh"]) {
            $(`#gioiTinhNamU`).prop('checked', true);
        } else {
            $(`#gioiTinhNuU`).prop('checked', true);
        }
        if (nhanV["trangThai"] === "active") {
            $(`#trangThaiAc`).prop('checked', true);
        } else {
            $(`#trangThaiIn`).prop('checked', true);
        }


        let listDCGoc = nhanV.diaChi.split(", ");
        let dcGoc = listDCGoc[0];
        let xa = listDCGoc[1];
        let huyen = listDCGoc[2];
        let tinh = listDCGoc[3];


        // Gán địa chỉ cụ thể vào ô input
        $('#diaChiU').val(dcGoc);

        $.getJSON('https://raw.githubusercontent.com/kenzouno1/DiaGioiHanhChinhVN/master/data.json', function (data) {
            let provinces = data;

            // Load provinces into the Tỉnh select
            provinces.forEach(function (province) {
                $('#tinhU').append(new Option(province.Name, province.Id));
            });

            // Event listener for Tỉnh select
            $('#tinhU').change(function () {
                let selectedProvinceId = $(this).val();
                let selectedProvince = provinces.find(province => province.Id === selectedProvinceId);
                let districts = selectedProvince ? selectedProvince.Districts : [];

                $('#huyenU').empty().append(new Option('Chọn Quận/Huyện', ''))
                $('#xaU').empty().append(new Option('Chọn Phường/Xã', ''))

                // Load districts into the Huyện select
                districts.forEach(function (district) {
                    $('#huyenU').append(new Option(district.Name, district.Id));
                });
            });

            // Event listener for Huyện select
            $('#huyenU').change(function () {
                let selectedProvinceId = $('#tinhU').val();
                let selectedDistrictId = $(this).val();
                let selectedProvince = provinces.find(province => province.Id === selectedProvinceId);
                let selectedDistrict = selectedProvince ? selectedProvince.Districts.find(district => district.Id === selectedDistrictId) : null;
                let communes = selectedDistrict ? selectedDistrict.Wards : [];

                $('#xaU').empty().append(new Option('Chọn Phường/Xã', ''))

                // Load communes into the Xã select
                communes.forEach(function (commune) {
                    $('#xaU').append(new Option(commune.Name, commune.Id));
                });
            });
            $('#tinhU').find('option').filter(function () {
                return $(this).text() === tinh;
            }).prop('selected', true);
            $('#tinhU').trigger('change');
            $('#huyenU').find('option').filter(function () {
                return $(this).text() === huyen;
            }).prop('selected', true);
            $('#huyenU').trigger('change');
            $('#xaU').find('option').filter(function () {
                return $(this).text() === xa;
            }).prop('selected', true);

        });

        $('#formUpdate').find('input').prop('disabled', true);
        $('#tinhU').prop('disabled', true);
        $('#huyenU').prop('disabled', true);
        $('#xaU').prop('disabled', true);

        $('#btnChangeUpdate').off('click').click(function () {
            $('#formUpdate').find('input').prop('disabled', false);
            $('#btnSubmitUpdate').show();
            $('#tinhU').prop('disabled', false);
            $('#huyenU').prop('disabled', false);
            $('#xaU').prop('disabled', false);
            $(this).hide();
        });


        $('#btnSubmitUpdate').off('click').on('click', function () {
            const tinh = $('#tinhU option:selected').text();
            const huyen = $('#huyenU option:selected').text();
            const xa = $('#xaU option:selected').text();
            const tennv = nhanV.tenNhanVien;
            const manv = nhanV.maNhanVien;
            const matKhau = nhanV.matKhau;
            const dcgoc = $('#diaChiU').val();
            const diaChi = `${$('#diaChiU').val()}, ${xa}, ${huyen}, ${tinh}`;
            const hoten = $('#hoTenU').val();
            const email = $('#emailU').val();
            const sdt = $('#soDienThoaiU').val();
            const ngaySinh = $('#ngaySinhU').val();
            let gioiTinh;
            if ($(`#gioiTinhNamU`).is(`:checked`)) {
                gioiTinh = true;
            }
            if ($(`#gioiTinhNuU`).is(`:checked`)) {
                gioiTinh = false;
            }
            let trangThai;
            if ($(`#trangThaiAc`).is(`:checked`)) {
                trangThai = "active";
            }
            if ($(`#trangThaiIn`).is(`:checked`)) {
                trangThai = "inactive";
            }

            $('#ngaySinhU').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#ngaySinhUError').remove(); // Loại bỏ thông báo lỗi
                }
            });

            $('#emailU').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#emailUError').remove(); // Loại bỏ thông báo lỗi
                }
            });
            $('#soDienThoaiU').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#soDienThoaiUError').remove(); // Loại bỏ thông báo lỗi
                }
            });
            $('#tinhU').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#tinhUError').remove(); // Loại bỏ thông báo lỗi
                }
            });
            $('#huyenU').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#huyenUError').remove(); // Loại bỏ thông báo lỗi
                }
            });
            $('#xaU').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#xaUError').remove(); // Loại bỏ thông báo lỗi
                }
            });
            $('#diaChiU').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#diaChiUError').remove(); // Loại bỏ thông báo lỗi
                }
            });
            $('#hoTenU').on('input', function () {
                if ($(this).val().trim() !== '') {
                    $(this).removeClass('is-invalid'); // Loại bỏ class is-invalid nếu không rỗng
                    $('#hoTenUError').remove(); // Loại bỏ thông báo lỗi
                }
            });

            function valid() {
                if (ngaySinh === '') {
                    $('#ngaySinhU').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#ngaySinhUError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#ngaySinhU').after('<div id="ngaySinhUError" class="invalid-feedback">Vui lòng nhập ngày sinh.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }
                if (hoten === '') {
                    $('#hoTenU').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#hoTenUError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#hoTenU').after('<div id="hoTenUError" class="invalid-feedback">Vui lòng nhập Tên nhân viên.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }
                if (email === '') {
                    $('#emailU').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#emailUError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#emailU').after('<div id="emailUError" class="invalid-feedback">Vui lòng nhập email.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }

                const regex = /^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,7}$/;
                if (!regex.test(email)) {
                    $('#emailU').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#emailUError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#emailU').after('<div id="emailUError" class="invalid-feedback">Email sai định dạng! Vui lòng nhập lại.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }

                if (sdt === '') {
                    $('#soDienThoaiU').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#soDienThoaiUError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#soDienThoaiU').after('<div id="soDienThoaiUError" class="invalid-feedback">Vui lòng nhập số điện thoại.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }
                const regexSdt = /(84|0[3|5|7|8|9])+([0-9]{8})\b/g;
                if (!regexSdt.test(sdt)) {
                    $('#soDienThoaiU').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#soDienThoaiUError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#soDienThoaiU').after('<div id="soDienThoaiUError" class="invalid-feedback">Số điện thoại sai định dang! Vui lòng nhập lại.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }
                if (tinh === 'Chọn Tỉnh/Thành phố') {
                    $('#tinhU').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#tinhUError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#tinhU').after('<div id="tinhUError" class="invalid-feedback">Vui lòng chọn tỉnh thành.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }
                if (huyen === 'Chọn Quận/Huyện') {
                    $('#huyenU').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#huyenUError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#huyenU').after('<div id="huyenUError" class="invalid-feedback">Vui lòng chọn quận/huyện.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }

                if (xa === 'Chọn Phường/Xã') {
                    $('#xaU').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#xaUError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#xaU').after('<div id="xaUError" class="invalid-feedback">Vui lòng chọn xã/phường.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }
                if (dcgoc === '') {
                    $('#diaChiU').addClass('is-invalid'); // Thêm class is-invalid để bôi đỏ ô input
                    $('#diaChiUError').remove(); // Loại bỏ thông báo lỗi cũ nếu có
                    $('#diaChiU').after('<div id="diaChiUError" class="invalid-feedback">Vui lòng nhập địa chỉ.</div>'); // Thêm thông báo lỗi mới
                    return false;
                }

                return true;
            }

            if (!valid()) {
                return;
            }
            Swal.fire({
                title: 'Xác nhận',
                text: 'Xác nhận chỉnh sửa ?',
                icon: 'question',
                showCancelButton: true,
                confirmButtonText: 'Yes',
                cancelButtonText: 'Cancel'
            }).then((result) => {
                if (result.isConfirmed) {
                    let updatedEmployee = {
                        id: nhanV.id,
                        hoTen: hoten,
                        maNhanVien: manv,
                        matKhau: matKhau,
                        tenNhanVien: tennv,
                        email: email,
                        diaChi: diaChi,
                        gioiTinh: gioiTinh,
                        soDienThoai: sdt,
                        trangThai: trangThai,
                        ngaySinh: ngaySinh,
                        imageNV: imageData
                    };
                    $.ajax({
                        url: 'http://localhost:8080/nhan-vien/update',
                        type: 'PUT',
                        contentType: 'application/json',
                        data: JSON.stringify(updatedEmployee),
                        success: function (response) {
                            if (response === true) {
                                showSuccessToast('Cập nhật  thành công');
                                $(`#tableNhanVien`).show();
                                loadTable(apiGetAll, '', currentPage, recordsPerPage);
                                $('#btnSubmitUpdate').hide();
                                $('#btnChangeUpdate').show();
                                $('#btnSubmitUpdate').show();
                                $(`#formAdd`).hide();
                                $(`#formUpdate`).hide();
                                $('#imageNVdetail').attr('src', 'https://cellphones.com.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg');
                                imageData = '';
                                $('#formUpdate').find('input').val('');
                            } else {
                                showErrorToast('Không thể cập nhật trạng thái');
                            }
                        },
                        error: function () {
                            showErrorToast('Đã xảy ra lỗi, không thể cập nhật trạng thái');
                        }
                    });
                } else if (result.dismiss === Swal.DismissReason.cancel) {
                }
            });

        });
    }
});



