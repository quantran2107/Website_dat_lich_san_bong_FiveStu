$(document).ready(function () {

    const content = $('#contentProfile');

    showCustomerDetails();

    $("#profileCustomer").click(function (event) {
        event.preventDefault();
        showCustomerDetails();
    });

    $("#changerPass").click(function (event) {
        event.preventDefault();
        showCustomerChangerPass();
    });

    $("#historyBill").click(function (event) {
        event.preventDefault();
        showCustomerCalendar();
    });

    async function loadCustomer() {
        const response = await fetch('http://localhost:8080/customer/get-customer');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return await response.json();
    }

    // Bằng

    async function showCustomerDetails() {
        try {
            let response = await loadCustomer(); // Gọi hàm để lấy dữ liệu
            htmlDetaisCustomer(response.response)
        } catch (error) {
        }
    }

    function htmlDetaisCustomer(customer) {

        content.html('');
        let html = ``;
        html += `<p>${customer.hoVaTen}</p>`;
        content.html(html);
    }

    async function showCustomerChangerPass() {
        try {
            let response = await loadCustomer(); // Gọi hàm để lấy dữ liệu
            htmlCustomerChangerPass(response.response)
        } catch (error) {
        }

    }

    function htmlCustomerChangerPass(customer) {
        content.html('');
        let html = ``;
        html += `<p>Nội dung thay đổi password của TK ở đây</p>`;
        content.html(html);
    }

    //Trường
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
                    <td style="border: 1px solid #ccc; padding: 8px;">${wait}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["tenSanBong"]}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${ca}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${ngayDat}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["tongTien"]}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["tienCoc"]}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["tongGiamGia"]}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["maHoaDon"]}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["trangThaiHoaDon"]}</td>
                </tr>
                `;
                $(`#tbodyFuture`).append(row);
            } else {
                done++;
                let row = `
                <tr>
                    <td style="border: 1px solid #ccc; padding: 8px;">${done}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["tenSanBong"]}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${ca}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${ngayDat}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["tongTien"]}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["tienCoc"]}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["tongGiamGia"]}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["maHoaDon"]}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["trangThaiHoaDon"]}</td>
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
        content.html('');
        let html = ``;
        html += `<div class="row">
                                <h3>SẮP DIỄN RA</h3>
                            </div>
                            <div class="row" style="max-height: 400px; overflow-y: auto; border: 1px solid #ccc; border-radius: 5px;">
                                <table class="table table-hover" style="width: 100%; border-collapse: collapse;">
                                    <thead>
                                    <tr>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">#</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Tên sân</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Thời gian (Ca)</th>                                        
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Ngày đặt</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Tổng tiền</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Tiền cọc</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Giảm giá</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Mã hóa đơn</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Trạng thái hóa đơn</th>
                                        
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
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">#</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Tên sân</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Thời gian (Ca)</th>                                        
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Ngày đặt</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Tổng tiền</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Tiền cọc</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Giảm giá</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Mã hóa đơn</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Trạng thái hóa đơn</th>
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
        content.html(html);
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