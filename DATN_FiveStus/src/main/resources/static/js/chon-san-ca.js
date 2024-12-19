document.addEventListener("DOMContentLoaded", function () {
    const ngayDenSanInput = document.getElementById('ngayDenSan');
    const ngayBatDauInput = document.getElementById('ngayBatDau');
    const ngayKetThucInput = document.getElementById('ngayKetThuc');
    let hoaDonChiTietList = []; // Lưu trữ danh sách hóa đơn chi tiết
    const sanCaTableBody = document.querySelector('#sanCaTable tbody');

    // Đặt giá trị ngày hiện tại vào ô nhập ngày
    const today = new Date().toISOString().split('T')[0];
    ngayDenSanInput.value = today;
    ngayBatDauInput.value = today;
    ngayKetThucInput.value = today;

    // Lấy danh sách hóa đơn chi tiết từ API
    fetchHoaDonChiTiet();
    loadLoaiSan();

    // Gán sự kiện cho các item trong dropdown
    const dropdownItems = document.querySelectorAll('#loaiNgayDropdown .dropdown-item');
    dropdownItems.forEach(item => {
        item.addEventListener('click', function (event) {
            event.preventDefault(); // Ngăn chặn hành vi mặc định của thẻ <a>
            const type = this.getAttribute('data-value'); // Lấy giá trị loại ngày
            showInput(type); // Gọi hàm showInput
        });
    });

    let selectedDayType = 'Theo ngày';

    // Hàm hiển thị ô input ngày theo loại ngày đặt
    function showInput(type) {
        selectedDayType = type; // Cập nhật loại ngày đã chọn
        var inputNgayDon = document.getElementById('inputNgayDon');
        var inputNhieuNgay = document.getElementById('inputNhieuNgay');
        var dropdownButton = document.getElementById('actionMenuButton2');
        var infoButtonContainer = document.querySelector('.input-group.col-md-2'); // Lấy phần chứa nút "Điền thông tin"

        if (type === 'Theo ngày') {
            inputNgayDon.style.display = 'block';
            inputNhieuNgay.style.display = 'none';
            dropdownButton.textContent = 'Theo ngày'; // Cập nhật nội dung nút
            infoButtonContainer.style.display = 'block'; // Hiển thị nút "Điền thông tin"
            sanCaTableBody.innerHTML = '';
        } else if (type === 'Nhiều ngày') {
            inputNgayDon.style.display = 'none';
            inputNhieuNgay.style.display = 'block';
            dropdownButton.textContent = 'Nhiều ngày'; // Cập nhật nội dung nút
            infoButtonContainer.style.display = 'none'; // Ẩn nút "Điền thông tin"
            sanCaTableBody.innerHTML = '';
        }

        // Lấy danh sách sân bóng
        fetch('http://localhost:8080/san-bong/hien-thi-active')
            .then(response => response.json())
            .then(sanBongs => displaySanBong(sanBongs))
            .catch(error => showError('Error fetching san bong data:', error));
    }

    // Hàm lấy danh sách hóa đơn chi tiết từ API
    function fetchHoaDonChiTiet() {
        fetch('http://localhost:8080/hoa-don-chi-tiet/hien-thi')
            .then(response => response.json())
            .then(data => {
                hoaDonChiTietList = data;
            })
            .catch(error => showError('Error fetching hoa don chi tiet data:', error));
    }

    // Hàm hiển thị lỗi cho người dùng
    function showError(message, error) {
    }

    // Lấy danh sách sân bóng
    fetch('http://localhost:8080/san-bong/hien-thi-active')
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

        fetch(`http://localhost:8080/san-ca/danh-sach-san-ca-2/${sanBongId}/${idNgayTrongTuan}`)
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

    function createDoiSanContent(sanCa, ngayChon) {
        const content = document.createElement('div');
        content.classList.add('san-ca-content', 'justify-content-between', 'align-items-center');

        // Thời gian của sân ca
        const batDau = new Date(sanCa.thoiGianBatDauCa).getHours();
        const ketThuc = new Date(sanCa.thoiGianKetThucCa).getHours();
        const thoiGian = document.createElement('p');
        thoiGian.innerHTML = `<i class="bi bi-clock-fill"></i> ${batDau}:00 - ${ketThuc}:00`;

        // Giá của sân ca
        const gia = document.createElement('p');
        gia.innerHTML = `<i class="bi bi-currency-dollar"></i> ${sanCa.gia.toLocaleString()} VND`;

        // Thêm thông tin thời gian và giá vào content
        const infoContainer = document.createElement('div');
        infoContainer.classList.add('info-container'); // Áp dụng class cho container
        infoContainer.appendChild(thoiGian);
        infoContainer.appendChild(gia);

        // Tạo phần tử để hiển thị trạng thái
        const statusParagraph = document.createElement('p');
        statusParagraph.textContent = 'Đang tải...'; // Hiển thị trạng thái tạm thời

        // Kiểm tra trạng thái của sân ca với ngày được chọn
        fetch(`http://localhost:8080/hoa-don-chi-tiet/kiem-tra-dat?idSanCa=${sanCa.id}&ngayDenSan=${ngayChon}`)
            .then(response => response.text())
            .then(status => {
                if (status === 'Còn trống') {
                    statusParagraph.classList.add('custom-1'); // Áp dụng class custom-1 cho trạng thái "Còn trống"
                    statusParagraph.textContent = 'Còn trống';
                } else if (status === 'Đã được đặt') {
                    statusParagraph.classList.add('custom-3'); // Áp dụng class custom-3 cho trạng thái "Đã được đặt"
                    statusParagraph.textContent = 'Đã được đặt';
                } else {
                    statusParagraph.textContent = 'Không xác định';
                }
            })
            .catch(error => {
                statusParagraph.textContent = 'Lỗi khi kiểm tra trạng thái';
            });

        // Nút "Đổi lịch"
        const doiLichButton = document.createElement('button');
        doiLichButton.classList.add('btn', 'btn-booking');
        doiLichButton.textContent = 'Đổi lịch';

        // Gán sự kiện cho nút "Đổi lịch"
        doiLichButton.addEventListener('click', function () {

            // Lấy thông tin của sân ca hiện tại
            const row = doiLichButton.closest('tr'); // Tìm dòng <tr> chứa nút
            const idSanCa = sanCa.id; // Lấy ID sân ca từ đối tượng sanCa
            const idNgayTrongTuan = getDayOfWeek(ngayChon);

            const sanCaTableBody = document.querySelector('#sanCaTable tbody');
            const rows = sanCaTableBody.querySelectorAll('tr');

            rows.forEach(row => {
                const rowId = row.id; // Lấy id của dòng hiện tại
                if (rowId.endsWith(ngayChon)) {
                    // Nếu id kết thúc bằng suffixToRemove, xóa dòng
                    sanCaTableBody.removeChild(row);
                }
            });

            // Gọi API để lấy thông tin mới cho sân ca
            fetch(`http://localhost:8080/san-ca/danh-sach-san-ca/${sanCa.idSanBong}/${idNgayTrongTuan}/${sanCa.idCa}`)
                .then(response => response.json())
                .then(data => {

                    const newRow = document.createElement('tr');
                    newRow.id = `sanCaRow_${data.id}_${ngayChon}`; // Gán ID cho dòng mới

                    const soThuTuCell = document.createElement('td');
                    soThuTuCell.textContent = sanCaTableBody.children.length + 1; // Cập nhật số thứ tự
                    newRow.appendChild(soThuTuCell);

                    const sanBongCell = document.createElement('td');
                    sanBongCell.textContent = data.tenSanBong; // Tên sân bóng mới
                    newRow.appendChild(sanBongCell);

                    const ngayCell = document.createElement('td');
                    ngayCell.textContent = convertDateFormat(ngayChon); // Ngày đã chọn
                    newRow.appendChild(ngayCell);

                    const tenCaCell = document.createElement('td');
                    tenCaCell.textContent = data.tenCa; // Tên ca mới
                    newRow.appendChild(tenCaCell);

                    const thoiGianCell = document.createElement('td');
                    thoiGianCell.innerHTML = `${batDau}:00 - ${ketThuc}:00`;
                    newRow.appendChild(thoiGianCell);

                    const giaCell = document.createElement('td');
                    giaCell.innerHTML = `${data.gia.toLocaleString()} VND`; // Giá mới
                    newRow.appendChild(giaCell);

                    const trangThaiCell = document.createElement('td');
                    trangThaiCell.textContent = 'Đang tải...'; // Hiển thị trạng thái tạm thời
                    newRow.appendChild(trangThaiCell);

                    const thaoTacCell = document.createElement('td');
                    const deleteButton = document.createElement('button');
                    deleteButton.classList.add('btn', 'btn-outline-danger');
                    deleteButton.setAttribute('type', 'button');
                    deleteButton.setAttribute('title', 'Xóa');

                    const deleteIcon = document.createElement('p');
                    deleteIcon.classList.add('fe', 'fe-trash-2');
                    deleteButton.appendChild(deleteIcon);

                    deleteButton.addEventListener('click', () => {
                        sanCaTableBody.removeChild(newRow);
                        updateSTT(); // Cập nhật lại STT sau khi xóa dòng
                        calculateTotalPrice();
                    });

                    thaoTacCell.appendChild(deleteButton);

                    newRow.appendChild(thaoTacCell);

                    fetch(`http://localhost:8080/hoa-don-chi-tiet/kiem-tra-dat?idSanCa=${data.id}&ngayDenSan=${ngayChon}`)
                        .then(response => response.text())
                        .then(status => {
                            if (status === 'Còn trống') {
                                trangThaiCell.classList.add('custom-1');
                                trangThaiCell.textContent = 'Còn trống';
                            } else if (status === 'Đã được đặt') {
                                trangThaiCell.classList.add('custom-3');
                                trangThaiCell.textContent = 'Đã được đặt';
                            } else {
                                trangThaiCell.textContent = 'Không xác định';
                            }
                        })
                        .catch(error => {
                            trangThaiCell.textContent = 'Lỗi';
                        });

                    sanCaTableBody.appendChild(newRow);
                })
                .catch(error => {
                    // console.error('Lỗi khi lấy thông tin sân ca mới:', error);
                });
        });

        // Thêm thông tin trạng thái và nút vào content
        content.appendChild(infoContainer);
        content.appendChild(statusParagraph);
        content.appendChild(doiLichButton);

        return content;
    }

    function createDoiSanHeader(sanCa) {
        const header = document.createElement('div');
        header.classList.add('san-ca-header'); // Áp dụng class cho header

        const tenCa = document.createElement('h5');
        tenCa.textContent = sanCa.tenCa;

        const tenLoaiSan = document.createElement('p');
        tenLoaiSan.classList.add('ten-loai-san'); // Áp dụng class cho tên loại sân
        tenLoaiSan.textContent = sanCa.tenLoaiSan;

        header.appendChild(tenCa);
        header.appendChild(tenLoaiSan);

        return header;
    }

    function renderDoiSanList(sanCaList, modalContainer, ngayChon) {
        modalContainer.innerHTML = ''; // Xóa dữ liệu cũ trong modal trước khi render dữ liệu mới

        sanCaList.forEach(sanCa => {
            const sanCaBox = document.createElement('div');
            sanCaBox.classList.add('san-ca-box', 'doi-san-flex-20'); // Áp dụng class san-ca-box

            // Tạo header cho sân ca
            const sanCaHeader = createDoiSanHeader(sanCa);

            // Tạo nội dung cho sân ca, truyền thêm 'ngayChon' vào hàm createDoiSanContent
            const sanCaContent = createDoiSanContent(sanCa, ngayChon);

            // Thêm header, separator, và content vào sanCaBox
            sanCaBox.appendChild(sanCaHeader);
            sanCaBox.appendChild(createSeparator()); // Thêm separator giữa header và content
            sanCaBox.appendChild(sanCaContent);

            // Thêm sanCaBox vào modal container
            modalContainer.appendChild(sanCaBox);
        });
    }

    // Hàm tính tổng số ngày (nếu chọn Nhiều ngày)
    function calculateTotalDays(ngayBatDau, ngayKetThuc) {
        const start = new Date(ngayBatDau);
        const end = new Date(ngayKetThuc);
        return Math.ceil((end - start) / (1000 * 60 * 60 * 24)) + 1;
    }

    // Hàm thêm số ngày vào một ngày
    function addDays(date, days) {
        const result = new Date(date);
        result.setDate(result.getDate() + days);
        return result;
    }

    // Hàm định dạng ngày
    function formatDate(date) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0 nên phải +1
        const day = String(date.getDate()).padStart(2, '0'); // Lấy ngày theo múi giờ hiện tại
        return `${year}-${month}-${day}`;
    }

    // Tạo phần content cho mỗi ca sân
    function createSanCaContent(sanCa) {

        const content = document.createElement('div');
        content.classList.add('san-ca-content');

        // Thời gian của sân ca
        const batDau = new Date(sanCa.thoiGianBatDauCa).getHours();
        const ketThuc = new Date(sanCa.thoiGianKetThucCa).getHours();
        const thoiGian = document.createElement('p');
        thoiGian.innerHTML = `<i class="bi bi-clock-fill"></i> ${batDau}:00 - ${ketThuc}:00`;

        // Giá của sân ca
        const gia = document.createElement('p');
        gia.innerHTML = `<i class="bi bi-currency-dollar"></i> ${sanCa.gia.toLocaleString()} VND`;

        // Tạo phần tử hiển thị tổng số ngày còn trống
        const totalAvailableDaysParagraph = document.createElement('p');
        totalAvailableDaysParagraph.innerHTML = `<strong>Tổng số ngày còn trống: Đang kiểm tra...</strong>`;

        // Xử lý phần ngày dựa vào loại ngày đã chọn
        let ngayQuery = '';
        const fetchPromises = [];
        let totalAvailableDays = 0; // Biến đếm số ngày còn trống

        if (selectedDayType === 'Theo ngày') {
            if (ngayDenSanInput.value) {
                const formattedDate = formatDateToDDMMYYYY(ngayDenSanInput.value); // Định dạng ngày "Theo ngày"
                ngayQuery = formattedDate;

                // Lấy thời gian hiện tại
                const currentTime = new Date(); // Thời gian hiện tại
                const sanCaStartTime = new Date(sanCa.thoiGianBatDauCa); // Thời gian sân ca bắt đầu

                // Lấy giờ và phút
                const currentHour = currentTime.getHours();
                const currentMinute = currentTime.getMinutes();

                const sanCaHour = sanCaStartTime.getHours();
                const sanCaMinute = sanCaStartTime.getMinutes();

                // So sánh thời gian
                const isPast =
                    sanCaHour < currentHour || (sanCaHour === currentHour && sanCaMinute < currentMinute);

                // Kiểm tra trạng thái của sân ca theo ngày
                fetch(`http://localhost:8080/hoa-don-chi-tiet/kiem-tra-dat?idSanCa=${sanCa.id}&ngayDenSan=${ngayQuery}`)
                    .then(response => response.text())
                    .then(status => {
                        if (isPast) {
                            statusParagraph.classList.add('custom-3');
                            statusParagraph.textContent = 'Quá giờ đặt';
                        } else if (status === 'Còn trống') {
                            statusParagraph.classList.add('custom-1');
                            statusParagraph.textContent = 'Còn trống';
                        } else if (status === 'Đã được đặt') {
                            statusParagraph.classList.add('custom-3');
                            statusParagraph.textContent = 'Đã được đặt';
                        } else {
                            // Xử lý trạng thái không xác định
                            statusParagraph.classList.add('custom-1');
                            statusParagraph.textContent = 'Trạng thái không xác định';
                        }
                    })
                    .catch(error => {
                        // console.error('Lỗi khi kiểm tra trạng thái:', error);
                        statusParagraph.classList.add('custom-1');
                        statusParagraph.textContent = 'Lỗi khi kiểm tra trạng thái';
                    });
            }
        } else if (selectedDayType === 'Nhiều ngày') {
            if (ngayBatDauInput.value && ngayKetThucInput.value) {
                const ngayBatDau = formatDateToDDMMYYYY(ngayBatDauInput.value);
                const ngayKetThuc = formatDateToDDMMYYYY(ngayKetThucInput.value);
                ngayQuery = `${ngayBatDau} - ${ngayKetThuc}`; // Định dạng khoảng ngày "Nhiều ngày"
            }
        }

        const dateParagraph = document.createElement('p');
        dateParagraph.classList.add('date-paragraph');
        dateParagraph.innerHTML = `<i class="bi bi-calendar-event"></i> Ngày: ${ngayQuery}`;

        // Trạng thái của sân ca
        const statusParagraph = document.createElement('p');
        statusParagraph.classList.add('status-paragraph');

        // Thêm các thành phần vào thẻ content
        content.appendChild(thoiGian);
        content.appendChild(gia);
        content.appendChild(dateParagraph);
        content.appendChild(statusParagraph); // Thêm trạng thái vào cuối cùng

        if (selectedDayType === 'Nhiều ngày') {
            if (ngayBatDauInput.value && ngayKetThucInput.value) {
                // Tính tổng số ngày
                const ngayBatDau = new Date(ngayBatDauInput.value.replace(/-/g, '/'));
                const ngayKetThuc = new Date(ngayKetThucInput.value.replace(/-/g, '/'));
                const totalDays = Math.ceil((ngayKetThuc - ngayBatDau) / (1000 * 60 * 60 * 24)) + 1; // +1 để tính cả ngày đầu
                const totalDaysParagraph = document.createElement('p');
                totalDaysParagraph.innerHTML = `<strong>Tổng số ngày: ${totalDays} ngày</strong>`;
                // Lặp qua từng ngày trong khoảng thời gian
                // Tạo một mảng các lời hứa (promise) để lưu các lệnh fetch
                const fetchPromises = [];

                content.appendChild(totalAvailableDaysParagraph);

                const currentTime = new Date(); // Thời gian hiện tại
                const sanCaStartTime = new Date(sanCa.thoiGianBatDauCa); // Thời gian sân ca bắt đầu

                // Lấy giờ và phút
                const currentHour = currentTime.getHours();
                const currentMinute = currentTime.getMinutes();

                const sanCaHour = sanCaStartTime.getHours();
                const sanCaMinute = sanCaStartTime.getMinutes();

                // So sánh thời gian
                const isPast =
                    sanCaHour < currentHour || (sanCaHour === currentHour && sanCaMinute < currentMinute);

                for (let i = 0; i < totalDays; i++) {
                    const currentDay = addDays(ngayBatDau, i);
                    const formattedDate = formatDate(currentDay); // Định dạng ngày thành yyyy-mm-dd
                    const idNgayTrongTuan = getDayOfWeek(formattedDate); // Lấy idNgàyTrongTuan

                    // Đẩy lời hứa fetch vào mảng
                    const fetchPromise = fetch(`http://localhost:8080/san-ca/danh-sach-san-ca/${sanCa.idSanBong}/${idNgayTrongTuan}/${sanCa.idCa}`)
                        .then(response => response.json())
                        .then(data => {
                            const idSanCa = data.id; // Lấy idSanCa từ API trả về

                            // Kiểm tra trạng thái của ngày này bằng idSanCa
                            return fetch(`http://localhost:8080/hoa-don-chi-tiet/kiem-tra-dat?idSanCa=${idSanCa}&ngayDenSan=${formattedDate}`);
                        })
                        .then(response => response.text())
                        .then(status => {
                            if (status === 'Còn trống') {
                                totalAvailableDays++; // Nếu còn trống, tăng biến đếm
                            }
                        })
                        .catch(error => {
                            // console.error('Lỗi khi kiểm tra trạng thái:', error);
                        });

                    fetchPromises.push(fetchPromise);
                }

                // Đợi tất cả các fetch hoàn tất
                Promise.all(fetchPromises).then(() => {
                    if (isPast && totalAvailableDays > 0) {
                        totalAvailableDays--;
                    }
                    // console.log('Tổng số ngày có sẵn:', totalAvailableDays);
                });

                // Đợi tất cả các lệnh fetch hoàn thành
                Promise.all(fetchPromises).then(() => {
                    // Sau khi hoàn tất kiểm tra tất cả các ngày, hiển thị kết quả
                    totalAvailableDaysParagraph.innerHTML = `<strong>${totalAvailableDays}/${totalDays} ngày còn trống</strong>`;

                    // Kiểm tra nếu totalAvailableDays = 0 thì thêm class custom-3, ngược lại thì thêm class custom-1
                    if (totalAvailableDays === 0) {
                        totalAvailableDaysParagraph.classList.remove('custom-1'); // Xóa class custom-1 nếu có
                        totalAvailableDaysParagraph.classList.add('custom-3'); // Thêm class custom-3
                    } else {
                        totalAvailableDaysParagraph.classList.remove('custom-3'); // Xóa class custom-3 nếu có
                        totalAvailableDaysParagraph.classList.add('custom-1'); // Thêm class custom-1
                    }

                    // Nút Đặt lịch
                    const bookingButton = document.createElement('button');
                    bookingButton.textContent = 'Đặt lịch';
                    bookingButton.classList.add('btn', 'btn-booking');
                    bookingButton.onclick = function () {

                        // Nếu totalAvailableDays = 0, hiển thị thông báo và không thực hiện hành động tiếp theo
                        if (totalAvailableDays === 0) {
                            showErrorToast('Không có ngày nào còn trống cho sân này.');
                            return; // Kết thúc sự kiện nếu không còn ngày trống
                        }

                        // Làm trống bảng trước khi thêm dữ liệu mới
                        sanCaTableBody.innerHTML = ''; // Xóa toàn bộ dữ liệu trong bảng

                        // Tính tổng số ngày (nếu chọn "Nhiều ngày")
                        const totalDays = selectedDayType === 'Nhiều ngày' ? calculateTotalDays(ngayBatDauInput.value, ngayKetThucInput.value) : 1;

                        // Lặp qua từng ngày
                        for (let i = 0; i < totalDays; i++) {
                            const currentDay = addDays(new Date(ngayBatDauInput.value || ngayDenSanInput.value), i);
                            const formattedDate = formatDate(currentDay); // Định dạng ngày thành yyyy-mm-dd

                            // Tạo dòng cho bảng
                            const row = document.createElement('tr');
                            row.id = `sanCaRow_${sanCa.id}_${formattedDate}`;
                            row.setAttribute('data-idSanCa', sanCa.id);

                            // Cột số thứ tự
                            const soThuTuCell = document.createElement('td');
                            soThuTuCell.textContent = i + 1; // Hiển thị số thứ tự bắt đầu từ 1
                            row.appendChild(soThuTuCell); // Thêm cột số thứ tự vào dòng

                            // Các cột khác
                            const sanBongCell = document.createElement('td');
                            sanBongCell.textContent = sanCa.tenSanBong;
                            row.appendChild(sanBongCell);

                            const ngayCell = document.createElement('td');

                            // Đổi định dạng ngày từ yyyy-mm-dd sang dd-mm-yyyy
                            const parts = formattedDate.split('-'); // Tách ngày thành các phần (năm, tháng, ngày)
                            const formattedNgayThangNam = `${parts[2]}-${parts[1]}-${parts[0]}`; // Đảo thứ tự thành dd-mm-yyyy

                            ngayCell.textContent = formattedNgayThangNam; // Gán ngày đã đổi định dạng
                            row.appendChild(ngayCell);

                            const tenCaCell = document.createElement('td');
                            tenCaCell.textContent = sanCa.tenCa;
                            row.appendChild(tenCaCell);

                            const thoiGianCell = document.createElement('td');
                            thoiGianCell.innerHTML = `${batDau}:00 - ${ketThuc}:00`;
                            row.appendChild(thoiGianCell);

                            const giaCell = document.createElement('td');
                            giaCell.innerHTML = `${sanCa.gia.toLocaleString()} VND`;
                            row.appendChild(giaCell);

                            // Tạo cột Trạng thái và thêm vào dòng ngay lập tức
                            const trangThaiCell = document.createElement('td');
                            trangThaiCell.textContent = 'Đang tải...'; // Hiển thị "Đang tải" tạm thời trước khi nhận được kết quả API
                            row.appendChild(trangThaiCell); // Thêm cột trạng thái vào dòng trước khi gọi API

                            // Gọi API để lấy idSanCa và kiểm tra trạng thái
                            const idNgayTrongTuan = getDayOfWeek(formattedDate); // Lấy idNgàyTrongTuan

                            const thaoTacCell = document.createElement('td');
                            const deleteButton = document.createElement('button');
                            deleteButton.classList.add('btn', 'btn-outline-danger');
                            deleteButton.setAttribute('type', 'button');
                            deleteButton.setAttribute('title', 'Xóa');

                            const deleteIcon = document.createElement('p');
                            deleteIcon.classList.add('fe', 'fe-trash-2');
                            deleteButton.appendChild(deleteIcon);

                            deleteButton.addEventListener('click', () => {
                                sanCaTableBody.removeChild(row);
                                updateSTT(); // Cập nhật lại STT sau khi xóa dòng
                                calculateTotalPrice();
                            });

                            thaoTacCell.appendChild(deleteButton);

                            row.appendChild(thaoTacCell);

                            fetch(`http://localhost:8080/san-ca/danh-sach-san-ca/${sanCa.idSanBong}/${idNgayTrongTuan}/${sanCa.idCa}`)
                                .then(response => response.json())
                                .then(data => {
                                    const idSanCa = data.id; // Lấy idSanCa từ API trả về

                                    // Gọi API kiểm tra trạng thái với idSanCa
                                    return fetch(`http://localhost:8080/hoa-don-chi-tiet/kiem-tra-dat?idSanCa=${idSanCa}&ngayDenSan=${formattedDate}`);
                                })
                                .then(response => response.text())
                                .then(status => {
                                    if (isPast && i === 0){
                                        trangThaiCell.classList.add('custom-3');
                                        trangThaiCell.textContent = 'Quá giờ đặt';
                                    } else if (status === 'Còn trống') {
                                        trangThaiCell.classList.add('custom-1');
                                        trangThaiCell.textContent = 'Còn trống';
                                    } else if (status === 'Đã được đặt') {
                                        trangThaiCell.classList.add('custom-3');
                                        trangThaiCell.textContent = 'Đã được đặt';

                                        // Nút Đổi lịch
                                        const changeButton = document.createElement('button');
                                        changeButton.classList.add('btn', 'btn-outline-primary', 'changeButton');
                                        changeButton.setAttribute('type', 'button');
                                        changeButton.setAttribute('title', 'Đổi lịch');

                                        const changeIcon = document.createElement('p');
                                        changeIcon.classList.add('fe', 'fe-edit');
                                        changeButton.appendChild(changeIcon);

                                        changeButton.addEventListener('click', async () => {
                                            const row = changeButton.closest('tr');

                                            const tenSanBong = row.cells[1].textContent.trim();

                                            const ca = row.cells[3].textContent.trim();
                                            const idCa = getLastCharacterAsNumber(ca); // Hàm lấy idCa từ chuỗi ca

                                            const ngayDenSan = convertDateFormat(row.cells[2].textContent.trim()); // Định dạng lại ngày
                                            const idNgayTrongTuan = getDayOfWeek(ngayDenSan); // Lấy id ngày trong tuần

                                            const idLoaiSan = await getLoaiSanId(tenSanBong); // Hàm async để lấy ID loại sân từ tên sân bóng

                                            if (idLoaiSan) {
                                                fetch(`http://localhost:8080/san-ca/danh-sach-doi-lich/${idLoaiSan}/${idNgayTrongTuan}/${idCa}`)
                                                    .then(response => response.json())
                                                    .then(sanCaList => {
                                                        renderDoiSanList(sanCaList, document.getElementById('doiLichContainer'), ngayDenSan);
                                                        // displayDoiSan(sanCaList); // Hàm để hiển thị danh sách sân ca
                                                    })
                                                    .catch(error => {
                                                        // console.error('Lỗi khi lấy danh sách sân ca:', error);
                                                    });
                                            }

                                            $('#change-schedule-modal').modal('show');
                                        });

                                        thaoTacCell.appendChild(changeButton);
                                    } else {
                                        trangThaiCell.textContent = 'Không xác định';
                                    }
                                })
                                .catch(error => {
                                    // console.error('Lỗi khi kiểm tra trạng thái:', error);
                                    trangThaiCell.textContent = 'Lỗi';
                                });
                            // Thêm dòng vào bảng
                            sanCaTableBody.appendChild(row);
                        }
                        paginateTable('sanCaTableBody', 0, 5);
                        calculateTotalPrice();
                        // Hiển thị modal sau khi thêm dữ liệu mới
                        $('#book-modal').modal('show');
                    };

                    content.appendChild(bookingButton);
                });

            }
        }

        return content;
    }

    // Tạo phần header cho mỗi ca sân (Cập nhật để không tạo checkbox nếu trạng thái không phải là "Còn trống")
    function createSanCaHeader(sanCa) {
        const header = document.createElement('div');
        header.classList.add('san-ca-header');

        const formattedDate = formatDateToDDMMYYYY(ngayDenSanInput.value); // Định dạng ngày
        const currentTime = new Date(); // Thời gian hiện tại
        const sanCaStartTime = new Date(sanCa.thoiGianBatDauCa); // Thời gian sân ca bắt đầu

// Lấy giờ và phút
        const currentHour = currentTime.getHours();
        const currentMinute = currentTime.getMinutes();

        const sanCaHour = sanCaStartTime.getHours();
        const sanCaMinute = sanCaStartTime.getMinutes();

// So sánh thời gian
        const isPast =
            sanCaHour < currentHour || (sanCaHour === currentHour && sanCaMinute < currentMinute);

        fetch(`http://localhost:8080/hoa-don-chi-tiet/kiem-tra-dat?idSanCa=${sanCa.id}&ngayDenSan=${formattedDate}`)
            .then(response => response.text())
            .then(status => {
                // Kiểm tra trạng thái
                if (status === 'Còn trống' && !isPast) {
                    // Chỉ hiển thị checkbox nếu loại ngày là "Theo ngày"
                    if (selectedDayType === 'Theo ngày') {
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

                        // Lắng nghe sự kiện thay đổi trạng thái checkbox
                        checkbox.addEventListener('change', handleCheckboxChange);

                        header.appendChild(checkboxContainer);
                    }
                }

                // Tạo phần text của header
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
                // console.error('Lỗi khi lấy dữ liệu sân ca:', error);
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
    function updateSTT() {
        const rows = sanCaTableBody.querySelectorAll('tr'); // Lấy tất cả các dòng trong bảng
        rows.forEach((row, index) => {
            const sttCell = row.querySelector('td:first-child'); // Lấy ô STT đầu tiên trong mỗi dòng
            sttCell.textContent = index + 1; // Cập nhật STT dựa trên vị trí dòng
        });
    }

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

                    // Thêm cột STT (sẽ được cập nhật sau khi thêm dòng vào bảng)
                    const sttCell = document.createElement('td');
                    row.appendChild(sttCell);

                    const sanBongCell = document.createElement('td');
                    sanBongCell.textContent = data.tenSanBong;
                    row.appendChild(sanBongCell);

                    const ngayCell = document.createElement('td');

                    // Lấy ngày từ ô nhập và đổi định dạng từ yyyy-mm-dd sang dd-mm-yyyy
                    const parts = ngayDenSanInput.value.split('-'); // Tách ngày thành các phần (năm, tháng, ngày)
                    const formattedNgayThangNam = `${parts[2]}-${parts[1]}-${parts[0]}`; // Đảo thứ tự thành dd-mm-yyyy

                    ngayCell.textContent = formattedNgayThangNam; // Gán ngày đã đổi định dạng
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
                    giaCell.innerHTML = `${data.gia.toLocaleString()} VND`;
                    row.appendChild(giaCell);

                    // Thêm cột Trạng thái mặc định là "Còn trống"
                    const trangThaiCell = document.createElement('td');
                    trangThaiCell.classList.add('custom-1');
                    trangThaiCell.textContent = 'Còn trống'; // Trạng thái mặc định
                    row.appendChild(trangThaiCell);

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
                        updateSTT(); // Cập nhật lại STT sau khi xóa dòng
                        calculateTotalPrice();
                    });

                    thaoTacCell.appendChild(deleteButton);
                    row.appendChild(thaoTacCell);

                    // Thêm dòng vào bảng
                    sanCaTableBody.appendChild(row);

                    // Cập nhật STT sau khi thêm dòng
                    updateSTT();
                    calculateTotalPrice();
                    paginateTable('sanCaTableBody', 0, 5);
                })
                .catch(error => showError('Error fetching san ca data:', error));
        } else {
            const row = document.getElementById(`sanCaRow_${sanCaId}`);
            if (row) {
                sanCaTableBody.removeChild(row);
                updateSTT(); // Cập nhật lại STT sau khi xóa dòng
                calculateTotalPrice();
            }
            paginateTable('sanCaTableBody', 0, 5);
        }
    }

    function updateSanCaStatus(sanCaId, ngayDenSan) {
        const formattedDate = formatDateToDDMMYYYY(ngayDenSan);

        // Debug để kiểm tra giá trị ngày
        // console.log(`Calling API with sanCaId: ${sanCaId} and ngayDenSan: ${formattedDate}`);

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
            // .catch(error => console.error('Error fetching status:', error));
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

    // Khi thay đổi ngày, kiểm tra và cập nhật bảng chỉ với các checkbox đã chọn
    ngayBatDauInput.addEventListener('change', function () {
        // Xóa toàn bộ dữ liệu trong bảng
        sanCaTableBody.innerHTML = '';

        // Cập nhật nội dung san-ca-container cho ngày mới
        const sanCaContainers = document.querySelectorAll('.san-ca-container');
        sanCaContainers.forEach(container => {
            const sanBongId = container.id.split('_')[1]; // Lấy sanBongId từ id của container
            loadSanCaDetails(sanBongId); // Tải lại dữ liệu san-ca cho sân bóng
        });
    });

    // Khi thay đổi ngày, kiểm tra và cập nhật bảng chỉ với các checkbox đã chọn
    ngayKetThucInput.addEventListener('change', function () {
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
            // .catch(error => console.error('Lỗi khi tải loại sân:', error));
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
            // .catch(error => console.error('Lỗi khi tải sân bóng theo loại sân:', error));
    }

    let customers = []; // Biến lưu danh sách khách hàng từ API

// Lấy danh sách khách hàng từ API khi trang được tải
    let currentPage = 0;
    const pageSize = 10;
    let totalPages = 0;
    let allCustomers = []; // Lưu toàn bộ danh sách khách hàng

// Lấy toàn bộ danh sách khách hàng từ API và tính toán phân trang
    async function fetchCustomers() {
        try {
            const response = await fetch('http://localhost:8080/khach-hang/search-active?page=0&pageSize=1000'); // Lấy toàn bộ dữ liệu
            const data = await response.json();

            // Lưu toàn bộ khách hàng
            allCustomers = data.content.map(customer => ({
                id: customer.id,
                name: customer.hoVaTen,
                phone: customer.soDienThoai
            }));

            // Tính tổng số trang dựa trên kích thước trang
            totalPages = Math.ceil(allCustomers.length / pageSize);

            // Hiển thị khách hàng ở trang đầu tiên
            displayCustomers(getCustomersByPage(currentPage));

            // Hiển thị phân trang
            displayPagination();
        } catch (error) {
            // console.error('Lỗi khi gọi API:', error);
        }
    }

// Lấy danh sách khách hàng theo trang
    function getCustomersByPage(page) {
        const startIndex = page * pageSize;
        const endIndex = startIndex + pageSize;
        return allCustomers.slice(startIndex, endIndex);
    }

// Hiển thị danh sách khách hàng
    function displayCustomers(customerList) {
        const tbody = document.querySelector('#customerTable tbody');
        tbody.innerHTML = ''; // Xóa nội dung cũ

        customerList.forEach((customer, index) => {
            const row = document.createElement('tr');
            row.innerHTML = `
            <td>${index + 1 + currentPage * pageSize}</td>
            <td>${customer.name}</td>
            <td>${customer.phone}</td>
            <td>
                <button type="button" class="btn btn-sm btn-primary" data-id="${customer.id}" data-name="${customer.name}" data-phone="${customer.phone}">
                    Chọn
                </button>
            </td>
        `;
            tbody.appendChild(row);
        });
    }

// Hiển thị điều hướng phân trang
    function displayPagination() {
        const paginationDiv = document.getElementById('pagination');
        paginationDiv.innerHTML = ''; // Xóa nội dung cũ

        for (let i = 0; i < totalPages; i++) {
            const button = document.createElement('button');
            button.className = 'btn btn-sm btn-light mx-1';
            button.textContent = i + 1;
            button.disabled = i === currentPage; // Vô hiệu hóa nút trang hiện tại

            button.addEventListener('click', () => {
                currentPage = i;
                displayCustomers(getCustomersByPage(currentPage)); // Hiển thị khách hàng theo trang
                displayPagination(); // Cập nhật lại trạng thái các nút phân trang
            });

            paginationDiv.appendChild(button);
        }
    }

// Tìm kiếm khách hàng
    const searchInput = document.getElementById('searchCustomer');
    searchInput.addEventListener('input', function () {
        const query = this.value.toLowerCase();
        const filteredCustomers = allCustomers.filter(customer =>
            customer.name.toLowerCase().includes(query) || customer.phone.includes(query)
        );

        // Tính lại tổng số trang và hiển thị
        totalPages = Math.ceil(filteredCustomers.length / pageSize);
        currentPage = 0; // Quay về trang đầu khi tìm kiếm

        displayCustomers(getCustomersByPage(currentPage));
        displayPagination();
    });

// Gọi lần đầu để hiển thị khách hàng trang đầu
    fetchCustomers();
    paginateTable('sanCaTableBody', 0, 5);

    $('#customerTable').off('click').on('click', function (event) {
        if (event.target.tagName === 'BUTTON') {
            const selectedCustomer = {
                id: event.target.getAttribute('data-id'),
                name: event.target.getAttribute('data-name'),
                phone: event.target.getAttribute('data-phone')
            };

            // Điền thông tin khách hàng vào các trường
            document.getElementById('hoVaTen').value = selectedCustomer.name;
            document.getElementById('soDienThoai').value = selectedCustomer.phone || 'Không có';

            // Đóng modal
            $('#selectCustomerModal').modal('hide');
        }
    });

    flatpickr("#ngayDenSan", {
        minDate: "today"
    });
    flatpickr("#ngayBatDau", {
        minDate: "today"
    });
    flatpickr("#ngayKetThuc", {
        minDate: "today"
    });

});

