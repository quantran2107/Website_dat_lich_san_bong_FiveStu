// File tối ưu giữ nguyên chức năng gốc
document.addEventListener("DOMContentLoaded", function () {
    showLoading();
    fetchDataAndRenderTable();
    hideLoading();
});

// Các biến và hàm giữ nguyên
document.addEventListener("DOMContentLoaded", function () {
    showLoading();
    fetchDataAndRenderTable();
    hideLoading();
});

let initialTableData = [];
let currentSearchQuery = "";
let currentSearchStartDate = "";
let currentSearchEndDate = "";
let currentDoiTuongApDung = null;
let currentHinhThucGiamGia = null;
let currentStatusFilter = null;


function fetchDataAndRenderTable(page = 0, size = 10) {
    let apiURL = `/api-phieu-giam-gia/search-and-filter?page=${page}&size=${size}`;

    if (currentSearchQuery) {
        apiURL += `&keyword=${encodeURIComponent(currentSearchQuery)}`;
    }
    if (currentSearchStartDate) {
        apiURL += `&ngayBatDau=${encodeURIComponent(currentSearchStartDate)}`;
    }
    if (currentSearchEndDate) {
        apiURL += `&ngayKetThuc=${encodeURIComponent(currentSearchEndDate)}`;
    }
    if (currentDoiTuongApDung !== null) {
        apiURL += `&doiTuongApDung=${currentDoiTuongApDung}`;
    }
    if (currentHinhThucGiamGia !== null) {
        apiURL += `&hinhThucGiamGia=${currentHinhThucGiamGia}`;
    }
    if (currentStatusFilter) {
        apiURL += `&trangThai=${encodeURIComponent(currentStatusFilter)}`;
    }


    fetch(apiURL)
        .then(response => response.json())
        .then(data => {
            renderTable(data, page, size);
        })
}

function renderTable(data) {
    const tableData = data.content || data;
    if (!Array.isArray(tableData)) {
        return;
    }
    initialTableData = tableData;
    const tableBody = document.getElementById('tableBody');
    tableBody.innerHTML = '';

    tableData.forEach((phieuGiamGia, index) => {
        const mucGiamText = phieuGiamGia.hinhThucGiamGia ? `${phieuGiamGia.mucGiam} %` : `${phieuGiamGia.mucGiam ? phieuGiamGia.mucGiam.toLocaleString() : 0} VND`;
        const ngayBatDauFormatted = new Date(phieuGiamGia.ngayBatDau).toLocaleDateString();
        const ngayKetThucFormatted = new Date(phieuGiamGia.ngayKetThuc).toLocaleDateString();
        const now = new Date();
        const ngayKetThuc = new Date(phieuGiamGia.ngayKetThuc);
        const isSwitchChecked = (phieuGiamGia.trangThai === 'Sắp diễn ra' || phieuGiamGia.trangThai === 'Đang diễn ra')
            && phieuGiamGia.soLuong > 0 && ngayKetThuc >= now;
        const isSwitchHidden = ngayKetThuc < now;
        const row = `
            <tr>
                <td>${index + 1}</td>
                <td class="maPhieuGiamGia">${phieuGiamGia.maPhieuGiamGia}</td>
                <td class="tenPhieuGiamGia">${phieuGiamGia.tenPhieuGiamGia}</td>
                <td class="doiTuongApDung">
                    <span class="${phieuGiamGia.doiTuongApDung ? 'custom-4' : 'custom-1'}" style="width: 135px;">${phieuGiamGia.doiTuongApDung == false ? "Công khai" : "Cá nhân"}</span>
                </td>
                <td class="mucGiam">${mucGiamText}</td>
                <td class="soLuong">${phieuGiamGia.soLuong}</td>
                <td class="thoiGian">${ngayBatDauFormatted} - ${ngayKetThucFormatted}</td>
                <td class="doiTuongApDung">
                    <span style="width: 135px;" class="${phieuGiamGia.trangThai === 'Đã kết thúc' ? 'custom-3' : phieuGiamGia.trangThai === 'Sắp diễn ra' ? 'custom-1' : 'custom-4'}">
                        ${phieuGiamGia.trangThai}
                    </span>
                </td>
                <td style="display: flex; text-align: center; margin-right: 0px;">
                    <button class="btn btn-outline-success" type="button" data-toggle="modal" style="justify-content: center" onclick="showUpdate(${phieuGiamGia.id})" title="Sửa">
                        <span class="fe fe-edit-3"></span>
                    </button>
                   <div class="custom-control custom-switch" ${isSwitchHidden ? 'style="display: none;"' : ''}>
                        <input type="checkbox" class="custom-control-input" id="switch-${phieuGiamGia.id}" 
                               data-id="${phieuGiamGia.id}" ${isSwitchChecked ? 'checked' : ''} 
                               onchange="updateVoucherStatus(this)" title="Sửa trạng thái"
                               ${!isSwitchChecked ? 'disabled' : ''}> <!-- Disable switch khi tắt -->
                        <label class="custom-control-label" for="switch-${phieuGiamGia.id}"></label>
                    </div>
                </td>
            </tr>
        `;
        tableBody.insertAdjacentHTML('beforeend', row);
    });

    const paginationElement = document.getElementById('pagination');
    paginationElement.innerHTML = '';

    const totalPages = data.totalPages;
    const currentPage = data.number;

    // Nút "Lùi"
    if (currentPage > 0) {
        const previousButton = `<li class="page-item"><a class="page-link" href="#" onclick="fetchDataAndRenderTable(${currentPage - 1}, ${data.size})" style="color: green"><</a></li>`;
        paginationElement.insertAdjacentHTML('beforeend', previousButton);
    }

    // Dropdown để chọn trang
    const selectWrapper = document.createElement('li');
    selectWrapper.className = 'page-item';
    const select = document.createElement('select');
    select.className = 'custom-select'; // Thêm margin để tạo khoảng cách với các nút
    for (let i = 0; i < totalPages; i++) {
        const option = document.createElement('option');
        option.value = i;
        option.textContent = `Trang ${i + 1}`;
        if (i === currentPage) {
            option.selected = true; // Đánh dấu trang hiện tại
        }
        select.appendChild(option);
    }
    select.addEventListener('change', (event) => {
        const selectedPage = parseInt(event.target.value);
        fetchDataAndRenderTable(selectedPage, data.size); // Gọi hàm fetch dữ liệu trang mới
    });
    selectWrapper.appendChild(select);
    paginationElement.appendChild(selectWrapper);

    // Nút "Tiến"
    if (currentPage < totalPages - 1) {
        const nextButton = `<li class="page-item"><a class="page-link" href="#" onclick="fetchDataAndRenderTable(${currentPage + 1}, ${data.size})" style="color: green">></a></li>`;
        paginationElement.insertAdjacentHTML('beforeend', nextButton);
    }
}

