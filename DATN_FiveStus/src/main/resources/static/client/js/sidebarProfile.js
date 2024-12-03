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
        } else {
            // Đảo ngược mảng địa chỉ để hiển thị theo thứ tự mong muốn
            const reversedAddresses = addresses.reverse();

            // Duyệt qua danh sách địa chỉ đã đảo ngược và tạo phần hiển thị cho mỗi địa chỉ
            reversedAddresses.forEach(address => {
                // Lấy tên Tỉnh/Thành, Quận/Huyện, Phường/Xã từ dataCache
                const selectedCity = dataCache.find(city => city.Id === address.thanhPho);
                const selectedDistrict = selectedCity ? selectedCity.Districts.find(district => district.Id === address.quanHuyen) : null;
                const selectedWard = selectedDistrict ? selectedDistrict.Wards.find(ward => ward.Id === address.phuongXa) : null;

                // Tạo chuỗi địa chỉ với tên thay vì ID
                const cityName = selectedCity ? selectedCity.Name : '';
                const districtName = selectedDistrict ? selectedDistrict.Name : '';
                const wardName = selectedWard ? selectedWard.Name : '';

                const addressHtml = `
                <div class="list-group-item mb-3" style="border: 1px solid #ddd; border-radius: 5px; padding: 10px;">
                    <div class="mb-2">
                        <h5 class="mb-1">${address.diaChiCuThe}</h5>
                        <p class="mb-1">${wardName}, ${districtName}, ${cityName}</p>
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
        }

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
    let dataCache = []; // Biến lưu trữ dữ liệu hành chính

    $(document).ready(function () {
        // Tải dữ liệu hành chính và lưu trữ vào dataCache
        async function fetchData() {
            try {
                const response = await axios.get("https://raw.githubusercontent.com/kenzouno1/DiaGioiHanhChinhVN/master/data.json");
                dataCache = response.data;
                console.log("Dữ liệu hành chính đã tải:", dataCache); // Kiểm tra dữ liệu đã tải
                renderCity(dataCache); // Render thành phố sau khi tải dữ liệu
            } catch (error) {
                console.error("Lỗi khi tải dữ liệu:", error);
            }
        }

        // Hàm render thành phố
        function renderCity(data) {
            const citis = document.getElementById("city");
            citis.innerHTML = "<option value='' disabled selected>Chọn Tỉnh/Thành</option>"; // Reset thành phố
            data.forEach(city => {
                const opt = document.createElement('option');
                opt.value = city.Id;  // Gán ID thành phố vào value
                opt.text = city.Name; // Gán tên thành phố vào text
                opt.dataset.id = city.Id; // Lưu ID thành phố vào dataset
                citis.appendChild(opt);
            });
        }

        // Khi chọn thành phố, render danh sách quận/huyện
        $('#city').on('change', function () {
            const districts = document.getElementById("district");
            const wards = document.getElementById("ward");

            // Reset quận/huyện và phường/xã
            districts.innerHTML = "<option value=''>Chọn Quận/Huyện</option>";
            wards.innerHTML = "<option value=''>Chọn Phường/Xã</option>";
            const cityId = this.value;  // Sử dụng `value` thay vì `dataset.id`
            const selectedCity = dataCache.find(city => city.Id == cityId); // So khớp với `Id` của thành phố

            if (selectedCity) {
                selectedCity.Districts.forEach(district => {
                    const opt = document.createElement('option');
                    opt.value = district.Id;  // Sử dụng ID quận/huyện
                    opt.text = district.Name; // Gán tên quận/huyện vào text
                    opt.dataset.id = district.Id; // Lưu ID quận/huyện vào dataset
                    districts.appendChild(opt);
                });
            }
        });

        // Khi chọn quận/huyện, render danh sách phường/xã
        $('#district').on('change', function () {
            const wards = document.getElementById("ward");
            wards.innerHTML = "<option value=''>Chọn Phường/Xã</option>"; // Reset phường/xã

            const cityId = $('#city').val();  // Lấy giá trị thành phố đã chọn
            const districtId = this.value; // Sử dụng `value` của district đã chọn
            const selectedCity = dataCache.find(city => city.Id == cityId); // Tìm thành phố từ `dataCache`

            if (selectedCity) {
                const selectedDistrict = selectedCity.Districts.find(district => district.Id == districtId); // So khớp với ID quận/huyệnệu

                if (selectedDistrict) {
                    selectedDistrict.Wards.forEach(ward => {
                        const opt = document.createElement('option');
                        opt.value = ward.Id; // Sử dụng ID phường/xã
                        opt.text = ward.Name; // Gán tên phường/xã vào text
                        opt.dataset.id = ward.Id; // Lưu ID phường/xã vào dataset
                        wards.appendChild(opt);
                    });
                }
            }
        });

        // Gọi hàm fetchData khi trang được tải xong
        fetchData();
    });

    async function addAddress(email) {
        const specificAddressInput = document.getElementById("specificAddress");
        const ghiChuInput = document.getElementById("ghiChu");
        const citySelect = document.getElementById("city");
        const districtSelect = document.getElementById("district");
        const wardSelect = document.getElementById("ward");

        const clearErrors = () => {
            [specificAddressInput, citySelect, districtSelect, wardSelect].forEach(input => {
                input.classList.remove("is-invalid");
                const errorElement = input.nextElementSibling;
                if (errorElement && errorElement.classList.contains("invalid-feedback")) {
                    errorElement.remove();
                }
            });
        };

        const showError = (input, message) => {
            input.classList.add("is-invalid");
            if (!input.nextElementSibling || !input.nextElementSibling.classList.contains("invalid-feedback")) {
                input.insertAdjacentHTML("afterend", `<div class="invalid-feedback">${message}</div>`);
            }
        };

        const validateInputs = () => {
            clearErrors();
            let isValid = true;

            if (!specificAddressInput.value.trim()) {
                showError(specificAddressInput, "Địa chỉ cụ thể không được để trống.");
                isValid = false;
            }
            if (!citySelect.value) {
                showError(citySelect, "Vui lòng chọn Tỉnh/Thành.");
                isValid = false;
            }
            if (!districtSelect.value) {
                showError(districtSelect, "Vui lòng chọn Quận/Huyện.");
                isValid = false;
            }
            if (!wardSelect.value) {
                showError(wardSelect, "Vui lòng chọn Phường/Xã.");
                isValid = false;
            }

            return isValid;
        };

        $('#addressModal').modal('show'); // Hiển thị modal

        $('#saveAddressButton').off('click').on('click', async function () {
            if (!validateInputs()) return; // Dừng nếu dữ liệu không hợp lệ

            const newAddressData = {
                diaChiCuThe: specificAddressInput.value.trim(),
                thanhPho: citySelect.value,
                quanHuyen: districtSelect.value,
                phuongXa: wardSelect.value,
                ghiChu: ghiChuInput.value.trim(),
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
                console.error("Lỗi khi thêm địa chỉ:", error);
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi',
                    text: error.response?.data?.message || "Đã xảy ra lỗi, vui lòng thử lại.",
                });
            }
        });
    }



    async function updateAddress(addressId, email) {
        try {
            if (!dataCache || dataCache.length === 0) {
                console.error("Dữ liệu chưa được tải, vui lòng thử lại sau.");
                alert("Dữ liệu chưa được tải, vui lòng thử lại sau.");
                return;
            }

            const response = await axios.get(`/dia-chi/find/${email}/${addressId}`);
            const address = response.data;
            console.log(address);

            const specificAddressInput = document.getElementById("specificAddress");
            const ghiChuInput = document.getElementById("ghiChu");
            const citySelect = document.getElementById("city");
            const districtSelect = document.getElementById("district");
            const wardSelect = document.getElementById("ward");

            specificAddressInput.value = address.diaChiCuThe || '';
            ghiChuInput.value = address.ghiChu || '';

            const selectedCity = dataCache.find(city => city.Id === address.thanhPho);
            if (selectedCity) {
                citySelect.value = selectedCity.Id;
                districtSelect.innerHTML = '';
                selectedCity.Districts.forEach(district => {
                    const opt = document.createElement('option');
                    opt.value = district.Id;
                    opt.text = district.Name;
                    districtSelect.appendChild(opt);
                });
                districtSelect.value = address.quanHuyen || '';

                wardSelect.innerHTML = '';
                const selectedDistrict = selectedCity.Districts.find(district => district.Id === address.quanHuyen);
                if (selectedDistrict) {
                    selectedDistrict.Wards.forEach(ward => {
                        const opt = document.createElement('option');
                        opt.value = ward.Id;
                        opt.text = ward.Name;
                        wardSelect.appendChild(opt);
                    });
                    wardSelect.value = address.phuongXa || '';
                }
            }

            const clearErrors = () => {
                [specificAddressInput, citySelect, districtSelect, wardSelect].forEach(input => {
                    input.classList.remove("is-invalid");
                    const errorElement = input.nextElementSibling;
                    if (errorElement && errorElement.classList.contains("invalid-feedback")) {
                        errorElement.remove();
                    }
                });
            };

            const showError = (input, message) => {
                input.classList.add("is-invalid");
                if (!input.nextElementSibling || !input.nextElementSibling.classList.contains("invalid-feedback")) {
                    input.insertAdjacentHTML("afterend", `<div class="invalid-feedback">${message}</div>`);
                }
            };

            const validateInputs = () => {
                clearErrors();
                let isValid = true;

                if (!specificAddressInput.value.trim()) {
                    showError(specificAddressInput, "Địa chỉ cụ thể không được để trống.");
                    isValid = false;
                }
                if (!citySelect.value) {
                    showError(citySelect, "Vui lòng chọn Tỉnh/Thành.");
                    isValid = false;
                }
                if (!districtSelect.value) {
                    showError(districtSelect, "Vui lòng chọn Quận/Huyện.");
                    isValid = false;
                }
                if (!wardSelect.value) {
                    showError(wardSelect, "Vui lòng chọn Phường/Xã.");
                    isValid = false;
                }

                return isValid;
            };

            $('#addressModal').modal('show');

            $('#saveAddressButton').off('click').on('click', async function () {
                if (!validateInputs()) return;

                const updatedAddressData = {
                    diaChiCuThe: specificAddressInput.value.trim(),
                    ghiChu: ghiChuInput.value.trim(),
                    thanhPho: citySelect.value,
                    quanHuyen: districtSelect.value,
                    phuongXa: wardSelect.value,
                };

                try {
                    await axios.put(`/dia-chi/update/${email}/${addressId}`, updatedAddressData);

                    Swal.fire({
                        icon: 'success',
                        title: 'Thành công',
                        text: 'Địa chỉ đã được cập nhật thành công!',
                    });

                    $('#addressModal').modal('hide');
                    showCustomerAddress(); // Cập nhật danh sách địa chỉ
                } catch (error) {
                    console.error("Lỗi khi cập nhật địa chỉ:", error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi',
                        text: error.response?.data?.message || "Không thể cập nhật địa chỉ. Vui lòng thử lại.",
                    });
                }
            });
        } catch (error) {
            console.error("Lỗi khi lấy thông tin địa chỉ:", error);
            Swal.fire({
                icon: 'error',
                title: 'Lỗi',
                text: "Không thể tải dữ liệu địa chỉ để cập nhật.",
            });
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

        const hiddenPhone = customer.soDienThoai ? `********${customer.soDienThoai.slice(-2)}` : 'Không có số điện thoại';
        const hoVaTenValue = customer.hoVaTen || 'Chưa có tên'; // Hiển thị "Chưa có tên" nếu không có dữ liệu
        const emailValue = customer.email || 'Chưa có email';
        let actualPhone = customer.soDienThoai || ''; // Lưu tạm số điện thoại thật để lưu sau

        let buttonLabel = actualPhone ? "Đổi" : "Thêm"; // Tùy chỉnh nút theo trạng thái số điện thoại

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
            <div class="invalid-feedback">Họ tên không được để trống</div>
        </div>
        <div class="form-group mb-3">
            <label><strong>Số Điện Thoại:</strong></label>
            <div class="input-group">
                <input type="text" class="form-control" id="soDienThoai" value="${hiddenPhone}" readonly />
                <button id="changePhoneButton" class="btn btn-outline-secondary">${buttonLabel}</button>
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

        // Đặt màu nhạt cho "Chưa có tên" nếu giá trị là mặc định
        const hoVaTenInput = $('#hoVaTen');
        if (hoVaTenInput.val() === 'Chưa có tên') {
            hoVaTenInput.css('color', '#6c757d'); // Màu nhạt Bootstrap
        }

        // Khi người dùng nhấp vào input, xóa màu nhạt nếu là giá trị mặc định
        hoVaTenInput.focus(function () {
            if ($(this).val() === 'Chưa có tên') {
                $(this).val('').css('color', '#000'); // Đặt màu đen khi nhập
            }
        });

        // Khi người dùng rời khỏi input mà không nhập gì, đặt lại giá trị mặc định
        hoVaTenInput.blur(function () {
            if ($(this).val().trim() === '') {
                $(this).val('Chưa có tên').css('color', '#6c757d');
            }
        });
        $('#saveCustomerButton').click(function () {
            const hoVaTen = $('#hoVaTen').val().trim();

            // Reset trạng thái is-invalid
            $('#hoVaTen').removeClass('is-invalid');

            if (hoVaTen === 'Chưa có tên' || hoVaTen === '') {
                $('#hoVaTen').addClass('is-invalid');
                $('.invalid-feedback').text('Họ tên không được để trống');
                return;
            }

            if (!validateName(hoVaTen)) {
                $('#hoVaTen').addClass('is-invalid');
                $('.invalid-feedback').text('Họ và tên không được chứa ký tự đặc biệt hoặc số');
                return;
            }

            saveCustomerDetails();
        });
        // Gán sự kiện click cho nút Change/Thêm Phone
        $('#changePhoneButton').click(function () {
            if (!actualPhone) {
                // Trường hợp thêm số mới
                openPhoneInputModal("Thêm Số Điện Thoại", "Vui lòng nhập số điện thoại mới");
            } else {
                // Trường hợp đổi số điện thoại
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
                        openPhoneInputModal("Nhập Số Điện Thoại Mới", "Vui lòng nhập số điện thoại mới");
                    }
                });
            }
        });

        // Hàm mở modal nhập số điện thoại
        function openPhoneInputModal(title, label) {
            Swal.fire({
                title: title,
                input: 'text',
                inputLabel: label,
                showCancelButton: true,
                confirmButtonText: 'Lưu',
                cancelButtonText: 'Hủy',
                inputValidator: (newPhone) => {
                    if (!newPhone) {
                        return 'Vui lòng nhập số điện thoại!';
                    } else if (!validateVietnamesePhone(newPhone)) {
                        return 'Số điện thoại không đúng định dạng của Việt Nam!';
                    }
                    return null;
                }
            }).then((newPhoneResult) => {
                if (newPhoneResult.isConfirmed) {
                    const newPhone = newPhoneResult.value;

                    // Kiểm tra trùng số điện thoại
                    checkDuplicatePhone(newPhone)
                        .then((isDuplicate) => {
                            if (isDuplicate) {
                                Swal.fire({
                                    title: 'Lỗi!',
                                    text: 'Số điện thoại đã tồn tại. Vui lòng nhập số khác.',
                                    icon: 'error',
                                    confirmButtonText: 'OK'
                                }).then(() => {
                                    // Gọi lại hàm mở form nhập số mới
                                    openPhoneInputModal(title, label);
                                });
                            } else {
                                actualPhone = newPhone;
                                $('#soDienThoai').val(actualPhone);
                                saveCustomerDetails();
                            }
                        })
                        .catch((error) => {
                            console.error('Error checking phone:', error);
                            Swal.fire({
                                title: 'Lỗi!',
                                text: 'Có lỗi xảy ra khi kiểm tra số điện thoại.',
                                icon: 'error',
                                confirmButtonText: 'OK'
                            });
                        });
                }
            });
        }

        // Hàm kiểm tra trùng số điện thoại
        function checkDuplicatePhone(phone) {
            return fetch(`/quan-ly-khach-hang/kiem-tra-so-dien-thoai?soDienThoai=${encodeURIComponent(phone)}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to check phone');
                    }
                    return response.json();
                })
                .then(data => {
                    return data; // Trả về giá trị boolean
                });
        }

        // Hàm validate số điện thoại
        function validateVietnamesePhone(phone) {
            const phoneRegex = /^(0[3|5|7|8|9])+([0-9]{8})$/;
            return phoneRegex.test(phone);
        }
        function validateName(name) {
            const nameRegex = /^[a-zA-ZÀ-Ỹà-ỹ\s]+$/;
            return nameRegex.test(name);
        }
        // Hàm lưu thông tin khách hàng
        function saveCustomerDetails() {
            const email = $('#email').val();
            const hoVaTen = $('#hoVaTen').val();
            const gioiTinh = $('input[name="gioiTinh"]:checked').val() === 'true';

            const customerData = {
                hoVaTen: hoVaTen,
                gioiTinh: gioiTinh,
                soDienThoai: actualPhone,
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
                        htmlDetaisCustomer(response);
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
            let oldPasswordInput = $('#oldPassword');
            let oldPassword = oldPasswordInput.val();

            // Reset trạng thái lỗi trước đó
            oldPasswordInput.removeClass('is-invalid');
            const invalidFeedback = oldPasswordInput.next('.invalid-feedback');
            if (invalidFeedback.length) {
                invalidFeedback.remove();
            }

            // Kiểm tra nếu người dùng chưa nhập mật khẩu
            if (!oldPassword) {
                oldPasswordInput.addClass('is-invalid'); // Đánh dấu input là không hợp lệ
                oldPasswordInput.after('<div class="invalid-feedback">Vui lòng nhập mật khẩu hiện tại.</div>'); // Hiển thị thông báo lỗi
                return; // Dừng xử lý nếu không có mật khẩu
            }

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
                    oldPasswordInput.addClass('is-invalid'); // Đánh dấu input mật khẩu cũ
                    Swal.fire({
                        title: 'Lỗi!',
                        text: 'Mật khẩu hiện tại không chính xác.',
                        icon: 'error',
                        confirmButtonText: 'OK'
                    });
                    // Xóa giá trị của mật khẩu cũ để người dùng nhập lại
                    oldPasswordInput.val('');
                }
            });
        });


        // Gửi mật khẩu mới
        $('#submitNewPassword').on('click', function () {
            let newPassword = $('#newPassword').val();
            let confirmPassword = $('#confirmPassword').val();

            // Hàm kiểm tra tính hợp lệ của mật khẩu
            function isPasswordValid(password) {
                const minLength = 6;
                const maxLength = 15;
                return password.length >= minLength && password.length <= maxLength;
            }

            // Reset thông báo lỗi
            $('.invalid-feedback').remove();
            $('#newPassword').removeClass('is-invalid');
            $('#confirmPassword').removeClass('is-invalid');

            if (!isPasswordValid(newPassword)) {
                $('#newPassword').addClass('is-invalid');
                $('#newPassword').after('<div class="invalid-feedback">Mật khẩu phải từ 6-15 ký tự.</div>');
                return;
            }

            // Kiểm tra mật khẩu xác nhận
            if (newPassword !== confirmPassword) {
                $('#confirmPassword').addClass('is-invalid');
                $('#confirmPassword').after('<div class="invalid-feedback">Mật khẩu mới và xác nhận không khớp.</div>');
                return;
            }

            // Gửi yêu cầu đổi mật khẩu nếu hợp lệ
            $.ajax({
                url: '/api/auth/change-pass',
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify({ password: newPassword }),
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
        });
    }

});