document.querySelector('#datLich').addEventListener('click', async function () {
    // console.log("Button Đặt Lịch đã được nhấn."); // Để xác nhận rằng hàm đã được gọi

    // Hiển thị hộp thoại xác nhận
    const result = await Swal.fire({
        title: 'Xác nhận đặt lịch',
        text: 'Bạn có chắc chắn muốn đặt lịch không?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6', // Đổi màu confirmButton
        cancelButtonColor: '#d33', // Đổi màu cancelButton
        confirmButtonText: 'Thanh toán', // Đổi tên nút xác nhận
        cancelButtonText: 'Hủy',
    });

    if (!result.isConfirmed) {
        // console.log('Người dùng đã hủy thanh toán.');
        return; // Dừng thực hiện nếu người dùng chọn "Hủy"
    }

    // console.log('Người dùng xác nhận thanh toán.');

    // Lấy danh sách các dòng sân ca từ bảng
    const rows = document.querySelectorAll("#sanCaTable tbody tr");
    let hasBookedSlot = false;

    rows.forEach(row => {
        const trangThaiCell = row.querySelector('td:nth-child(7)'); // Giả sử trạng thái nằm ở cột thứ 6
        if (trangThaiCell) {
            if (trangThaiCell.textContent.trim() === 'Đã được đặt') {
                hasBookedSlot = true;
            }
            if (trangThaiCell.textContent.trim() === 'Quá giờ đặt') {
                hasBookedSlot = true;
            }
        } else {
            // console.log("Không tìm thấy ô trạng thái trong dòng này.");
        }
    });

    if (hasBookedSlot) {
        showWarningToast('Có sân đã được đặt hoặc quá giờ đặt, vui lòng chọn sân khác.');
        return;
    }

    // Lấy số điện thoại từ input
    var soDienThoai = document.getElementById('soDienThoai').value;
    var hoVaTen = document.getElementById('hoVaTen').value;

    // Kiểm tra xem số điện thoại có hợp lệ không
    if (!soDienThoai) {
        showWarningToast('Vui lòng nhập số điện thoại!');
        return;
    }

    try {
        // Gọi API để lấy idNhanVien
        const responseNhanVien = await fetch('http://localhost:8080/hoa-don/get-nhan-vien-trong-ca');
        if (!responseNhanVien.ok) {
            throw new Error("Không thể lấy thông tin nhân viên");
        }
        const nhanVienData = await responseNhanVien.json();
        const idNhanVien = nhanVienData.id;

        // Gọi API để tìm khách hàng bằng số điện thoại
        const responseKhachHang = await fetch('http://localhost:8080/khach-hang/tim-kiem-kh?soDienThoai=' + soDienThoai);

        // Nếu mã trạng thái là 400, thêm khách hàng mới
        let idKhachHang;
        if (responseKhachHang.status === 400) {
            const responseSaveKhachHang = await fetch('http://localhost:8080/khach-hang/save2', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    hoVaTen: hoVaTen,
                    soDienThoai: soDienThoai
                })
            });
            const savedKhachHang = await responseSaveKhachHang.json();
            idKhachHang = savedKhachHang.id;
        } else if (responseKhachHang.status === 200) {
            const khachHangData = await responseKhachHang.json();
            idKhachHang = khachHangData.id;
        } else {
            throw new Error('Có lỗi xảy ra khi tìm kiếm khách hàng');
        }

        // Lấy giá trị từ các trường input
        var tongTienElement = document.getElementById("tongTien");
        var tongTienText = tongTienElement.textContent || tongTienElement.innerText;

        var tongTienCocElement = document.getElementById("tienCoc");
        var tongTienCocText = tongTienCocElement.textContent || tongTienElement.innerText;

        var tongTienSan = parseInt(tongTienText.replace(/\D/g, ''), 10); // Chỉ lấy số
        var tongTienCoc = parseInt(tongTienCocText.replace(/\D/g, ''), 10); // Chỉ lấy số

        var payload = {
            idKhachHang: idKhachHang,
            tongTienSan: tongTienSan,
            tienCoc: tongTienCoc,
            idNhanVien: idNhanVien, // Lấy idNhanVien từ API
            tongTien: tongTienSan
        };

        // Gọi API để tạo hóa đơn
        const responseHoaDon = await fetch('http://localhost:8080/hoa-don/save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        const hoaDonData = await responseHoaDon.json();
        var idHoaDon = hoaDonData.id;

        // Gọi hàm thêm hóa đơn chi tiết sau khi thêm hóa đơn thành công
        await themHoaDonChiTiet(idHoaDon);

    } catch (error) {
        showErrorToast('Lỗi khi thêm hóa đơn: ' + error.message);
    }
});