function searchByDate() {
    currentSearchStartDate = document.getElementById('searchStartDate').value;
    currentSearchEndDate = document.getElementById('searchEndDate').value;
    fetchDataAndRenderTable();
}

function showLoading() {
    document.getElementById('loadingOverlay').classList.remove('loading-hidden');
}

function hideLoading() {
    document.getElementById('loadingOverlay').classList.add('loading-hidden');
}

function searchPGG(query) {
    currentSearchQuery = query.trim() === "" ? null : query;
    fetchDataAndRenderTable();
}

function setDoiTuongApDung(doiTuongApDung) {
    currentDoiTuongApDung = doiTuongApDung;
    updateButtonContent('actionMenuButton1', doiTuongApDung ? 'Cá nhân' : 'Công khai');
    fetchDataAndRenderTable();
}

function setHinhThucGiamGia(hinhThucGiamGia) {
    currentHinhThucGiamGia = hinhThucGiamGia;
    updateButtonContent('actionMenuButton2', hinhThucGiamGia ? 'Giảm theo %' : 'Giảm theo VND');
    fetchDataAndRenderTable();
}

function setStatusFilter(statusFilter) {
    currentStatusFilter = statusFilter;
    updateButtonContent('actionMenuButton3', statusFilter);
    fetchDataAndRenderTable();
}

function updateButtonContent(buttonId, content) {
    const button = document.getElementById(buttonId);
    if (button) {
        button.textContent = content;
    }
}

// Hàm resetFilters để reset các giá trị của các trường lọc về giá trị mặc định
function resetFilters() {
    document.getElementById("search").value = ""; // Reset trường tìm kiếm
    document.getElementById("searchStartDate").value = ""; // Reset trường ngày bắt đầu
    document.getElementById("searchEndDate").value = ""; // Reset trường ngày kết thúc

    // Đặt lại các biến lưu trữ filter về giá trị mặc định
    currentSearchQuery = "";
    currentSearchStartDate = "";
    currentSearchEndDate = "";
    currentDoiTuongApDung = null;
    currentHinhThucGiamGia = null;
    currentStatusFilter = null;

    // Đặt lại nội dung của các dropdown về mặc định
    updateButtonContent('actionMenuButton1', 'Kiểu ');
    updateButtonContent('actionMenuButton2', 'Loại ');
    updateButtonContent('actionMenuButton3', 'Trạng thái ');

    fetchDataAndRenderTable(); // Gọi lại hàm để fetch và render lại bảng dữ liệu
}

//Ket thuc phan trang

//Notification
function showSuccessToast(message) {
    Toastify({
        text: message,
        duration: 3000,
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
        duration: 3000,
        gravity: "top",
        position: "right",
        style: {
            background: "#f44336",
        },
        stopOnFocus: true
    }).showToast();
}

// Confirm  SweetAlert2
//Update trang thai
function updateVoucherStatus(checkbox) {
    const idPhieuGiamGia = checkbox.dataset.id;
    const isChecked = checkbox.checked;
    if (!checkbox.id.startsWith('switch-')) {
        // Nếu không phải là switch, không thực hiện
        return;
    }
    // Confirm action using SweetAlert2
    Swal.fire({
        title: 'Xác nhận',
        text: 'Bạn chắc chắn muốn kết thúc phiếu giảm giá?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Có',
        cancelButtonText: 'Không'
    }).then((result) => {
        if (result.isConfirmed) {
            // Update status
            fetch(`/api-phieu-giam-gia/${idPhieuGiamGia}`)
                .then(response => response.json())
                .then(data => {
                    const currentStatus = data.trangThai;

                    if ((currentStatus === 'Sắp diễn ra' || currentStatus === 'Đang diễn ra') && !isChecked) {

                        fetch(`http://localhost:8080/api-phieu-giam-gia-chi-tiet/${idPhieuGiamGia}/ket-thuc`, {
                            method: 'PUT',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                        })
                            .then(response => {
                                if (!response.ok) {
                                    throw new Error('Failed to update status');
                                }
                                fetchDataAndRenderTable();
                            })
                            .then(data => {
                                Swal.fire({
                                    title: 'Cập nhật trạng thái thành công',
                                    text: 'Cập nhật trạng thái thành công',
                                    icon: 'success'
                                });
                                showSuccessToast('Cập nhật trạng thái thành công');
                                // Refresh table data
                                fetchDataAndRenderTable();
                            })
                            .catch(error => {
                                Swal.fire({
                                    title: 'Error',
                                    text: 'Không thể cập nhật trạng thái',
                                    icon: 'error'
                                });
                                showErrorToast('Không thể cập nhật trạng thái');
                                fetchDataAndRenderTable();
                            });
                    } else {
                    }
                })
                .catch(error => {
                    Swal.fire({
                        title: 'Error',
                        text: 'Failed to fetch current status.',
                        icon: 'error'
                    });
                    fetchDataAndRenderTable();
                });
        } else if (result.dismiss === Swal.DismissReason.cancel) {
            fetchDataAndRenderTable();
        }
    });
}

//Add

var selectedButton = null;

function selectButton(type) {
    if (type === 'VND') {
        document.getElementById('hinhThucGiamGiaVND').checked = true;
        selectedButton = 'VND';
        updateTextColor('VND');
    } else if (type === 'Percent') {
        document.getElementById('hinhThucGiamGiaPercent').checked = true;
        selectedButton = 'Percent';
        updateTextColor('Percent');
    }
}

function updateTextColor(type) {
    var btnVND = document.getElementById('btnVND');
    var btnPercent = document.getElementById('btnPercent');
    const giaTriToiDa = document.getElementById('giaTriToiDa');
    // Reset previous styles
    btnVND.style.color = '';
    btnPercent.style.color = '';

    // Set new styles
    if (type === 'VND') {
        btnVND.style.color = 'green';
        btnVND.style.fontWeight = 'bold';
        giaTriToiDa.value = '';
        giaTriToiDa.disabled = true;
    } else if (type === 'Percent') {
        btnPercent.style.color = 'green';
        btnPercent.style.fontWeight = 'bold';
        giaTriToiDa.disabled = false;
    }
}

async function checkDuplicateMaPhieuGiamGia(maPhieuGiamGia) {
    const response = await fetch(`/api-phieu-giam-gia/check-duplicate/${encodeURIComponent(maPhieuGiamGia)}`);
    if (!response.ok) {
        throw new Error('Không thể kiểm tra mã phiếu giảm giá');
    }
    return await response.json();
}

