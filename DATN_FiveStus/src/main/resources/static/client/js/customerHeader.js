$(document).ready(function () {

    let tokenJWT = Cookies.get('authToken');

    if (tokenJWT) {
        checkLogin();
    }


    async function loadCustomer() {
        const response = await fetch('http://localhost:8080/customer/get-customer');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return await response.json();
    }

    async function showCustomerDetails() {
        try {
            let response = await loadCustomer(); // Gọi hàm để lấy dữ liệu
            removeModalAndAddDropDown(response.response)
        } catch (error) {
        }
    }
    function checkLogin() {
        $.ajax({
            url: 'http://localhost:8080/customer/check-login',
            type: 'GET',
            dataType: 'json',
            success: (response) => {
                if (response ===true){
                    showCustomerDetails();
                } else {
                    reloadHeader()
                }
            }
        })
    }

    function removeModalAndAddDropDown(customer) {
        const userMenuLink = $('#userMenuLink');
        if (userMenuLink.siblings('.dropdown-menu').length === 0) {

            userMenuLink.removeAttr('data-bs-toggle');
            userMenuLink.removeAttr('data-bs-target');
            let ten = (customer["hoVaTen"] === null)? customer["email"].split(" ").pop() :customer["hoVaTen"].split(" ").pop();
            let customerSider =`
                <li class="pe-3 nav-item dropdown">
                      <a href="#" 
                         class="border-0">
                        <span style="text-decoration: underline;">Xin chào ${ten} !</span>
                      </a>
                </li>
            `;
            let dropdownHtml = `
            <ul class="dropdown-menu fw-bold">
                <li>
                  <a href="/customer" class="dropdown-item">Tài khoản</a>
                </li>
                <li>
                  <a href="/history-book" class="dropdown-item">Lịch sử đặt sân</a>
                </li>
                <li>
                  <a class="dropdown-item" href="/client/logout">Đăng xuất</a>
                </li>
              </ul>
         `;
            let isUser = `
               <li class="nav-item">
                    <a class="nav-link me-5" href="/khach-hang/dat-san" >Đặt sân</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link me-5" href="/history-book" >Danh sách đặt lịch</a>
                </li>
               
            `;
            userMenuLink.parent().append(dropdownHtml);
            userMenuLink.attr('data-bs-toggle', 'dropdown');
            userMenuLink.attr('aria-expanded', 'false');
            $(`#customerSider`).prepend(customerSider);
            $("#noiquy").after(isUser)
        }
    }

    function reloadHeader() {
        const userMenuLink = $('#userMenuLink');
        userMenuLink.attr('data-bs-toggle', 'modal');
        userMenuLink.attr('data-bs-target', '#modallogin');
        Cookies.remove('authToken', {path: '/'});

    }


});