async function themHoaDonChiTiet(idHoaDon) {

    // Lấy danh sách các dòng sân ca từ bảng
    const rows = document.querySelectorAll("#sanCaTable tbody tr");

    // Dùng Promise.all để xử lý tất cả các dòng sân ca cùng lúc
    const promises = Array.from(rows).map(async (row) => {
        // Lấy tên sân bóng từ bảng (Giả sử tên sân bóng ở cột 0)
        const tenSanBong = row.cells[1].textContent.trim(); // Thay đổi chỉ số cột nếu cần

        // Lấy idCa từ cột "Ca" (Giả sử cột Ca ở vị trí thứ 3)
        const ca = row.cells[3].textContent.trim();
        const idCa = getLastCharacterAsNumber(ca);

        // Lấy ngày đến sân từ cột "Ngày" (Giả sử cột ngày ở vị trí thứ 2)
        const ngayDenSan = convertDateFormat(row.cells[2].textContent.trim());
        const idNgayTrongTuan = getDayOfWeek(ngayDenSan); // Lấy idNgàyTrongTuan từ ngày

        // Lấy idSanBong từ API dựa trên tên sân bóng
        const idSanBong = await getSanBongId(tenSanBong);
        if (!idSanBong) {
            return null; // Bỏ qua nếu không lấy được idSanBong
        }

        // Gọi API để lấy danh sách sân ca dựa trên idSanBong, idNgayTrongTuan, idCa
        const sanCa = await fetchSanCa(idSanBong, idNgayTrongTuan, idCa);
        if (!sanCa) {
            return null; // Bỏ qua nếu không lấy được sân ca
        }

        const idSanCa = sanCa.id; // Giả sử bạn nhận được idSanCa từ API

        const gia = row.cells[5].textContent.trim();

        const tongTien = gia.replace(/\./g, '').replace(/,/g, '').replace(' VND', '');

        const tongTienSo = Number(tongTien); // Chuyển đổi chuỗi sang số
        const tienCocHdct = await calculateDeposit(tongTienSo || 0);
        // Kiểm tra nếu idSanCa hoặc idHoaDon bị null
        if (!idHoaDon || !idSanCa) {
            return null;
        }

        // Chuyển đổi ngày từ "yyyy-MM-dd" sang "dd/MM/yyyy"
        const dateParts = ngayDenSan.split("-");
        if (dateParts.length !== 3) {
            // console.error("Lỗi định dạng ngày:", ngayDenSan);
            return null;
        }
        const formattedDate = `${dateParts[2]}/${dateParts[1]}/${dateParts[0]}`; // Định dạng lại ngày theo dd/MM/yyyy

        // Dữ liệu để thêm hóa đơn chi tiết
        const chiTietPayload = {
            idHoaDon: idHoaDon,
            idSanCa: idSanCa,
            ngayDenSan: formattedDate, // Sử dụng ngày đã định dạng lại
            tongTien: tongTienSo, // Chuyển đổi thành số nếu cần
            tienCocHdct: Number(tienCocHdct)
        };


        // Gọi API để thêm hóa đơn chi tiết
        return fetch("http://localhost:8080/hoa-don-chi-tiet/save2", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(chiTietPayload),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                // console.log('Thêm hóa đơn chi tiết thành công:', data);
            })
            .catch(error => {
                // console.error('Lỗi khi thêm hóa đơn chi tiết:', error);
            });
    });

    // Chờ tất cả các yêu cầu API hoàn thành
    await Promise.all(promises);

    // Đóng modal sau khi đặt lịch thành công
    showSuccessToast('Đặt lịch thành công!');
    $('#book-modal').modal('hide');
    window.location.href = 'http://localhost:8080/dat-lich-tai-quay';
}

