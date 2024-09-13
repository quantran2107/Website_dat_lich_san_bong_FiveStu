document.addEventListener("DOMContentLoaded", function () {
    const ngayDenSanInput = document.getElementById('ngayDenSan');
    let hoaDonChiTietList = []; // Lưu trữ danh sách hóa đơn chi tiết
    const sanCaTableBody = document.querySelector('#sanCaTable tbody');

    // Đặt giá trị ngày hiện tại vào ô nhập ngày
    const today = new Date().toISOString().split('T')[0];
    ngayDenSanInput.value = today;

    // Lấy danh sách hóa đơn chi tiết từ API
    fetchHoaDonChiTiet();
    loadLoaiSan();

    // Hàm lấy danh sách hóa đơn chi tiết từ API
    function fetchHoaDonChiTiet() {
        fetch('http://localhost:8080/hoa-don-chi-tiet/hien-thi')
            .then(response => response.json())
            .then(data => {
                hoaDonChiTietList = data;
            })
            .catch(error => showError('Error fetching hoa don chi tiet data:', error));
    }

    // Hàm lấy tên ngày trong tuần
    function getDayOfWeek(dateString) {
        const daysOfWeek = ['7', '1', '2', '3', '4', '5', '6'];
        const date = new Date(dateString);
        return daysOfWeek[date.getDay()];
    }

    // Hàm hiển thị lỗi cho người dùng
    function showError(message, error) {
        console.error(message, error);
        alert("Đã xảy ra lỗi, vui lòng thử lại sau.");
    }

    // Lấy danh sách sân bóng
    fetch('http://localhost:8080/san-bong/hien-thi')
        .then(response => response.json())
        .then(sanBongs => displaySanBong(sanBongs))
        .catch(error => showError('Error fetching san bong data:', error));

    // Hiển thị danh sách sân bóng
    function displaySanBong(sanBongs) {
        const sanCaContainer = document.getElementById('sanCaContainer');
        sanCaContainer.innerHTML = ''; // Xóa nội dung cũ

        sanBongs.forEach(sanBong => {
            // Tạo khung card cho sân bóng
            const cardWrapper = document.createElement('div');
            cardWrapper.classList.add('row', 'my-4'); // Bọc khung card vào div.row

            const cardColumn = document.createElement('div');
            cardColumn.classList.add('col-md-12'); // Cột cho khung card

            const card = document.createElement('div');
            card.classList.add('card', 'shadow'); // Thêm class card và shadow
            card.id = `sanBong_${sanBong.id}`; // Thêm id cho card để dễ dàng truy cập

            const cardBody = document.createElement('div');
            cardBody.classList.add('card-body');

            const formRow = document.createElement('div');
            formRow.classList.add('form-row', 'mb-3', 'd-flex', 'align-items-center');

            const title = document.createElement('h4');
            title.classList.add('card-title', 'mb-0');
            title.textContent = sanBong.tenSanBong;

            // Create container for SanCa details
            const sanCaDetails = document.createElement('div');
            sanCaDetails.classList.add('san-ca-container');
            sanCaDetails.id = `sanCaDetails_${sanBong.id}`;

            formRow.appendChild(title);
            cardBody.appendChild(formRow);
            cardBody.appendChild(sanCaDetails); // Add the container for san ca details
            card.appendChild(cardBody);
            cardColumn.appendChild(card);
            cardWrapper.appendChild(cardColumn);
            sanCaContainer.appendChild(cardWrapper);

            loadSanCaDetails(sanBong.id); // Load dữ liệu cho từng sân
        });
    }

    // Chuyển đổi chuỗi ngày tháng sang đối tượng Date
    function convertToDate(dateString) {
        const [year, month, day] = dateString.split('-').map(Number);
        return new Date(year, month - 1, day);
    }

    // Hàm tải dữ liệu ca của sân bóng theo ngày đến sân
    function loadSanCaDetails(sanBongId) {
        const sanCaDetailsSection = document.getElementById(`sanCaDetails_${sanBongId}`);
        sanCaDetailsSection.innerHTML = ''; // Xóa dữ liệu cũ

        const ngayDenSan = ngayDenSanInput.value;
        const idNgayTrongTuan = getDayOfWeek(ngayDenSan);

        fetch(`http://localhost:8080/san-ca/danh-sach-san-ca/${sanBongId}/${idNgayTrongTuan}`)
            .then(response => response.json())
            .then(sanCaList => renderSanCaList(sanCaList, sanCaDetailsSection))
            .catch(error => showError('Error fetching san ca data:', error));
    }

    // Render danh sách ca sân
    function renderSanCaList(sanCaList, container) {
        sanCaList.forEach(sanCa => {
            const sanCaBox = document.createElement('div');
            sanCaBox.classList.add('san-ca-box', 'col-md-2');

            const sanCaHeader = createSanCaHeader(sanCa);
            const sanCaContent = createSanCaContent(sanCa);

            sanCaBox.appendChild(sanCaHeader);
            sanCaBox.appendChild(createSeparator());
            sanCaBox.appendChild(sanCaContent);

            container.appendChild(sanCaBox);

            // Thêm sự kiện 'change' cho các checkbox
            const checkbox = sanCaHeader.querySelector('input[type="checkbox"]');
            if (checkbox) {
                checkbox.addEventListener('change', handleCheckboxChange);
            }
        });
    }

    // Tạo phần content cho mỗi ca sân
    function createSanCaContent(sanCa) {
        const content = document.createElement('div');
        content.classList.add('san-ca-content');

        // Thời gian
        const formattedDate = formatDateToDDMMYYYY(ngayDenSanInput.value); // Định dạng ngày
        const thoiGian = document.createElement('p');
        const batDau = new Date(sanCa.thoiGianBatDauCa).getHours();
        const ketThuc = new Date(sanCa.thoiGianKetThucCa).getHours();
        thoiGian.innerHTML = `<i class="bi bi-clock-fill"></i> ${batDau}:00 - ${ketThuc}:00`;

        // Giá
        const gia = document.createElement('p');
        gia.innerHTML = `<i class="bi bi-currency-dollar"></i> ${sanCa.gia.toLocaleString()} VNĐ`;

        // Ngày
        const dateParagraph = document.createElement('p');
        dateParagraph.classList.add('date-paragraph');
        dateParagraph.innerHTML = `<i class="bi bi-calendar-event"></i> Ngày: ${formattedDate}`;

        // Trạng thái
        const statusParagraph = document.createElement('p');
        statusParagraph.classList.add('status-paragraph');

        // Kiểm tra trạng thái theo ngày được chọn
        fetch(`http://localhost:8080/hoa-don-chi-tiet/kiem-tra-dat?idSanCa=${sanCa.id}&ngayDenSan=${formattedDate}`)
            .then(response => response.text())
            .then(status => {
                if (status === 'Còn trống') {
                    statusParagraph.classList.add('custom-1');
                    statusParagraph.textContent = 'Còn trống';
                } else if (status === 'Đã được đặt') {
                    statusParagraph.classList.add('custom-3');
                    statusParagraph.textContent = 'Đã được đặt';
                } else {
                    // Trạng thái không xác định
                    statusParagraph.classList.add('custom-1');
                    statusParagraph.textContent = 'Trạng thái không xác định';
                }
            })
            .catch(error => {
                console.error('Lỗi khi kiểm tra trạng thái:', error);
                statusParagraph.classList.add('custom-1');
                statusParagraph.textContent = 'Lỗi khi kiểm tra trạng thái';
            });

        content.appendChild(thoiGian);
        content.appendChild(gia);
        content.appendChild(dateParagraph);
        content.appendChild(statusParagraph); // Thêm trạng thái vào cuối cùng

        return content;
    }

// Tạo phần header cho mỗi ca sân (Cập nhật để không tạo checkbox nếu trạng thái không phải là "Còn trống")
    function createSanCaHeader(sanCa) {
        const header = document.createElement('div');
        header.classList.add('san-ca-header');

        const formattedDate = formatDateToDDMMYYYY(ngayDenSanInput.value); // Định dạng ngày

        fetch(`http://localhost:8080/hoa-don-chi-tiet/kiem-tra-dat?idSanCa=${sanCa.id}&ngayDenSan=${formattedDate}`)
            .then(response => response.text())
            .then(status => {
                if (status === 'Còn trống') {
                    const checkboxContainer = document.createElement('div');
                    checkboxContainer.classList.add('checkbox-container');

                    const checkboxLabel = document.createElement('label');
                    const checkbox = document.createElement('input');
                    checkbox.type = 'checkbox';
                    checkbox.value = sanCa.id;

                    const labelText = document.createTextNode('Chọn sân');
                    checkboxLabel.appendChild(checkbox);
                    checkboxLabel.appendChild(labelText);
                    checkboxContainer.appendChild(checkboxLabel);
                    checkbox.addEventListener('change', handleCheckboxChange);

                    header.appendChild(checkboxContainer);
                }
                // Tạo phần text
                const textContainer = document.createElement('div');
                textContainer.classList.add('text-container');

                const tenCa = document.createElement('h5');
                tenCa.textContent = sanCa.tenCa;

                const tenLoaiSan = document.createElement('p');
                tenLoaiSan.classList.add('ten-loai-san');
                tenLoaiSan.textContent = sanCa.tenLoaiSan;

                textContainer.appendChild(tenCa);
                textContainer.appendChild(tenLoaiSan);

                header.appendChild(textContainer);
            })
            .catch(error => {

            });

        return header;
    }

    // Tạo đường kẻ phân cách
    function createSeparator() {
        const separator = document.createElement('div');
        separator.classList.add('separator');
        return separator;
    }

    // Xử lý sự thay đổi của checkbox
    function handleCheckboxChange(event) {
        const checkbox = event.target;
        const sanCaId = checkbox.value; // Lấy idSanCa từ giá trị của checkbox

        if (checkbox.checked) {
            fetch(`http://localhost:8080/san-ca/${sanCaId}`)
                .then(response => response.json())
                .then(data => {
                    const row = document.createElement('tr');
                    row.id = `sanCaRow_${sanCaId}`;
                    row.setAttribute('data-idSanCa', sanCaId); // Lưu idSanCa vào thuộc tính data-idSanCa

                    const sanBongCell = document.createElement('td');
                    sanBongCell.textContent = data.tenSanBong;
                    row.appendChild(sanBongCell);

                    const ngayCell = document.createElement('td');
                    ngayCell.textContent = ngayDenSanInput.value; // Lấy ngày từ ô nhập
                    row.appendChild(ngayCell);

                    const tenCaCell = document.createElement('td');
                    tenCaCell.textContent = data.tenCa;
                    row.appendChild(tenCaCell);

                    const thoiGianCell = document.createElement('td');
                    const batDau = new Date(data.thoiGianBatDauCa).getHours();
                    const ketThuc = new Date(data.thoiGianKetThucCa).getHours();
                    thoiGianCell.innerHTML = `${batDau}:00 - ${ketThuc}:00`;
                    row.appendChild(thoiGianCell);

                    const giaCell = document.createElement('td');
                    giaCell.innerHTML = `${data.gia.toLocaleString()} VNĐ`;
                    row.appendChild(giaCell);

                    // Thao tác (Button để xóa hàng)
                    const thaoTacCell = document.createElement('td');
                    const deleteButton = document.createElement('button');
                    deleteButton.classList.add('btn', 'btn-outline-danger');
                    deleteButton.setAttribute('type', 'button');
                    deleteButton.setAttribute('title', 'Xóa');
                    deleteButton.style.justifyContent = 'center';

                    const deleteIcon = document.createElement('span');
                    deleteIcon.classList.add('fe', 'fe-trash-2');
                    deleteButton.appendChild(deleteIcon);

                    deleteButton.addEventListener('click', () => {
                        sanCaTableBody.removeChild(row);
                        checkbox.checked = false;
                    });

                    thaoTacCell.appendChild(deleteButton);
                    row.appendChild(thaoTacCell);

                    sanCaTableBody.appendChild(row);
                })
                .catch(error => showError('Error fetching san ca data:', error));
        } else {
            const row = document.getElementById(`sanCaRow_${sanCaId}`);
            if (row) {
                sanCaTableBody.removeChild(row);
            }
        }
    }

    function updateSanCaStatus(sanCaId, ngayDenSan) {
        const formattedDate = formatDateToDDMMYYYY(ngayDenSan);

        // Debug để kiểm tra giá trị ngày
        console.log(`Calling API with sanCaId: ${sanCaId} and ngayDenSan: ${formattedDate}`);

        fetch(`http://localhost:8080/hoa-don-chi-tiet/kiem-tra-dat?idSanCa=${sanCaId}&ngayDenSan=${formattedDate}`)
            .then(response => response.json())
            .then(trangThai => {
                const statusParagraph = document.createElement('p');
                statusParagraph.classList.add('status-paragraph');

                if (trangThai === 'Còn trống') {
                    statusParagraph.classList.add('custom-1');
                    statusParagraph.textContent = 'Còn trống';
                } else if (trangThai === 'Đã được đặt') {
                    statusParagraph.classList.add('custom-4');
                    statusParagraph.textContent = 'Đã được đặt';
                }

                const sanCaElement = document.getElementById(`sanCa_${sanCaId}`);
                if (sanCaElement) {
                    const statusContainer = sanCaElement.querySelector('.status-container');
                    if (statusContainer) {
                        statusContainer.innerHTML = ''; // Xóa trạng thái cũ
                        statusContainer.appendChild(statusParagraph); // Thêm trạng thái mới
                    }
                }
            })
            .catch(error => console.error('Error fetching status:', error));
    }

    function formatDateToDDMMYYYY(dateString) {
        // Kiểm tra định dạng của dateString và chuyển đổi nếu cần
        const [year, month, day] = dateString.split('-');
        return `${day}/${month}/${year}`;
    }

    // Khi thay đổi ngày, kiểm tra và cập nhật bảng chỉ với các checkbox đã chọn
    ngayDenSanInput.addEventListener('change', function () {
        // Xóa toàn bộ dữ liệu trong bảng
        sanCaTableBody.innerHTML = '';

        // Cập nhật nội dung san-ca-container cho ngày mới
        const sanCaContainers = document.querySelectorAll('.san-ca-container');
        sanCaContainers.forEach(container => {
            const sanBongId = container.id.split('_')[1]; // Lấy sanBongId từ id của container
            loadSanCaDetails(sanBongId); // Tải lại dữ liệu san-ca cho sân bóng
        });
    });


    // Hàm lấy danh sách loại sân và hiển thị trong dropdown
    function loadLoaiSan() {
        fetch('http://localhost:8080/loai-san/hien-thi')
            .then(response => response.json())
            .then(loaiSans => {
                const loaiSanDropdown = document.getElementById('loaiSanDropdown');
                loaiSanDropdown.innerHTML = ''; // Xóa nội dung cũ

                loaiSans.forEach(loaiSan => {
                    const aTag = document.createElement('a');
                    aTag.classList.add('dropdown-item');
                    aTag.textContent = loaiSan.tenLoaiSan;
                    aTag.href = '#';
                    aTag.onclick = function () {
                        setLoaiSan(loaiSan.id);
                    };
                    loaiSanDropdown.appendChild(aTag);
                });
            })
            .catch(error => console.error('Lỗi khi tải loại sân:', error));
    }

    // Hàm hiển thị các sân bóng theo loại sân
    function setLoaiSan(idLoaiSan) {
        fetch(`http://localhost:8080/san-bong/findByIdLoaiSan/${idLoaiSan}`)
            .then(response => response.json())
            .then(sanBongs => {
                displaySanBong(sanBongs); // Hiển thị các sân bóng được lọc

                // Cập nhật tên loại sân vào button
                const selectedLoaiSan = sanBongs.length > 0 ? sanBongs[0].tenLoaiSan : 'Chọn loại sân';
                document.getElementById('actionMenuButton1').textContent = selectedLoaiSan;
            })
            .catch(error => console.error('Lỗi khi tải sân bóng theo loại sân:', error));
    }

    flatpickr("#ngayDenSan", {});
});

