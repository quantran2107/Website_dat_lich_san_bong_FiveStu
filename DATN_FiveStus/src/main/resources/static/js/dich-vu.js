

    function downloadTemplate(type) {
    window.location.href = `http://localhost:8080/template/${type}/download`;
}

    function openFileDialog(type) {
    // Lưu loại dịch vụ để sử dụng khi gửi dữ liệu
    window.currentImportType = type;

    // Kích hoạt hộp thoại chọn tệp
    document.getElementById('fileInput').click();
}

    function handleFileChange(event) {
    const file = event.target.files[0];
    if (!file) {
    alert("Please select a file.");
    return;
}

    // Gửi tệp đến API
    importFromExcel(window.currentImportType, file);
}


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

    document.addEventListener('DOMContentLoaded', async function () {
    showLoading();
    currentServiceType = 'do_thue'; // Default service type
    await fetchPriceRange(currentServiceType);
    await loadData(currentServiceType); // Load default data
    hideLoading();
    changeTab(currentServiceType);
});

    let currentServiceType = 'do_thue';
    let currentSearchQuery = '';
    let currentStatusFilter = '';
    let currentDonGiaMin = '';
    let currentDonGiaMax = '';

    const pageSize = 10;
    const debounceDelay = 300;
    let debounceTimeout;

    const cache = {};
    const cacheDuration = 30 * 1000; // 30 giây


    async function fetchData(apiUrl) {
    const currentTime = Date.now();
    const cacheEntry = cache[apiUrl];

    if (cacheEntry && (currentTime - cacheEntry.timestamp < cacheDuration)) {
    return cacheEntry.data;
}

    try {
    const response = await fetch(apiUrl);
    if (!response.ok) throw new Error('Network response was not ok');
    const data = await response.json();
    cache[apiUrl] = { data, timestamp: currentTime };
    return data;
} catch (error) {
    console.error('Error fetching data:', error);
    return null;
}
}

    async function loadData(serviceType, page = 0) {
    currentServiceType = serviceType;
    const size = pageSize;
    let apiUrl = `http://localhost:8080/${currentServiceType}/search-and-filter?page=${page}&size=${size}`;

    // Thêm các tham số lọc vào URL
    if (currentSearchQuery) apiUrl += `&keyword=${encodeURIComponent(currentSearchQuery)}`;
    if (currentStatusFilter) apiUrl += `&trangThai=${encodeURIComponent(currentStatusFilter)}`;
    if (currentDonGiaMin !== undefined && currentDonGiaMax !== undefined) {
    apiUrl += `&donGiaMin=${encodeURIComponent(currentDonGiaMin)}&donGiaMax=${encodeURIComponent(currentDonGiaMax)}`;
}

    // Fetch dữ liệu từ API
    const data = await fetchData(apiUrl);

    // Kiểm tra xem dữ liệu trả về có hợp lệ không
    if (data && data.totalElements !== undefined && data.size !== undefined) {
    renderTable(data.content, page, size);
    updatePagination(data.totalElements, page, size);
}

}



    function renderTable(items, page, size) {
    const tableBody = document.getElementById(`tableBody${currentServiceType === 'do_thue' ? 'DoThue' : 'NuocUong'}`);
    const fragment = document.createDocumentFragment();

    items.forEach((item, index) => {
    const row = document.createElement('tr');
    row.dataset.itemId = item.id; // Add identifier for easier update
    row.innerHTML = `
            <td>${index + 1 + page * size}</td>
            <td>${item.tenDoThue || item.tenNuocUong}</td>
            <td>
                <img src="${item.imageData ? 'data:image/jpeg;base64,' + item.imageData : 'default-image.png'}" alt="${item.tenDoThue || item.tenNuocUong || 'No image'}"
                style="width: 70px; height: 70px; object-fit: cover;">
            </td>
            <td>${item.donGia ? item.donGia + ' VND' : 'N/A'}</td>
            <td>${item.soLuong || '0'}</td>
            <td>
                <span class="${item.trangThai === 'Còn' ? 'custom-4' : 'custom-3'}"
                style="font-size: 14px; width: 84px;">
                ${item.trangThai || 'N/A'}
                </span>
            </td>
            <td>
                <button class="btn btn-outline-success" type="button" data-toggle="modal" style="justify-content: center" onclick="editItem('${item.id}')" title="Sửa">
                    <span class="fe fe-edit-3"></span>
                </button>
                <button class="btn btn-outline-danger" type="button" data-toggle="modal" style="justify-content: center" onclick="deleteItem('${item.id}')" title="Xóa">
                    <span class="fe fe-trash-2"></span>
                </button>
            </td>
        `;
    fragment.appendChild(row);
});

    // Replace all rows
    tableBody.innerHTML = '';
    tableBody.appendChild(fragment);
}

    function updatePagination(totalElements, currentPage, size) {
    const pagination = document.getElementById(`pagination${currentServiceType === 'do_thue' ? 'DoThue' : 'NuocUong'}`);
    const fragment = document.createDocumentFragment();

    // Tính số trang
    const totalPages = Math.ceil(totalElements / size);

    // Xóa phân trang cũ
    pagination.innerHTML = '';

    // Nếu chỉ có một trang hoặc không có trang nào, chỉ hiển thị số trang đó
    if (totalPages <= 1) {
    if (totalPages === 1) {
    const singlePageLi = document.createElement('li');
    singlePageLi.className = 'page-item active'; // Nổi bật trang hiện tại
    const singlePageA = document.createElement('a');
    singlePageA.className = 'page-link';
    singlePageA.href = '#';
    singlePageA.textContent = '1'; // Hiển thị số trang
    singlePageLi.appendChild(singlePageA);
    fragment.appendChild(singlePageLi);
}
    pagination.appendChild(fragment);
    return;
}

    // Số trang tối đa để hiển thị
    const maxPagesToShow = 3;
    let startPage, endPage;

    // Điều chỉnh startPage và endPage để chỉ hiển thị số trang hợp lý
    if (totalPages <= maxPagesToShow) {
    startPage = 0;
    endPage = totalPages;
} else {
    // Hiển thị trang xung quanh trang hiện tại
    startPage = Math.max(0, currentPage - Math.floor(maxPagesToShow / 2));
    endPage = Math.min(totalPages, startPage + maxPagesToShow);

    // Điều chỉnh startPage nếu không đủ trang trước đó
    if (endPage - startPage < maxPagesToShow) {
    startPage = Math.max(0, endPage - maxPagesToShow);
}
}

    // Thêm nút "Previous" nếu không phải là trang đầu tiên
    if (currentPage > 0) {
    const prevPageLi = document.createElement('li');
    prevPageLi.className = 'page-item';
    const prevPageA = document.createElement('a');
    prevPageA.className = 'page-link';
    prevPageA.href = '#';
    prevPageA.textContent = '<';
    prevPageA.addEventListener('click', (event) => {
    event.preventDefault();
    loadData(currentServiceType, currentPage - 1);
});
    prevPageLi.appendChild(prevPageA);
    fragment.appendChild(prevPageLi);
}

    // Thêm các trang số
    for (let i = startPage; i < endPage; i++) {
    const li = document.createElement('li');
    li.className = `page-item${i === currentPage ? ' active' : ''}`;
    const a = document.createElement('a');
    a.className = 'page-link';
    a.href = '#';
    a.textContent = i + 1;
    a.addEventListener('click', (event) => {
    event.preventDefault();
    loadData(currentServiceType, i);
});
    li.appendChild(a);
    fragment.appendChild(li);
}

    // Thêm nút "Next" nếu không phải là trang cuối cùng
    if (currentPage < totalPages - 1) {
    const nextPageLi = document.createElement('li');
    nextPageLi.className = 'page-item';
    const nextPageA = document.createElement('a');
    nextPageA.className = 'page-link';
    nextPageA.href = '#';
    nextPageA.textContent = '>';
    nextPageA.addEventListener('click', (event) => {
    event.preventDefault();
    loadData(currentServiceType, currentPage + 1);
});
    nextPageLi.appendChild(nextPageA);
    fragment.appendChild(nextPageLi);
}
    console.log(totalElements, currentPage, size)
    pagination.appendChild(fragment); // Append all pagination items at once
}



    function debounce(func, delay) {
    if (debounceTimeout) clearTimeout(debounceTimeout);
    debounceTimeout = setTimeout(func, delay);
}


    const debouncedSearchDonGia = debounce(function (min, current, max) {
    currentDonGiaMin = min;
    currentDonGiaMax = current;
    loadData(currentServiceType, 0); // Load data with updated price range
}, debounceDelay);

    function updatePriceRange(value) {
    document.getElementById('rangeValue').textContent = value;
    searchDonGia(value);
}
    function showLoading() {
    document.getElementById('loadingOverlay').classList.remove('loading-hidden');
}

    function hideLoading() {
    document.getElementById('loadingOverlay').classList.add('loading-hidden');
}


    // Function to initialize price range slider
    function initializePriceRange(minPrice, maxPrice) {
    const rangeInput = document.getElementById('customRange2');
    if (rangeInput) {
    rangeInput.min = minPrice;
    rangeInput.max = maxPrice;
    rangeInput.value = maxPrice; // Set initial value to maxPrice
    document.getElementById('rangeMin').textContent = minPrice;
    document.getElementById('rangeMax').textContent = maxPrice;
    document.getElementById('rangeValue').textContent = maxPrice;
}
}

    document.getElementById('customRange2').addEventListener('input', function () {
    const value = this.value;
    updatePriceRange(value);
});


    function setStatusFilterDV(status) {
    currentStatusFilter = status;
    document.getElementById('statusFilterButton').textContent = status;
    loadData(currentServiceType, 0); // Load data with updated status filter
}

    function searchDV(query) {
    clearTimeout(debounceTimeout);
    debounceTimeout = setTimeout(() => {
    currentSearchQuery = query;
    loadData(currentServiceType, 0); // Load data with updated search query
}, debounceDelay);
}

    function resetFilters() {
    // Đặt lại các bộ lọc
    currentSearchQuery = '';
    currentStatusFilter = '';
    currentDonGiaMin = ''; // Hoặc giá trị mặc định bạn muốn
    currentDonGiaMax = ''; // Hoặc giá trị mặc định bạn muốn

    // Cập nhật giao diện
    document.getElementById('search').value = '';
    document.getElementById('statusFilterButton').textContent = 'Trạng thái';

    // Đặt lại thanh range
    const rangeInput = document.getElementById('customRange2');
    rangeInput.value = rangeInput.max; // Hoặc giá trị mặc định bạn muốn
    document.getElementById('rangeValue').textContent = rangeInput.max;

    // Tải lại dữ liệu không có bộ lọc
    loadData(currentServiceType, 0);
}


    function changeTab(serviceType) {
    currentServiceType = serviceType;
    fetchPriceRange(serviceType).then(() => {
    loadData(serviceType, 0); // Load data for the selected tab
});
}

    function searchDonGia(value) {
    const min = document.getElementById('customRange2').min;
    const max = document.getElementById('customRange2').max;
    currentDonGiaMin = min;
    currentDonGiaMax = value;
    loadData(currentServiceType, 0);
}


    // Function to fetch price range for a service type
    async function fetchPriceRange(serviceType) {
    let apiUrl = `http://localhost:8080/${serviceType}/hien-thi`;
    const data = await fetchData(apiUrl);
    if (data) {
    const donGia = data.map(item => item.donGia).filter(donGia => typeof donGia === 'number');
    if (donGia.length > 0) {
    const minPrice = Math.min(...donGia);
    const maxPrice = Math.max(...donGia);
    initializePriceRange(minPrice, maxPrice);
} else {
    console.warn('No valid prices found.');
    initializePriceRange(0, 0);
}
}
}

    function deleteItem(id) {
    Swal.fire({
        title: 'Xác nhận',
        text: 'Bạn chắc chắn muốn xóa dịch vụ này?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Có',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            let url = `http://localhost:8080/${currentServiceType}/delete-soft/${id}`;

            fetch(url, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ deletedAt: true }) // Gửi body với trường deletedAt
            })
                .then(response => {
                    if (response.ok) {
                        Swal.fire({
                            title: 'Thành công',
                            text: 'Xóa dịch vụ thành công',
                            icon: 'success'
                        }).then(() => {
                            loadData(currentServiceType, 0);
                        });
                    } else {
                        response.text().then(text => {
                            Swal.fire({
                                title: 'Lỗi',
                                text: 'Có lỗi xảy ra: ' + text,
                                icon: 'error'
                            });
                        });
                    }
                })
                .catch(error => {
                    Swal.fire({
                        title: 'Lỗi',
                        text: 'Có lỗi xảy ra: ' + error,
                        icon: 'error'
                    });
                });
        }
    });
}


    function importFromExcel(type) {
    const fileInput = document.getElementById('fileInput');
    const file = fileInput.files[0];
    if (!file) {
    showErrorToast('Chọn 1 file');
    return;
}

    const formData = new FormData();
    formData.append("file", file);

    axios.post(`http://localhost:8080/import/${type}`, formData, {
    headers: {
    'Content-Type': 'multipart/form-data'
}
})
    .then(response => {
    showSuccessToast('Thêm dịch vụ thành công');
    fileInput.value = '';  // Xóa tệp đã chọn
    loadData(currentServiceType, 0);
})
    .catch(error => {
    showErrorToast('Thêm dịch vụ thất bại');
    loadData(currentServiceType, 0);
});
}

    //Export excel
    async function exportToExcel(serviceType) {
    try {
    // Tạo workbook mới
    var wb = XLSX.utils.book_new();

    // Tạo URL API dựa trên loại dịch vụ
    let apiUrl = `http://localhost:8080/${serviceType}/hien-thi`; // Đảm bảo URL là chính xác

    // Fetch dữ liệu từ API
    const response = await fetch(apiUrl);
    if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status} - ${response.statusText}`);
}
    const data = await response.json();

    // Debug: In dữ liệu trả về từ API
    console.log("Dữ liệu từ API:", data);

    if (data && Array.isArray(data)) {
    // Tạo bảng dữ liệu từ kết quả API
    const items = data;
    const table = document.createElement('table');
    const thead = document.createElement('thead');
    const tbody = document.createElement('tbody');

    // Tạo tiêu đề bảng
    const headerRow = document.createElement('tr');
    headerRow.innerHTML = `
                <th>STT</th>
                <th>Tên</th>
                <th>Đơn Giá</th>
                <th>Số Lượng</th>
                <th>Trạng Thái</th>
            `;
    thead.appendChild(headerRow);

    // Tạo dữ liệu bảng
    items.forEach((item, index) => {
    const row = document.createElement('tr');
    row.innerHTML = `
                    <td>${index + 1}</td>
                    <td>${item.tenDoThue || item.tenNuocUong}</td>
                    <td>${item.donGia ? item.donGia + ' VND' : 'N/A'}</td>
                    <td>${item.soLuong || '0'}</td>
                    <td>${item.trangThai || 'N/A'}</td>
                `;
    tbody.appendChild(row);
});

    table.appendChild(thead);
    table.appendChild(tbody);

    // Chuyển đổi bảng HTML thành sheet
    var ws = XLSX.utils.table_to_sheet(table);

    // Thêm sheet vào workbook
    XLSX.utils.book_append_sheet(wb, ws, serviceType === 'do_thue' ? "Đồ Thuê" : "Nước Uống");

    // Xuất file Excel
    XLSX.writeFile(wb, serviceType === 'do_thue' ? 'do_thue_data.xlsx' : 'nuoc_uong_data.xlsx');
} else {
    console.error("Dữ liệu trả về không phải là mảng hoặc dữ liệu không hợp lệ.");
}
} catch (error) {
    console.error("Có lỗi xảy ra khi xuất dữ liệu:", error);
}
}

    // Hàm hiển thị form thêm dữ liệu
    function showAddForm() {
    const cardBody = $('#tableCardBody');
    const cardBody1 = $('#tableCardBody1');

    // Ẩn cardBody1 và làm trống cardBody
    cardBody1.hide();
    // Tạo giao diện tab và form động
    const tabs = `

                            <ul class="nav nav-tabs" id="myTab" role="tablist">
                                <li class="nav-item" role="presentation">
                                    <a class="nav-link $
                        {
                            currentServiceType === 'do_thue' ? 'active' : ''
                        }
                    " id="tabDoThue-tab" data-toggle="tab" href="#tabDoThue" role="tab" aria-controls="tabDoThue" aria-selected="$
                        {
                            currentServiceType === 'do_thue'
                        }
                    ">Quản lý đồ thuê</a>
                                </li>
                                <li class="nav-item" role="presentation">
                                    <a class="nav-link $
                        {
                            currentServiceType === 'nuoc_uong' ? 'active' : ''
                        }
                    " id="tabNuocUong-tab" data-toggle="tab" href="#tabNuocUong" role="tab" aria-controls="tabNuocUong" aria-selected="$
                        {
                            currentServiceType === 'nuoc_uong'
                        }
                    ">Quản lý nước uống</a>
                                </li>
                            </ul>
                            <div class="tab-content" id="myTabContent">
                                <div class="tab-pane fade $
                        {
                            currentServiceType === 'do_thue' ? 'show active' : ''
                        }
                    " id="tabDoThue" role="tabpanel" aria-labelledby="tabDoThue-tab">
<!-- Form cho đồ thuê -->
        <form id="addFormDoThue" enctype="multipart/form-data">
        <div class="form-row">
        <div class="form-group col-md-6">
        <label for="tenDoThue">Tên</label>
        <input type="text" class="form-control" id="tenDoThue" placeholder="Tên">
        <span id="errorTenDoThue" class="text-danger"></span>
        </div>
        </div>
        <div class="form-row">
        <div class="form-group col-md-6">
        <label for="soLuongDoThue">Số lượng</label>
        <div class="input-group mb-3">
        <input type="text" class="form-control" id="soLuongDoThue">
        <span class="input-group-text">#</span>
        </div>
        <span id="errorSoLuongDoThue" class="text-danger"></span>
        </div>
        </div>
        <div class="form-row">
        <div class="form-group col-md-6">
        <label for="donGiaDoThue">Đơn giá</label>
        <div class="input-group mb-3">
        <input type="text" class="form-control" id="donGiaDoThue" placeholder="Đơn giá"
        aria-label="Đơn giá" aria-describedby="basic-addon2">
        <span class="input-group-text" id="basic-addon2">VND</span>
        </div>
        <span id="errorDonGiaDoThue" class="text-danger"></span>
        </div>
        </div>
        <div class="form-row">
        <div class="form-group col-md-6">
        <label for="imageDoThue" class="form-label">Chọn Ảnh</label>
        <input class="form-control" type="file" id="imageDoThue">
        <span id="errorImageDoThue" class="text-danger"></span>
        </div>
        </div>
        <button type="submit" class="btn btn-primary" onclick="submitAddFormDoThue(event)">Lưu
        </button>
        <button type="button" class="btn btn-success" onclick="cancelAdd()">Hủy</button>
        </form>
        </div>
        <div class="tab-pane fade ${currentServiceType === 'nuoc_uong' ? 'show active' : ''}"
        id="tabNuocUong" role="tabpanel" aria-labelledby="tabNuocUong-tab">
            <!-- Form cho nước uống -->
        <form id="addFormNuocUong" enctype="multipart/form-data">
        <div class="form-row">
        <div class="form-group col-md-6">
        <label for="tenNuocUong">Tên</label>
        <input type="text" class="form-control" id="tenNuocUong" placeholder="Tên">
        <span id="errorTenNuocUong" class="text-danger"></span>
        </div>
        </div>
        <div class="form-row">
        <div class="form-group col-md-6">
        <label for="soLuongNuocUong">Số lượng</label>
        <div class="input-group mb-3">
        <input type="text" class="form-control" id="soLuongNuocUong">
        <span class="input-group-text">#</span>
        </div>
        <span id="errorSoLuongNuocUong" class="text-danger"></span>
        </div>
        </div>
        <div class="form-row">
        <div class="form-group col-md-6">
        <label for="donGiaNuocUong">Đơn giá</label>
        <div class="input-group mb-3">
        <input type="text" class="form-control" id="donGiaNuocUong"
        placeholder="Đơn giá" aria-label="Đơn giá"
        aria-describedby="basic-addon2">
        <span class="input-group-text" id="basic-addon2">VND</span>
        </div>
        <span id="errorDonGiaNuocUong" class="text-danger"></span>
        </div>
        </div>
        <div class="form-row">
        <div class="form-group col-md-6">
        <label for="imageNuocUong" class="form-label">Chọn Ảnh</label>
        <input class="form-control" type="file" id="imageNuocUong">
        <span id="errorImageNuocUong" class="text-danger"></span>
        </div>
        </div>
        <button type="submit" class="btn btn-primary"
        onclick="submitAddFormNuocUong(event)">Lưu
        </button>
        <button type="button" class="btn btn-success" onclick="cancelAdd()">Hủy</button>
        </form>
        </div>
        </div>
        `;

    // Chèn giao diện tab vào cardBody
    cardBody.html(tabs);

    // Kích hoạt tab hiện tại
    $('#myTab a[href="#' + currentServiceType + '"]').tab('show');
}

    // Hàm submit form cho đồ thuê
    function submitAddFormDoThue(event) {
    event.preventDefault(); // Ngăn không cho form reload lại trang

    // Lấy giá trị từ các trường nhập liệu
    const ten = $('#tenDoThue').val().trim();
    const donGia = $('#donGiaDoThue').val().trim();
    const soLuong = parseInt($('#soLuongDoThue').val().trim());
    const imageFile = $('#imageDoThue')[0].files[0];

    // Xóa các thông báo lỗi trước đó
    $('#errorTenDoThue').text('');
    $('#errorSoLuongDoThue').text('');
    $('#errorDonGiaDoThue').text('');
    $('#errorImageDoThue').text('');

    // Biến để kiểm tra lỗi
    let hasError = false;

    // Kiểm tra các trường dữ liệu
    if (!ten) {
    $('#errorTenDoThue').text('Tên không được để trống.');
    hasError = true;
}

    if (isNaN(soLuong) || soLuong <= 0) {
    $('#errorSoLuongDoThue').text('Số lượng phải là số dương.');
    hasError = true;
}

    if (!donGia || isNaN(donGia) || donGia <= 0) {
    $('#errorDonGiaDoThue').text('Đơn giá phải là số dương.');
    hasError = true;
}

    // Kiểm tra tệp hình ảnh
    if (!imageFile) {
    $('#errorImageDoThue').text('Vui lòng chọn một tệp hình ảnh.');
    hasError = true;
}

    // Nếu có lỗi, không gửi form
    if (hasError) {
    return;
}

    // Xác định trạng thái dịch vụ
    let trangThai = soLuong === 0 ? 'Hết' : 'Còn';

    // Xác định URL API
    const apiUrl = 'http://localhost:8080/do_thue/save';
    const formData = new FormData();
    formData.append("tenDoThue", ten);
    formData.append("donGia", donGia);
    formData.append("soLuong", soLuong);
    formData.append("trangThai", trangThai);
    formData.append("imageFile", imageFile);

    // Hiển thị xác nhận trước khi gửi yêu cầu
    Swal.fire({
    title: 'Xác nhận',
    text: 'Bạn chắc chắn muốn thêm dịch vụ này?',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: 'Có',
    cancelButtonText: 'Hủy'
}).then((result) => {
    if (result.isConfirmed) {
    // Gửi yêu cầu AJAX để thêm dịch vụ
    $.ajax({
    url: apiUrl,
    type: 'POST',
    data: formData,
    contentType: false,
    processData: false,
    success: function (response) {
    // Hiển thị thông báo thành công
    Swal.fire({
    title: 'Thành công',
    text: 'Thêm dịch vụ thành công',
    icon: 'success'
}).then(() => {
    showSuccessToast('Thêm dịch vụ thành công');
    cancelAdd();
});
},
    error: function (xhr, status, error) {
    console.error('Có lỗi xảy ra:', error);
    // Hiển thị thông báo lỗi
    Swal.fire({
    title: 'Lỗi',
    text: 'Có lỗi xảy ra: ' + xhr.responseText,
    icon: 'error'
});
}
});
}
});
}

    // Hàm submit form cho nước uống
    function submitAddFormNuocUong(event) {
    event.preventDefault(); // Ngăn không cho form reload lại trang

    // Lấy giá trị từ các trường nhập liệu
    const ten = $('#tenNuocUong').val().trim();
    const donGia = $('#donGiaNuocUong').val().trim();
    const soLuong = parseInt($('#soLuongNuocUong').val().trim());
    const imageFile = $('#imageNuocUong')[0].files[0];

    // Xóa các thông báo lỗi trước đó
    $('#errorTenNuocUong').text('');
    $('#errorSoLuongNuocUong').text('');
    $('#errorDonGiaNuocUong').text('');
    $('#errorImageNuocUong').text('');

    // Biến để kiểm tra lỗi
    let hasError = false;

    // Kiểm tra các trường dữ liệu
    if (!ten) {
    $('#errorTenNuocUong').text('Tên không được để trống.');
    hasError = true;
}

    if (isNaN(soLuong) || soLuong <= 0) {
    $('#errorSoLuongNuocUong').text('Số lượng phải là số dương.');
    hasError = true;
}

    if (!donGia || isNaN(donGia) || donGia <= 0) {
    $('#errorDonGiaNuocUong').text('Đơn giá phải là số dương.');
    hasError = true;
}

    // Kiểm tra tệp hình ảnh
    if (!imageFile) {
    $('#errorImageNuocUong').text('Vui lòng chọn một tệp hình ảnh.');
    hasError = true;
}

    // Nếu có lỗi, không gửi form
    if (hasError) {
    return;
}

    // Xác định trạng thái dịch vụ
    let trangThai = soLuong === 0 ? 'Hết' : 'Còn';

    // Xác định URL API
    const apiUrl = 'http://localhost:8080/nuoc_uong/save';
    const formData = new FormData();
    formData.append("tenNuocUong", ten);
    formData.append("donGia", donGia);
    formData.append("soLuong", soLuong);
    formData.append("trangThai", trangThai);
    formData.append("imageFile", imageFile);

    // Hiển thị xác nhận trước khi gửi yêu cầu
    Swal.fire({
    title: 'Xác nhận',
    text: 'Bạn chắc chắn muốn thêm dịch vụ này?',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: 'Có',
    cancelButtonText: 'Hủy'
}).then((result) => {
    if (result.isConfirmed) {
    // Gửi yêu cầu AJAX để thêm dịch vụ
    $.ajax({
    url: apiUrl,
    type: 'POST',
    data: formData,
    contentType: false,
    processData: false,
    success: function (response) {
    // Hiển thị thông báo thành công
    Swal.fire({
    title: 'Thành công',
    text: 'Thêm dịch vụ thành công',
    icon: 'success'
}).then(() => {
    showSuccessToast('Thêm dịch vụ thành công');
    cancelAdd();
});
},
    error: function (xhr, status, error) {
    console.error('Có lỗi xảy ra:', error);
    // Hiển thị thông báo lỗi
    Swal.fire({
    title: 'Lỗi',
    text: 'Có lỗi xảy ra: ' + xhr.responseText,
    icon: 'error'
});
}
});
}
});
}


    //Edit
    // Hàm hiển thị form thêm dữ liệu
    function editItem(id) {
    const cardBody = $('#tableCardBody');
    const cardBody1 = $('#tableCardBody1');
    const apiUrl = `


            http://localhost:8080/$
                {
                    currentServiceType
                }
            /$
                {
                    id
                }


        `;

    // Ẩn cardBody1 và làm trống cardBody
    cardBody1.hide();
    cardBody.empty();

    // Gọi API để lấy dữ liệu
    $.get(apiUrl, function (data) {
    let formContent = '';
    let imageDisplay = '';

    // URL của ảnh hiện tại
    const imageUrl = `


            data:image/jpeg;base64,$
                {
                    data.imageData
                }


        `;

    if (currentServiceType === 'do_thue') {
    imageDisplay = `



                    <div class="form-group col-md-6">
                        <label>Ảnh Hiện Tại:</label>
                        <img id="currentImage" src="$
                {
                    imageUrl
                }
            " alt="Hình Ảnh" style="width: 100%; max-width: 300px;">
                    </div>



        `;
    formContent = `



                    <form id="updateDichVu" enctype="multipart/form-data">
                        <input type="text" class="form-control" id="id" value="$
                {
                    data.id
                }
            " hidden>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="tenDoThue">Tên</label>
                                <input type="text" class="form-control" id="tenDoThue" value="$
                {
                    data.tenDoThue
                }
            " placeholder="Tên">
                                <span id="errorTenDoThue" class="text-danger"></span>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="soLuongDoThue">Số lượng</label>
                                <div class="input-group mb-3">
                                    <input type="number" class="form-control" id="soLuongDoThue" value="$
                {
                    data.soLuong
                }
            ">
                                    <span class="input-group-text">#</span>
                                </div>
                                <span id="errorSoLuongDoThue" class="text-danger"></span>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="donGiaDoThue">Đơn giá</label>
                                <div class="input-group mb-3">
                                    <input type="number" step="0.01" class="form-control" id="donGiaDoThue" value="$
                {
                    data.donGia
                }
            " placeholder="Đơn giá" aria-label="Đơn giá" aria-describedby="basic-addon2">
                                    <span class="input-group-text" id="basic-addon2">VND</span>
                                </div>
                                <span id="errorDonGiaDoThue" class="text-danger"></span>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="trangThai">Trạng thái</label>
                                <div class="input-group mb-3">
                                    <input type="text" step="0.01" class="form-control" id="trangThai" value="$
                {
                    data.trangThai
                }
            "  aria-label="Đơn giá" aria-describedby="basic-addon2" disabled>
                                </div>
                                <span id="errorTrangThai" class="text-danger"></span>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="imageDoThue" class="form-label">Chọn Ảnh</label>
                                <input class="form-control" type="file" id="imageDoThue" onchange="previewImage(event)">
                                <span id="errorImageDoThue" class="text-danger"></span>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary" onclick="submitUpdateFormDoThue(event, $
                {
                    data.id
                }
            )">Lưu</button>
                        <button type="button" class="btn btn-success" onclick="cancelAdd()">Hủy</button>
                    </form>



        `;
} else if (currentServiceType === 'nuoc_uong') {
    imageDisplay = `



                    <div class="form-group col-md-6">
                        <label>Ảnh Hiện Tại:</label>
                        <img id="currentImage" src="$
                {
                    imageUrl
                }
            " alt="Hình Ảnh" style="width: 100%; max-width: 300px;">
                    </div>



        `;
    formContent = `



                    <form id="updateDichVu" enctype="multipart/form-data">
                        <input type="text" class="form-control" id="id" value="$
                {
                    data.id
                }
            " hidden>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="tenNuocUong">Tên</label>
                                <input type="text" class="form-control" id="tenNuocUong" value="$
                {
                    data.tenNuocUong
                }
            " placeholder="Tên">
                                <span id="errorTenNuocUong" class="text-danger"></span>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="soLuongNuocUong">Số lượng</label>
                                <div class="input-group mb-3">
                                    <input type="number" class="form-control" id="soLuongNuocUong" value="$
                {
                    data.soLuong
                }
            ">
                                    <span class="input-group-text">#</span>
                                </div>
                                <span id="errorSoLuongNuocUong" class="text-danger"></span>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="donGiaNuocUong">Đơn giá</label>
                                <div class="input-group mb-3">
                                    <input type="number" step="0.01" class="form-control" id="donGiaNuocUong" value="$
                {
                    data.donGia
                }
            " placeholder="Đơn giá" aria-label="Đơn giá" aria-describedby="basic-addon2">
                                    <span class="input-group-text" id="basic-addon2">VND</span>
                                </div>
                                <span id="errorDonGiaNuocUong" class="text-danger"></span>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="trangThai">Trạng thái</label>
                                <div class="input-group mb-3">
                                    <input type="number" step="0.01" class="form-control" id="trangThai" value="$
                {
                    data.trangThai
                }
            " aria-label="Đơn giá" aria-describedby="basic-addon2" disabled>
                                </div>
                                <span id="errorTrangThai" class="text-danger"></span>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label for="imageNuocUong" class="form-label">Chọn Ảnh</label>
                                <input class="form-control" type="file" id="imageNuocUong" onchange="previewImage(event)">
                                <span id="errorImageNuocUong" class="text-danger"></span>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary" onclick="submitUpdateFormNuocUong(event, $
                {
                    data.id
                }
            )">Lưu</button>
                        <button type="button" class="btn btn-success" onclick="cancelAdd()">Hủy</button>
                    </form>



        `;
}

    // Thêm phần hiển thị ảnh vào formContent
    cardBody.html(`



                <div class="row">
                    <div class="col-md-6">
        $
                {
                    formContent
                }

                    </div>
                    <div class="col-md-6">
        $
                {
                    imageDisplay
                }

                    </div>
                </div>



        `);
});
}


    // Hàm preview ảnh
    function previewImage(event) {
    const input = event.target;
    const file = input.files[0];
    const reader = new FileReader();

    reader.onload = function (e) {
    // Cập nhật hình ảnh hiện tại
    const currentImage = document.getElementById('currentImage');
    if (currentImage) {
    currentImage.src = e.target.result;
}
}

    if (file) {
    reader.readAsDataURL(file);
}
}

    // Submit form update cho dịch vụ cho thuê

    function submitUpdateFormDoThue(event, id) {
    event.preventDefault(); // Ngăn không cho form reload lại trang

    // Lấy giá trị từ các trường nhập liệu
    const ten = $('#tenDoThue').val().trim();
    const donGia = $('#donGiaDoThue').val().trim();
    let trangThai = $('#trangThai').val().trim();
    const soLuong = parseInt($('#soLuongDoThue').val().trim());
    const imageData = $('#imageDoThue')[0].files[0];

    // Xóa các thông báo lỗi trước đó
    $('#errorTenDoThue').text('');
    $('#errorSoLuongDoThue').text('');
    $('#errorDonGiaDoThue').text('');
    $('#errorImageDoThue').text('');

    // Biến để kiểm tra lỗi
    let hasError = false;

    // Kiểm tra các trường dữ liệu
    if (!ten) {
    $('#errorTenDoThue').text('Tên không được để trống.');
    hasError = true;
}

    if (isNaN(soLuong) || soLuong < 0) {
    $('#errorSoLuongDoThue').text('Số lượng phải là số dương.');
    hasError = true;
}

    if (!donGia || isNaN(donGia) || donGia <= 0) {
    $('#errorDonGiaDoThue').text('Đơn giá phải là số dương.');
    hasError = true;
}

    // Nếu có lỗi, không gửi form
    if (hasError) {
    return;
}
    // Đọc tệp tin hình ảnh và chuyển đổi thành base64
    const fileInput = document.getElementById('imageDoThue');
    let imageFile = null;

    if (fileInput.files.length > 0) {
    imageFile = fileInput.files[0];
}

    // Xác định trạng thái dịch vụ
    trangThai = soLuong === 0 ? 'Hết' : 'Còn';

    // Tạo đối tượng dữ liệu để gửi
    let requestData = {
    id: id,
    tenDoThue: ten,
    donGia: donGia,
    soLuong: soLuong,
    trangThai: trangThai
};

    // Chuyển đổi đối tượng dữ liệu thành JSON
    const jsonData = JSON.stringify(requestData);

    // Tạo đối tượng FormData để gửi dữ liệu
    const formData = new FormData();
    formData.append("doThueDTO", jsonData);
    if (imageFile) {
    formData.append("imageFile", imageFile);
}

    // Hiển thị xác nhận trước khi gửi yêu cầu
    Swal.fire({
    title: 'Xác nhận',
    text: 'Bạn chắc chắn muốn sửa đồ thuê này?',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: 'Có',
    cancelButtonText: 'Hủy'
}).then((result) => {
    if (result.isConfirmed) {
    // Gửi yêu cầu AJAX để cập nhật dữ liệu
    $.ajax({
    url: `


            http://localhost:8080/do_thue/$
                {
                    id
                }


        `,
    type: 'PUT',
    data: formData,
    contentType: false,
    processData: false,
    success: function (response) {
    // Hiển thị thông báo thành công
    Swal.fire({
    title: 'Thành công',
    text: 'Sửa đồ thuê thành công',
    icon: 'success'
}).then(() => {
    showSuccessToast('Sửa đồ thuê thành công');
    cancelAdd();
});
},
    error: function (xhr, status, error) {
    console.error('Có lỗi xảy ra:', error);
    // Hiển thị thông báo lỗi
    Swal.fire({
    title: 'Lỗi',
    text: 'Có lỗi xảy ra: ' + xhr.responseText,
    icon: 'error'
});
}
});
}
});
}

    // Submit form update cho nước uống
    function submitUpdateFormDoThue(event, id) {
    event.preventDefault(); // Ngăn không cho form reload lại trang

    // Lấy giá trị từ các trường nhập liệu
    const ten = $('#tenDoThue').val().trim();
    const donGia = $('#donGiaDoThue').val().trim();
    let trangThai = $('#trangThai').val().trim();
    const soLuong = parseInt($('#soLuongDoThue').val().trim());
    const imageData = $('#imageDoThue')[0].files[0];

    // Xóa các thông báo lỗi trước đó
    $('#errorTenDoThue').text('');
    $('#errorSoLuongDoThue').text('');
    $('#errorDonGiaDoThue').text('');
    $('#errorImageDoThue').text('');

    // Biến để kiểm tra lỗi
    let hasError = false;

    // Kiểm tra các trường dữ liệu
    if (!ten) {
    $('#errorTenDoThue').text('Tên không được để trống.');
    hasError = true;
}

    if (isNaN(soLuong) || soLuong < 0) {
    $('#errorSoLuongDoThue').text('Số lượng phải là số dương.');
    hasError = true;
}

    if (!donGia || isNaN(donGia) || donGia < 0) {
    $('#errorDonGiaDoThue').text('Đơn giá phải là số dương.');
    hasError = true;
}

    // Nếu có lỗi, không gửi form
    if (hasError) {
    return;
}
    // Đọc tệp tin hình ảnh và chuyển đổi thành base64
    const fileInput = document.getElementById('imageDoThue');
    let imageFile = null;

    if (fileInput.files.length > 0) {
    imageFile = fileInput.files[0];
}

    // Xác định trạng thái dịch vụ
    trangThai = soLuong === 0 ? 'Hết' : 'Còn';

    // Tạo đối tượng dữ liệu để gửi
    let requestData = {
    id: id,
    tenDoThue: ten,
    donGia: donGia,
    soLuong: soLuong,
    trangThai: trangThai
};

    // Chuyển đổi đối tượng dữ liệu thành JSON
    const jsonData = JSON.stringify(requestData);

    // Tạo đối tượng FormData để gửi dữ liệu
    const formData = new FormData();
    formData.append("doThueDTO", jsonData);
    if (imageFile) {
    formData.append("imageFile", imageFile);
}

    // Hiển thị xác nhận trước khi gửi yêu cầu
    Swal.fire({
    title: 'Xác nhận',
    text: 'Bạn chắc chắn muốn sửa đồ thuê này?',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: 'Có',
    cancelButtonText: 'Hủy'
}).then((result) => {
    if (result.isConfirmed) {
    // Gửi yêu cầu AJAX để cập nhật dữ liệu
    $.ajax({
    url: `


            http://localhost:8080/do_thue/$
                {
                    id
                }


        `,
    type: 'PUT',
    data: formData,
    contentType: false,
    processData: false,
    success: function (response) {
    // Hiển thị thông báo thành công
    Swal.fire({
    title: 'Thành công',
    text: 'Sửa đồ thuê thành công',
    icon: 'success'
}).then(() => {
    showSuccessToast('Sửa đồ thuê thành công');
    cancelAdd();
});
},
    error: function (xhr, status, error) {
    console.error('Có lỗi xảy ra:', error);
    // Hiển thị thông báo lỗi
    Swal.fire({
    title: 'Lỗi',
    text: 'Có lỗi xảy ra: ' + xhr.responseText,
    icon: 'error'
});
}
});
}
});
}


    //Cancel Add

    function cancelAdd() {
    // Lưu loại dịch vụ hiện tại vào localStorage
    localStorage.setItem('currentServiceType', currentServiceType);

    const tableBodyCard = document.getElementById('tableCardBody');
    const tableBodyCard1 = document.getElementById('tableCardBody1');

    // Hiển thị lại card body ban đầu
    fetchPriceRange(currentServiceType);
    loadData(currentServiceType); // Tải dữ liệu mặc định
    tableBodyCard1.style.display = 'block';

    // Xóa nội dung và đảm bảo rằng form vẫn nằm yên trong hàng
    tableBodyCard.innerHTML = `



                <div class="toolbar row mb-3 mt-3">
                                            <div class="col-md-12">
                                                <form class="form-inline" id="filterDateForm"
                                                      style="display: flex; justify-content: space-between; align-items: center;">
                                                    <div class="form-group d-flex" style="flex-grow: 1;">
<!-- Search -->
        <div class="form-group" style="flex-grow: 1;">
            <label for="search">Tìm kiếm</label>
            <input class="form-control ml-3" id="search"
                   oninput="searchDV(this.value)" placeholder="Tìm kiếm theo tên"
                   style="width: 180px;" type="text">
        </div>
        <!-- Price Range -->
        <label class="form-label" for="customRange2"
               style="margin-left:-65px;margin-right: 10px">Khoảng giá</label>
        <div class="form-group" style="flex-grow: 1;">
            <div class="range-container" style="position: relative; width: 100%;">
                <input class="form-range" id="customRange2" max="1000" min="0"
                       oninput="updatePriceRange(this.value)"
                       step="10" type="range" value="500">
                    <div class="range-labels d-flex justify-content-between">
                        <span id="rangeMin">0</span>
                        <span id="rangeValue"
                              style="position: absolute; left: 50%; transform: translateX(-50%);">500</span>
                        <span id="rangeMax">1000</span>
                    </div>
            </div>
        </div>


        <div class="dropdown ml-3 mr-2">
            <button aria-expanded="false"
                    aria-haspopup="true"
                    class="btn btn-outline-success dropdown-toggle"
                    data-toggle="dropdown" id="statusFilterButton"
                    type="button">Trạng thái
            </button>
            <div aria-labelledby="statusFilterButton" class="dropdown-menu"
                 id="statusFilter">
                <a class="dropdown-item" href="#"
                   onclick="setStatusFilterDV('Còn')">Còn</a>
                <a class="dropdown-item" href="#"
                   onclick="setStatusFilterDV('Hết')">Hết</a>
            </div>
        </div>
    </div>
        <div class="form-group d-flex gap-2">
            <div>
                <button aria-expanded="false"
                        aria-haspopup="true"
                        class="btn btn-outline-success dropdown-toggle"
                        data-toggle="dropdown" id="exportExcelButton"
                        type="button" title="Xuất Excel">
                    <span class="fe fe-file-text"></span>
                </button>
                <div aria-labelledby="exportExcelButton" class="dropdown-menu"
                     id="exportExcel">
                    <a class="dropdown-item" href="#"
                       onclick="exportToExcel('do_thue')">Đồ thuê</a>
                    <a class="dropdown-item" href="#"
                       onclick="exportToExcel('nuoc_uong')">Nước uống</a>
                </div>
            </div>
            <div>
                <button aria-expanded="false"
                        aria-haspopup="true"
                        class="btn btn-outline-success dropdown-toggle"
                        data-toggle="dropdown" id="templateExcelButton"
                        type="button" title="Download Template">
                    <span class="fe fe-download"></span>
                </button>
                <div aria-labelledby="templateExcelButton" class="dropdown-menu"
                     id="templateExcel">
                    <a class="dropdown-item" href="#"
                       onclick="downloadTemplate('nuoc-uong')">Đồ thuê</a>
                    <a class="dropdown-item" href="#"
                       onclick="downloadTemplate('do-thue')">Nước uống</a>
                </div>
            </div>
            <div>
                <button aria-expanded="false"
                        aria-haspopup="true"
                        class="btn btn-outline-success dropdown-toggle"
                        data-toggle="dropdown" id="importExcelButton"
                        type="button" title="Nhập Excel">
                    <span class="fe fe-folder-plus"></span>
                </button>
                <div aria-labelledby="importExcelButton" class="dropdown-menu" id="importExcel">
                    <a class="dropdown-item" href="#" onclick="openFileDialog('nuoc-uong')">Nước uống</a>
                    <a class="dropdown-item" href="#" onclick="openFileDialog('do-thue')">Đồ thuê</a>
                </div>
            </div>

            <input type="file" id="fileInput" style="display: none;" onchange="handleFileChange(event)">

                <button class="btn btn-primary" onclick="resetFilters()"
                        type="button" title="Reset">
                    <span class="fe fe-refresh-ccw"></span>
                </button>
                <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                    <button class="btn btn-success"
                            data-service-type="do_thue"
                            onclick="showAddForm(this)"
                            type="button" title="Thêm">
                        <span class="fe fe-plus"></span>
                    </button>
                </div>
        </div>
    </form>
    </div>
    </div>
    `;
}