// Hàm lấy idSanBong từ API dựa trên tên sân bóng
async function getSanBongId(tenSanBong) {
    const response = await fetch(`http://localhost:8080/san-bong/findByName?tenSanBong=${encodeURIComponent(tenSanBong)}`);
    if (!response.ok) {
        return null;
    }
    const sanBong = await response.json();
    return sanBong.id; // Giả sử idSanBong nằm trong đối tượng trả về
}

// Hàm lấy sân ca từ API dựa trên idSanBong, idNgayTrongTuan và idCa
async function fetchSanCa(idSanBong, idNgayTrongTuan, idCa) {
    const response = await fetch(`http://localhost:8080/san-ca/danh-sach-san-ca/${idSanBong}/${idNgayTrongTuan}/${idCa}`);
    if (!response.ok) {
        return null;
    }
    return await response.json();
}

function getLastCharacterAsNumber(str) {
    // Kiểm tra nếu chuỗi không rỗng
    if (str.length === 0) {
        return null; // Hoặc bạn có thể trả về một giá trị khác để xử lý trường hợp này
    }

    // Lấy ký tự cuối cùng của chuỗi
    const lastChar = str.charAt(str.length - 1);

    // Chuyển đổi ký tự cuối cùng sang số
    const number = Number(lastChar);

    // Kiểm tra xem việc chuyển đổi có thành công không
    if (isNaN(number)) {
        return null; // Hoặc xử lý theo cách khác
    }

    return number;
}

