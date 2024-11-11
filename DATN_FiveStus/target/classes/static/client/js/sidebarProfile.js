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

    $("#customerAddress").click(function (event) {
        event.preventDefault();
        showCustomerAddress();
    });

    async function loadCustomer() {
        const response = await fetch('http://localhost:8080/customer/get-customer');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return await response.json();
    }

    async function loadCustomerAddressByEmail(email) {
        const response = await fetch(`http://localhost:8080/dia-chi/email/${email}`);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return await response.json();
    }

    async function showCustomerAddress() {
        try {
            let response = await loadCustomer();
            let email = response.response.email;

            // Gọi API để lấy danh sách địa chỉ theo email
            let addressResponse = await loadCustomerAddressByEmail(email);

            // Hiển thị danh sách địa chỉ
            htmlAddressCustomer(addressResponse, email); // Truyền email vào hàm hiển thị
        } catch (error) {
            console.error("Lỗi khi tải địa chỉ khách hàng:", error);
            $('#content').html('<p>Không thể tải dữ liệu địa chỉ.</p>');
        }
    }

    function htmlAddressCustomer(addresses, email) {
        const addressContainer = $('#contentProfile');

        // Xóa nội dung hiện tại của container
        addressContainer.html('');

        // Tạo phần tiêu đề và nút thêm (chỉ một lần)
        const headerHtml = `
    <div class="card-header d-flex justify-content-between align-items-center text-black">
        <h4 class="mb-0">Địa chỉ của tôi</h4>
        <button class="btn btn-primary add-address" style="padding: 10px 15px; background-color: #007bff; color: white; border: none; border-radius: 5px; cursor: pointer;">
                 + Thêm địa chỉ mới
        </button>
    </div>
    <hr>
    `;
        addressContainer.append(headerHtml); // Giữ tiêu đề ở trên cùng

        // Kiểm tra nếu không có địa chỉ nào
        if (!addresses || addresses.length === 0) {
            addressContainer.append('<p class="text-center text-muted">Chưa có địa chỉ nào được lưu.</p>');
            return;
        }

        // Đảo ngược mảng địa chỉ để hiển thị theo thứ tự mong muốn
        const reversedAddresses = addresses.reverse();

        // Duyệt qua danh sách địa chỉ đã đảo ngược và tạo phần hiển thị cho mỗi địa chỉ
        reversedAddresses.forEach(address => {
            const addressHtml = `
        <div class="list-group-item mb-3" style="border: 1px solid #ddd; border-radius: 5px; padding: 10px;">
            <div class="mb-2">
                <h5 class="mb-1">${address.diaChiCuThe}</h5>
                <p class="mb-1">${address.phuongXa}, ${address.quanHuyen}, ${address.thanhPho}</p>
            </div>
            <div class="d-flex gap-2">
                <a href="#" class="btn-link update-address" style="color: #007bff; text-decoration: none;" data-id="${address.id}" data-email="${email}">Cập nhật</a>
                <span style="color: #888;">|</span>
                <a href="#" class="btn-link delete-address text-danger" style="color: #dc3545; text-decoration: none;">Xóa</a>
            </div>
        </div>
        `;
            addressContainer.append(addressHtml); // Thêm từng địa chỉ vào container
        });

        // Gán sự kiện cho nút thêm địa chỉ
        $('.add-address').off('click').on('click', function (event) {
            event.preventDefault();
            addAddress(email); // Truyền email vào đây
        });

        // Gán sự kiện cho các nút cập nhật và xóa
        $('.update-address').click(function (event) {
            event.preventDefault();
            const addressId = $(this).data('id');
            const email = $(this).data('email');
            updateAddress(addressId, email);
        });

        $('.delete-address').click(function (event) {
            event.preventDefault();
            const addressId = $(this).closest('.list-group-item').find('.update-address').data('id');
            const email = $(this).closest('.list-group-item').find('.update-address').data('email');
            deleteAddress(addressId, email); // Gọi hàm xóa
        });
    }

    async function addAddress(email) {
        document.getElementById("specificAddress").value = '';
        document.getElementById("city").value = '';
        document.getElementById("district").value = '';
        document.getElementById("ward").value = '';
        $('#addressModal').modal('show');

        // Khi người dùng nhấn nút "Lưu địa chỉ" trong modal
        $('#saveAddressButton').off('click').on('click', async function () {
            const specificAddress = document.getElementById("specificAddress").value;
            const city = document.getElementById("city").value;
            const district = document.getElementById("district").value;
            const ward = document.getElementById("ward").value;

            // Kiểm tra tính đầy đủ của thông tin địa chỉ
            if (!specificAddress || !city || !district || !ward) {
                alert("Vui lòng điền đầy đủ thông tin địa chỉ.");
                return; // Dừng lại nếu thông tin không đầy đủ
            }

            const newAddressData = {
                diaChiCuThe: specificAddress,
                thanhPho: city,
                quanHuyen: district,
                phuongXa: ward,
            };

            try {
                await axios.post(`/dia-chi/add/${encodeURIComponent(email)}`, newAddressData);
                Swal.fire({
                    icon: 'success',
                    title: 'Thành công',
                    text: 'Địa chỉ đã được thêm thành công!',
                });
                $('#addressModal').modal('hide');
                showCustomerAddress(); // Cập nhật danh sách địa chỉ
            } catch (error) {
                console.error("Lỗi khi thêm địa chỉ:", error.response.data);
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi',
                    text: `Không thể thêm địa chỉ. Lý do: ${error.response.data.message || 'Vui lòng thử lại.'}`,
                });
            }
        });
    }


    async function updateAddress(addressId, email) {
        try {
            // Đảm bảo `dataCache` đã được tải
            if (!dataCache) {
                console.error("Dữ liệu chưa được tải, vui lòng thử lại sau.");
                alert("Dữ liệu chưa được tải, vui lòng thử lại sau.");
                return;
            }

            // Gọi API để lấy thông tin chi tiết của địa chỉ theo `email` và `id`
            let response = await axios.get(`/dia-chi/find/${email}/${addressId}`);
            let address = response.data;
            console.log(address); // Kiểm tra dữ liệu nhận được

            // Điền dữ liệu vào trường địa chỉ cụ thể
            document.getElementById("specificAddress").value = address.diaChiCuThe;

            // Cập nhật select cho thành phố
            const citySelect = document.getElementById("city");
            citySelect.value = address.thanhPho;
            if (!Array.from(citySelect.options).some(option => option.value === address.thanhPho)) {
                const optCity = document.createElement('option');
                optCity.value = address.thanhPho;
                optCity.text = address.thanhPho;
                citySelect.appendChild(optCity);
                citySelect.value = address.thanhPho; // Gán lại giá trị
            }

            // Cập nhật select cho quận/huyện
            const districtSelect = document.getElementById("district");
            districtSelect.innerHTML = "<option value=''>Chọn Quận/Huyện</option>"; // Reset danh sách quận/huyện
            const selectedCity = dataCache.find(city => city.Name === address.thanhPho);
            if (selectedCity) {
                selectedCity.Districts.forEach(district => {
                    const opt = document.createElement('option');
                    opt.value = district.Name;
                    opt.text = district.Name;
                    opt.setAttribute('data-id', district.Id);
                    districtSelect.appendChild(opt);
                });
                districtSelect.value = address.quanHuyen;
            }
            if (!Array.from(districtSelect.options).some(option => option.value === address.quanHuyen)) {
                const optDistrict = document.createElement('option');
                optDistrict.value = address.quanHuyen;
                optDistrict.text = address.quanHuyen;
                districtSelect.appendChild(optDistrict);
                districtSelect.value = address.quanHuyen; // Gán lại giá trị
            }

            // Cập nhật select cho phường/xã
            const wardSelect = document.getElementById("ward");
            wardSelect.innerHTML = "<option value=''>Chọn Phường/Xã</option>"; // Reset danh sách phường/xã
            const selectedDistrict = selectedCity?.Districts.find(district => district.Name === address.quanHuyen);
            if (selectedDistrict) {
                selectedDistrict.Wards.forEach(ward => {
                    const opt = document.createElement('option');
                    opt.value = ward.Name;
                    opt.text = ward.Name;
                    wardSelect.appendChild(opt);
                });
                wardSelect.value = address.phuongXa;
            }
            if (!Array.from(wardSelect.options).some(option => option.value === address.phuongXa)) {
                const optWard = document.createElement('option');
                optWard.value = address.phuongXa;
                optWard.text = address.phuongXa;
                wardSelect.appendChild(optWard);
                wardSelect.value = address.phuongXa; // Gán lại giá trị
            }

            $('#addressModal').modal('show');
            document.getElementById("saveAddressButton").onclick = async function () {
                const updatedAddressData = {
                    diaChiCuThe: document.getElementById("specificAddress").value,
                    thanhPho: citySelect.value,
                    quanHuyen: districtSelect.value,
                    phuongXa: wardSelect.value,
                };

                try {
                    // Gọi API để cập nhật địa chỉ
                    await axios.put(`/dia-chi/update/${email}/${addressId}`, updatedAddressData);
                    Swal.fire({
                        icon: 'success',
                        title: 'Thành công',
                        text: 'Địa chỉ đã được cập nhật thành công!',
                    });

                    // Đóng modal
                    $('#addressModal').modal('hide');
                    showCustomerAddress();

                } catch (updateError) {
                    console.error("Lỗi khi cập nhật địa chỉ:", updateError);
                    alert("Không thể cập nhật địa chỉ. Vui lòng thử lại.");
                }
            };
        } catch (error) {
            console.error("Lỗi khi lấy thông tin địa chỉ:", error);
            alert("Không thể tải dữ liệu địa chỉ để cập nhật.");
        }
    }

    function deleteAddress(id, email) {
        Swal.fire({
            title: 'Xác nhận',
            text: 'Bạn chắc chắn muốn xóa địa chỉ này không?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Yes',
            cancelButtonText: 'Cancel',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                const url = `http://localhost:8080/dia-chi/delete/${encodeURIComponent(email)}/${id}`;

                // Thực hiện yêu cầu DELETE
                fetch(url, {
                    method: 'GET', // Sửa thành phương thức DELETE
                    headers: {
                        'Content-Type': 'application/json' // Đặt Content-Type nếu cần thiết
                    }
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Có lỗi xảy ra khi xóa địa chỉ');
                        }
                        return response.json(); // Chuyển đổi phản hồi thành JSON
                    })
                    .then(data => {
                        // Hiển thị thông báo thành công và cập nhật giao diện
                        console.log('Địa chỉ đã được xóa:', data);
                        Swal.fire({
                            icon: 'success',
                            title: 'Thành công',
                            text: 'Địa chỉ đã được xóa thành công!',
                        });
                        showCustomerAddress(); // Gọi lại hàm để cập nhật danh sách địa chỉ
                    })
                    .catch(error => {
                        console.error('Lỗi:', error);
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi',
                            text: error.message,
                        });
                    });
            } else {
                Swal.fire({
                    icon: 'info',
                    title: 'Đã hủy',
                    text: 'Địa chỉ không bị xóa.',
                });
            }
        });
    }


    async function showCustomerDetails() {
        try {
            let response = await loadCustomer(); // Gọi hàm để lấy dữ liệu
            htmlDetaisCustomer(response.response)
        } catch (error) {
        }
    }

    function htmlDetaisCustomer(customer) {
        content.html(''); // Đảm bảo rằng `content` đã có sẵn
        if (!customer) {
            content.html('<p class="text-center text-danger">Không tìm thấy thông tin khách hàng.</p>');
            return;
        }

        const hiddenPhone = customer.soDienThoai ? `********${customer.soDienThoai.slice(-2)}` : 'Chưa có thông tin';
        const hoVaTenValue = customer.hoVaTen || 'Chưa có tên';
        const emailValue = customer.email || 'Chưa có email';
        let actualPhone = customer.soDienThoai; // Lưu tạm số điện thoại thật để lưu sau

        let html = `
    <div class="card-header text-black text-center">
        <h4>Thông Tin Khách Hàng</h4>
    </div>
    <div class="card-body">
        <div class="form-group mb-3">
            <label for="email"><strong>Email:</strong></label>
            <input type="text" class="form-control-plaintext" id="email" value="${emailValue}" readonly />
        </div>
        <div class="form-group mb-3">
            <label for="hoVaTen"><strong>Họ Và Tên:</strong></label>
            <input type="text" class="form-control" id="hoVaTen" value="${hoVaTenValue}" />
        </div>
        <div class="form-group mb-3">
            <label><strong>Số Điện Thoại:</strong></label>
            <div class="input-group">
                <input type="text" class="form-control" id="soDienThoai" value="${hiddenPhone}" readonly />
                <button id="changePhoneButton" class="btn btn-outline-secondary">Đổi</button>
            </div>
        </div>
        <div class="form-group mb-4">
            <label><strong>Giới Tính:</strong></label><br>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="gioiTinh" id="male" value="true" ${customer.gioiTinh ? 'checked' : ''}>
                <label class="form-check-label" for="male">Nam</label>
            </div>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="gioiTinh" id="female" value="false" ${!customer.gioiTinh ? 'checked' : ''}>
                <label class="form-check-label" for="female">Nữ</label>
            </div>
        </div>
        <button id="saveCustomerButton" class="btn btn-success w-100">Save</button>
    </div>`;

        content.html(html); // Hiển thị nội dung vào DOM

        // Xóa nội dung mặc định 'Chưa có tên' khi nhấp vào ô nhập tên
        $('#hoVaTen').focus(function () {
            if ($(this).val() === 'Chưa có tên') {
                $(this).val('');
            }
        });

        // Kiểm tra và lưu dữ liệu
        $('#saveCustomerButton').click(function () {
            const hoVaTen = $('#hoVaTen').val();
            if (!hoVaTen) {
                Swal.fire({
                    title: 'Lỗi!',
                    text: 'Họ và tên không được để trống!',
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
                return;
            }
            saveCustomerDetails();
        });

        // Gán sự kiện click cho nút Change Phone
        $('#changePhoneButton').click(function () {
            Swal.fire({
                title: 'Xác Nhận Số Điện Thoại',
                input: 'text',
                inputLabel: 'Nhập số điện thoại hiện tại của bạn',
                inputPlaceholder: hiddenPhone,
                showCancelButton: true,
                confirmButtonText: 'Xác nhận',
                cancelButtonText: 'Hủy',
                inputValidator: (value) => {
                    if (value !== actualPhone) {
                        return 'Số điện thoại không khớp!';
                    }
                    return null;
                }
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire({
                        title: 'Nhập Số Điện Thoại Mới',
                        input: 'text',
                        inputLabel: 'Vui lòng nhập số điện thoại mới',
                        showCancelButton: true,
                        confirmButtonText: 'Lưu',
                        cancelButtonText: 'Hủy',
                        inputValidator: (newPhone) => {
                            if (!newPhone) {
                                return 'Vui lòng nhập số điện thoại mới!';
                            } else if (!validateVietnamesePhone(newPhone)) {
                                return 'Số điện thoại không đúng định dạng của Việt Nam!';
                            }
                            return null;
                        }
                    }).then((newPhoneResult) => {
                        if (newPhoneResult.isConfirmed) {
                            // Cập nhật số điện thoại thật và hiển thị trong ô nhập
                            actualPhone = newPhoneResult.value;
                            $('#soDienThoai').val(actualPhone); // Cập nhật input thành số thực

                            // Gọi hàm save để lưu số điện thoại mới ngay lập tức
                            saveCustomerDetails();
                        }
                    });
                }
            });
        });

        function validateVietnamesePhone(phone) {
            const phoneRegex = /^(0[3|5|7|8|9])+([0-9]{8})$/;
            return phoneRegex.test(phone);
        }

        function saveCustomerDetails() {
            const email = $('#email').val();
            const hoVaTen = $('#hoVaTen').val();
            const gioiTinh = $('input[name="gioiTinh"]:checked').val() === 'true';

            const customerData = {
                hoVaTen: hoVaTen,
                gioiTinh: gioiTinh,
                soDienThoai: actualPhone, // Sử dụng actualPhone thay vì giá trị input có thể chứa `*`
            };

            $.ajax({
                url: `http://localhost:8080/khach-hang/update/${encodeURIComponent(email)}`,
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(customerData),
                success: function (response) {
                    Swal.fire({
                        title: 'Thành công!',
                        text: 'Thông tin khách hàng đã được cập nhật thành công!',
                        icon: 'success',
                        confirmButtonText: 'OK'
                    }).then(() => {
                        htmlDetaisCustomer(response); // Cập nhật giao diện với dữ liệu mới
                    });
                },
                error: function (xhr, status, error) {
                    Swal.fire({
                        title: 'Lỗi!',
                        text: 'Có lỗi xảy ra khi cập nhật thông tin: ' + error,
                        icon: 'error',
                        confirmButtonText: 'OK'
                    });
                }
            });
        }
    }

    async function showCustomerChangerPass() {
        try {
            let response = await loadCustomer(); // Gọi hàm để lấy dữ liệu
            htmlCustomerChangerPass(response.response)
        } catch (error) {
        }

    }

    function htmlCustomerChangerPass(customer) {
        content.html(''); // Xóa nội dung cũ

        // Kiểm tra xem khách hàng có thông tin hợp lệ không
        if (!customer || !customer.email) {
            content.html('<p class="text-danger text-center">Không tìm thấy thông tin khách hàng hợp lệ.</p>');
            return;
        }

        // Nội dung đổi mật khẩu
        let html = `
        <div id="changePasswordContainer" class="container mt-4 p-4 rounded shadow-sm bg-light">
            <h2 class="password-change-title text-center mb-4">Thay Đổi Mật Khẩu</h2>
            <p class="password-change-description text-center text-muted">Vui lòng nhập mật khẩu hiện tại của bạn.</p>
            
            <!-- Form nhập mật khẩu cũ -->
            <div id="changePasswordForm" class="form-group">
                <label for="oldPassword" class="password-label font-weight-bold">Mật khẩu hiện tại:</label>
                <input type="password" id="oldPassword" class="form-control password-input" required>
                <button id="verifyOldPassword" class="btn btn-primary mt-3 btn-block">Xác nhận</button>
            </div>

            <!-- Form mật khẩu mới và xác nhận mật khẩu, ẩn mặc định -->
            <div id="newPasswordForm" class="form-group" style="display: none;">
                <label for="newPassword" class="password-label font-weight-bold">Mật khẩu mới:</label>
                <input type="password" id="newPassword" class="form-control password-input" required>
                
                <label for="confirmPassword" class="password-label font-weight-bold mt-2">Xác nhận mật khẩu mới:</label>
                <input type="password" id="confirmPassword" class="form-control password-input" required>
                
                <button id="submitNewPassword" class="btn btn-success mt-3 btn-block">Cập Nhật Mật Khẩu</button>
            </div>
        </div>
    `;

        content.html(html);

        // Xác nhận mật khẩu cũ
        $('#verifyOldPassword').on('click', function () {
            let oldPassword = $('#oldPassword').val();

            let formData = {
                username: customer.email,
                password: oldPassword
            };

            $.ajax({
                url: '/api/auth/sign-in',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function () {
                    $('#changePasswordForm').hide();
                    $('#newPasswordForm').show();
                },
                error: function (xhr) {
                    $('#oldPassword').addClass('is-invalid'); // Đánh dấu input mật khẩu cũ
                    Swal.fire({
                        title: 'Lỗi!',
                        text: 'Mật khẩu hiện tại không chính xác.',
                        icon: 'error',
                        confirmButtonText: 'OK'
                    });
                    // Xóa giá trị của mật khẩu cũ để người dùng nhập lại
                    $('#oldPassword').val('');
                }
            });
        });

        // Gửi mật khẩu mới
        $('#submitNewPassword').on('click', function () {
            let newPassword = $('#newPassword').val();
            let confirmPassword = $('#confirmPassword').val();

            if (newPassword === confirmPassword) {
                $.ajax({
                    url: '/api/auth/change-pass',
                    type: 'PUT',
                    contentType: 'application/json',
                    data: JSON.stringify({password: newPassword}),
                    success: function () {
                        Swal.fire({
                            title: 'Thành công!',
                            text: 'Mật khẩu đã được thay đổi thành công!',
                            icon: 'success',
                            confirmButtonText: 'OK'
                        }).then(() => {
                            window.location.reload();
                        });
                    },
                    error: function (xhr) {
                        console.log("Password change error:", xhr.responseText);
                        Swal.fire({
                            title: 'Lỗi!',
                            text: 'Đã xảy ra lỗi, vui lòng thử lại.',
                            icon: 'error',
                            confirmButtonText: 'OK'
                        });
                    }
                });
            } else {
                Swal.fire({
                    title: 'Lỗi!',
                    text: 'Mật khẩu mới và xác nhận không khớp.',
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            }
        });
    }

});

// Bằng
let dataCache = null;
document.addEventListener("DOMContentLoaded", function () {
    const citis = document.getElementById("city");
    const districts = document.getElementById("district");
    const wards = document.getElementById("ward");

    // Tải dữ liệu hành chính một lần và lưu trữ vào biến dataCache
    async function fetchData() {
        try {
            const response = await axios.get("https://raw.githubusercontent.com/kenzouno1/DiaGioiHanhChinhVN/master/data.json");
            dataCache = response.data;
            renderCity(dataCache);
        } catch (error) {
            console.error("Lỗi khi tải dữ liệu:", error);
        }
    }

    // Hàm render thành phố
    function renderCity(data) {
        data.forEach(city => {
            const opt = document.createElement('option');
            opt.value = city.Name;
            opt.text = city.Name;
            opt.setAttribute('data-id', city.Id);
            citis.appendChild(opt);
        });
    }

    // Khi chọn thành phố, render danh sách quận/huyện
    citis.addEventListener("change", function () {
        districts.innerHTML = "<option value=''>Chọn Quận/Huyện</option>";
        wards.innerHTML = "<option value=''>Chọn Phường/Xã</option>";

        const cityId = citis.options[citis.selectedIndex].dataset.id;
        const selectedCity = dataCache.find(city => city.Id === cityId);

        if (selectedCity) {
            selectedCity.Districts.forEach(district => {
                const opt = document.createElement('option');
                opt.value = district.Name;
                opt.text = district.Name;
                opt.setAttribute('data-id', district.Id);
                districts.appendChild(opt);
            });
        }
    });

    // Khi chọn quận/huyện, render danh sách phường/xã
    districts.addEventListener("change", function () {
        wards.innerHTML = "<option value=''>Chọn Phường/Xã</option>";

        const cityId = citis.options[citis.selectedIndex].dataset.id;
        const districtId = districts.options[districts.selectedIndex].dataset.id;
        const selectedCity = dataCache.find(city => city.Id === cityId);

        if (selectedCity) {
            const selectedDistrict = selectedCity.Districts.find(district => district.Id === districtId);
            if (selectedDistrict) {
                selectedDistrict.Wards.forEach(ward => {
                    const opt = document.createElement('option');
                    opt.value = ward.Name;
                    opt.text = ward.Name;
                    wards.appendChild(opt);
                });
            }
        }
    });
    fetchData();
});

// Ly viết hàm để click vào Đặt sân ngay
async function checkLoginAndRedirect() {
    try {
        const customer = await loadCustomer();
        if (customer && customer.id) {
            // Nếu có thông tin khách hàng (đã đăng nhập), chuyển hướng đến trang đặt sân
            window.location.href = '/khach-hang/dat-san';  // Thay vì th:href, dùng JavaScript để chuyển hướng
        } else {
            // Nếu không có thông tin khách hàng (chưa đăng nhập), mở modal login
            $('#modallogin').modal('show');
        }
    } catch (error) {
        console.error('Lỗi khi kiểm tra khách hàng:', error);
        // Nếu gặp lỗi, bạn có thể mở modal hoặc xử lý lỗi ở đây
        $('#modallogin').modal('show');
    }
}