function showAddForm() {
    const cardBody = document.getElementById('tableCardBody');
    const cardBody1 = document.getElementById('tableCardBody1');
    cardBody.style.display = 'none';

    // Khởi tạo form
    renderAddForm(cardBody1);

    // Mặc định chọn "Công khai" khi mở modal
    const doiTuongTatCa = document.getElementById('doiTuongTatCa');
    if (doiTuongTatCa) {
        doiTuongTatCa.checked = true;
        document.getElementById('soLuong').disabled = false; // Enable ô số lượng
    }

    // Thêm sự kiện cho form và trạng thái
    setupEventListeners();
    handleAddFormSubmit()
    fetchKhachHang('', 1, 5); // Load dữ liệu khách hàng
}


function renderAddForm(container) {
    const formHTML = `
        <form id="addForm">
            <div class="row">
                ${renderLeftTable()}
                ${renderRightCustomerTable()}
            </div>
            <button type="submit" class="btn btn-success" >Lưu</button>
            <button type="button" class="btn btn-secondary" onclick="cancelAdd()">Hủy</button>
        </form>
    `;
    container.innerHTML = formHTML;
}

function renderLeftTable() {
    return `
        <div class="col-md-6">
        <div class="form-row">
        <input type="text" class="form-control" id="id" placeholder="Tên" hidden>
        <div class="form-group col-md-6">
        <label for="maPhieuGiamGia" name="maPhieuGiamGia">Mã phiếu giảm giá</label>
        <input type="text" class="form-control" id="maPhieuGiamGia" placeholder="Nhập mã hoặc tự động tạo mã">
        <span id="maPhieuGiamGiaError" class="text-danger"></span>
        </div>
        <div class="form-group col-md-6">
        <label for="tenPhieuGiamGia" name="tenPhieuGiamGia">Tên phiếu giảm giá</label>
        <input type="text" class="form-control" id="tenPhieuGiamGia" placeholder="Tên">
        <span id="tenPhieuGiamGiaError" class="text-danger"></span>
        </div>
        </div>
        <div class="form-row">
        <div class="form-group col-md-6">
        <label for="mucGiam" name="mucGiam">Mức giảm</label>
        <div class="input-group">
        <input type="text" class="form-control" id="mucGiam" placeholder="Mức giảm" style="padding-right: 40px;">
        <span class="input-group-btn">
        <label class="btn" id="btnVND" onclick="selectButton('VND')" style="border: 1px  #e9ecef;">
        $<input type="radio" name="hinhThucGiamGia" id="hinhThucGiamGiaVND" style="display: none; color: #0a0c0d" value="false">
        </label>
        <label class="btn" id="btnPercent" onclick="selectButton('Percent')" style="border: 1px  #e9ecef;">
        %<input type="radio" name="hinhThucGiamGia" id="hinhThucGiamGiaPercent" style="display: none; color: #0a0c0d" value="true">
        </label>
        </span>
        </div>
        <span id="mucGiamError" class="text-danger"></span>
        </div>
        <div class="form-group col-md-6">
        <label for="giaTriToiDa">Giá trị tối đa</label>
        <div class="input-group mb-3">
        <input type="text" class="form-control" id="giaTriToiDa" placeholder="Giá trị tối đa">
        <span class="input-group-text">$</span>
        </div>
        <span id="giaTriToiDaError" class="text-danger"></span>
        </div>
        </div>
        <div class="form-row">
        <div class="form-group col-md-6">
        <label for="soLuong">Số lượng</label>
        <div class="input-group mb-3">
        <input type="text" class="form-control" id="soLuong" placeholder="Số lượng" >
        <span class="input-group-text">#</span>
        </div>
        <span id="soLuongError" class="text-danger"></span>
        </div>
        <div class="form-group col-md-6">
        <label for="dieuKienSuDung">Điều kiện tối thiểu</label>
        <div class="input-group mb-3">
        <input type="text" class="form-control" id="dieuKienSuDung" placeholder="Điều kiện tối thiểu">
        <span class="input-group-text">$</span>
        </div>
        <span id="dieuKienSuDungError" class="text-danger"></span>
        </div>
        </div>
        <div class="form-row">
        <div class="form-group col-md-6">
        <label for="ngayBatDau">Ngày bắt đầu</label>
        <input type="date" class="form-control" id="ngayBatDau" placeholder="Ngày bắt đầu">
        <span id="ngayBatDauError" class="text-danger"></span>

        </div>
        <div class="form-group col-md-6">
        <label for="ngayKetThuc">Ngày kết thúc</label>
        <input type="date" class="form-control" id="ngayKetThuc" placeholder="Ngày kết thúc">
        <span id="ngayKetThucError" class="text-danger"></span>

        </div>
        </div>
        <div class="form-row">
        <div class="form-group col-md-12">
        <div class="form-check form-check-inline">
        <input class="form-check-input" type="radio" name="doiTuongApDung" id="doiTuongCaNhan" value="true">
        <label class="form-check-label" for="doiTuongCaNhan">Cá nhân</label>
        </div>
        <div class="form-check form-check-inline">
        <input class="form-check-input" type="radio" name="doiTuongApDung" id="doiTuongTatCa" value="false">
        <label class="form-check-label" for="doiTuongTatCa">Công khai</label>
        </div>
        </div>
        </div>
        <div class="form-row">
        <div class="form-group col-md-12">
        <label for="ghiChu">Ghi chú</label>
        <div class="input-group mb-3">
        <input type="text" class="form-control" id="ghiChu" placeholder="Ghi chú">
        </div>
        </div>
        </div>
        <div class="form-row">
        <div class="form-group col-md-12">
        <label for="trangThai">Trạng thái</label>
        <div class="input-group mb-3">
        <input type="text" class="form-control" id="trangThai" disabled>
        <input type="text" class="form-control" id="deletedAt" hidden>
        </div>
        </div>
        </div>
        </div>
    `;
}

function renderRightCustomerTable() {
    return `
        <div class="col-md-6" id="tableBenPhai"  style="display: none;">
        <label class="sr-only" for="search">Tìm kiếm</label>
        <input class="form-control mr-5" id="searchKH"
        placeholder="Tìm kiếm theo mã,tên,sdt"
        type="text" style="margin-top: 33px;display: block; width: 450px" >
        <span id="khachHangError" class="text-danger"></span>
        <table class="table mt-3" id="tableKhachHangContainer">
        <thead>
        <tr>
        <th>#</th>
        <th>Mã</th>
        <th>Tên</th>
        <th>SDT</th>
        <th>Email</th>
        <th>
        <span id="khachHangError" class="text-danger"></span>
        </th>
        </tr>
        </thead>
        <tbody id="khachHangTableBody">
            <!-- Danh sách khách hàng sẽ được thêm vào đây -->
        </tbody>
        </table>
        <div id="paginationContainer" class="pagination-container "
        style="display: block;text-align: center"></div>
        </div>
    `;
}

