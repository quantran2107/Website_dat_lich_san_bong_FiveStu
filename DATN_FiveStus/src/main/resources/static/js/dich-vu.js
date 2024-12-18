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
    await loadData(currentServiceType); // Load default data
    hideLoading();
    changeTab(currentServiceType);
});

let currentServiceType = 'do_thue';
let currentSearchQuery = '';
let currentStatusFilter = '';

const pageSize = 10;

async function fetchData(apiUrl) {
    try {
        const response = await fetch(apiUrl);
        if (!response.ok) throw new Error('Network response was not ok');
        const data = await response.json();
        return data;
    } catch (error) {
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

        // Kiểm tra số lượng để xác định trạng thái
        let trangThai = item.trangThai;
        if (item.soLuongs && item.soLuongs > 0) {
            trangThai = 'Còn'; // Nếu số lượng > 0 thì trạng thái là "Còn"
        } else {
            trangThai = 'Hết'; // Nếu số lượng <= 0 thì trạng thái là "Hết"
        }

        // Cập nhật nội dung của hàng trong bảng
        row.innerHTML = `
            <td>${index + 1 + page * size}</td>
            <td>${item.tenDoThue || item.tenNuocUong}</td>
            <td>
                <img src="${item.imageData}" alt="${item.tenDoThue || item.tenNuocUong || 'No image'}"
                style="width: 70px; height: 70px; object-fit: cover;">
            </td>
            <td>${item.donGias ? item.donGias.toLocaleString() + ' VND' : 'N/A'}</td>
            <td>${item.soLuongs || '0'}</td>
            <td>
                <span class="${trangThai === 'Còn' ? 'custom-4' : 'custom-3'}"
                style="font-size: 14px; width: 84px;">
                ${trangThai}
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
function searchDV(query) {
    currentSearchQuery = query;
    loadData(currentServiceType, 0);
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
        const singlePageLi = document.createElement('li');
        singlePageLi.className = 'page-item active'; // Nổi bật trang hiện tại
        const singlePageA = document.createElement('a');
        singlePageA.className = 'page-link';
        singlePageA.href = '#';
        singlePageA.textContent = '1'; // Hiển thị số trang
        singlePageLi.appendChild(singlePageA);
        fragment.appendChild(singlePageLi);
        pagination.appendChild(fragment);
        return;
    }

    // Tạo một div chứa các nút "Lùi", "Dropdown", "Tiến"
    const navContainer = document.createElement('div');
    navContainer.className = 'd-flex justify-content-center align-items-center mt-2';

    // Nút "Lùi"
    const prevButton = document.createElement('button');
    prevButton.className = 'btn btn-success';
    prevButton.textContent = '<';
    prevButton.disabled = currentPage === 0; // Vô hiệu hóa nếu đã là trang đầu tiên
    prevButton.addEventListener('click', (event) => {
        event.preventDefault();
        if (currentPage > 0) {
            loadData(currentServiceType, currentPage - 1);
        }
    });

    // Tạo dropdown (select) cho việc chọn trang
    const select = document.createElement('select');
    select.className = 'custom-select mx-1'; // Thêm margin để tạo khoảng cách với các nút
    for (let i = 0; i < totalPages; i++) {
        const option = document.createElement('option');
        option.value = i;
        option.textContent = `Trang ${i + 1}`;
        if (i === currentPage) {
            option.selected = true; // Đánh dấu trang hiện tại
        }
        select.appendChild(option);
    }

    // Thêm sự kiện thay đổi trang khi chọn trong dropdown
    select.addEventListener('change', (event) => {
        const selectedPage = parseInt(event.target.value);
        loadData(currentServiceType, selectedPage); // Gọi hàm loadData để tải dữ liệu trang mới
    });

    // Nút "Tiến"
    const nextButton = document.createElement('button');
    nextButton.className = 'btn btn-success';
    nextButton.textContent = '>';
    nextButton.disabled = currentPage === totalPages - 1; // Vô hiệu hóa nếu đã là trang cuối cùng
    nextButton.addEventListener('click', (event) => {
        event.preventDefault();
        if (currentPage < totalPages - 1) {
            loadData(currentServiceType, currentPage + 1);
        }
    });

    // Thêm các phần tử vào container chỉ nếu có trang trước và sau
    if (currentPage > 0 && currentPage < totalPages - 1) {
        // Nếu có cả trang trước và sau, hiển thị đầy đủ
        navContainer.appendChild(prevButton); // Thêm nút "Lùi"
        navContainer.appendChild(select); // Thêm dropdown
        navContainer.appendChild(nextButton); // Thêm nút "Tiến"
    } else if (currentPage === 0) {
        // Chỉ hiển thị dropdown và "Tiến" khi đang ở trang đầu
        navContainer.appendChild(select);
        navContainer.appendChild(nextButton);
    } else if (currentPage === totalPages - 1) {
        // Chỉ hiển thị dropdown và "Lùi" khi đang ở trang cuối
        navContainer.appendChild(prevButton);
        navContainer.appendChild(select);
    }

    // Thêm container vào phân trang
    fragment.appendChild(navContainer);
    pagination.appendChild(fragment); // Thêm tất cả vào phân trang
}

function showLoading() {
    document.getElementById('loadingOverlay').classList.remove('loading-hidden');
}

function hideLoading() {
    document.getElementById('loadingOverlay').classList.add('loading-hidden');
}

function setStatusFilterDV(status) {
    currentStatusFilter = status;
    document.getElementById('statusFilterButton').textContent = status;
    loadData(currentServiceType, 0); // Load data with updated status filter
}

function resetFilters() {
    // Đặt lại các bộ lọc
    currentSearchQuery = '';
    currentStatusFilter = '';

    // Cập nhật giao diện
    document.getElementById('search').value = '';
    document.getElementById('statusFilterButton').textContent = 'Trạng thái';

    // Tải lại dữ liệu không có bộ lọc
    loadData(currentServiceType, 0);
}

function changeTab(serviceType) {
    currentServiceType = serviceType;
    loadData(currentServiceType)
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
                body: JSON.stringify({deletedAt: true}) // Gửi body với trường deletedAt
            })
                .then(response => {
                    if (response.ok) {
                        Swal.fire({
                            title: 'Thành công',
                            text: 'Xóa dịch vụ thành công',
                            icon: 'success'
                        }).then(() => {
                            loadData(currentServiceType);//(Bản chất cái này thực hiện xóa được nhưng giao diện vẫn dữ nguyên cái mình vừa xóa, phải reload mới xóa đc)
                            // window.location.reload();(Buộc phải reload lại thì mới mất được dữ liệu trên form)
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
                    <td>${item.donGias ? item.donGias.toLocaleString() + ' VND' : 'N/A'}</td>
                    <td>${item.soLuongs || '0'}</td>
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
        }
    } catch (error) {
    }
}

// Hàm hiển thị form thêm dữ liệu
function showAddForm() {
    const cardBody = $('#tableCardBody');
    const cardBody1 = $('#tableCardBody1');

    // Ẩn cardBody1 và làm trống cardBody
    cardBody1.hide();
    cardBody.empty();

    // Tạo giao diện tab và form động
    const tabs = `
        <ul class="nav nav-tabs" id="myTab" role="tablist">
            <li class="nav-item" role="presentation">
                <a class="nav-link ${currentServiceType === 'do_thue' ? 'active' : ''}" 
                   id="tabDoThue-tab" data-toggle="tab" href="#tabDoThue" role="tab" 
                   aria-controls="tabDoThue" aria-selected="${currentServiceType === 'do_thue'}">
                   Quản lý đồ thuê
                </a>
            </li>
            <li class="nav-item" role="presentation">
                <a class="nav-link ${currentServiceType === 'nuoc_uong' ? 'active' : ''}" 
                   id="tabNuocUong-tab" data-toggle="tab" href="#tabNuocUong" role="tab" 
                   aria-controls="tabNuocUong" aria-selected="${currentServiceType === 'nuoc_uong'}">
                   Quản lý nước uống
                </a>
            </li>
        </ul>
        <div class="tab-content mt-3" id="myTabContent">
            <!-- Tab Đồ Thuê -->
            <div class="tab-pane fade ${currentServiceType === 'do_thue' ? 'show active' : ''}" id="tabDoThue" role="tabpanel" aria-labelledby="tabDoThue-tab">
                <div class="row">
                    <!-- Phần nhập liệu bên trái -->
                    <div class="col-md-6">
                        <form id="addFormDoThue" enctype="multipart/form-data">
                            <div class="form-group">
                                <label for="tenDoThue">Tên</label>
                                <input type="text" class="form-control" id="tenDoThue" placeholder="Nhập tên">
                                <span id="errorTenDoThue" class="text-danger"></span>
                            </div>
                            <div class="form-group">
                                <label for="soLuongsDoThue">Số lượng</label>
                                <div class="input-group">
                                    <input type="number" class="form-control" id="soLuongsDoThue" placeholder="Nhập số lượng">
                                    <span class="input-group-text">#</span>
                                </div>
                                <span id="errorsoLuongsDoThue" class="text-danger"></span>
                            </div>
                            <div class="form-group">
                                <label for="donGiasDoThue">Đơn giá</label>
                                <div class="input-group">
                                    <input type="number" class="form-control" id="donGiasDoThue" placeholder="Nhập đơn giá">
                                    <span class="input-group-text">VND</span>
                                </div>
                                <span id="errordonGiasDoThue" class="text-danger"></span>
                            </div>
                            <div class="form-group">
                                <label for="imageDoThue">Chọn Ảnh</label>
                                <input class="form-control" type="file" id="imageDoThue" onchange="previewImage1(event, 'imagePreviewDoThue')">
                                <span id="errorImageDoThue" class="text-danger"></span>
                            </div>
                            <div class="form-group mt-3">
                                <button type="submit" class="btn btn-primary" onclick="submitAddFormDoThue(event)">Lưu</button>
                                <button type="button" class="btn btn-secondary" onclick="cancelAdd()">Hủy</button>
                            </div>
                        </form>
                    </div>
                    <!-- Phần hiển thị ảnh bên phải -->
                    <div class="col-md-6 d-flex align-items-center justify-content-center">
                        <img id="imagePreviewDoThue" src="" alt="Ảnh xem trước" style="max-width: 100%; max-height: 300px;"/>
                    </div>
                </div>
            </div>

            <!-- Tab Nước Uống -->
            <div class="tab-pane fade ${currentServiceType === 'nuoc_uong' ? 'show active' : ''}" id="tabNuocUong" role="tabpanel" aria-labelledby="tabNuocUong-tab">
                <div class="row">
                    <!-- Phần nhập liệu bên trái -->
                    <div class="col-md-6">
                        <form id="addFormNuocUong" enctype="multipart/form-data">
                            <div class="form-group">
                                <label for="tenNuocUong">Tên</label>
                                <input type="text" class="form-control" id="tenNuocUong" placeholder="Nhập tên">
                                <span id="errorTenNuocUong" class="text-danger"></span>
                            </div>
                            <div class="form-group">
                                <label for="soLuongsNuocUong">Số lượng</label>
                                <div class="input-group">
                                    <input type="number" class="form-control" id="soLuongsNuocUong" placeholder="Nhập số lượng">
                                    <span class="input-group-text">#</span>
                                </div>
                                <span id="errorsoLuongsNuocUong" class="text-danger"></span>
                            </div>
                            <div class="form-group">
                                <label for="donGiasNuocUong">Đơn giá</label>
                                <div class="input-group">
                                    <input type="number" class="form-control" id="donGiasNuocUong" placeholder="Nhập đơn giá">
                                    <span class="input-group-text">VND</span>
                                </div>
                                <span id="errordonGiasNuocUong" class="text-danger"></span>
                            </div>
                            <div class="form-group">
                                <label for="imageNuocUong">Chọn Ảnh</label>
                                <input class="form-control" type="file" id="imageNuocUong" onchange="previewImage1(event, 'imagePreviewNuocUong')">
                                <span id="errorImageNuocUong" class="text-danger"></span>
                            </div>
                            <div class="form-group mt-3">
                                <button type="submit" class="btn btn-primary" onclick="submitAddFormNuocUong(event)">Lưu</button>
                                <button type="button" class="btn btn-secondary" onclick="cancelAdd()">Hủy</button>
                            </div>
                        </form>
                    </div>
                    <!-- Phần hiển thị ảnh bên phải -->
                    <div class="col-md-6 d-flex align-items-center justify-content-center">
                        <img id="imagePreviewNuocUong" src="" alt="Ảnh xem trước" style="max-width: 100%; max-height: 300px;"/>
                    </div>
                </div>
            </div>
        </div>
    `;

    // Chèn giao diện tab vào cardBody
    cardBody.html(tabs);

    // Kích hoạt tab hiện tại
    $('#myTab a[href="#' + currentServiceType + '"]').tab('show');
}

function previewImage1(event, imgPreviewId) {
    const input = event.target;
    const file = input.files[0];
    const imgPreview = document.getElementById(imgPreviewId);

    if (file) {
        const reader = new FileReader();

        reader.onload = function (e) {
            imgPreview.src = e.target.result;
        };

        reader.readAsDataURL(file);
    } else {
        imgPreview.src = ''; // Xóa ảnh xem trước nếu không có file
    }
}

// Hàm submit form cho đồ thuê
function submitAddFormDoThue(event) {
    event.preventDefault(); // Ngăn không cho form reload lại trang

    // Lấy giá trị từ các trường nhập liệu
    const ten = $('#tenDoThue').val().trim();
    const donGias = $('#donGiasDoThue').val().trim();
    const soLuongs = parseInt($('#soLuongsDoThue').val().trim());
    const imageFile = $('#imageDoThue')[0].files[0];

    // Xóa các thông báo lỗi trước đó
    $('#errorTenDoThue').text('');
    $('#errorsoLuongsDoThue').text('');
    $('#errordonGiasDoThue').text('');
    $('#errorImageDoThue').text('');

    // Biến để kiểm tra lỗi
    let hasError = false;

    // Kiểm tra các trường dữ liệu
    if (!ten) {
        $('#errorTenDoThue').text('Tên không được để trống.');
        hasError = true;
    }

    // Chỉ báo lỗi khi số lượng là số âm hoặc không phải là số hợp lệ
    if (isNaN(soLuongs) || soLuongs < 0) {
        $('#errorsoLuongsDoThue').text('Số lượng không được nhỏ hơn 0.');
        hasError = true;
    }

    // Kiểm tra đơn giá, phải là số dương
    if (!donGias || isNaN(donGias) || donGias <= 0) {
        $('#errordonGiasDoThue').text('Đơn giá phải là số dương.');
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
    let trangThai = soLuongs === 0 ? 'Hết' : 'Còn';

    // Xác định URL API
    const apiUrl = 'http://localhost:8080/do_thue/save';
    const formData = new FormData();
    formData.append("tenDoThue", ten);
    formData.append("donGias", donGias);
    formData.append("soLuongs", soLuongs);
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
    const donGias = $('#donGiasNuocUong').val().trim();
    const soLuongs = parseInt($('#soLuongsNuocUong').val().trim());
    const imageFile = $('#imageNuocUong')[0].files[0];

    // Xóa các thông báo lỗi trước đó
    $('#errorTenNuocUong').text('');
    $('#errorsoLuongsNuocUong').text('');
    $('#errordonGiasNuocUong').text('');
    $('#errorImageNuocUong').text('');

    // Biến để kiểm tra lỗi
    let hasError = false;

    // Kiểm tra các trường dữ liệu
    if (!ten) {
        $('#errorTenNuocUong').text('Tên không được để trống.');
        hasError = true;
    }

    if (isNaN(soLuongs) || soLuongs <= 0) {
        $('#errorsoLuongsNuocUong').text('Số lượng phải là số dương.');
        hasError = true;
    }

    if (!donGias || isNaN(donGias) || donGias <= 0) {
        $('#errordonGiasNuocUong').text('Đơn giá phải là số dương.');
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
    let trangThai = soLuongs === 0 ? 'Hết' : 'Còn';

    // Xác định URL API
    const apiUrl = 'http://localhost:8080/nuoc_uong/save';
    const formData = new FormData();
    formData.append("tenNuocUong", ten);
    formData.append("donGias", donGias);
    formData.append("soLuongs", soLuongs);
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


// Hàm hiển thị form thêm dữ liệu
function editItem(id) {
    const cardBody = $('#tableCardBody');
    const cardBody1 = $('#tableCardBody1');
    const apiUrl = `http://localhost:8080/${currentServiceType}/${id}`;

    // Ẩn cardBody1 và làm trống cardBody
    cardBody1.hide();
    cardBody.empty();

    // Gọi API để lấy dữ liệu
    $.get(apiUrl, function (data) {
        let formContent = '';
        let imageDisplay = '';

        // URL của ảnh hiện tại
        const imageUrl = `${data.imageData}`;

        if (currentServiceType === 'do_thue') {
            imageDisplay = `
        <div class="form-group col-md-6">
            <label>Ảnh Hiện Tại:</label>
            <img id="currentImage" src="${imageUrl || ''}" alt="Hình Ảnh" style="width: 100%; max-width: 300px;">
        </div>`;
            formContent = `
        <form id="updateDichVu" enctype="multipart/form-data">
            <input type="text" class="form-control" id="id" value="${data.id}" hidden>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="tenDoThue">Tên</label>
                    <input type="text" class="form-control" id="tenDoThue" value="${data.tenDoThue}" placeholder="Tên" required>
                    <span id="errorTenDoThue" class="text-danger"></span>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="soLuongsDoThue">Số lượng</label>
                    <div class="input-group mb-3">
                        <input type="number" class="form-control" id="soLuongsDoThue" value="${data.soLuongs}" required>
                        <span class="input-group-text">#</span>
                    </div>
                    <span id="errorsoLuongsDoThue" class="text-danger"></span>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="donGiasDoThue">Đơn giá</label>
                    <div class="input-group mb-3">
                        <input type="number" step="0.01" class="form-control" id="donGiasDoThue" value="${data.donGias}" placeholder="Đơn giá" aria-label="Đơn giá" aria-describedby="basic-addon2" required>
                        <span class="input-group-text" id="basic-addon2">VND</span>
                    </div>
                    <span id="errordonGiasDoThue" class="text-danger"></span>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="trangThai">Trạng thái</label>
                    <div class="input-group mb-3">
                        <input type="text" class="form-control" id="trangThai" value="${data.trangThai}" disabled>
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
            <button type="submit" class="btn btn-primary" onclick="submitUpdateFormDoThue(event, ${data.id})">Lưu</button>
            <button type="button" class="btn btn-success" onclick="cancelAdd()">Hủy</button>
        </form>`;
        } else if (currentServiceType === 'nuoc_uong') {
            imageDisplay = `
        <div class="form-group col-md-6">
            <label>Ảnh Hiện Tại:</label>
            <img id="currentImage" src="${imageUrl || ''}" alt="Hình Ảnh" style="width: 100%; max-width: 300px;">
        </div>`;
            formContent = `
        <form id="updateDichVu" enctype="multipart/form-data">
            <input type="text" class="form-control" id="id" value="${data.id}" hidden>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="tenNuocUong">Tên</label>
                    <input type="text" class="form-control" id="tenNuocUong" value="${data.tenNuocUong}" placeholder="Tên" required>
                    <span id="errorTenNuocUong" class="text-danger"></span>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="soLuongsNuocUong">Số lượng</label>
                    <div class="input-group mb-3">
                        <input type="number" class="form-control" id="soLuongsNuocUong" value="${data.soLuongs}" required>
                        <span class="input-group-text">#</span>
                    </div>
                    <span id="errorsoLuongsNuocUong" class="text-danger"></span>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="donGiasNuocUong">Đơn giá</label>
                    <div class="input-group mb-3">
                        <input type="number" step="0.01" class="form-control" id="donGiasNuocUong" value="${data.donGias}" placeholder="Đơn giá" aria-label="Đơn giá" aria-describedby="basic-addon2" required>
                        <span class="input-group-text" id="basic-addon2">VND</span>
                    </div>
                    <span id="errordonGiasNuocUong" class="text-danger"></span>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="trangThai">Trạng thái</label>
                    <div class="input-group mb-3">
                        <input type="text" class="form-control" id="trangThai" value="${data.trangThai}" disabled>
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
            <button type="submit" class="btn btn-primary" onclick="submitUpdateFormNuocUong(event, ${data.id})">Lưu</button>
            <button type="button" class="btn btn-success" onclick="cancelAdd()">Hủy</button>
        </form>`;
        }

// Thêm phần hiển thị ảnh vào formContent
        cardBody.html(`
    <div class="row">
        <div class="col-md-6">${formContent}</div>
        <div class="col-md-6">${imageDisplay}</div>
    </div>`);

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
    };

    if (file) {
        reader.readAsDataURL(file);
    }
}

// Submit form update cho dịch vụ cho thuê
function submitUpdateFormDoThue(event, id) {
    event.preventDefault(); // Ngăn không cho form reload lại trang

    // Kiểm tra xem `id` có hợp lệ không
    if (!id) {
        Swal.fire({
            title: 'Lỗi!',
            text: 'ID không hợp lệ!',
            icon: 'error'
        });
        return;
    }

    // Lấy giá trị từ các trường nhập liệu
    const ten = $('#tenDoThue').val().trim();
    const soLuongs = parseInt($('#soLuongsDoThue').val().trim());
    const donGias = parseFloat($('#donGiasDoThue').val().trim());
    const imageFile = $('#imageDoThue')[0].files[0]; // Thay đổi biến tên cho rõ ràng hơn

    // Xóa các thông báo lỗi trước đó
    $('#errorTenDoThue').text('');
    $('#errorsoLuongsDoThue').text('');
    $('#errordonGiasDoThue').text('');
    $('#errorImageDoThue').text('');

    // Biến để kiểm tra lỗi
    let hasError = false;

    // Kiểm tra các trường nhập liệu
    if (!ten) {
        $('#errorTenDoThue').text('Tên không được để trống');
        hasError = true;
    }

    if (isNaN(soLuongs) || soLuongs < 0) {
        $('#errorsoLuongsDoThue').text('Số lượng phải là một số dương');
        hasError = true;
    }

    if (isNaN(donGias) || donGias <= 0) {
        $('#errordonGiasDoThue').text('Đơn giá phải là một số dương');
        hasError = true;
    }

    if (hasError) {
        return; // Dừng lại nếu có lỗi
    }

    // Tạo FormData để gửi dữ liệu, bao gồm file ảnh
    const formData = new FormData();
    formData.append('tenDoThue', ten);
    formData.append('soLuongs', soLuongs);
    formData.append('donGias', donGias);

    // Ghi đè `imageFile` bằng hình ảnh mới (nếu có)
    if (imageFile) {
        formData.append('imageFile', imageFile); // Sử dụng tên `imageFile` thay vì `imageData`
    }

    // Xác nhận trước khi gửi dữ liệu lên server
    Swal.fire({
        title: 'Xác nhận',
        text: 'Bạn chắc chắn muốn cập nhật thông tin này không?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Có',
        cancelButtonText: 'Hủy',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            // Gửi dữ liệu lên server qua AJAX
            $.ajax({
                url: `http://localhost:8080/do_thue/${id}`, // Sử dụng đúng id
                type: 'PUT',
                data: formData,
                processData: false,
                contentType: false,
                success: function(response) {
                    Swal.fire({
                        title: 'Thành công!',
                        text: 'Cập nhật thông tin đồ thuê thành công!',
                        icon: 'success'
                    }).then(() => {
                        cancelAdd(); // Quay lại bảng dữ liệu
                        loadData(currentServiceType, 0); // Reload dữ liệu
                    });
                },
                error: function(xhr, status, error) {
                    // Hiển thị lỗi chi tiết từ server
                    const errorMessage = xhr.responseJSON ? xhr.responseJSON.message : 'Đã xảy ra lỗi khi cập nhật thông tin đồ thuê';
                    Swal.fire({
                        title: 'Lỗi!',
                        text: errorMessage,
                        icon: 'error'
                    });
                }
            });
        }
    });
}





// Submit form update cho dịch vụ nước uống
function submitUpdateFormNuocUong(event, id) {
    event.preventDefault(); // Ngăn không cho form reload lại trang

    if (!id) {
        Swal.fire({
            title: 'Lỗi!',
            text: 'ID không hợp lệ!',
            icon: 'error'
        });
        return;
    }

    const ten = $('#tenNuocUong').val().trim();
    const soLuongs = parseInt($('#soLuongsNuocUong').val().trim());
    const donGias = parseFloat($('#donGiasNuocUong').val().trim());
    const imageFile = $('#imageNuocUong')[0].files[0];

    $('#errorTenNuocUong, #errorsoLuongsNuocUong, #errordonGiasNuocUong').text(''); // Xóa lỗi trước đó

    let hasError = false;

    if (!ten) {
        $('#errorTenNuocUong').text('Tên không được để trống');
        hasError = true;
    }

    if (isNaN(soLuongs) || soLuongs < 0) {
        $('#errorsoLuongsNuocUong').text('Số lượng phải là một số dương');
        hasError = true;
    }

    if (isNaN(donGias) || donGias <= 0) {
        $('#errordonGiasNuocUong').text('Đơn giá phải là một số dương');
        hasError = true;
    }

    if (hasError) {
        return; // Dừng lại nếu có lỗi
    }

    const formData = new FormData();
    formData.append('tenNuocUong', ten); // Gửi tên
    formData.append('soLuongs', soLuongs); // Gửi số lượng
    formData.append('donGias', donGias); // Gửi đơn giá

    if (imageFile) {
        formData.append('imageFile', imageFile); // Gửi ảnh
    }

    Swal.fire({
        title: 'Xác nhận',
        text: 'Bạn chắc chắn muốn cập nhật thông tin này không?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Có',
        cancelButtonText: 'Hủy',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: `http://localhost:8080/nuoc_uong/${id}`,
                type: 'PUT',
                data: formData,
                processData: false,
                contentType: false,
                success: function(response) {
                    Swal.fire({
                        title: 'Thành công!',
                        text: 'Cập nhật thông tin nước uống thành công!',
                        icon: 'success'
                    }).then(() => {
                        cancelAdd(); // Quay lại bảng dữ liệu
                        loadData(currentServiceType, 0); // Reload dữ liệu
                    });
                },
                error: function(xhr) {
                    const errorMessage = xhr.responseJSON ? xhr.responseJSON.message : 'Đã xảy ra lỗi khi cập nhật thông tin nước uống';
                    Swal.fire({
                        title: 'Lỗi!',
                        text: errorMessage,
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