$(document).ready(function() {
    $('#soDienThoai').on('input', function() {
        var soDienThoai = $(this).val();

        if (soDienThoai.length >= 10) { // Chỉ thực hiện tìm kiếm khi số điện thoại có ít nhất 10 ký tự
            $.ajax({
                url: 'http://localhost:8080/khach-hang/tim-kiem-kh',
                type: 'GET',
                data: { soDienThoai: soDienThoai },
                success: function(response) {
                    if (response) {
                        $('#hoVaTen').val(response.hoVaTen); // Cập nhật tên khách hàng vào ô tên
                    } else {
                        $('#hoVaTen').val(''); // Nếu không tìm thấy khách hàng, xóa ô tên
                    }
                },
                error: function() {
                    $('#hoVaTen').val(''); // Nếu có lỗi trong gọi API, xóa ô tên
                }
            });
        } else {
            $('#hoVaTen').val(''); // Xóa ô tên khi số điện thoại không đủ dài
        }
    });
});

document.querySelector('#datLich').addEventListener('click', function () {
    // Lấy số điện thoại từ input
    var soDienThoai = document.getElementById('soDienThoai').value;

    // Kiểm tra xem số điện thoại có hợp lệ không
    if (!soDienThoai) {
        alert('Vui lòng nhập số điện thoại!');
        return;
    }

    // Gọi API để tìm khách hàng bằng số điện thoại
    fetch('http://localhost:8080/khach-hang/tim-kiem-kh?soDienThoai=' + soDienThoai)
        .then(response => {
            if (!response.ok) {
                throw new Error('Không tìm thấy khách hàng với số điện thoại này.');
            }
            return response.json();
        })
        .then(data => {
            // Lấy idKhachHang từ kết quả trả về
            var idKhachHang = data.id;

            // Kiểm tra nếu không tìm thấy khách hàng
            if (!idKhachHang) {
                alert('Không tìm thấy khách hàng.');
                return;
            }

            // Tạo payload cho API tạo hóa đơn
            var payload = {
                idKhachHang: idKhachHang
            };

            // Gọi API để tạo hóa đơn
            return fetch('http://localhost:8080/hoa-don/save', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });
        })
        .then(response => response.json())
        .then(data => {
            // Lấy idHoaDon từ kết quả trả về của API tạo hóa đơn
            var idHoaDon = data.id;
            console.log('Thêm hóa đơn thành công:', data);

            // Gọi hàm thêm hóa đơn chi tiết sau khi thêm hóa đơn thành công
            themHoaDonChiTiet(idHoaDon);

            // Đóng modal sau khi đặt lịch thành công
            alert('Đặt lịch thành công!');
            $('#book-modal').modal('hide');
            window.location.href = 'http://localhost:8080/dat-lich-tai-quay';
        })
        .catch(error => {
            console.error('Lỗi khi thêm hóa đơn:', error);
            alert('Lỗi khi thêm hóa đơn: ' + error.message);
        });
});

