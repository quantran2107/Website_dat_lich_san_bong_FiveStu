

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
    .catch(error => console.error('Error fetching data:', error));
}

    function renderTable(data) {
    const tableData = data.content || data;
    if (!Array.isArray(tableData)) {
    console.error('Invalid data format:', tableData);
    return;
}
    initialTableData = tableData;
    const tableBody = document.getElementById('tableBody');
    tableBody.innerHTML = '';

    tableData.forEach((phieuGiamGia, index) => {
    const mucGiamText = phieuGiamGia.hinhThucGiamGia ? `${phieuGiamGia.mucGiam} %` : `${phieuGiamGia.mucGiam} VND`;
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
                        data-id="${phieuGiamGia.id}" ${isSwitchChecked ? 'checked' : ''} onchange="updateVoucherStatus(this)"
                        title="Sửa trạng thái">
                        <label class="custom-control-label" for="switch-${phieuGiamGia.id}"></label>
                    </div>
                </td>
            </tr>
        `;
    tableBody.insertAdjacentHTML('beforeend', row);
});

    const paginationElement = document.getElementById('pagination');
    paginationElement.innerHTML = '';

    if (data.number > 0) {
    const previousButton = `<li class="page-item"><a class="page-link" href="#" onclick="fetchDataAndRenderTable(${data.number - 1}, ${data.size})"><</a></li>`;
    paginationElement.insertAdjacentHTML('beforeend', previousButton);
}

    for (let i = 0; i < data.totalPages; i++) {
    const pageButton = `<li class="page-item ${i === data.number ? 'active' : ''}"><a class="page-link" href="#" onclick="fetchDataAndRenderTable(${i}, ${data.size})">${i + 1}</a></li>`;
    paginationElement.insertAdjacentHTML('beforeend', pageButton);
}

    if (data.number < data.totalPages - 1) {
    const nextButton = `<li class="page-item"><a class="page-link" href="#" onclick="fetchDataAndRenderTable(${data.number + 1}, ${data.size})">></a></li>`;
    paginationElement.insertAdjacentHTML('beforeend', nextButton);
}
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

    // Đảm bảo rằng các hàm không nằm trong một phạm vi đóng gói
    window.searchStartDate = function (date) {
    console.log("Start Date:", date);
    currentSearchStartDate = date;
    fetchDataAndRenderTable();
}

    window.searchEndDate = function (date) {
    console.log("End Date:", date);
    currentSearchEndDate = date;
    fetchDataAndRenderTable();
}


    function setDoiTuongApDung(doiTuongApDung) {
    currentDoiTuongApDung = doiTuongApDung;
    updateButtonContent('actionMenuButton1', doiTuongApDung ? 'Cá nhân' : 'Công khai');
    fetchDataAndRenderTable();
}

    function setHinhThucGiamGia(hinhThucGiamGia) {
    currentHinhThucGiamGia = hinhThucGiamGia;
    updateButtonContent('actionMenuButton2', hinhThucGiamGia ? 'Giảm theo %' : 'Giảm theo số tiền');
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
    const id = checkbox.dataset.id;
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
    confirmButtonText: 'Yes',
    cancelButtonText: 'Cancel'
}).then((result) => {
    if (result.isConfirmed) {
    // Update status
    fetch(`/api-phieu-giam-gia/${id}`)
    .then(response => response.json())
    .then(data => {
    const currentStatus = data.trangThai;

    if ((currentStatus === 'Sắp diễn ra' || currentStatus === 'Đang diễn ra') && !isChecked) {
    const updateStatus = 'Đã kết thúc';

    fetch(`/api-phieu-giam-gia/trang-thai/${id}`, {
    method: 'PUT',
    headers: {
    'Content-Type': 'application/json'
},
    body: JSON.stringify({trangThai: updateStatus})
})
    .then(response => {
    if (!response.ok) {
    throw new Error('Failed to update status');
}
    fetchDataAndRenderTable();
})
    .then(data => {
    Swal.fire({
    title: 'Updated Successfully',
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
    console.log('Action canceled');
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
    giaTriToiDa.disabled = true;
} else if (type === 'Percent') {
    btnPercent.style.color = 'green';
    btnPercent.style.fontWeight = 'bold';
    giaTriToiDa.disabled = false;
}
}


    function showAddForm() {

    const cardBody = document.getElementById('tableCardBody');
    const cardBody1 = document.getElementById('tableCardBody1');
    cardBody.style.display = 'none';

    const form = `
        <form id="addForm" >
            <div class="row">
                <!--Table bên trái -->
        <div class="col-md-6">
        <div class="form-row">
        <input type="text" class="form-control" id="id" placeholder="Tên" hidden>
        <div class="form-group col-md-6">
        <label for="maPhieuGiamGia" name="maPhieuGiamGia">Mã phiếu giảm giá</label>
        <input type="text" class="form-control" id="maPhieuGiamGia" placeholder="Nhập mã hoặc tự động tạo mã">
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
        <input type="text" class="form-control" id="soLuong" placeholder="Số lượng">
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
        </div>
            <!-- Bảng khách hàng bên phải -->
        <div class="col-md-6">
        <label class="sr-only" for="search">Tìm kiếm</label>
        <input class="form-control mr-5" id="searchKH"
        placeholder="Tìm kiếm theo mã,tên,sdt"
        type="text" style="margin-top: 33px;display: none; width: 450px" >
        <span id="khachHangError" class="text-danger"></span>
        <table class="table mt-3" id="tableKhachHangContainer" style="display: none;">
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
        style="display: none;text-align: center"></div>
        </div>
        </div>
        <button type="submit" class="btn btn-primary">Lưu</button>
        <button type="button" class="btn btn-success" onclick="cancelAdd()">Hủy</button>
        </form>
        `;


    cardBody1.innerHTML = form;
    document.getElementById('doiTuongTatCa').checked = true;
    // Xử lý sự kiện change cho các radio buttons

    document.addEventListener('change', function (event) {
    const doiTuongCaNhan = document.getElementById('doiTuongCaNhan');
    const doiTuongTatCa = document.getElementById('doiTuongTatCa');
    const tableKhachHangContainer = document.getElementById('tableKhachHangContainer');
    const searchKH = document.getElementById('searchKH');
    const paginationContainer = document.getElementById('paginationContainer');

    // Xử lý logic khi radio button thay đổi
    if (event.target === doiTuongTatCa && doiTuongTatCa.checked) {
    tableKhachHangContainer.style.display = 'none';
    searchKH.style.display = 'none';
    paginationContainer.style.display = 'none';
    // Lấy danh sách tất cả ID khách hàng
    fetch('http://localhost:8080/khach-hang/hien-thi')
    .then(response => response.json())
    .then(data => {
    const allIds = data.map(khachHang => khachHang.id);
    setSelectedIdKhachHangs(allIds); // Chọn tất cả ID khách hàng
})
    .catch(error => console.error('Error fetching khach hang:', error));
} else if (event.target === doiTuongCaNhan && doiTuongCaNhan.checked) {
    tableKhachHangContainer.style.display = 'block';
    searchKH.style.display = 'block';
    paginationContainer.style.display = 'block';
}
});
    // ADD
    // Xử lý sự kiện khi người dùng thay đổi nội dung ô tìm kiếm
    let currentPage = 1;
    const pageSize = 5;

    // Lấy các phần tử cần thiết từ DOM
    const doiTuongCaNhan = document.getElementById('doiTuongCaNhan');
    const doiTuongTatCa = document.getElementById('doiTuongTatCa');
    const tableKhachHangContainer = document.getElementById('tableKhachHangContainer');
    const searchKH = document.getElementById('searchKH');
    const paginationContainer = document.getElementById('paginationContainer');

    // Ẩn bảng và phân trang khi trang được tải lần đầu
    tableKhachHangContainer.style.display = 'none';
    paginationContainer.style.display = 'none';

    // Xử lý sự kiện khi người dùng thay đổi nội dung ô tìm kiếm
    searchKH.addEventListener('input', function () {
    const query = searchKH.value.trim(); // Lấy nội dung tìm kiếm và loại bỏ khoảng trắng đầu cuối

    // Nếu query không rỗng, gọi hàm fetchKhachHang với query hiện tại và trang đầu tiên
    if (query !== '') {
    fetchKhachHang(query, 1, pageSize);
} else {
    // Nếu query rỗng, hiển thị lại danh sách Công khai nếu đang chọn Công khai
    const doiTuongTatCa = document.getElementById('doiTuongTatCa');
    if (doiTuongTatCa.checked) {
    fetch('http://localhost:8080/khach-hang/hien-thi')
    .then(response => response.json())
    .then(data => {
    const allIds = data.map(khachHang => khachHang.id);
    setSelectedIdKhachHangs(allIds); // Chọn tất cả ID khách hàng
})
    .catch(error => console.error('Error fetching khach hang:', error));
} else {
    // Nếu đang chọn tìm kiếm theo cá nhân, hiển thị danh sách theo query rỗng
    fetchKhachHang(query, 1, pageSize);
}
}
});
    // Xử lý sự kiện change cho các radio buttons
    doiTuongCaNhan.onchange = function () {
    if (doiTuongCaNhan.checked) {
    // Hiển thị bảng và phân trang khi chọn tìm kiếm theo cá nhân
    tableKhachHangContainer.style.display = 'block';
    paginationContainer.style.display = 'block';
}
    fetchKhachHang(searchKH.value, currentPage, pageSize); // Gọi hàm fetchKhachHang với query hiện tại
};

    doiTuongTatCa.onchange = function () {
    if (doiTuongTatCa.checked) {
    // Ẩn bảng và phân trang khi chọn tìm kiếm tất cả
    tableKhachHangContainer.style.display = 'none';
    paginationContainer.style.display = 'none';
    fetch('http://localhost:8080/khach-hang/hien-thi')
    .then(response => response.json())
    .then(data => {
    const allIds = data.map(khachHang => khachHang.id);
    setSelectedIdKhachHangs(allIds); // Chọn tất cả ID khách hàng
})
    .catch(error => console.error('Error fetching khach hang:', error));
}
};

    // Hàm fetch dữ liệu khách hàng từ máy chủ và xử lý dữ liệu trả về
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
    tableKhachHangContainer.style.display = 'block';
    paginationContainer.style.display = 'block';
} else if (filteredData.length === 0 && doiTuongCaNhan.checked) {
    // Nếu không có kết quả tìm kiếm, ẩn bảng và phân trang
    tableKhachHangContainer.style.display = 'none';
    paginationContainer.style.display = 'none';
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


    // Gọi hàm fetchKhachHang này để hiển thị danh sách khách hàng khi form được hiển thị lần đầu
    fetchKhachHang('', currentPage, pageSize);


    function getSelectedRadioValue(name) {
    const selectedRadio = document.querySelector(`input[name="${name}"]:checked`);
    return selectedRadio ? selectedRadio.value : ''; // Trả về giá trị của radio nếu có, ngược lại trả về chuỗi trống
}

    // Hàm chọn ID khách hàng
    function setSelectedIdKhachHangs(ids) {
    const checkboxes = document.querySelectorAll('input.item-checkbox');
    checkboxes.forEach(checkbox => {
    const checkboxId = checkbox.getAttribute('data-id');
    if (ids.includes(checkboxId)) {
    checkbox.checked = true;
} else {
    checkbox.checked = false;
}
});
}


    // Hàm lấy danh sách ID và email khách hàng đã chọn
    async function getSelectedIdKhachHangs() {
    const doiTuongTatCa = document.getElementById('doiTuongTatCa');
    if (doiTuongTatCa.checked) {
    // Nếu đã chọn "Công khai", lấy danh sách tất cả ID và email khách hàng
    try {
    const response = await fetch('http://localhost:8080/khach-hang/hien-thi');
    const data = await response.json();
    return data.map(khachHang => ({id: khachHang.id, email: khachHang.email}));
} catch (error) {
    console.error('Error fetching khach hang:', error);
    return []; // Trả về mảng rỗng nếu có lỗi
}
} else {
    // Nếu không phải "Công khai", lấy danh sách ID và email khách hàng đã chọn từ checkbox
    const checkboxes = document.querySelectorAll('input.item-checkbox:checked');
    const ids = Array.from(checkboxes).map(checkbox => checkbox.getAttribute('data-id'));

    try {
    const response = await fetch('http://localhost:8080/khach-hang/hien-thi');
    const data = await response.json();
    return data.filter(khachHang => ids.includes(khachHang.id.toString()))
    .map(khachHang => ({id: khachHang.id, email: khachHang.email}));
} catch (error) {
    console.error('Error fetching khach hang:', error);
    return []; // Trả về mảng rỗng nếu có lỗi
}
}
}


    document.getElementById('addForm').addEventListener('submit', async function (event) {
    event.preventDefault();

    const selectedKhachHangs = await getSelectedIdKhachHangs();
    const selectedIdKhachHangs = selectedKhachHangs.map(khachHang => khachHang.id);
    const selectedEmailsKhachHangs = selectedKhachHangs.map(khachHang => khachHang.email);

    const data = {
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
    idKhachHangs: selectedIdKhachHangs,
    emailKhachHangs: selectedEmailsKhachHangs
};

    if (validateForm()) {
    Swal.fire({
    title: 'Xác nhận',
    text: 'Bạn chắc chắn muốn thêm phiếu giảm giá?',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: 'Yes',
    cancelButtonText: 'Cancel'
}).then((result) => {
    if (result.isConfirmed) {
    fetch('http://localhost:8080/api-phieu-giam-gia/save', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json',
},
    body: JSON.stringify(data),
})
    .then(response => {
    if (!response.ok) {
    return response.json().then(error => {
    throw new Error(error.message || 'Unknown error');
});
}
    return response.json();
})
    .then(async phieuGiamGia => {
    const idPhieuGiamGia = phieuGiamGia.id;
    const chiTietData = selectedIdKhachHangs.map(idKhachHang => ({
    idPhieuGiamGia,
    idKhachHang
}));

    // Lưu chi tiết phiếu giảm giá
    for (const chiTiet of chiTietData) {
    const response = await fetch('http://localhost:8080/api-phieu-giam-gia-chi-tiet/save', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json',
},
    body: JSON.stringify(chiTiet),
});

    if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || 'Unknown error');
}
}

    Swal.fire({
    title: 'Thành công',
    text: 'Thêm phiếu giảm giá thành công',
    icon: 'success'
});
    showSuccessToast('Thêm phiếu giảm giá thành công');
    cancelAdd();
})
    .catch(error => {
    Swal.fire({
    title: 'Lỗi',
    text: error.message,
    icon: 'error'
});
});
} else {
    cancelAdd();
}
}).catch(error => {
    Swal.fire({
    title: 'Lỗi',
    text: 'Có lỗi xảy ra khi xác nhận thêm phiếu giảm giá',
    icon: 'error'
});
    cancelAdd();
});
}
});


    function validateForm() {
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
} else if (hinhThucGiamGiaPercent.checked && parseFloat(mucGiam) >= 100) {
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
}


    //Update Form


    function showUpdate(id) {
    const cardBody = document.getElementById('tableCardBody');
    const cardBody1 = document.getElementById('tableCardBody1');
    cardBody.style.display = 'none';
    cardBody1.innerHTML = '';

    const form = `
        <form id="updateForm">
        <div class="row">
            <!--Table bên trái -->
        <div class="col-md-6">
        <div class="form-row">
        <input type="text" class="form-control" id="id"  hidden>
        <input type="text" class="form-control" id="deletedAt"  hidden>
        <div class="form-group col-md-6">
        <label for="maPhieuGiamGia" name="maPhieuGiamGia">Mã phiếu giảm giá</label>
        <input type="text" class="form-control" id="maPhieuGiamGia" >
        <span id="errorMaPhieuGiamGia" class="text-danger"></span>
        </div>
        <div class="form-group col-md-6">
        <label for="tenPhieuGiamGia" name="tenPhieuGiamGia">Tên phiếu giảm giá</label>
        <input type="text" class="form-control" id="tenPhieuGiamGia" placeholder="Tên">
        <span id="errorTenPhieuGiamGia" class="text-danger"></span>
        </div>
        </div>
        <div class="form-row">
        <div class="form-group col-md-6">
        <label for="mucGiam" name="mucGiam">Mức giảm</label>
        <div class="input-group">
        <input type="text" class="form-control" id="mucGiam" placeholder="mucGiam" style="padding-right: 40px;">
        <span class="input-group-btn">
        <label class="btn" id="btnVND" onclick="selectButton('VND')" style="border: 1px  #e9ecef;">
        $<input type="radio" name="hinhThucGiamGia" id="hinhThucGiamGiaVND" style="display: none; color: #0a0c0d" value="false">
        </label>
        <label class="btn" id="btnPercent" onclick="selectButton('Percent')" style="border: 1px  #e9ecef;">
        %<input type="radio" name="hinhThucGiamGia" id="hinhThucGiamGiaPercent" style="display: none; color: #0a0c0d" value="true">
        </label>
        </span>
        </div>
        <span id="errorMucGiam" class="text-danger"></span>
        </div>
        <div class="form-group col-md-6">
        <label for="giaTriToiDa">Giá trị tối đa</label>
        <div class="input-group mb-3">
        <input type="text" class="form-control" id="giaTriToiDa">
        <span class="input-group-text">$</span>
        </div>
        <span id="errorGiaTriToiDa" class="text-danger"></span>
        </div>
        </div>
        <div class="form-row">
        <div class="form-group col-md-6">
        <label for="soLuong">Số lượng</label>
        <div class="input-group mb-3">
        <input type="text" class="form-control" id="soLuong">
        <span class="input-group-text">#</span>
        </div>
        <span id="errorSoLuong" class="text-danger"></span>
        </div>
        <div class="form-group col-md-6">
        <label for="dieuKienSuDung">Điều kiện tối thiểu</label>
        <div class="input-group mb-3">
        <input type="text" class="form-control" id="dieuKienSuDung">
        <span class="input-group-text">$</span>
        </div>
        <span id="errorDieuKienSuDung" class="text-danger"></span>
        </div>
        </div>
        <div class="form-row">
        <div class="form-group col-md-6">
        <label for="ngayBatDau">Ngày bắt đầu</label>
        <input type="date" class="form-control" id="ngayBatDau" placeholder="Ngày bắt đầu">
        <span id="errorNgayBatDau" class="text-danger"></span>
        </div>
        <div class="form-group col-md-6">
        <label for="ngayKetThuc">Ngày kết thúc</label>
        <input type="date" class="form-control" id="ngayKetThuc" placeholder="Ngày kết thúc">
        <span id="errorNgayKetThuc" class="text-danger"></span>
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
        <span id="errorDoiTuongApDung" class="text-danger"></span>
        </div>
        <div class="form-row">
        <div class="form-group col-md-12">
        <label for="ghiChu">Ghi chú</label>
        <div class="input-group mb-3">
        <input type="text" class="form-control" id="ghiChu">
        </div>
        </div>
        </div>
        <div class="form-row">
        <div class="form-group col-md-12">
        <label for="trangThai">Trạng thái</label>
        <div class="input-group mb-3">
        <input type="text" class="form-control" id="trangThai" disabled>
        </div>
        </div>
        </div>
        </div>
            <!-- Bảng khách hàng bên phải -->
        <div class="col-md-6">
        <label class="sr-only" for="search">Tìm kiếm</label>
        <input class="form-control mr-5" id="searchKH"
        placeholder="Tìm kiếm theo mã, tên , sdt"
        type="text" style="margin-top: 33px;display: block; width: 450px" >
        <span id="khachHangError" class="text-danger"></span>
        <table class="table" id="tableKhachHangContainer" style="display: block;">
        <thead>
        <tr>
        <th>#</th>
        <th>Mã</th>
        <th>Tên</th>
        <th>SDT</th>
        <th>Email</th>
        </tr>
        </thead>
        <tbody id="khachHangTableBody">
            <!-- Danh sách khách hàng sẽ được thêm vào đây -->
        </tbody>
        </table>
        <div id="paginationContainer" class="pagination-container "
        style="display: block;text-align: center"></div>
        </div>
        </div>
        </div>
        <button type="submit" class="btn btn-primary">Lưu</button>
        <button type="button" class="btn btn-success" onclick="cancelAdd()">Hủy</button>
        </form>
        `;

    cardBody1.innerHTML = form;

    fetch(`http://localhost:8080/api-phieu-giam-gia/${id}`)
    .then(response => response.json())
    .then(data => {
    document.getElementById('id').value = data.id;
    document.getElementById('maPhieuGiamGia').value = data.maPhieuGiamGia;
    document.getElementById('tenPhieuGiamGia').value = data.tenPhieuGiamGia;
    document.getElementById('maPhieuGiamGia').value = data.maPhieuGiamGia;
    document.getElementById('mucGiam').value = data.mucGiam;
    document.getElementById('giaTriToiDa').value = data.giaTriToiDa;
    document.getElementById('soLuong').value = data.soLuong;
    // Kiểm tra giá trị doiTuongApDung và set checked đúng cách
    if (data.doiTuongApDung === false) {
    document.getElementById('doiTuongTatCa').checked = true;
    // document.getElementById('tableKhachHangContainer').style.display = 'none';
} else {
    document.getElementById('doiTuongCaNhan').checked = true;
    // document.getElementById('tableKhachHangContainer').style.display = 'block';
}
    //Hinh thuc giam gia
    if (data.hinhThucGiamGia === false) {
    document.getElementById('hinhThucGiamGiaVND').checked = true;
    selectButton('VND');
} else {
    document.getElementById('hinhThucGiamGiaPercent').checked = true;
    selectButton('Percent');
}
    document.getElementById('dieuKienSuDung').value = data.dieuKienSuDung;
    document.getElementById('ngayBatDau').value = data.ngayBatDau;
    document.getElementById('ngayKetThuc').value = data.ngayKetThuc;
    document.getElementById('ghiChu').value = data.ghiChu;
    document.getElementById('deletedAt').value = data.deletedAt;
    document.getElementById('trangThai').value = data.trangThai;
    // Xử lý trạng thái dựa vào ngày bắt đầu và ngày kết thúc
    const ngayBatDauInput = document.getElementById('ngayBatDau');
    const ngayKetThucInput = document.getElementById('ngayKetThuc');

    function updateTrangThai() {
    const currentDate = new Date();
    const startDate = new Date(ngayBatDauInput.value);
    const endDate = new Date(ngayKetThucInput.value);
    const soLuong = parseInt(document.getElementById('soLuong').value);
    let trangThai = '';
    if (currentDate < startDate) {
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

    document.getElementById('soLuong').addEventListener('change', updateTrangThai);
    ngayBatDauInput.addEventListener('change', updateTrangThai);
    ngayKetThucInput.addEventListener('change', updateTrangThai);


    // Lấy danh sách khách hàng từ API và hiển thị trong bảng
    fetch('http://localhost:8080/khach-hang/hien-thi')
    .then(response => response.json())
    .then(data => {
    const khachHangs = data; // Lưu danh sách khách hàng vào biến

    const pageSize = 5; // Số lượng khách hàng trên mỗi trang
    let currentPage = 1; // Trang hiện tại, bắt đầu từ trang đầu tiên

    // Mảng lưu trữ trạng thái checked của các checkbox
    const checkedStatus = {};

    // Function để hiển thị dữ liệu khách hàng trên trang hiện tại
    const displayCustomersOnPage = (page) => {
    const tableBody = document.getElementById('khachHangTableBody');
    tableBody.innerHTML = '';

    // Tính chỉ số bắt đầu và kết thúc của danh sách khách hàng cho trang hiện tại
    const startIndex = (page - 1) * pageSize;
    const endIndex = Math.min(startIndex + pageSize, khachHangs.length);

    for (let i = startIndex; i < endIndex; i++) {
    const khachHang = khachHangs[i];
    const row = document.createElement('tr');
    row.innerHTML = `
        <td>
        <div class="custom-control custom-checkbox">
        <input type="checkbox" class="custom-control-input item-checkbox" id="checkbox-${khachHang.id}" data-id="${khachHang.id}" ${checkedStatus[khachHang.id] ? 'checked' : ''}>
        <label class="custom-control-label" for="checkbox-${khachHang.id}"></label>
        </div>
        </td>
        <td id="idKhachHang" hidden>${khachHang.id}</td>
        <td>${khachHang.maKhachHang}</td>
        <td>${khachHang.hoVaTen}</td>
        <td>${khachHang.soDienThoai}</td>
        <td>${khachHang.email}</td>
        `;
    row.addEventListener('click', () => {
    // Lưu idKhachHang khi người dùng chọn khách hàng
    const idKhachHangInput = document.getElementById('idKhachHang');
    idKhachHangInput.value = khachHang.id;
});
    tableBody.appendChild(row);
}
};


    // Function để tạo các nút phân trang
    const createPaginationButtons = () => {
    const paginationContainer = document.getElementById('paginationContainer');
    paginationContainer.innerHTML = '';

    // Tính tổng số trang dựa trên số lượng khách hàng và kích thước mỗi trang
    const totalPages = Math.ceil(khachHangs.length / pageSize);

    // Tạo nút cho mỗi trang
    for (let i = 1; i <= totalPages; i++) {
    const button = document.createElement('button');
    button.textContent = i;
    button.className = 'btn btn-success';
    button.style = 'margin-right: 10px';
    // Xử lý sự kiện click của nút phân trang
    button.onclick = function (event) {
    event.preventDefault(); // Ngăn chặn hành vi mặc định của trình duyệt
    currentPage = i;
    displayCustomersOnPage(currentPage);
};
    paginationContainer.appendChild(button);
}
};

    // Function để lấy và cập nhật trạng thái checked từ API
    const fetchCheckedStatus = () => {
    fetch(`http://localhost:8080/api-phieu-giam-gia-chi-tiet/pggct/${id}`)
    .then(response => response.json())
    .then(dataCT => {
    dataCT.forEach(chiTiet => {
    checkedStatus[chiTiet.idKhachHang] = true;
});
    // Sau khi lấy trạng thái checked, hiển thị dữ liệu và phân trang
    displayCustomersOnPage(currentPage);
    createPaginationButtons();
})
    .catch(error => {
    console.error('Error fetching chi tiet phieu giam gia:', error);
    // Nếu có lỗi, vẫn hiển thị dữ liệu và phân trang
    displayCustomersOnPage(currentPage);
    createPaginationButtons();
});
};

    // Khởi đầu lấy trạng thái checked và hiển thị phân trang
    fetchCheckedStatus();
})
    .catch(error => {
    console.error('Error fetching or displaying customers:', error);
});
});


    //End pagnitation


    async function getSelectedIdKhachHangs() {
    const doiTuongTatCa = document.getElementById('doiTuongTatCa');
    if (doiTuongTatCa.checked) {
    // Nếu đã chọn "Công khai", lấy danh sách tất cả ID khách hàng từ API
    try {
    const response = await fetch('http://localhost:8080/khach-hang/hien-thi');
    if (!response.ok) {
    throw new Error('Failed to fetch customers');
}
    const data = await response.json();
    return data.map(khachHang => khachHang.id);
} catch (error) {
    console.error('Error fetching khach hang:', error);
    return []; // Trả về mảng rỗng nếu có lỗi
}
} else {
    // Nếu không phải "Công khai", lấy danh sách ID khách hàng đã chọn từ checkbox
    const checkboxes = document.querySelectorAll('input.item-checkbox:checked');
    return Array.from(checkboxes).map(checkbox => checkbox.getAttribute('data-id'));
}
}


    // Ham check id khach hang chua ton tai de them moi
    async function checkChiTietPhieuGiamGia(idPhieuGiamGia, selectedIds) {
    try {
    const response = await fetch(`http://localhost:8080/api-phieu-giam-gia-chi-tiet/pggct/${idPhieuGiamGia}`);
    const data = await response.json();

    // Tạo một mảng để lưu các ID khách hàng không tồn tại
    const notFoundIds = [];

    // Duyệt qua từng ID khách hàng đã chọn và kiểm tra tồn tại trong dữ liệu từ API
    for (let idKhachHang of selectedIds) {
    let exists = false;
    for (let chiTiet of data) {
    if (chiTiet.idKhachHang == idKhachHang) {
    exists = true;
    break; // Nếu đã tồn tại, thoát vòng lặp
}
}
    if (!exists) {
    notFoundIds.push(idKhachHang); // Nếu không tồn tại, thêm vào mảng notFoundIds
}
}

    return notFoundIds; // Trả về mảng các ID khách hàng không tồn tại
} catch (error) {
    console.error('Error checking chi tiet phieu giam gia:', error);
    return []; // Trả về mảng rỗng nếu có lỗi
}
}


    // Hàm kiểm tra ID khách hàng đã tồn tại nhưng không có trong danh sách đã chọn
    async function getNotSelectedIds(idPhieuGiamGia, selectedIds) {
    try {
    // Lấy danh sách ID khách hàng đã tồn tại từ API
    const response = await fetch(`http://localhost:8080/api-phieu-giam-gia-chi-tiet/pggct/${idPhieuGiamGia}`);
    if (!response.ok) {
    throw new Error('Lỗi khi lấy chi tiết phiếu giảm giá');
}
    const existingData = await response.json();

    // Tạo tập hợp các ID khách hàng đã tồn tại từ dữ liệu nhận được từ API
    const existingIdsSet = new Set(existingData.map(chiTiet => chiTiet.idKhachHang.toString())); // Chuyển tất cả thành chuỗi

    // Chuyển selectedIds thành chuỗi để so sánh chính xác
    const selectedIdsSet = new Set(selectedIds.map(id => id.toString()));

    // Tạo một mảng để lưu các ID đã tồn tại nhưng không có trong selectedIds
    const notSelectedIds = Array.from(existingIdsSet).filter(idKhachHang => !selectedIdsSet.has(idKhachHang));

    console.log('Các ID khách hàng đã tồn tại nhưng đã bị bỏ chọn:', notSelectedIds);

    return notSelectedIds; // Trả về mảng các ID khách hàng đã tồn tại nhưng không có trong selectedIds
} catch (error) {
    console.error('Lỗi khi kiểm tra ID khách hàng đã tồn tại nhưng không có trong danh sách đã chọn:', error);
    return []; // Trả về mảng rỗng nếu có lỗi
}
}


    // Hàm cập nhật phiếu giảm giá và chi tiết
    document.getElementById('updateForm').addEventListener('submit', async function (event) {
    event.preventDefault();
    if (validateFormUpdate()) {
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
    const selectedIdKhachHangs = await getSelectedIdKhachHangs(); // Lấy danh sách ID khách hàng đã chọn
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
    doiTuongApDung: document.querySelector('input[name="doiTuongApDung"]:checked').value === 'true',
    hinhThucGiamGia: document.querySelector('input[name="hinhThucGiamGia"]:checked').value === 'true',
    ghiChu: document.getElementById('ghiChu').value,
    deletedAt: document.getElementById('deletedAt').value,
    trangThai: document.getElementById('trangThai').value
};

    // Gọi API để cập nhật phiếu giảm giá
    const responseUpdate = await fetch(`http://localhost:8080/api-phieu-giam-gia/${dataUpdate.id}`, {
    method: 'PUT',
    headers: {
    'Content-Type': 'application/json',
},
    body: JSON.stringify(dataUpdate),
});

    if (!responseUpdate.ok) {
    const errorData = await responseUpdate.json();
    throw new Error(`Error updating phiếu giảm giá: ${errorData.message}`);
}

    // Gọi hàm kiểm tra chi tiết phiếu giảm giá với ID phiếu và danh sách ID khách hàng đã chọn
    const notFoundIds = await checkChiTietPhieuGiamGia(dataUpdate.id, selectedIdKhachHangs);
    const notSelectedIds = await getNotSelectedIds(dataUpdate.id, selectedIdKhachHangs);

    if (notFoundIds.length > 0) {
    for (const idKhachHang of notFoundIds) {
    const chiTietData = {
    idPhieuGiamGia: dataUpdate.id,
    idKhachHang
};

    const responseSave = await fetch('http://localhost:8080/api-phieu-giam-gia-chi-tiet/save', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json',
},
    body: JSON.stringify(chiTietData),
});

    if (!responseSave.ok) {
    const errorData = await responseSave.json();
    throw new Error(`Error saving chi tiết phiếu giảm giá: ${errorData.message}`);
}
}
}

    // Xử lý các ID đã tồn tại nhưng bị bỏ chọn (cập nhật deletedAt = true)
    if (notSelectedIds.length > 0) {
    for (const idKhachHang of notSelectedIds) {
    await fetch(`http://localhost:8080/api-phieu-giam-gia-chi-tiet/${dataUpdate.id}/khach-hang/${idKhachHang}`, {
    method: 'PUT',
    headers: {
    'Content-Type': 'application/json',
},
    body: JSON.stringify(true), // Gửi giá trị boolean dưới dạng đối tượng JSON
});
}
}

    // Xử lý các ID mới được chọn lại (cập nhật deletedAt = false)
    if (selectedIdKhachHangs.length > 0) {
    for (const idKhachHang of selectedIdKhachHangs) {
    await fetch(`http://localhost:8080/api-phieu-giam-gia-chi-tiet/${dataUpdate.id}/khach-hang/${idKhachHang}`, {
    method: 'PUT',
    headers: {
    'Content-Type': 'application/json',
},
    body: JSON.stringify(false), // Gửi giá trị boolean dưới dạng đối tượng JSON
});

}
}

    // Hiển thị thông báo khi cập nhật thành công
    Swal.fire({
    title: 'Updated Successfully',
    text: 'Sửa thành công',
    icon: 'success'
});
    showSuccessToast('Sửa thành công');
    cancelAdd();
} catch (error) {
    Swal.fire({
    title: 'Lỗi',
    text: `Có lỗi xảy ra: ${error.message}`,
    icon: 'error'
});
}
}
}).catch(error => {
    console.error('Error in confirmation dialog:', error);
    Swal.fire({
    title: 'Lỗi',
    text: 'Có lỗi xảy ra khi xác nhận sửa phiếu giảm giá',
    icon: 'error'
});
});
}
});


    function validateFormUpdate() {
    // Lấy các giá trị từ các trường input
    const tenPhieuGiamGia = document.getElementById('tenPhieuGiamGia').value.trim();
    const maPhieuGiamGia = document.getElementById('maPhieuGiamGia').value.trim();
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
    document.getElementById('errorMaPhieuGiamGia').innerText = '';
    document.getElementById('errorTenPhieuGiamGia').innerText = '';
    document.getElementById('errorMucGiam').innerText = '';
    document.getElementById('errorGiaTriToiDa').innerText = '';
    document.getElementById('errorSoLuong').innerText = '';
    document.getElementById('errorDieuKienSuDung').innerText = '';
    document.getElementById('errorNgayBatDau').innerText = '';
    document.getElementById('errorNgayKetThuc').innerText = '';

    let isValid = true;

    // Kiểm tra các trường bắt buộc
    const validRadio = hinhThucGiamGiaVND.checked || hinhThucGiamGiaPercent.checked;

    if (!maPhieuGiamGia) {
    document.getElementById('errorMaPhieuGiamGia').innerText = 'Tên phiếu giảm giá không được để trống';
    isValid = false;
}
    if (!tenPhieuGiamGia) {
    document.getElementById('errorTenPhieuGiamGia').innerText = 'Tên phiếu giảm giá không được để trống';
    isValid = false;
}
    if (!validRadio) {
    document.getElementById('errorMucGiam').innerText = "Vui lòng chọn một hình thức giảm giá";
    isValid = false;
} else if (!mucGiam) {
    document.getElementById('errorMucGiam').innerText = 'Mức giảm không được để trống';
    isValid = false;
} else if (hinhThucGiamGiaPercent.checked && parseFloat(mucGiam) >= 100) {
    document.getElementById('errorMucGiam').innerText = "Mức giảm phải nhỏ hơn 100%";
    isValid = false;
}

    if (hinhThucGiamGiaPercent.checked && !giaTriToiDa) {
    document.getElementById('errorGiaTriToiDa').innerText = 'Giá trị tối đa không được để trống';
    isValid = false;
}

    if (soLuong !== "") { // Chỉ kiểm tra nếu không trống
    if (!Number.isInteger(Number(soLuong)) || Number(soLuong) <= 0) {
    document.getElementById('errorSoLuong').innerText = "Số lượng phải là số nguyên dương";
    isValid = false;
}
}
    if (!dieuKienSuDung) {
    document.getElementById('errorDieuKienSuDung').innerText = 'Điều kiện sử dụng không được để trống';
    isValid = false;
}
    if (!ngayBatDau) {
    document.getElementById('errorNgayBatDau').innerText = 'Ngày bắt đầu không được để trống';
    isValid = false;
}
    if (!ngayKetThuc) {
    document.getElementById('errorNgayKetThuc').innerText = 'Ngày kết thúc không được để trống';
    isValid = false;
}

    // Kiểm tra nếu ngày bắt đầu lớn hơn ngày kết thúc
    if (new Date(ngayBatDau) > new Date(ngayKetThuc)) {
    document.getElementById('errorNgayKetThuc').innerText = 'Ngày kết thúc phải sau ngày bắt đầu';
    isValid = false;
}
    // Lấy ngày hiện tại và loại bỏ thời gian
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    if (new Date(ngayBatDau) < today) {
    document.getElementById('errorNgayBatDau').innerText = 'Ngày bắt đầu không được nhỏ hơn ngày hiện tại';
    isValid = false;
}

    if (doiTuongCaNhan.checked && selectedCheckboxes.length === 0) {
    const khachHangError = document.getElementById('khachHangError');
    khachHangError.innerText = 'Vui lòng chọn ít nhất một khách hàng.';
    isValid = false;
}
    return isValid;
}
}