// Hàm lấy tên ngày trong tuần
function getDayOfWeek(dateString) {
    const daysOfWeek = ['7', '1', '2', '3', '4', '5', '6'];
    const date = new Date(dateString);
    return daysOfWeek[date.getDay()];
}

function formatDate(date) {
    // Đảm bảo định dạng ngày theo yyyy-mm-dd
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
}

function convertDateFormat(dateString) {
    // Tách chuỗi ngày thành các phần: ngày, tháng, năm
    const parts = dateString.split('-');

    // Kiểm tra nếu định dạng ngày đúng
    if (parts.length !== 3) {
        throw new Error('Định dạng ngày không hợp lệ, vui lòng sử dụng dd-mm-yyyy');
    }

    const day = parts[0]; // Ngày
    const month = parts[1]; // Tháng
    const year = parts[2]; // Năm

    // Trả về định dạng mới yyyy-mm-dd
    return `${year}-${month}-${day}`;
}

function showErrorToast(message) {
    Toastify({
        text: message,
        duration: 3000,
        gravity: "top",
        position: "right",
        style: {
            background: "#FF0000", // Màu đỏ cho thông báo lỗi
        },
        stopOnFocus: true
    }).showToast();
}

function showWarningToast(message) {
    Toastify({
        text: message,
        duration: 3000,
        gravity: "top",
        position: "right",
        style: {
            background: "#d9b038", // Màu đỏ cho thông báo lỗi
        },
        stopOnFocus: true
    }).showToast();
}

