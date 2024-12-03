$(document).ready(function () {

    $.ajax({
        url: 'http://localhost:8080/nhan-vien/get-nv',
        type: 'GET',
        dataType: 'json',
        success: (response) => {
            if (response !== null) {
                const newListItem = `
        <li class="nav-item">
            <a class="nav-link text-muted my-2" href="#">
                 <span style="text-decoration: underline;">Xin chào ${response.tenNhanVien} !</span>
            </a>
        </li>
    `;

                $('#navbarADMIN').append(newListItem);
            } else {

            }
        }
    })


});