// Hàm thêm hóa đơn chi tiết
function themHoaDonChiTiet(idHoaDon) {
    console.log("idHoaDon:", idHoaDon); // Kiểm tra idHoaDon

    // Lấy danh sách các dòng sân ca từ bảng
    const rows = document.querySelectorAll("#sanCaTable tbody tr");

    // Lặp qua từng dòng để lấy thông tin idSanCa và ngày đến sân
    rows.forEach((row) => {
        const idSanCa = row.getAttribute("data-idSanCa"); // Lấy idSanCa từ thuộc tính data-idSanCa của mỗi hàng
        const ngayDenSan = document.querySelector("#ngayDenSan").value; // Lấy ngày đến sân từ input

        console.log("idSanCa:", idSanCa); // Kiểm tra idSanCa
        console.log("ngayDenSan:", ngayDenSan); // Kiểm tra ngày đến sân

        // Kiểm tra nếu idSanCa hoặc idHoaDon bị null
        if (!idHoaDon || !idSanCa) {
            console.error("Lỗi: idHoaDon hoặc idSanCa bị null");
            return;
        }

        // Chuyển đổi ngày từ "yyyy-MM-dd" sang "dd/MM/yyyy"
        const dateParts = ngayDenSan.split("-");
        const formattedDate = `${dateParts[2]}/${dateParts[1]}/${dateParts[0]}`; // Định dạng lại ngày theo dd/MM/yyyy

        // Dữ liệu để thêm hóa đơn chi tiết
        const chiTietPayload = {
            idHoaDon: idHoaDon,
            idSanCa: idSanCa,
            ngayDenSan: formattedDate // Sử dụng ngày đã định dạng lại
        };

        // Gọi API để thêm hóa đơn chi tiết
        fetch("http://localhost:8080/hoa-don-chi-tiet/save2", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(chiTietPayload),
        })
            .then(response => response.json())
            .then(data => {
                console.log('Thêm hóa đơn chi tiết thành công:', data);
            })
            .catch(error => {
                console.error('Lỗi khi thêm hóa đơn chi tiết:', error);
            });
    });
}