function showSuccessToast(message) {
    Toastify({
        text: message,
        duration: 3000,
        gravity: "top",
        position: "right",
        style: {
            background: "#4CAF50", // Màu đỏ cho thông báo lỗi
        },
        stopOnFocus: true
    }).showToast();
}

async function calculateTotalPrice() {
    let total = 0;
    const rows = document.querySelectorAll('#sanCaTable tbody tr');

    // Duyệt qua từng dòng trong bảng
    rows.forEach(row => {
        const priceCell = row.querySelector('td:nth-child(6)'); // Cột giá nằm ở vị trí thứ 6
        if (priceCell) {
            const priceText = priceCell.textContent.replace(/\D/g, ''); // Xóa các ký tự không phải số
            const price = parseInt(priceText);
            if (!isNaN(price)) {
                total += price;
            }
        }
    });

    // Cập nhật tổng tiền trong modal footer
    document.getElementById('tongTien').textContent = `${total.toLocaleString()} VND`;

    try {
        // Gọi API để lấy giá trị tiền cọc
        const response = await fetch('http://localhost:8080/tham-so/searchMaFake/TSTIEN_COC');
        const data = await response.json();

        // Lấy giá trị tiền cọc từ API và tính toán
        if (data && data.trangThai) {
            const depositRate = parseFloat(data.giaTri);
            const depositAmount = (total * depositRate) / 100;

            // Cập nhật tiền cọc trong modal
            document.getElementById('tienCoc').textContent = `${depositAmount.toLocaleString()} VND`;
        }
    } catch (error) {
        document.getElementById('tienCoc').textContent = '0 VND'; // Hiển thị giá trị mặc định nếu API lỗi
    }
}

