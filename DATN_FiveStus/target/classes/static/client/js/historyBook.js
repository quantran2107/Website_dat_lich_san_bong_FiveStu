$(document).ready(function () {

    const content = $('#contentHistory');

    showCustomerCalendar();

    async function loadCustomer() {
        const response = await fetch('http://localhost:8080/customer/get-customer');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return await response.json();
    }

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
                if (!response.response.length < 1) {
                    setRowInTable(response.response);
                }

            }
        });
    }

    function setRowInTable(response) {
        let wait = 0;
        let done = 0;
        let huy = 0;
        $.each(response, function (index, item) {
            let ngayDat = formatDate(item["ngayDat"]);
            let ca = formatHour(item["thoiGianBatDau"]);
            if (item["cancel"]) {
                huy++;
                let row = `
                <tr>
                    <td style="border: 1px solid #ccc; padding: 8px;">${huy}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["tenSanBong"]}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${ca}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${ngayDat}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${(item["tongTien"] ? item["tongTien"].toLocaleString() : '0') + ' VND'}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;"> ${(item["tienCoc"] ? item["tienCoc"].toLocaleString() : '0') + ' VND'}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${(item["tongGiamGia"] ? item["tongGiamGia"].toLocaleString() : '0') + ' VND'}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["maHoaDon"]}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">Đã hủy</td>
                </tr>
                `
                $(`#tbodyCancel`).append(row);
            } else if (item["trangThaiCheckIn"] === "Chờ nhận sân" || item["trangThaiCheckIn"] === "Đang hoạt động") {
                wait++;
                let row = `
                <tr>
                    <td style="border: 1px solid #ccc; padding: 8px;">${wait}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["tenSanBong"]}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${ca}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${ngayDat}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${(item["tongTien"] ? item["tongTien"].toLocaleString() : '0') + ' VND'}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;"> ${(item["tienCoc"] ? item["tienCoc"].toLocaleString() : '0') + ' VND'}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${(item["tongGiamGia"] ? item["tongGiamGia"].toLocaleString() : '0') + ' VND'}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["maHoaDon"]}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["trangThaiHoaDon"]}</td>
                   <td style="border: 1px solid #ccc; padding: 8px;">
                       
                        <button type="button" class="btn btn-outline-danger"  id="huy${item['idHdct']}" >Hủy</button>
                   </td>
                </tr>
                `;
                $(`#tbodyFuture`).append(row);
                // $(`#detail${item['idHdct']}`).on('click', () => {
                //     detailsHD(item)
                // });
                $(`#huy${item['idHdct']}`).on('click', () => {
                    huyHD(item)
                });
            } else {
                done++;
                let row = `
                <tr>
                    <td style="border: 1px solid #ccc; padding: 8px;">${done}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["tenSanBong"]}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${ca}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${ngayDat}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${(item["tongTien"] ? item["tongTien"].toLocaleString() : '0') + ' VND'}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;"> ${(item["tienCoc"] ? item["tienCoc"].toLocaleString() : '0') + ' VND'}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${(item["tongGiamGia"] ? item["tongGiamGia"].toLocaleString() : '0') + ' VND'}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["maHoaDon"]}</td>
                    <td style="border: 1px solid #ccc; padding: 8px;">${item["trangThaiHoaDon"]}</td>
                </tr>
                `;
                $(`#tbodySince`).append(row);
            }

        })
        if (done > 0) {
            $('#tbodySince tr:first').remove();
        }
        if (wait > 0) {
            $('#tbodyFuture tr:first').remove();
        }
        if (huy > 0) {
            $('#tbodyCancel tr:first').remove();
        }

    }


    function setTableInTab() {
        content.html('');
        let html = ``;
        html += `<ul class="nav nav-tabs" id="myTab" role="tablist">
            <li class="nav-item">
                <a class="nav-link active" id="home-tab" data-bs-toggle="tab" href="#future" role="tab" aria-controls="future" aria-selected="true">Sắp diễn ra</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="profile-tab" data-bs-toggle="tab" href="#past" role="tab" aria-controls="past" aria-selected="false">Đã kết thúc</a>
            </li>
             <li class="nav-item">
                <a class="nav-link" id="profile-tab" data-bs-toggle="tab" href="#cancel" role="tab" aria-controls="cancel" aria-selected="false">Đã hủy</a>
            </li>
        </ul>

        <div class="tab-content mt-3" id="myTabContent">
            <div class="tab-pane fade show active" id="future" role="tabpanel" aria-labelledby="home-tab">
               <table class="table table-hover" style="width: 100%; border-collapse: collapse;">
                                    <thead>
                                    <tr >
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">#</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Tên sân</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Thời gian (Ca)</th>                                        
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Ngày đặt</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Tổng tiền</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Tiền cọc</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Giảm giá</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Mã hóa đơn</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Trạng thái hóa đơn</th>
                                        <th scope="col" style="position: sticky; top: 0; background-color: white; z-index: 10; border: 1px solid #ccc; padding: 8px;">Hành động</th>
                                        
                                    </tr>
                                    </thead>
                                    <tbody id="tbodyFuture">
                                    <tr>
                                        <td colspan="10" style="text-align: center; font-weight: bold;">
                                            Không có dữ liệu
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
            </div>
            <div class="tab-pane fade" id="past" role="tabpanel" aria-labelledby="profile-tab">
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

            </div>
            <div class="tab-pane fade" id="cancel" role="tabpanel" aria-labelledby="profile-tab">
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
                                    <tbody id="tbodyCancel">
                                    <tr>
                                        <td colspan="9" style="text-align: center; font-weight: bold;">
                                            Không có dữ liệu
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>

            </div>
        </div>
`;
        content.html(html);
    }

    function huyHD(item) {
        Swal.fire({
            title: "Bạn có muốn hủy không?",
            text: "Nếu hủy bạn sẽ mất tiền cọc!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Đồng ý",
            cancelButtonText: "Đóng",
        }).then((result) => {
            if (result.isConfirmed) {
                checkDataFromServer(item).then((check) => {
                    if (check) {
                        Swal.fire({
                            title: "Không thể hủy đơn!",
                            text: "Đã quá thời gian có thể hủy đơn.",
                            icon: "error"
                        });
                    } else {

                        $.ajax({
                            url: 'http://localhost:8080/hoa-don-chi-tiet/huy-dat/' + item["idHdct"],
                            type: 'PUT',
                            success: function (response) {
                                if (response === true) {
                                    Swal.fire({
                                        title: "Thành công!",
                                        text: "Bạn đã hủy hóa đơn này.",
                                        icon: "success"
                                    });
                                    showCustomerCalendar();
                                } else {
                                    Swal.fire({
                                        title: "Có lỗi xảy ra!",
                                        text: "Hãy thử lại.",
                                        icon: "error"
                                    });
                                }

                            },
                            error: function (xhr, status, error) {
                                Swal.fire({
                                    title: "Có lỗi xảy ra!",
                                    text: "Hãy thử lại.",
                                    icon: "error"
                                });
                            }
                        });

                    }
                });
            }
        });

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

    function formatHour(dateString) {
        const date = new Date(dateString);

        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        ;
        return `${hours}:${minutes}`;
    }

    function checkDataFromServer(item) {
        return new Promise((resolve, reject) => {
            $.ajax({
                url: 'http://localhost:8080/san-ca/tham-so',
                method: 'GET',
                dataType: 'json',
                success: function (response) {
                    const now = new Date();

                    const hoursBeforeItem = parseInt(response);

                    const itemTime = new Date(item["ngayDenSan"]);

                    const now1 = new Date(itemTime);
                    now1.setHours(now1.getHours() - hoursBeforeItem);

                    if (now < now1) {
                        resolve(true);
                    } else {
                        resolve(false);
                    }
                },
                error: function () {

                    resolve(false);
                }
            });
        });
    }
});