function setupEventListeners() {
    const ngayBatDau = document.getElementById('ngayBatDau');
    const ngayKetThuc = document.getElementById('ngayKetThuc');
    const doiTuongCaNhan = document.getElementById('doiTuongCaNhan');
    const doiTuongTatCa = document.getElementById('doiTuongTatCa');
    const tableBenPhai = document.getElementById('tableBenPhai');
    const soLuongInput = document.getElementById('soLuong');
    const searchKH = document.getElementById('searchKH');
    let currentPage = 1;
    const pageSize = 5;

    // Gán sự kiện update trạng thái
    ngayBatDau.addEventListener('change', updateTrangThai);
    ngayKetThuc.addEventListener('change', updateTrangThai);
    soLuongInput.addEventListener('change', updateTrangThai);

    // Gán sự kiện cho radio buttons
    doiTuongTatCa.addEventListener('change', function () {
        if (doiTuongTatCa.checked) {
            tableBenPhai.style.display = 'none'; // Ẩn bảng khách hàng
            soLuongInput.value = "";
            soLuongInput.disabled = false; // Cho phép chỉnh sửa số lượng
        }
    });

    doiTuongCaNhan.addEventListener('change', function () {
        if (doiTuongCaNhan.checked) {
            tableBenPhai.style.display = 'block'; // Hiển thị bảng khách hàng
            soLuongInput.disabled = true; // Không cho phép chỉnh sửa số lượng
            calculateSoLuongFromCheckboxes();
        }
        fetchKhachHang(searchKH.value, currentPage, pageSize);
    });

    // Xử lý ô tìm kiếm khách hàng
    searchKH.addEventListener('input', function () {
        const query = searchKH.value.trim();
        fetchKhachHang(query, 1, pageSize);
    });

    // Hàm tính số lượng checkbox được chọn
    function calculateSoLuongFromCheckboxes() {
        const selectedCheckboxes = document.querySelectorAll('input.item-checkbox:checked');
        soLuongInput.value = selectedCheckboxes.length;
    }

    // Sự kiện checkbox thay đổi
    document.addEventListener('change', (event) => {
        if (doiTuongCaNhan.checked && event.target.classList.contains('item-checkbox')) {
            calculateSoLuongFromCheckboxes();
        }
    });
}

function updateTrangThai() {
    const currentDate = new Date();
    const startDate = new Date(ngayBatDau.value);
    const endDate = new Date(ngayKetThuc.value);
    const soLuong = parseInt(document.getElementById('soLuong').value);
    let trangThai = '';
    if (currentDate <= startDate) {
        trangThai = 'Sắp diễn ra';
    } else if (currentDate > endDate) {
        trangThai = 'Đã kết thúc';
    } else {
        trangThai = 'Đang diễn ra';
    }
    if (soLuong === 0) {
        trangThai = 'Đã kết thúc';
    }
    document.getElementById('trangThai').value = trangThai;
}

function fetchKhachHang(query, page, pageSize) {
    fetch('http://localhost:8080/khach-hang/hien-thi')
        .then(response => response.json())
        .then(data => {
            // Lọc dữ liệu khách hàng theo query
            const filteredData = data.filter(khachHang =>
                khachHang.hoVaTen.toLowerCase().includes(query.toLowerCase()) ||
                khachHang.maKhachHang.toLowerCase().includes(query.toLowerCase()) ||
                khachHang.soDienThoai.includes(query) ||
                khachHang.email.toLowerCase().includes(query.toLowerCase())
            );

            // Phân trang dữ liệu lọc được và render bảng khách hàng và phân trang
            const paginatedData = paginate(filteredData, page, pageSize);
            renderKhachHangTable(paginatedData);
            renderPagination(filteredData.length, pageSize, page);

            // Hiển thị bảng và phân trang nếu có kết quả tìm kiếm và đang chọn tìm kiếm theo cá nhân
            if (filteredData.length > 0 && doiTuongCaNhan.checked) {
                tableBenPhai.style.display = 'block';
            } else if (filteredData.length === 0 && doiTuongCaNhan.checked) {
                // Nếu không có kết quả tìm kiếm, ẩn bảng và phân trang
                tableBenPhai.style.display = 'none';
            }
        })
        .catch(error => console.error('Error fetching khach hang:', error));
}

// Hàm phân trang dữ liệu
function paginate(data, page, pageSize) {
    const start = (page - 1) * pageSize;
    const end = page * pageSize;
    return data.slice(start, end);
}

// Hàm render bảng khách hàng
function renderKhachHangTable(data) {
    const tableBody = document.getElementById('khachHangTableBody');
    tableBody.innerHTML = '';

    data.forEach(khachHang => {
        const row = document.createElement('tr');
        row.innerHTML = `
        <td>
        <div class="custom-control custom-checkbox">
        <input type="checkbox" class="custom-control-input item-checkbox" id="checkbox-${khachHang.id}" data-id="${khachHang.id}">
        <label class="custom-control-label" for="checkbox-${khachHang.id}"></label>
        </div>
        </td>
        <td>${khachHang.maKhachHang}</td>
        <td>${khachHang.hoVaTen}</td>
        <td>${khachHang.soDienThoai}</td>
        <td>${khachHang.email}</td>
        `;
        tableBody.appendChild(row);
    });
}

// Hàm render phân trang
function renderPagination(totalItems, pageSize, currentPage) {
    const paginationContainer = document.getElementById('paginationContainer');
    paginationContainer.innerHTML = '';

    const totalPages = Math.ceil(totalItems / pageSize);

    for (let i = 1; i <= totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.className = 'btn btn-success';
        pageButton.style = 'margin-right: 10px';
        pageButton.innerText = i;

        // Xử lý sự kiện click của nút phân trang
        pageButton.onclick = function (event) {
            event.preventDefault(); // Ngăn chặn hành vi mặc định của trình duyệt
            currentPage = i;
            fetchKhachHang(searchKH.value, currentPage, pageSize);
        };

        paginationContainer.appendChild(pageButton);
    }
}

// Hàm lấy danh sách ID và email khách hàng đã chọn
async function getSelectedIdKhachHangs() {
    const doiTuongTatCa = document.getElementById('doiTuongTatCa');
    if (doiTuongTatCa.checked) {
        // Lấy tất cả khách hàng nếu chọn Công khai
        try {
            // const response = await fetch('http://localhost:8080/khach-hang/hien-thi');
            // if (!response.ok) throw new Error('Không thể lấy danh sách khách hàng');
            // const data = await response.json();
            // return data.map(khachHang => khachHang.id);
            return [];
        } catch (error) {

            return [];
        }
    } else {
        // Lấy các khách hàng được chọn từ checkbox
        const checkboxes = document.querySelectorAll('input.item-checkbox:checked');
        return Array.from(checkboxes).map(checkbox => parseInt(checkbox.dataset.id, 10));
    }
}