// Hàm giả lập lấy idSanBong từ tên sân bóng
async function getLoaiSanId(tenSanBong) {
    const response = await fetch(`http://localhost:8080/san-bong/findByName?tenSanBong=${tenSanBong}`);
    const data = await response.json();
    return data.idLoaiSan;
}

// Hàm gọi API để lấy tỷ lệ tiền cọc
async function fetchDepositRate() {
    try {
        const response = await fetch('http://localhost:8080/tham-so/searchMaFake/TSTIEN_COC');
        const data = await response.json();
        return {
            value: data.giaTri,  // Giá trị tỷ lệ tiền cọc
            type: data.typeGiaTri // Loại tỷ lệ (% hoặc giá trị cố định)
        };
    } catch (error) {
        // Trả về giá trị mặc định nếu không tìm thấy hoặc gặp lỗi
        return {
            value: 0,  // Giá trị mặc định = 0
            type: '%'  // Loại mặc định là %, để xử lý an toàn
        };
    }
}

// Hàm tính toán tiền cọc
async function calculateDeposit(giaSan) {
    const depositRate = await fetchDepositRate();  // Lấy tỷ lệ tiền cọc

    let deposit = 0;
    if (depositRate.type === '%') {
        deposit = (giaSan * depositRate.value) / 100; // Tính theo phần trăm
    } else {
        deposit = depositRate.value; // Giá trị cố định
    }

    return deposit;
}

