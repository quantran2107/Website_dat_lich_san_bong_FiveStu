$(document).ready(function () {
    let list = [];

    let apiGetUrl = "http://localhost:8080/lich-lam-viec/hien-thi";

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
                    url: '/lich-lam-viec/upload',
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
            fileInput.remove(); // Xóa input sau khi xử lý xong
        });

        // Kích hoạt input file
        fileInput.click();
    });


    loadTable('', '', '');
    $.ajax({
        url: 'http://localhost:8080/nhan-vien/hien-thi',
        method: 'GET',
        success: function(response) {
            response.forEach(function(nv) {
                let nvValue = JSON.stringify(nv);
                let option = $('<option></option>')
                    .val(nvValue)
                    .text(nv.hoTen);
                $('#nhanVienSel').append(option);

            });
        },
        error: function(xhr, status, error) {
        }
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

        let key = $('#searchInput').val();
        let selectedDate = $('#date-input').val();
        loadTable(key, selectedStatus, selectedDate);

    });
    $('#date-input').change(function () {
        let selectedDate = $(this).val();
        let key = $('#searchInput').val();
        let selectedValue = $('#actionMenuButton3').text().trim();
        loadTable(key, selectedValue, selectedDate);
    });
    $('#searchInput').on('input', function () {
        let key = $(this).val().trim();
        let selectedDate = $('#date-input').val();
        let selectedValue = $('#actionMenuButton3').text().trim();
        loadTable(key, selectedValue, selectedDate);
    });
    $('#load').on('click',()=>{
        $('#actionMenuButton3').text('Tất cả');
        $('#searchInput').val('')
        $('#date-input').val('');
        loadTable('', '', '');
    })

    function loadTable(keyString = '', ca = '', date = '') {

        let tbody = '';
        $.getJSON(apiGetUrl, function (data) {
            list = data;

            let today = new Date();

            let year = today.getFullYear();
            let month = ('0' + (today.getMonth() + 1)).slice(-2);
            let day = ('0' + today.getDate()).slice(-2);

            let formattedDate = year + '-' + month + '-' + day;
            if (keyString !== '' && ca !== '' && date !== '') {
                list = list.filter(lich => lich["gioBatDau"] === ca && lich["ngay"] === date &&
                    (lich["nhanVien"].maNhanVien.toLowerCase().includes(keyString.toLowerCase()) ||
                        lich["nhanVien"].hoTen.toLowerCase().includes(keyString.toLowerCase())));
            }
            if (date !== '' && keyString === '' && ca !== '') {
                list = list.filter(lich => lich["gioBatDau"] === ca && lich["ngay"] === date);
            }
            if (date !== '' && keyString === '' && ca === '') {
                list = list.filter(lich => lich["ngay"] === date);
            }

            if (date === '' && keyString === '' && ca === '') {
                list = list.filter(lich => lich["ngay"] === formattedDate
                );
            }

            if (date === '' && keyString !== '' & ca !== '') {
                list = list.filter(lich => lich["ngay"] === formattedDate && lich["gioBatDau"] === ca &&
                    (lich["nhanVien"].maNhanVien.toLowerCase().includes(keyString.toLowerCase()) ||
                        lich["nhanVien"].hoTen.toLowerCase().includes(keyString.toLowerCase())));
            }

            if (date === '' && keyString === '' && ca !== '') {
                list = list.filter(lich => lich["ngay"] === formattedDate && lich["gioBatDau"] === ca);
            }

            if (date === '' && keyString !== '' && ca === '') {
                list = list.filter(lich => lich["ngay"] === formattedDate &&
                    (lich["nhanVien"].maNhanVien.toLowerCase().includes(keyString.toLowerCase()) ||
                        lich["nhanVien"].hoTen.toLowerCase().includes(keyString.toLowerCase())));
            }

            if (list.length === 0) {
                tbody += `<tr style="cursor: default"><td colspan="8" style="font-size: 18px; padding: 20px">Chưa có dữ liệu</td></tr>`
                $('#tbodyContainer').html(tbody);
            } else {
                list.forEach((lich, index) => {
                    let ngayFormatted = new Date(lich["ngay"]).toLocaleDateString('vi-VN');
                    let gioBd = lich["gioBatDau"]=== null?"--:--":lich["gioBatDau"].slice(0, -3);
                    let giokt = lich["gioKetThuc"]=== null?"--:--":lich["gioKetThuc"].slice(0, -3);
                    tbody += `<tr style="cursor: default">
                            <td class="special-td">${index + 1}</td>
                            <td class="special-td">${lich["nhanVien"].maNhanVien}</td>
                            <td class="special-td">${lich["nhanVien"].hoTen}</td>                                                
                            <td class="special-td">${lich["viTri"]}</td>
                            <td class="special-td">${lich["gioBatDau"] > '00:30:00' &&  lich["gioKetThuc"] <'13:00:00'? 'Ca sáng' : 'Ca chiều'}</td>
                            <td class="special-td">${gioBd}</td>
                            <td class="special-td">${giokt}</td>
                            <td class="special-td">${ngayFormatted}</td>
                        </tr>`;
                });
                $('#tbodyContainer').html(tbody);
            }
        });
    }

});