async function validateForm() {
    // Lấy các giá trị từ các trường input
    const tenPhieuGiamGia = document.getElementById('tenPhieuGiamGia').value.trim();
    const mucGiam = document.getElementById('mucGiam').value.trim();
    const giaTriToiDa = document.getElementById('giaTriToiDa').value.trim();
    const soLuong = document.getElementById('soLuong').value.trim();
    const dieuKienSuDung = document.getElementById('dieuKienSuDung').value.trim();
    const ngayBatDau = document.getElementById('ngayBatDau').value.trim();
    const ngayKetThuc = document.getElementById('ngayKetThuc').value.trim();
    const hinhThucGiamGiaVND = document.getElementById('hinhThucGiamGiaVND');
    const hinhThucGiamGiaPercent = document.getElementById('hinhThucGiamGiaPercent');
    const doiTuongCaNhan = document.getElementById('doiTuongCaNhan');
    const tableKhachHangContainer = document.getElementById('tableKhachHangContainer');
    const selectedCheckboxes = document.querySelectorAll('#tableKhachHangContainer input[type="checkbox"]:checked');


    // Đặt lại thông báo lỗi
    document.getElementById('tenPhieuGiamGiaError').innerText = '';
    document.getElementById('mucGiamError').innerText = '';
    document.getElementById('giaTriToiDaError').innerText = '';
    document.getElementById('soLuongError').innerText = '';
    document.getElementById('dieuKienSuDungError').innerText = '';
    document.getElementById('ngayBatDauError').innerText = '';
    document.getElementById('ngayKetThucError').innerText = '';
    document.getElementById('khachHangError').innerText = '';

    let isValid = true;

    // Kiểm tra các trường bắt buộc
    const validRadio = hinhThucGiamGiaVND.checked || hinhThucGiamGiaPercent.checked;

    if (!tenPhieuGiamGia) {
        document.getElementById('tenPhieuGiamGiaError').innerText = 'Tên phiếu giảm giá không được để trống';
        isValid = false;
    }
    if (!validRadio) {
        document.getElementById('mucGiamError').innerText = "Vui lòng chọn một hình thức giảm giá";
        isValid = false;
    } else if (!mucGiam) {
        document.getElementById('mucGiamError').innerText = 'Mức giảm không được để trống';
        isValid = false;
    } else if (hinhThucGiamGiaPercent.checked && parseFloat(mucGiam) > 100) {
        document.getElementById('mucGiamError').innerText = "Mức giảm phải nhỏ hơn 100%";
        isValid = false;
    }

    if (hinhThucGiamGiaPercent.checked && !giaTriToiDa) {
        document.getElementById('giaTriToiDaError').innerText = 'Giá trị tối đa không được để trống';
        isValid = false;
    }

    if (soLuong !== "") { // Chỉ kiểm tra nếu không trống
        if (!Number.isInteger(Number(soLuong)) || Number(soLuong) <= 0) {
            document.getElementById('soLuongError').innerText = "Số lượng phải là số nguyên dương";
            isValid = false;
        }
    }
    if (!dieuKienSuDung) {
        document.getElementById('dieuKienSuDungError').innerText = 'Điều kiện sử dụng không được để trống';
        isValid = false;
    }
    if (!ngayBatDau) {
        document.getElementById('ngayBatDauError').innerText = 'Ngày bắt đầu không được để trống';
        isValid = false;
    }
    if (!ngayKetThuc) {
        document.getElementById('ngayKetThucError').innerText = 'Ngày kết thúc không được để trống';
        isValid = false;
    }

    // Kiểm tra nếu ngày bắt đầu lớn hơn ngày kết thúc
    if (new Date(ngayBatDau) > new Date(ngayKetThuc)) {
        document.getElementById('ngayKetThucError').innerText = 'Ngày kết thúc phải sau ngày bắt đầu';
        isValid = false;
    }
    // Lấy ngày hiện tại và loại bỏ thời gian
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    if (new Date(ngayBatDau) < today) {
        document.getElementById('ngayBatDauError').innerText = 'Ngày bắt đầu không được nhỏ hơn ngày hiện tại';
        isValid = false;
    }

    if (doiTuongCaNhan.checked && selectedCheckboxes.length === 0) {
        const khachHangError = document.getElementById('khachHangError');
        khachHangError.innerText = 'Vui lòng chọn ít nhất một khách hàng.';
        isValid = false;
    }
    return isValid;
}