function paginateTable(tableBodyId, currentPage, rowsPerPage) {
    const tableBody = document.getElementById(tableBodyId);
    if (!tableBody) {
        return;
    }

    const rows = tableBody.querySelectorAll('tr');
    const totalRows = rows.length;
    const totalPages = Math.ceil(totalRows / rowsPerPage);


    // Ẩn tất cả các hàng
    rows.forEach((row, index) => {
        row.style.display = 'none';
        if (index >= currentPage * rowsPerPage && index < (currentPage + 1) * rowsPerPage) {
            row.style.display = '';
        }
    });

    updatePagination(currentPage, totalPages, '', '', tableBodyId, 'paginationContainer', false);
}

function updatePagination(currentPage, totalPages, keyWord, trangThai, tableBodyId, paginationContainerId, isModal) {
    const paginationContainer = document.getElementById(paginationContainerId);
    if (!paginationContainer) {
        return;
    }

    paginationContainer.innerHTML = '';

    if (currentPage > 0) {
        const prevButton = document.createElement('button');
        prevButton.className = 'btn btn-success me-2';
        prevButton.textContent = '<';
        prevButton.onclick = () => {
            paginateTable(tableBodyId, currentPage - 1, 5);
        };
        paginationContainer.appendChild(prevButton);
    }

    const pageDisplay = document.createElement('span');
    pageDisplay.className = 'btn btn-outline-secondary';
    pageDisplay.textContent = `Trang ${currentPage + 1} / ${totalPages}`;
    paginationContainer.appendChild(pageDisplay);

    if (currentPage < totalPages - 1) {
        const nextButton = document.createElement('button');
        nextButton.className = 'btn btn-success ms-2';
        nextButton.textContent = '>';
        nextButton.onclick = () => {
            paginateTable(tableBodyId, currentPage + 1, 5);
        };
        paginationContainer.appendChild(nextButton);
    }
}