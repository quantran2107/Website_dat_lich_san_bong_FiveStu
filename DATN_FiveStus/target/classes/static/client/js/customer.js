$(document).ready(function () {

    showCustomerDetails();

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

    function  showCustomerDetails(){
        $('#content').html('');
        let html=``;
        html += `<p>Nội dung thông tin của TK ở đây</p>`;
        $('#content').html(html);
    }

    function  showCustomerChangerPass(){
        $('#content').html('');
        let html=``;
        html += `<p>Nội dung thay đổi password của TK ở đây</p>`;
        $('#content').html(html);
    }
    function showCustomerCalendar(){
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