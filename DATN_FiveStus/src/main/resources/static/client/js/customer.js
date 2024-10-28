$(document).ready(function () {


    showCustomerDetails();

    async function loadCustomer() {
        const response = await fetch('http://localhost:8080/customer/get-customer');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return await response.json();
    }


    $("#customerDetails").click(function (event) {
        event.preventDefault();
        showCustomerDetails();
    });

    $("#customerChangePassword").click(function (event) {
        event.preventDefault();
        showCustomerChangerPass();
    });

    $("#customerCalender").click(function (event) {
        event.preventDefault();
        showCustomerCalendar();
    });


    // KHU VỰC CODE CỦA BẰNG

    // hàm xử lý bất đồng bộ để chạy html
    async function showCustomerDetails() {
        try {
            let response = await loadCustomer(); // Gọi hàm để lấy dữ liệu
            htmlDetaisCustomer(response.response)
        } catch (error) {
        }

    }

    //hàm của Bằng (trường cmt)
    // customer trong hàm này là dạng KhachHangDTO
    // thực hiện generate html theo customer đã có
    //nếu có sự kiện click hoặc bất kì sự kiện nào cần tạo html mới thì viết 1 hàm khác và truyền customer vào đó
    function htmlDetaisCustomer(customer) {

        $('#content').html('');
        let html = ``;
        html += `<p>${customer.hoVaTen}</p>`;
        $('#content').html(html);
    }

    async function showCustomerChangerPass() {
        try {
            let response = await loadCustomer(); // Gọi hàm để lấy dữ liệu
            htmlCustomerChangerPass(response.response)
        } catch (error) {
        }

    }

    function htmlCustomerChangerPass(customer) {
        $('#content').html('');
        let html = ``;
        html += `<p>Nội dung thay đổi password của TK ở đây</p>`;
        $('#content').html(html);
    }


    // KHU VỰC CODE CỦA TRƯỜNG (từ đây trở xuống, bằng code thì lên trên hàm này)

    async function showCustomerCalendar() {
        try {
            let response = await loadCustomer(); // Gọi hàm để lấy dữ liệu
            htmlCustomerCalendar(response.response)
        } catch (error) {
        }
    }

    function htmlCustomerCalendar(customer) {
        setTableInTab();
        $.ajax({
            url: ' http://localhost:8080/san-ca/for-customer-profile/' + customer.id,
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                if (!response.response.length<1){
                    setRowInTable(response.response);
                }

            }
        });
    }

    function setRowInTable(response) {
        let wait = 0;
        let done = 0;
        $.each(response, function (index, item) {
            let ngayDat = formatDate(item["ngayDat"]);
            let ca = formatDate(item["thoiGianBatDau"]);
            if (item["trangThaiCheckIn"] === "Chờ nhận sân" || item["trangThaiCheckIn"] === "Đang hoạt động") {
                wait++;
                let row = `
                <tr>
                    <td>${wait}</td>
                    <td>${item["tenSanBong"]}</td>
                    <td>${ca}</td>
                    <td>${ngayDat}</td>
                    <td>${item["tongTien"]}</td>
                    <td>${item["tienCoc"]}</td>
                    <td>${item["tongGiamGia"]}</td>
                    <td>${item["maHoaDon"]}</td>
                    <td>${item["trangThaiHoaDon"]}</td>
                </tr>
                `;
                $(`#tbodyFuture`).append(row);
            } else {
                done++;
                let row = `
                <tr>
                    <td>${done}</td>
                    <td>${item["tenSanBong"]}</td>
                    <td>${ca}</td>
                    <td>${ngayDat}</td>
                    <td>${item["tongTien"]}</td>
                    <td>${item["tienCoc"]}</td>
                    <td>${item["tongGiamGia"]}</td>
                    <td>${item["maHoaDon"]}</td>
                    <td>${item["trangThaiHoaDon"]}</td>
                </tr>
                `;
                $(`#tbodySince`).append(row);
            }

        })
        if (done>0){
            $('#tbodySince tr:first').remove();
        }
        if (wait>0){
            $('#tbodyFuture tr:first').remove();
        }
    }

    function setTableInTab() {
        $('#content').html('');
        let html = ``;
        html += `<div class="row">
                                <h3>SẮP DIỄN RA</h3>
                            </div>
                            <div class="row">
                                <table class="table table-hover">
                                    <thead>
                                    <tr>
                                        <th scope="col">#</th>
                                        <th scope="col">Tên sân</th>
                                        <th scope="col">Thời gian (Ca)</th>                                        
                                        <th scope="col">Ngày đặt</th>
                                        <th scope="col">Tổng tiền</th>
                                        <th scope="col">Tiền cọc</th>
                                        <th scope="col">Giảm giá</th>
                                        <th scope="col">Mã hóa đơn</th>
                                        <th scope="col">Trạng thái hóa đơn</th>
                                        
                                    </tr>
                                    </thead>
                                    <tbody id="tbodyFuture">
                                    <tr>
                                        <td colspan="9" style="text-align: center; font-weight: bold;">
                                            Không có dữ liệu
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <hr>
                            <div class="row">
                                <h3>ĐÃ KẾT THÚC</h3>
                            </div>
                            <div class="row">
                                <table class="table table-hover">
                                    <thead>
                                    <tr>
                                        <th scope="col">#</th>
                                        <th scope="col">Tên sân</th>
                                        <th scope="col">Thời gian (Ca)</th>                                        
                                        <th scope="col">Ngày đặt</th>
                                        <th scope="col">Tổng tiền</th>
                                        <th scope="col">Tiền cọc</th>
                                        <th scope="col">Giảm giá</th>
                                        <th scope="col">Mã hóa đơn</th>
                                        <th scope="col">Trạng thái hóa đơn</th>
                                    </tr>
                                    </thead>
                                    <tbody id="tbodySince">
                                    <tr>
                                        <td colspan="9" style="text-align: center; font-weight: bold;">
                                            Không có dữ liệu
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>`;
        $('#content').html(html);
    }

    function formatDate(dateString) {
        const date = new Date(dateString);

        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();

        return `${hours}:${minutes} - ${day}/${month}/${year}`;
    }

});