function cancelAdd() {
    const tableBodyCard = document.getElementById('tableCardBody');
    const tableBodyCard1 = document.getElementById('tableCardBody1');

    // Hiển thị lại card body ban đầu
    tableBodyCard.style.display = 'block';

    // Xóa nội dung và đảm bảo rằng form vẫn nằm yên trong hàng
    tableBodyCard1.innerHTML = `
        <div class="toolbar row mb-3 mt-3">
                                <div class="col-md-12">
                                    <form class="form-inline" id="filterForm"
                                          style="display: flex; justify-content: space-between; align-items: center;">
                                        <!-- Search -->
                                        <div class="form-group" style="flex-grow: 1;">
                                            <label for="search">Tìm kiếm</label>
                                            <input class="form-control ml-3 mr-5" id="search"
                                                   oninput="searchPGG(this.value)"
                                                   placeholder="Tìm kiếm theo mã hoặc tên" style="width: 400px;"
                                                   type="text">
                                        </div>
                                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                            <button class="btn btn-success"
                                                    data-service-type="do_thue"
                                                    onclick="showAddForm(this)"
                                                    title="Thêm" type="button">
                                                <span class="fe fe-plus"></span>
                                            </button>
                                        </div>
                                    </form>
                                </div>

                                <div class="col-md-12 mt-4">
                                    <form class="form-inline" id="filterDateForm"
                                          style="display: flex; justify-content: space-between; align-items: center;">
                                        <div class="form-group d-flex" style="flex-grow: 1;">
                                            <label for="searchStartDate">Từ</label>
                                            <input class="form-control ml-3 mr-3" id="searchStartDate"
                                                   onchange="searchStartDate(this.value)"
                                                   style="width: 163px;"
                                                   type="date">
                                            <label for="searchEndDate">Đến</label>
                                            <input class="form-control" id="searchEndDate"
                                                   onchange="searchEndDate(this.value)"
                                                   style="width: 163px; margin-left: 15px;"
                                                   type="date">
                                            <div class="dropdown">
                                                <button aria-expanded="false"
                                                        aria-haspopup="true"
                                                        class="btn btn-outline-success ml-3 mr-3 dropdown-toggle"
                                                        data-toggle="dropdown" id="actionMenuButton1"
                                                        type="button">Kiểu
                                                </button>
                                                <div aria-labelledby="actionMenuButton1" class="dropdown-menu">
                                                    <a class="dropdown-item" href="#"
                                                       onclick="setDoiTuongApDung(true)">Cá nhân</a>
                                                    <a class="dropdown-item" href="#"
                                                       onclick="setDoiTuongApDung(false)">Công khai</a>
                                                </div>
                                            </div>
                                            <div class="dropdown mr-3">
                                                <button aria-expanded="false"
                                                        aria-haspopup="true"
                                                        class="btn btn-outline-success dropdown-toggle"
                                                        data-toggle="dropdown" id="actionMenuButton2"
                                                        type="button">Loại
                                                </button>
                                                <div aria-labelledby="actionMenuButton2" class="dropdown-menu">
                                                    <a class="dropdown-item" href="#"
                                                       onclick="setHinhThucGiamGia(true)">Giảm theo %</a>
                                                    <a class="dropdown-item" href="#"
                                                       onclick="setHinhThucGiamGia(false)">Giảm theo VND</a>
                                                </div>
                                            </div>
                                            <div class="dropdown mr-3">
                                                <button aria-expanded="false"
                                                        aria-haspopup="true"
                                                        class="btn btn-outline-success dropdown-toggle"
                                                        data-toggle="dropdown" id="actionMenuButton3"
                                                        type="button">Trạng thái
                                                </button>
                                                <div aria-labelledby="actionMenuButton3" class="dropdown-menu"
                                                     id="statusFilter">
                                                    <a class="dropdown-item" href="#"
                                                       onclick="setStatusFilter('Đã kết thúc')">Đã kết thúc</a>
                                                    <a class="dropdown-item" href="#"
                                                       onclick="setStatusFilter('Sắp diễn ra')">Sắp diễn ra</a>
                                                    <a class="dropdown-item" href="#"
                                                       onclick="setStatusFilter('Đang diễn ra')">Đang diễn ra</a>
                                                </div>
                                            </div>
                                            <button class="btn btn-outline-success"
                                                    onclick="exportToExcel()"
                                                    title="Xuất Excel" type="button">
                                                <span class="fe fe-file-text"></span>
                                            </button>
                                        </div>
                                        <div class="form-group d-flex gap-2">
                                            <button class="btn btn-primary" onclick="resetFilters()"
                                                    title="Reset" type="button">
                                                <span class="fe fe-refresh-ccw"></span>
                                            </button>
                                        </div>
                                    </form>
                                </div>


                            </div>
        `;
    fetchDataAndRenderTable();
}

function getSelectedRadioValue(name) {
    const selectedRadio = document.querySelector(`input[name="${name}"]:checked`);
    return selectedRadio ? selectedRadio.value : ''; // Trả về giá trị của radio nếu có, ngược lại trả về chuỗi trống
}

async function handleAddFormSubmit() {
    const form = document.getElementById('addForm');

    form.addEventListener('submit', async function (event) {
        event.preventDefault();

        try {
            let isValid = await validateForm();

            // Lấy giá trị mã phiếu giảm giá
            const maPhieuGiamGia = document.getElementById('maPhieuGiamGia').value.trim();

            // Kiểm tra mã phiếu giảm giá trùng
            if (maPhieuGiamGia) {
                const isDuplicate = await checkDuplicateMaPhieuGiamGia(maPhieuGiamGia);
                if (isDuplicate) {
                    document.getElementById('maPhieuGiamGiaError').innerText = 'Mã phiếu giảm giá đã tồn tại';
                    isValid = false; // Gán isValid = false nếu mã trùng
                }
            }

            // Dừng lại nếu form không hợp lệ
            if (!isValid) {
                return; // Không thực hiện hành động thêm
            }

            const selectedIdKhachHangs = await getSelectedIdKhachHangs();
            const data = collectFormData(selectedIdKhachHangs);

            const confirmed = await Swal.fire({
                title: 'Xác nhận',
                text: 'Bạn chắc chắn muốn thêm phiếu giảm giá?',
                icon: 'question',
                showCancelButton: true,
                confirmButtonText: 'Có',
                cancelButtonText: 'Không'
            });

            if (confirmed.isConfirmed) {
                await savePhieuGiamGia(data, selectedIdKhachHangs);
                Swal.fire({
                    title: 'Thành công',
                    text: 'Thêm phiếu giảm giá thành công',
                    icon: 'success'
                });
                showSuccessToast('Thêm phiếu giảm giá thành công');
                cancelAdd();
            }
        } catch (error) {
            Swal.fire({
                title: 'Lỗi',
                text: error.message || 'Có lỗi xảy ra khi thêm phiếu giảm giá',
                icon: 'error'
            });
        }
    });
}

function collectFormData(selectedIdKhachHangs) {
    return {
        maPhieuGiamGia: document.getElementById('maPhieuGiamGia').value,
        tenPhieuGiamGia: document.getElementById('tenPhieuGiamGia').value,
        mucGiam: document.getElementById('mucGiam').value,
        hinhThucGiamGia: getSelectedRadioValue('hinhThucGiamGia'),
        giaTriToiDa: document.getElementById('giaTriToiDa').value,
        soLuong: document.getElementById('soLuong').value,
        dieuKienSuDung: document.getElementById('dieuKienSuDung').value,
        ngayBatDau: document.getElementById('ngayBatDau').value,
        ngayKetThuc: document.getElementById('ngayKetThuc').value,
        doiTuongApDung: getSelectedRadioValue('doiTuongApDung'),
        ghiChu: document.getElementById('ghiChu').value,
        trangThai: document.getElementById('trangThai').value,
        idKhachHangs: selectedIdKhachHangs,
    };
}

async function savePhieuGiamGia(data, selectedIdKhachHangs) {
    const response = await fetch('http://localhost:8080/api-phieu-giam-gia/save', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Không thể lưu phiếu giảm giá');
    }

    const phieuGiamGia = await response.json();
    const chiTietData = [];
    if (data.doiTuongApDung === 'true') { // Cá nhân
        selectedIdKhachHangs.forEach(idKhachHang => {
            chiTietData.push({idPhieuGiamGia: phieuGiamGia.id, idKhachHang: idKhachHang});
        });
    } else { // Công khai
        for (let i = 0; i < data.soLuong; i++) {
            chiTietData.push({idPhieuGiamGia: phieuGiamGia.id, idKhachHang: null});
        }
    }

    // Lưu chi tiết phiếu giảm giá
    for (const chiTiet of chiTietData) {
        const detailResponse = await fetch('http://localhost:8080/api-phieu-giam-gia-chi-tiet/save', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(chiTiet)
        });

        if (!detailResponse.ok) {
            const error = await detailResponse.json();
            throw new Error(error.message || 'Không thể lưu chi tiết phiếu giảm giá');
        }
    }
}

