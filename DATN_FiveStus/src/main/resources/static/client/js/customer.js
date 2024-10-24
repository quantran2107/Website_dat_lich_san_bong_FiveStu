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
    async function showCustomerDetails(){
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
    function htmlDetaisCustomer(customer){

        $('#content').html('');
        let html=``;
        html += `<p>${customer.hoVaTen}</p>`;
        $('#content').html(html);
    }

    async function  showCustomerChangerPass(){
        try {
            let response = await loadCustomer(); // Gọi hàm để lấy dữ liệu
            htmlCustomerChangerPass(response.response)
        } catch (error) {
        }

    }

    function htmlCustomerChangerPass(customer){
        $('#content').html('');
        let html=``;
        html += `<p>Nội dung thay đổi password của TK ở đây</p>`;
        $('#content').html(html);
    }


    // KHU VỰC CODE CỦA TRƯỜNG (từ đây trở xuống, bằng code thì lên trên hàm này)

    async function showCustomerCalendar(){
        try {
            let response = await loadCustomer(); // Gọi hàm để lấy dữ liệu
            htmlCustomerCalendar(response.response)
        } catch (error) {
        }

    }

    function htmlCustomerCalendar(customer){

        $('#content').html('');
        let html=``;
        html += `<div class="row">
                                <h3>SẮP DIỄN RA</h3>
                            </div>
                            <div class="row">
                                <table class="table table-hover">
                                    <thead>
                                    <tr>
                                        <th scope="col">#</th>
                                        <th scope="col">Tên sân</th>
                                        <th scope="col">Ngày đặt</th>
                                        <th scope="col">Giá</th>
                                        <th scope="col">Trạng thái</th>
                                        <th scope="col">Hành động</th>
                                    </tr>
                                    </thead>
                                    <tbody id="tbodyFuture">
                                    <tr>
                                        <td colspan="6" style="text-align: center; font-weight: bold;">
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
                                        <th scope="col">Ngày đặt</th>
                                        <th scope="col">Giá</th>
                                        <th scope="col">Trạng thái</th>
                                        <th scope="col">Hành động</th>
                                    </tr>
                                    </thead>
                                    <tbody id="tbodySince">
                                    <tr>
                                        <td colspan="6" style="text-align: center; font-weight: bold;">
                                            Không có dữ liệu
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>`;
        $('#content').html(html);
    }

});