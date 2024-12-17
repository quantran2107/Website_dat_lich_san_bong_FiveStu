$(document).ready(function () {

    $('#uploadButton').on('click', function () {

        const fileInput = $('<input type="file" style="display: none;">');
        $('body').append(fileInput); // Thêm vào DOM

        fileInput.on('change', function () {
            const file = this.files[0];
            if (file) {
                const formData = new FormData();
                formData.append('file', file);

                $.ajax({
                    type: 'POST',
                    enctype: 'multipart/form-data',
                    url: '/lich-lam-viec/upload',
                    data: formData,
                    processData: false,
                    contentType: false,
                    cache: false,
                    timeout: 1000000,
                    success: function () {
                        showSuccessToast("Tải lên file thành công!");
                        loadTable()
                    },
                    error: function () {
                        showErrorToast("Tải lên file thất bại!");
                    }
                });
            }
            fileInput.remove();
        });

        fileInput.click();
    });

    loadForm();

    function loadForm() {

        $.ajax({
            url: 'http://localhost:8080/lich-lam-viec/hien-thi?date=' + (formatDateToYYYYMMDD(new Date())) + '&status=' + "Chọn ca",
            method: 'GET',
            success: function (response) {
                response.forEach(function (nv) {
                    let nvValue = JSON.stringify(nv);
                    let option = $('<option></option>')
                        .val(nvValue)
                        .text(nv.hoTen);
                    $('#nhanVienSel').append(option);

                });
            },
            error: function (xhr, status, error) {
            }
        });
    }


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

    $('#btnExcelMauLich').on('click', function () {
        let url = 'https://docs.google.com/spreadsheets/d/1Zi34HdFkU5Ik4L3T8JT82k4KJFmP7kvD/export?format=xlsx';

        let $a = $('<a></a>').attr('href', url).attr('download', 'file.xlsx').appendTo('body');

        $a[0].click();

        $a.remove();
    });

    $('#status-select .dropdown-item').click(function (event) {
        event.preventDefault();
        let selectedStatus = $(this).text().trim();
        $('#actionMenuButton3').text(selectedStatus);
        loadTable();
    });
    $('#date-input').change(function () {
        loadTable();
    });
    $('#searchInput').on('input', function () {
        loadTable();
    });
    $('#load').on('click', () => {
        $('#actionMenuButton3').text('Chọn ca');
        $('#searchInput').val('')
        $('#date-input').val('');
        loadTable();
    })

    function loadRow(list, key) {
        $('#tbodyContainer').empty();

        let totalPages = Math.ceil(list.length / 10);
        let selectOptions = `<option value="1" selected>1</option>`;

        let tbody = ``;
        if (key !== '') {
            list = list.filter(lich =>
                lich["nhanVien"].maNhanVien.toLowerCase().includes(key.toLowerCase()) ||
                lich["nhanVien"].hoTen.toLowerCase().includes(key.toLowerCase())
            );
        }
        if (list.length === 0) {
            tbody += `<tr style="cursor: default"><td colspan="8" style="font-size: 18px; padding: 20px">Chưa có dữ liệu</td></tr>`
            $('#tbodyContainer').html(tbody);
            $('#pageSelect').empty();
            return
        }


        if ($('#pageSelect option').length < 1) {
            for (let i = 2; i <= totalPages; i++) {
                selectOptions += `<option value="${i}" >${i}</option>`;
            }
            $('#pageSelect').html(selectOptions);
        }

        let currentPage = $('#pageSelect').val();

        const startIndex = (currentPage - 1) * 10;
        const endIndex = Math.min(startIndex + 10, list.length);
        const paginatedList = list.slice(startIndex, endIndex);

        paginatedList.forEach((lich, index) => {
            let ngayFormatted = new Date(lich["ngay"]).toLocaleDateString('vi-VN');
            let gioBd = lich["gioBatDau"] === null ? "--:--" : lich["gioBatDau"].slice(0, -3);
            let giokt = lich["gioKetThuc"] === null ? "--:--" : lich["gioKetThuc"].slice(0, -3);
            let nhanVien =  lich["nhanVien"];
            tbody += `<tr style="cursor: default">
                            <td class="special-td">${startIndex + index + 1}</td>
                            <td class="special-td">${nhanVien["maNhanVien"]}</td>
                            <td class="special-td">${nhanVien["hoTen"]}</td>                                                
                            <td class="special-td">${lich["viTri"]}</td>
                            <td class="special-td">${lich["gioBatDau"] > '00:30:00' && lich["gioKetThuc"] < '13:00:00' ? 'Ca sáng' : 'Ca chiều'}</td>
                            <td class="special-td">${gioBd}</td>
                            <td class="special-td">${giokt}</td>
                            <td class="special-td">${ngayFormatted}</td>
                        </tr>`;
        });
        $('#tbodyContainer').html(tbody);

    }

    $('#prevButton').on('click', function () {
        const $select = $('#pageSelect');
        const $selected = $select.find('option:selected'); // Lấy option đang được chọn
        const $prev = $selected.prev('option'); // Lấy option trước đó

        if ($prev.length) { // Nếu có option trước đó
            $prev.prop('selected', true); // Chọn option trước đó
        }
        loadTable()
    });

    $('#nextButton').on('click', function () {
        const $select = $('#pageSelect');
        const $selected = $select.find('option:selected'); // Lấy option đang được chọn
        const $next = $selected.next('option'); // Lấy option tiếp theo

        if ($next.length) { // Nếu có option tiếp theo
            $next.prop('selected', true); // Chọn option tiếp theo
        }
        loadTable()
    });


    $('#pageSelect').on('change', function () {
        loadTable();
    });

    function formatDateToYYYYMMDD(date) {
        let year = date.getFullYear();
        let month = date.getMonth() + 1;
        let day = date.getDate();

        month = month < 10 ? '0' + month : month;
        day = day < 10 ? '0' + day : day;

        return year + '-' + month + '-' + day;
    }

    loadTable();

    function loadTable() {
        let selected = $('#actionMenuButton3').text().trim()
        let today = new Date();
        let day = $('#date-input').val();
        let key = $('#searchInput').val().trim();

        $.ajax({
            url: 'http://localhost:8080/lich-lam-viec/hien-thi?date=' + (day === "" ? formatDateToYYYYMMDD(today) : day) + "&status=" + selected,
            method: 'GET',
            success: function (response) {
                loadRow(response, key)
            },
            error: function (xhr, status, error) {
            }
        });
    }

});