let selectedCustomerIds = []; // Lưu ID khách hàng được check
function renderUpdateForm(container) {
    const formHTML = `
        <form id="updateForm">
            <div class="row">
                ${renderLeftTable()}
                ${renderRightCustomerTable()}
            </div>
            <button type="submit" class="btn btn-success" >Lưu</button>
            <button type="button" class="btn btn-secondary" onclick="cancelAdd()">Hủy</button>
        </form>
    `;
    container.innerHTML = formHTML;
}

function showUpdate(id) {
    const cardBody = document.getElementById('tableCardBody');
    const cardBody1 = document.getElementById('tableCardBody1');
    cardBody.style.display = 'none';
    cardBody1.innerHTML = '';
    renderUpdateForm(cardBody1);

    fetch(`http://localhost:8080/api-phieu-giam-gia/${id}`)
        .then(response => response.json())
        .then(data => {
            // Điền dữ liệu vào form
            document.getElementById('id').value = data.id;
            document.getElementById('maPhieuGiamGia').value = data.maPhieuGiamGia;
            document.getElementById('tenPhieuGiamGia').value = data.tenPhieuGiamGia;
            document.getElementById('mucGiam').value = data.mucGiam;
            document.getElementById('giaTriToiDa').value = data.giaTriToiDa;
            document.getElementById('soLuong').value = data.soLuong;
            document.getElementById('dieuKienSuDung').value = data.dieuKienSuDung;
            document.getElementById('ngayBatDau').value = data.ngayBatDau;
            document.getElementById('ngayKetThuc').value = data.ngayKetThuc;
            document.getElementById('ghiChu').value = data.ghiChu;
            document.getElementById('trangThai').value = data.trangThai;
            document.getElementById('deletedAt').value = data.deletedAt;

            // Xử lý radio button
            if (data.doiTuongApDung) {
                document.getElementById('doiTuongCaNhan').checked = true;
                document.getElementById('tableBenPhai').style.display = 'block';
            } else {
                document.getElementById('doiTuongTatCa').checked = true;
                document.getElementById('soLuong').disabled = false;
            }

            // Xử lý hình thức giảm giá
            if (data.hinhThucGiamGia) {
                document.getElementById('hinhThucGiamGiaPercent').checked = true;
                selectButton('Percent');
            } else {
                document.getElementById('hinhThucGiamGiaVND').checked = true;
                selectButton('VND');
            }

            setupEventListenersUpdate();
            // Lấy danh sách khách hàng đã chọn
            handleUpdateFormSubmit();
            fetchCheckedCustomers(id);
        })
        .catch(error => console.error('Error fetching data:', error));
}

function fetchCheckedCustomers(id) {
    fetch(`http://localhost:8080/api-phieu-giam-gia-chi-tiet/pggct/${id}`)
        .then(response => response.json())
        .then(data => {
            selectedCustomerIds = data.map(item => item.idKhachHang); // Lưu ID khách hàng đã chọn
            fetchKhachHangUpdate('', 1, 5);
        })
        .catch(error => console.error('Error fetching customer details:', error));
}

function fetchKhachHangUpdate(query, page, pageSize) {
    fetch('http://localhost:8080/khach-hang/hien-thi')
        .then(response => response.json())
        .then(data => {
            const filteredData = data.filter(khachHang =>
                khachHang.hoVaTen.toLowerCase().includes(query.toLowerCase()) ||
                khachHang.maKhachHang.toLowerCase().includes(query.toLowerCase()) ||
                khachHang.soDienThoai.includes(query) ||
                khachHang.email.toLowerCase().includes(query.toLowerCase())
            );

            const paginatedData = paginate(filteredData, page, pageSize);
            renderKhachHangTableUpdate(paginatedData);
            renderPagination(filteredData.length, pageSize, page);
        });
}

function renderKhachHangTableUpdate(data) {
    const tableBody = document.getElementById('khachHangTableBody');
    tableBody.innerHTML = '';

    data.forEach(khachHang => {
        const isChecked = selectedCustomerIds.includes(khachHang.id);
        const row = `
            <tr>
                <td>
                    <div class="custom-control custom-checkbox">
                        <input type="checkbox" class="custom-control-input item-checkbox"
                               id="checkbox-${khachHang.id}" data-id="${khachHang.id}" ${isChecked ? 'checked' : ''}>
                        <label class="custom-control-label" for="checkbox-${khachHang.id}"></label>
                    </div>
                </td>
                <td>${khachHang.maKhachHang}</td>
                <td>${khachHang.hoVaTen}</td>
                <td>${khachHang.soDienThoai}</td>
                <td>${khachHang.email}</td>
            </tr>
        `;
        tableBody.insertAdjacentHTML('beforeend', row);
    });
}

function setupEventListenersUpdate() {
    const ngayBatDau = document.getElementById('ngayBatDau');
    const ngayKetThuc = document.getElementById('ngayKetThuc');
    const doiTuongCaNhan = document.getElementById('doiTuongCaNhan');
    const doiTuongTatCa = document.getElementById('doiTuongTatCa');
    const tableBenPhai = document.getElementById('tableBenPhai');
    const soLuongInput = document.getElementById('soLuong');
    const searchKH = document.getElementById('searchKH');
    let currentPage = 1;
    const pageSize = 5;

    // Gán sự kiện update trạng thái
    ngayBatDau.addEventListener('change', updateTrangThai);
    ngayKetThuc.addEventListener('change', updateTrangThai);
    soLuongInput.addEventListener('change', updateTrangThai);

    // Gán sự kiện cho radio buttons
    doiTuongTatCa.addEventListener('change', function () {
        if (doiTuongTatCa.checked) {
            tableBenPhai.style.display = 'none'; // Ẩn bảng khách hàng
            soLuongInput.value = "";
            soLuongInput.disabled = false; // Cho phép chỉnh sửa số lượng
        }
    });

    doiTuongCaNhan.addEventListener('change', function () {
        if (doiTuongCaNhan.checked) {
            tableBenPhai.style.display = 'block'; // Hiển thị bảng khách hàng
            soLuongInput.value = "0";
            soLuongInput.disabled = true; // Không cho phép chỉnh sửa số lượng
            calculateSoLuongFromCheckboxes();
        }
        fetchKhachHangUpdate(searchKH.value, currentPage, pageSize);
    });

    // Xử lý ô tìm kiếm khách hàng
    searchKH.addEventListener('input', function () {
        const query = searchKH.value.trim();
        fetchKhachHangUpdate(query, 1, pageSize);
    });

    // Hàm tính số lượng checkbox được chọn
    function calculateSoLuongFromCheckboxes() {
        const selectedCheckboxes = document.querySelectorAll('input.item-checkbox:checked');
        soLuongInput.value = selectedCheckboxes.length;
    }

    // Sự kiện checkbox thay đổi
    document.addEventListener('change', (event) => {
        if (doiTuongCaNhan.checked && event.target.classList.contains('item-checkbox')) {
            calculateSoLuongFromCheckboxes();
        }
    });
}

// Ham check id khach hang chua ton tai de them moi
async function checkChiTietPhieuGiamGia(idPhieuGiamGia, selectedIds) {
    try {
        const response = await fetch(`http://localhost:8080/api-phieu-giam-gia-chi-tiet/pggct/${idPhieuGiamGia}`);
        const data = await response.json();

        // Lọc các ID không tồn tại hoặc có deletedAt = true
        const notFoundOrDeletedIds = selectedIds.filter(id => {
            const existing = data.find(chiTiet => chiTiet.idKhachHang == id);
            return !existing || existing.deletedAt; // Không tồn tại hoặc bị xóa
        });

        return notFoundOrDeletedIds;
    } catch (error) {
        return [];
    }
}

// Hàm kiểm tra ID khách hàng đã tồn tại nhưng không có trong danh sách đã chọn
async function getNotSelectedIds(idPhieuGiamGia, selectedIds) {
    try {
        const response = await fetch(`http://localhost:8080/api-phieu-giam-gia-chi-tiet/pggct/${idPhieuGiamGia}`);
        const data = await response.json();

        // Lọc các ID có `deletedAt = false` nhưng không nằm trong `selectedIds`
        const notSelectedIds = data
            .filter(chiTiet => !selectedIds.includes(chiTiet.idKhachHang.toString()) && !chiTiet.deletedAt)
            .map(chiTiet => chiTiet.idKhachHang.toString());

        return notSelectedIds;
    } catch (error) {
        return [];
    }
}

async function handleUpdateFormSubmit() {
    const form = document.getElementById('updateForm');

    form.addEventListener('submit', async function (event) {
        event.preventDefault();

        // Gọi validateForm và dừng lại nếu không hợp lệ
        const isValid = await validateForm();

        console.log(isValid)
        if (!isValid) {
            return; // Dừng lại nếu form không hợp lệ
        }

        Swal.fire({
            title: 'Xác nhận',
            text: 'Bạn chắc chắn muốn update phiếu giảm giá?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Yes',
            cancelButtonText: 'Cancel'
        }).then(async (result) => {
            if (result.isConfirmed) {
                try {
                    // Thu thập dữ liệu từ form
                    const dataUpdate = {
                        id: document.getElementById('id').value,
                        tenPhieuGiamGia: document.getElementById('tenPhieuGiamGia').value,
                        maPhieuGiamGia: document.getElementById('maPhieuGiamGia').value,
                        mucGiam: document.getElementById('mucGiam').value,
                        giaTriToiDa: document.getElementById('giaTriToiDa').value,
                        soLuong: document.getElementById('soLuong').value,
                        dieuKienSuDung: document.getElementById('dieuKienSuDung').value,
                        ngayBatDau: document.getElementById('ngayBatDau').value,
                        ngayKetThuc: document.getElementById('ngayKetThuc').value,
                        doiTuongApDung: getSelectedRadioValue('doiTuongApDung'),
                        hinhThucGiamGia: getSelectedRadioValue('hinhThucGiamGia'),
                        ghiChu: document.getElementById('ghiChu').value,
                        deletedAt: document.getElementById('deletedAt').value,
                        trangThai: document.getElementById('trangThai').value
                    };

                    // Gửi yêu cầu cập nhật
                    const responseUpdate = await fetch(`http://localhost:8080/api-phieu-giam-gia/${dataUpdate.id}`, {
                        method: 'PUT',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(dataUpdate),
                    });

                    if (!responseUpdate.ok) {
                        const errorData = await responseUpdate.json();
                        throw new Error(`Error updating phiếu giảm giá: ${errorData.message}`);
                    }

                    // Xử lý logic nếu là "Cá nhân"
                    if (dataUpdate.doiTuongApDung === "true" || dataUpdate.doiTuongApDung === true) {
                        const selectedIdKhachHangs = await getSelectedIdKhachHangs(); // Lấy danh sách ID khách hàng đã chọn
                        const notSelectedIds = await getNotSelectedIds(dataUpdate.id, selectedIdKhachHangs); // Lấy danh sách ID không được chọn

                        // Xử lý các ID bị bỏ chọn (deletedAt = true)
                        for (const idKhachHang of notSelectedIds) {
                            await fetch(`http://localhost:8080/api-phieu-giam-gia-chi-tiet/${dataUpdate.id}/khach-hang/${idKhachHang}`, {
                                method: 'PUT',
                                headers: { 'Content-Type': 'application/json' },
                                body: JSON.stringify(true), // Cập nhật deletedAt = true
                            });
                        }

                        // Xử lý các ID mới được chọn lại (deletedAt = false)
                        for (const idKhachHang of selectedIdKhachHangs) {
                            const responseCheck = await fetch(
                                `http://localhost:8080/api-phieu-giam-gia-chi-tiet/${dataUpdate.id}/khach-hang/${idKhachHang}`
                            );

                            if (responseCheck.ok) {
                                const dataCheck = await responseCheck.json();
                                // Nếu tồn tại và `deletedAt = true`, cập nhật lại thành `false`
                                if (dataCheck.deletedAt) {
                                    await fetch(
                                        `http://localhost:8080/api-phieu-giam-gia-chi-tiet/${dataUpdate.id}/khach-hang/${idKhachHang}`,
                                        {
                                            method: 'PUT',
                                            headers: { 'Content-Type': 'application/json' },
                                            body: JSON.stringify(false), // Cập nhật lại trạng thái
                                        }
                                    );
                                }
                            } else if (responseCheck.status === 400) {
                                // Nếu chưa tồn tại, thêm mới phiếu giảm giá chi tiết
                                await fetch(
                                    `http://localhost:8080/api-phieu-giam-gia-chi-tiet/${dataUpdate.id}/chuyen-sang-ca-nhan`,
                                    {
                                        method: 'PUT',
                                        headers: { 'Content-Type': 'application/json' },
                                        body: JSON.stringify([idKhachHang]),
                                    }
                                );
                            } else {
                                throw new Error(`Error checking chi tiết phiếu giảm giá: ${responseCheck.status}`);
                            }
                        }
                    }

                    Swal.fire({
                        title: 'Thành công!',
                        text: 'Cập nhật phiếu giảm giá thành công!',
                        icon: 'success'
                    });
                    cancelAdd();
                } catch (error) {
                    Swal.fire({
                        title: 'Lỗi',
                        text: `Có lỗi xảy ra: ${error.message}`,
                        icon: 'error'
                    });
                }
            }
        });
    });
}
