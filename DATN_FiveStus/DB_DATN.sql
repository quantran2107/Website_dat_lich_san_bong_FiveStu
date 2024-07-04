CREATE DATABASE DuAnTotNghiep;

USE DuAnTotNghiep;

-- Table: khach_hang
CREATE TABLE khach_hang (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ma_khach_hang VARCHAR(50) NOT NULL,
  mat_khau VARCHAR(50) NOT NULL,
  ho_va_ten VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  gioi_tinh BOOLEAN NOT NULL,
  so_dien_thoai VARCHAR(50) NOT NULL,
  trang_thai VARCHAR(50) NOT NULL,
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit
);

-- Table: dia_chi
CREATE TABLE dia_chi (
  id INT AUTO_INCREMENT PRIMARY KEY,
  thanh_pho VARCHAR(100) DEFAULT '',
  quan_huyen VARCHAR(100) DEFAULT '',
  phuong_xa VARCHAR(100) DEFAULT '',
  ghi_chu VARCHAR(100) DEFAULT '',
  trang_thai bit,
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit
);

-- Table: dia_chi_khach_hang
CREATE TABLE dia_chi_khach_hang (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_khach_hang INT NOT NULL,
  id_dia_chi INT NOT NULL,
  so_nha VARCHAR(100) DEFAULT '',
  duong VARCHAR(100) DEFAULT '',
  ghi_chu VARCHAR(100) DEFAULT '',
  trang_thai VARCHAR(100) DEFAULT '',
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit,
  CONSTRAINT fk_diaChiKhachHang_khachHang FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id),
  CONSTRAINT fk_diaChiKhachHang_diaChi FOREIGN KEY (id_dia_chi) REFERENCES dia_chi(id)
);

-- Table: chuc_vu
CREATE TABLE chuc_vu (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ten_chuc_vu VARCHAR(100) NOT NULL,
  ma_nhan_vien VARCHAR(100) NOT NULL,
  trang_thai VARCHAR(100) NOT NULL,
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit
);

-- Table: nhan_vien
CREATE TABLE nhan_vien (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ten_nhan_vien VARCHAR(100) NOT NULL,
  ma_nhan_vien VARCHAR(100) NOT NULL,
  mat_khau VARCHAR(100) NOT NULL,
  ho_ten VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  gioi_tinh VARCHAR(100) NOT NULL,
  so_dien_thoai VARCHAR(100) NOT NULL,
  dia_chi VARCHAR(100) NOT NULL,
  trang_thai VARCHAR(100) NOT NULL,
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit
);

-- Table: diem_danh
CREATE TABLE diem_danh (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ten_chuc_vu VARCHAR(100) NOT NULL,
  gio_vao DATETIME,
  gio_ra DATETIME,
  ghi_chu VARCHAR(100) NOT NULL,  
  trang_thai VARCHAR(100) NOT NULL,
  id_nhan_vien INT NOT NULL,
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit,
  CONSTRAINT fk_diemDanh_nhanVien FOREIGN KEY (id_nhan_vien) REFERENCES nhan_vien(id)
);

-- Table: phieu_giam_gia
CREATE TABLE phieu_giam_gia (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_khach_hang int not null,
  ma_phieu_giam_gia VARCHAR(100),
  ten_phieu_giam_gia VARCHAR(100),
  so_luong int,
  muc_giam float,
  hinh_thuc_giam_gia bit,
  dieu_kien_su_dung float,
  gia_tri_toi_da float,
  doi_tuong_ap_dung bit,
  ngay_bat_dau date,
  ngay_ket_thuc date,
  trang_thai VARCHAR(100),
  ghi_chu VARCHAR(500),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit
);

-- Table: phieu_giam_gia_chi_tiet
CREATE TABLE phieu_giam_gia_chi_tiet (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_khach_hang int not null,
  id_phieu_giam_gia int not null,
  trang_thai bit,
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit,
    CONSTRAINT fk_phieuGiamGiaChiTiet_khachHang FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id),
    CONSTRAINT fk_phieuGiamGiaChiTiet_phieuGiamGia FOREIGN KEY (id_phieu_giam_gia) REFERENCES phieu_giam_gia(id)
);


-- Table: hoa_don
CREATE TABLE hoa_don (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_nhan_vien INT NOT NULL,
  id_phieu_giam_gia INT NOT NULL,
  id_khach_hang INT NOT NULL,
  ma_hoa_don VARCHAR(100) NOT NULL,
  ngay_tao DATETIME,
  tong_tien FLOAT,
  tien_coc FLOAT,
  tien_con_lai FLOAT,
  tien_thua FLOAT,
  ghi_chu VARCHAR(100) NOT NULL,
  trang_thai VARCHAR(100) NOT NULL,
  ngay_den_san DATETIME,
  ngay_thanh_toan DATETIME,
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit,
CONSTRAINT fk_hoaDon_khachHang FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id),
  CONSTRAINT fk_hoaDon_nhanVien FOREIGN KEY (id_nhan_vien) REFERENCES nhan_vien(id),
  CONSTRAINT fk_hoaDon_phieuGiamGia FOREIGN KEY (id_phieu_giam_gia) REFERENCES phieu_giam_gia(id)
);

-- loai_san

-- Table: san_bong
CREATE TABLE loai_san (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ten_loai_san VARCHAR(100) NOT NULL,
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit
);
-- Table: san_bong
CREATE TABLE san_bong (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ten_san_bong VARCHAR(100) NOT NULL,
  id_loai_san int not null,
  CONSTRAINT fk_sanBong_loaiSan FOREIGN KEY (id_loai_san) REFERENCES loai_san(id),
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit
);

-- Table: ca
CREATE TABLE ca (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ten_ca VARCHAR(100) NOT NULL,
  gia_ca FLOAT,
  thoi_gian_bat_dau DATETIME,
  thoi_gian_ket_thuc DATETIME,
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit
);


-- ngay
CREATE TABLE ngay_trong_tuan (
  id INT AUTO_INCREMENT PRIMARY KEY,
  thu_trong_tuan VARCHAR(100) NOT NULL,
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit
);

-- Table: san_ca
CREATE TABLE san_ca (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_ca INT NOT NULL,
  id_san_bong INT NOT NULL,
  id_ngay_trong_tuan INT NOT NULL,
  gia Float NOT NULL,
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at BIT(1),
  CONSTRAINT fk_sanCa_ca FOREIGN KEY (id_ca) REFERENCES ca(id),
  CONSTRAINT fk_sanCa_sanBong FOREIGN KEY (id_san_bong) REFERENCES san_bong(id),
  CONSTRAINT fk_sanCa_ngay_trong_tuan FOREIGN KEY (id_ngay_trong_tuan) REFERENCES ngay_trong_tuan(id)
);


-- Table: hoa_don_chi_tiet
CREATE TABLE hoa_don_chi_tiet (
  id INT AUTO_INCREMENT PRIMARY KEY,
    id_hoa_don INT NOT NULL,
  id_san_ca INT NOT NULL,
  tien_san DECIMAL(10, 2) NOT NULL,
  ghi_chu TEXT NOT NULL,
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit,
  CONSTRAINT fk_hoaDonChiTiet_hoaDon FOREIGN KEY (id_hoa_don) REFERENCES hoa_don(id),
  CONSTRAINT fk_hoaDonChiTiet_sanCa FOREIGN KEY (id_san_ca) REFERENCES san_ca(id)
);

-- Table: hinh_thuc_thanh_toan
CREATE TABLE hinh_thuc_thanh_toan (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_hoa_don INT,
  hinh_thuc_thanh_toan VARCHAR(100) NOT NULL,
  trang_thai VARCHAR(100),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit,
  CONSTRAINT fk_hinhThucThanhToan_hoaDon FOREIGN KEY (id_hoa_don) REFERENCES hoa_don(id)
);

-- Table: chi_tiet_hinh_thuc_thanh_toan
CREATE TABLE chi_tiet_hinh_thuc_thanh_toan (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_hoa_don INT,
  id_hinh_thuc_thanh_toan INT,
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit,
  CONSTRAINT fk_chiTietHinhThucThanhToan_hoaDon FOREIGN KEY (id_hoa_don) REFERENCES hoa_don(id),
  CONSTRAINT fk_chiTietHinhThucThanhToan_hinhThucThanhToan FOREIGN KEY (id_hinh_thuc_thanh_toan) REFERENCES hinh_thuc_thanh_toan(id)
);


-- Table: lich_su_hoa_don
CREATE TABLE lich_su_hoa_don (
id INT AUTO_INCREMENT PRIMARY KEY,
  id_hoa_don INT,
  hanh_dong VARCHAR(255),
  loai_hanh_dong VARCHAR(255),
  ngay_tao DATETIME,
  ngay_cap_nhat DATETIME,
  ten_nguoi_tao VARCHAR(255),
  ten_nguoi_cap_nhat VARCHAR(255),
  so_lan_thay_doi INT,
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit,
  CONSTRAINT fk_lichSuHoaDon_hoaDon FOREIGN KEY (id_hoa_don) REFERENCES hoa_don(id)
);

-- Table: do_thue
CREATE TABLE do_thue (
  id INT AUTO_INCREMENT PRIMARY KEY,
  don_gia DECIMAL(10, 2),
  image VARCHAR(255),
  so_luong INT,
  ten_do_thue VARCHAR(255),
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit
);

-- Table: nuoc_uong
CREATE TABLE nuoc_uong (
  id INT AUTO_INCREMENT PRIMARY KEY,
  don_gia DECIMAL(10, 2),
  image VARCHAR(255),
  so_luong INT,
  ten_nuoc_uong VARCHAR(255),
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit
);

-- Table: dich_vu_san_bong
CREATE TABLE dich_vu_san_bong (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_do_thue INT,
  id_nuoc_uong INT,
  don_gia DECIMAL(10, 2),
  so_luong_do_thue INT,
  so_luong_nuoc_uong INT,
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit,
  CONSTRAINT fk_dichVuSanBong_doThue FOREIGN KEY (id_do_thue) REFERENCES do_thue(id),
  CONSTRAINT fk_dichVuSanBong_nuocUong FOREIGN KEY (id_nuoc_uong) REFERENCES nuoc_uong(id)
);

-- Table: chi_tiet_dich_vu_san_bong
CREATE TABLE chi_tiet_dich_vu_san_bong (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_dich_vu_san_bong INT,
  id_hoa_don_chi_tiet INT,
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit,
  CONSTRAINT fk_chiTietDichVuSanBong_dichVuSanBong FOREIGN KEY (id_dich_vu_san_bong) REFERENCES dich_vu_san_bong(id),
  CONSTRAINT fk_chiTietDichVuSanBong_hoaDonChiTiet FOREIGN KEY (id_hoa_don_chi_tiet) REFERENCES hoa_don_chi_tiet(id)
);
-- phu-phi

-- Table: phu_phi_hoa_don
CREATE TABLE phu_phi (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ten_phu_phi VARCHAR(100),
  trang_thai VARCHAR(100),
    created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit 
  );
-- Table: phu_phi_hoa_don
CREATE TABLE phu_phi_hoa_don (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_hoa_don_chi_tiet INT,
  id_phu_phi INT,
  thoi_gian_tao DATETIME,
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit,
  CONSTRAINT fk_phuPhiHoaDon_hoaDonChiTiet FOREIGN KEY (id_hoa_don_chi_tiet) REFERENCES hoa_don_chi_tiet(id),
   CONSTRAINT fk_phuPhiHoaDon_phiPhi FOREIGN KEY (id_phu_phi) REFERENCES phu_phi(id)
);

-- Table: loai_tham_so
CREATE TABLE loai_tham_so (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ten VARCHAR(255),
  trang_thai INT,
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit
);

-- Table: tham_so
CREATE TABLE tham_so (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_loai_tham_so INT,
  ma VARCHAR(255),
  ten VARCHAR(255),
  gia_tri VARCHAR(255),
  mo_ta TEXT,
  trang_thai INT,
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit,
  CONSTRAINT fk_thamSo_loaiThamSo FOREIGN KEY (id_loai_tham_so) REFERENCES loai_tham_so(id)
);

-- Table: giao_ca
CREATE TABLE giao_ca (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_nv_nhan_ca INT,
  id_nv_ban_giao INT,
  tien_phat_sinh DECIMAL(10,2),
thoi_gian_ket_ca DATETIME,
  tien_ban_dau DECIMAL(10,2),
  tong_tien_khac DECIMAL(10,2),
  tong_tien_mat DECIMAL(10,2),
  tong_tien_mat_ca_truoc DECIMAL(10,2),
  tong_tien_mat_rut DECIMAL(10,2),
  tong_tien_trong_ca DECIMAL(10,2),
  tien_con_lai DECIMAL(10,2),
  ghi_chu_phat_sinh VARCHAR(255),
  trang_thai VARCHAR(255),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit,
  CONSTRAINT fk_giaoCa_nvNhanCa FOREIGN KEY (id_nv_nhan_ca) REFERENCES nhan_vien(id),
  CONSTRAINT fk_giaoCa_nvBanGiao FOREIGN KEY (id_nv_ban_giao) REFERENCES nhan_vien(id)
);




-- INserttt 


INSERT INTO khach_hang (ma_khach_hang, mat_khau, ho_va_ten, email, gioi_tinh, so_dien_thoai, trang_thai, created_at, updated_at, deleted_at)
VALUES
  ('KH001', 'password1', 'Nguyen Van A', 'nguyenvana@gmail.com', true, '0123456789', 'active', NOW(), NOW(), 1),
  ('KH002', 'password2', 'Nguyen Van B', 'nguyenvanb@gmail.com', false, '0987654321', 'active', NOW(), NOW(), 1),
  ('KH003', 'password3', 'Nguyen Van C', 'nguyenvanc@gmail.com', true, '0123456789', 'inactive', NOW(), NOW(), 1),
  ('KH004', 'password4', 'Nguyen Van D', 'nguyenvand@gmail.com', false, '0987654321', 'active', NOW(), NOW(), 0),
  ('KH005', 'password5', 'Nguyen Van E', 'nguyenvane@gmail.com', true, '0123456789', 'active', NOW(), NOW(), 0);


INSERT INTO dia_chi (id, thanh_pho, quan_huyen, phuong_xa, ghi_chu, trang_thai, created_at, updated_at, deleted_at)
VALUES
(1, 'Hà Nội', 'Ba Đình', 'Kim Mã', 'Ghi chú 1', 1, '2024-07-01 10:00:00', '2024-07-01 10:00:00', 0),
(2, 'Hồ Chí Minh', 'Quận 1', 'Bến Nghé', 'Ghi chú 2', 0, '2024-07-02 10:00:00', '2024-07-02 10:00:00', 0),
(3, 'Đà Nẵng', 'Hải Châu', 'Hải Châu 1', 'Ghi chú 3', 1, '2024-07-03 10:00:00', '2024-07-03 10:00:00', 0),
(4, 'Hải Phòng', 'Lê Chân', 'An Biên', 'Ghi chú 4', 0, '2024-07-04 10:00:00', '2024-07-04 10:00:00', 0),
(5, 'Cần Thơ', 'Ninh Kiều', 'Tân An', 'Ghi chú 5', 1, '2024-07-05 10:00:00', '2024-07-05 10:00:00', 0),
(6, 'Hà Nội', 'Đống Đa', 'Cát Linh', 'Ghi chú 6', 0, '2024-07-06 10:00:00', '2024-07-06 10:00:00', 0),
(7, 'Hồ Chí Minh', 'Quận 3', '6', 'Ghi chú 7', 1, '2024-07-07 10:00:00', '2024-07-07 10:00:00', 0),
(8, 'Đà Nẵng', 'Thanh Khê', 'Thạc Gián', 'Ghi chú 8', 0, '2024-07-08 10:00:00', '2024-07-08 10:00:00', 0),
(9, 'Hải Phòng', 'Ngô Quyền', 'Máy Chai', 'Ghi chú 9', 1, '2024-07-09 10:00:00', '2024-07-09 10:00:00', 0),
(10, 'Cần Thơ', 'Bình Thủy', 'An Thới', 'Ghi chú 10', 0, '2024-07-10 10:00:00', '2024-07-10 10:00:00', 0);

INSERT INTO dia_chi_khach_hang (id, id_khach_hang,id_dia_chi, so_nha, duong, ghi_chu, trang_thai, created_at, updated_at, deleted_at)
VALUES
(1, 1,1, 'Số 1', 'Đường Kim Mã', 'Ghi chú 1', 'Mặc định', '2024-07-01 10:00:00', '2024-07-01 10:00:00', 0),
(2, 2,2, 'Số 2', 'Đường Nguyễn Huệ', 'Ghi chú 2', 'Hoạt động', '2024-07-02 10:00:00', '2024-07-02 10:00:00', 0),
(3, 3,3, 'Số 3', 'Đường Bạch Đằng', 'Ghi chú 3', 'Không hoạt động', '2024-07-03 10:00:00', '2024-07-03 10:00:00', 0),
(4, 4,4, 'Số 4', 'Đường Lê Lợi', 'Ghi chú 4', 'Mặc định', '2024-07-04 10:00:00', '2024-07-04 10:00:00', 0),
(5, 5,5, 'Số 5', 'Đường 30/4', 'Ghi chú 5', 'Hoạt động', '2024-07-05 10:00:00', '2024-07-05 10:00:00', 0),
(6, 1,6, 'Số 6', 'Đường Cát Linh', 'Ghi chú 6', 'Không hoạt động', '2024-07-06 10:00:00', '2024-07-06 10:00:00', 0),
(7, 2,7, 'Số 7', 'Đường Pasteur', 'Ghi chú 7', 'Mặc định', '2024-07-07 10:00:00', '2024-07-07 10:00:00', 0),
(8, 3,8, 'Số 8', 'Đường Nguyễn Văn Linh', 'Ghi chú 8', 'Hoạt động', '2024-07-08 10:00:00', '2024-07-08 10:00:00', 0),
(9, 4, 9,'Số 9', 'Đường Trần Phú', 'Ghi chú 9', 'Không hoạt động', '2024-07-09 10:00:00', '2024-07-09 10:00:00', 0),
(10, 5, 10,'Số 10', 'Đường Lê Hồng Phong', 'Ghi chú 10', 'Mặc định', '2024-07-10 10:00:00', '2024-07-10 10:00:00', 0);

INSERT INTO chuc_vu (ten_chuc_vu, ma_nhan_vien, trang_thai, created_at, updated_at, deleted_at)
VALUES
  ('Chuc vu 1', 'NV001', 'active', NOW(), NOW(), 1),
  ('Chuc vu 2', 'NV002', 'active', NOW(), NOW(), 1),
  ('Chuc vu 3', 'NV003', 'inactive', NOW(), NOW(), 1),
  ('Chuc vu 4', 'NV004', 'active', NOW(), NOW(), 0),
  ('Chuc vu 5', 'NV005', 'active', NOW(), NOW(), 0);


INSERT INTO nhan_vien (ten_nhan_vien, ma_nhan_vien, mat_khau, ho_ten, email, gioi_tinh, so_dien_thoai, dia_chi, trang_thai, created_at, updated_at, deleted_at)
VALUES
  ('Nhan vien 1', 'NV001', 'password1', 'Nguyen Van A', 'nguyenvana@gmail.com', true, '0123456789', 'Dia chi 1', 'active', NOW(), NOW(), 1),
  ('Nhan vien 2', 'NV002', 'password2', 'Nguyen Van B', 'nguyenvanb@gmail.com', false, '0987654321', 'Dia chi 2', 'active', NOW(), NOW(), 1),
  ('Nhan vien 3', 'NV003', 'password3', 'Nguyen Van C', 'nguyenvanc@gmail.com', true, '0123456789', 'Dia chi 3', 'inactive', NOW(), NOW(), 1),
  ('Nhan vien 4', 'NV004', 'password4', 'Nguyen Van D', 'nguyenvand@gmail.com', false, '0987654321', 'Dia chi 4', 'active', NOW(), NOW(), 0),
  ('Nhan vien 5', 'NV005', 'password5', 'Nguyen Van E', 'nguyenvane@gmail.com', true, '0123456789', 'Dia chi 5', 'active', NOW(), NOW(), 0);


INSERT INTO diem_danh (ten_chuc_vu, gio_vao, gio_ra, ghi_chu, trang_thai, id_nhan_vien, created_at, updated_at, deleted_at)
VALUES
  ('Chuc vu 1', NOW(), NOW(), 'Ghi chu 1', 'active', 1, NOW(), NOW(), 1),
  ('Chuc vu 2', NOW(), NOW(), 'Ghi chu 2', 'active', 2, NOW(), NOW(), 1),
  ('Chuc vu 3', NOW(), NOW(), 'Ghi chu 3', 'inactive', 3, NOW(), NOW(), 1),
  ('Chuc vu 4', NOW(), NOW(), 'Ghi chu 4', 'active', 4, NOW(), NOW(), 0),
  ('Chuc vu 5', NOW(), NOW(), 'Ghi chu 5', 'active', 5, NOW(), NOW(), 0);
  

INSERT INTO phieu_giam_gia (id_khach_hang, ma_phieu_giam_gia, ten_phieu_giam_gia, so_luong, muc_giam, hinh_thuc_giam_gia, dieu_kien_su_dung, gia_tri_toi_da, doi_tuong_ap_dung, ngay_bat_dau, ngay_ket_thuc, trang_thai, ghi_chu, created_at, updated_at, deleted_at)
VALUES
(1, 'PGG001', 'Phiếu giảm giá 1', 10, 10.0, 0, 100.0, 500.0, 1, '2024-07-01', '2024-07-31', 'Đang diễn ra', 'Ghi chú 1', '2024-07-01 10:00:00', '2024-07-01 10:00:00', 0),
(2, 'PGG002', 'Phiếu giảm giá 2', 20, 50.0, 1, 200.0, 1000.0, 0, '2024-08-01', '2024-08-31', 'Sắp diễn ra', 'Ghi chú 2', '2024-07-02 10:00:00', '2024-07-02 10:00:00', 0),
(3, 'PGG003', 'Phiếu giảm giá 3', 30, 20.0, 0, 150.0, 750.0, 1, '2024-06-01', '2024-06-30', 'Kết thúc', 'Ghi chú 3', '2024-07-03 10:00:00', '2024-07-03 10:00:00', 0),
(4, 'PGG004', 'Phiếu giảm giá 4', 15, 5.0, 1, 50.0, 300.0, 0, '2024-07-05', '2024-07-25', 'Đang diễn ra', 'Ghi chú 4', '2024-07-04 10:00:00', '2024-07-04 10:00:00', 0),
(5, 'PGG005', 'Phiếu giảm giá 5', 25, 30.0, 0, 250.0, 1200.0, 1, '2024-08-05', '2024-08-20', 'Sắp diễn ra', 'Ghi chú 5', '2024-07-05 10:00:00', '2024-07-05 10:00:00', 0),
(6, 'PGG006', 'Phiếu giảm giá 6', 50, 15.0, 1, 300.0, 1000.0, 0, '2024-06-05', '2024-06-25', 'Kết thúc', 'Ghi chú 6', '2024-07-06 10:00:00', '2024-07-06 10:00:00', 0),
(7, 'PGG007', 'Phiếu giảm giá 7', 40, 25.0, 0, 350.0, 1500.0, 1, '2024-07-10', '2024-07-20', 'Đang diễn ra', 'Ghi chú 7', '2024-07-07 10:00:00', '2024-07-07 10:00:00', 0),
(8, 'PGG008', 'Phiếu giảm giá 8', 35, 40.0, 1, 400.0, 1800.0, 0, '2024-08-10', '2024-08-30', 'Sắp diễn ra', 'Ghi chú 8', '2024-07-08 10:00:00', '2024-07-08 10:00:00', 0),
(9, 'PGG009', 'Phiếu giảm giá 9', 60, 10.0, 0, 100.0, 500.0, 1, '2024-06-10', '2024-06-30', 'Kết thúc', 'Ghi chú 9', '2024-07-09 10:00:00', '2024-07-09 10:00:00', 0),
(10, 'PGG010', 'Phiếu giảm giá 10', 20, 35.0, 1, 450.0, 2000.0, 0, '2024-07-15', '2024-07-31', 'Đang diễn ra', 'Ghi chú 10', '2024-07-10 10:00:00', '2024-07-10 10:00:00', 0),
(11, 'PGG011', 'Phiếu giảm giá 11', 15, 12.0, 0, 120.0, 600.0, 1, '2024-08-15', '2024-08-31', 'Sắp diễn ra', 'Ghi chú 11', '2024-07-11 10:00:00', '2024-07-11 10:00:00', 0),
(12, 'PGG012', 'Phiếu giảm giá 12', 55, 28.0, 1, 280.0, 1400.0, 0, '2024-06-15', '2024-06-30', 'Kết thúc', 'Ghi chú 12', '2024-07-12 10:00:00', '2024-07-12 10:00:00', 0),
(13, 'PGG013', 'Phiếu giảm giá 13', 45, 22.0, 0, 220.0, 1100.0, 1, '2024-07-20', '2024-07-31', 'Đang diễn ra', 'Ghi chú 13', '2024-07-13 10:00:00', '2024-07-13 10:00:00', 0),
(14, 'PGG014', 'Phiếu giảm giá 14', 30, 18.0, 1, 180.0, 900.0, 0, '2024-08-20', '2024-08-31', 'Sắp diễn ra', 'Ghi chú 14', '2024-07-14 10:00:00', '2024-07-14 10:00:00', 0),
(15, 'PGG015', 'Phiếu giảm giá 15', 70, 24.0, 0, 240.0, 1200.0, 1, '2024-06-20', '2024-06-30', 'Kết thúc', 'Ghi chú 15', '2024-07-15 10:00:00', '2024-07-15 10:00:00', 0),
(16, 'PGG016', 'Phiếu giảm giá 16', 25, 26.0, 1, 260.0, 1300.0, 0, '2024-07-25', '2024-07-31', 'Đang diễn ra', 'Ghi chú 16', '2024-07-16 10:00:00', '2024-07-16 10:00:00', 0),
(17, 'PGG017', 'Phiếu giảm giá 17', 35, 32.0, 0, 320.0, 1600.0, 1, '2024-08-25', '2024-08-31', 'Sắp diễn ra', 'Ghi chú 17', '2024-07-17 10:00:00', '2024-07-17 10:00:00', 0),
(18, 'PGG018', 'Phiếu giảm giá 18', 60, 14.0, 1, 140.0, 700.0, 0, '2024-06-25', '2024-06-30', 'Kết thúc', 'Ghi chú 18', '2024-07-18 10:00:00', '2024-07-18 10:00:00', 0),
(19, 'PGG019', 'Phiếu giảm giá 19', 50, 8.0, 0, 80.0, 400.0, 1, '2024-07-28', '2024-07-31', 'Đang diễn ra', 'Ghi chú 19', '2024-07-19 10:00:00', '2024-07-19 10:00:00', 0),
(20, 'PGG020', 'Phiếu giảm giá 20', 65, 27.0, 1, 270.0, 1350.0, 0, '2024-08-28', '2024-08-31', 'Sắp diễn ra', 'Ghi chú 20', '2024-07-20 10:00:00', '2024-07-20 10:00:00', 0);


INSERT INTO phieu_giam_gia_chi_tiet (id_khach_hang, id_phieu_giam_gia, trang_thai, created_at, updated_at, deleted_at)
VALUES
(1, 1, 1, '2024-07-01 10:00:00', '2024-07-01 10:00:00', 0),
(2, 2, 0, '2024-07-02 10:00:00', '2024-07-02 10:00:00', 0),
(3, 3, 1, '2024-07-03 10:00:00', '2024-07-03 10:00:00', 0),
(4, 4, 1, '2024-07-04 10:00:00', '2024-07-04 10:00:00', 0),
(5, 5, 0, '2024-07-05 10:00:00', '2024-07-05 10:00:00', 0),
(1, 6, 1, '2024-07-06 10:00:00', '2024-07-06 10:00:00', 0),
(2, 7, 0, '2024-07-07 10:00:00', '2024-07-07 10:00:00', 0),
(3, 8, 1, '2024-07-08 10:00:00', '2024-07-08 10:00:00', 0),
(4, 9, 0, '2024-07-09 10:00:00', '2024-07-09 10:00:00', 0),
(5, 10, 1, '2024-07-10 10:00:00', '2024-07-10 10:00:00', 0),
(1, 11, 1, '2024-07-11 10:00:00', '2024-07-11 10:00:00', 0),
(2, 12, 0, '2024-07-12 10:00:00', '2024-07-12 10:00:00', 0),
(3, 13, 1, '2024-07-13 10:00:00', '2024-07-13 10:00:00', 0),
(4, 14, 0, '2024-07-14 10:00:00', '2024-07-14 10:00:00', 0),
(5, 15, 1, '2024-07-15 10:00:00', '2024-07-15 10:00:00', 0),
(1, 16, 0, '2024-07-16 10:00:00', '2024-07-16 10:00:00', 0),
(2, 17, 1, '2024-07-17 10:00:00', '2024-07-17 10:00:00', 0),
(3, 18, 0, '2024-07-18 10:00:00', '2024-07-18 10:00:00', 0),
(4, 19, 1, '2024-07-19 10:00:00', '2024-07-19 10:00:00', 0),
(5, 20, 0, '2024-07-20 10:00:00', '2024-07-20 10:00:00', 0);



INSERT INTO hoa_don (id_nhan_vien, id_phieu_giam_gia, id_khach_hang, ma_hoa_don, ngay_tao, tong_tien, tien_coc, tien_con_lai, tien_thua, ghi_chu, trang_thai, ngay_den_san, ngay_thanh_toan, created_at, updated_at, deleted_at)
VALUES
  (1, 1, 1, 'HD001',  NOW(),  100000, 50000, 50000, 0, 'Ghi chu 1', 'active',  NOW(), NOW(), NOW(), NOW(), 1),
  (2, 2, 2, 'HD002',  NOW(),  200000, 100000, 100000, 0, 'Ghi chu 2', 'active',  NOW(), NOW(), NOW(), NOW(), 1),
  (3, 3, 3, 'HD003',  NOW(),  300000, 150000, 150000, 0, 'Ghi chu 3', 'inactive',  NOW(), NOW(), NOW(), NOW(), 1),
  (4, 4, 4, 'HD004',  NOW(),400000, 200000, 200000, 0, 'Ghi chu 4', 'active',  NOW(), NOW(), NOW(), NOW(), 0),
  (5, 5, 5, 'HD005',  NOW(),  500000, 250000, 250000, 0, 'Ghi chu 5', 'active', NOW(), NOW(), NOW(), NOW(), 0);


INSERT INTO loai_san (ten_loai_san, trang_thai, created_at, updated_at, deleted_at) VALUES
('Sân 5', 'Hoạt động', '2023-06-01 10:00:00', '2023-06-01 10:00:00', 1),
('Sân 7', 'Hoạt động', '2023-06-02 11:00:00', '2023-06-02 11:00:00', 1),
('Sân 11', 'Hoạt động', '2023-06-03 12:00:00', '2023-06-03 12:00:00', 1);



INSERT INTO san_bong (ten_san_bong, id_loai_san, trang_thai, created_at, updated_at, deleted_at)
VALUES
  ('San bong 1', 1, 'active', NOW(), NOW(), 1),
  ('San bong 2', 1, 'active', NOW(), NOW(), 1),
  ('San bong 3', 1, 'inactive', NOW(), NOW(), 1),
  ('San bong 4', 1, 'active', NOW(), NOW(), 0),
  ('San bong 5', 1, 'active', NOW(), NOW(), 0),
('San 7 1', 2, 'active', NOW(), NOW(), 1),
  ('San 7 2', 2, 'active', NOW(), NOW(), 1),
  ('San 7 3', 2, 'inactive', NOW(), NOW(), 1),
  ('San 7 4', 2, 'active', NOW(), NOW(), 0),
  ('San 7 5', 2, 'active', NOW(), NOW(), 0),
  ('San 11 1', 3, 'active', NOW(), NOW(), 1),
  ('San 11 2', 3, 'active', NOW(), NOW(), 1),
  ('San 11 3', 3, 'inactive', NOW(), NOW(), 1),
  ('San 11 4', 3, 'active', NOW(), NOW(), 0),
  ('San 11 5', 3, 'active', NOW(), NOW(), 0);
  


  
  INSERT INTO ca (ten_ca, gia_ca, thoi_gian_bat_dau, thoi_gian_ket_thuc, trang_thai, created_at, updated_at, deleted_at)
VALUES
  ('Ca 1', 100000, NOW(), NOW(), 'active', NOW(), NOW(), 1),
  ('Ca 2', 200000, NOW(), NOW(), 'active', NOW(), NOW(), 1),
  ('Ca 3', 300000, NOW(), NOW(), 'inactive', NOW(), NOW(), 1),
  ('Ca 4', 400000, NOW(), NOW(), 'active', NOW(), NOW(), 0),
  ('Ca 5', 500000, NOW(), NOW(), 'active', NOW(), NOW(), 0);

INSERT INTO ngay_trong_tuan (thu_trong_tuan, trang_thai, created_at, updated_at, deleted_at)
VALUES
  ('Monday',  'active', NOW(), NOW(), 1),
  ('Tuesday',  'active', NOW(), NOW(), 1),
  ('Wednesday',  'inactive', NOW(), NOW(), 1),
  ('Thursday', 'active', NOW(), NOW(), 0),
  ('Friday', 'active', NOW(), NOW(), 0);

INSERT INTO san_ca (id_ca, id_san_bong, id_ngay_trong_tuan,gia, trang_thai, created_at, updated_at, deleted_at)
VALUES
  (1, 1, 1,11000 ,'active', NOW(), NOW(), 1),
  (2, 2, 2, 22000,'active', NOW(), NOW(), 1),
  (3, 3, 3, 33000,'inactive', NOW(), NOW(), 1),
  (4, 4, 4, 44000,'active', NOW(), NOW(), 0),
  (5, 5, 5, 55000,'active', NOW(), NOW(), 0);


INSERT INTO hoa_don_chi_tiet (id_hoa_don, id_san_ca, tien_san, ghi_chu, trang_thai, created_at, updated_at, deleted_at)
VALUES
  (1, 1, 100000, 'Ghi chu 1', 'active', NOW(), NOW(), 1),
  (2, 2, 200000, 'Ghi chu 2', 'active', NOW(), NOW(), 1),
  (3, 3, 300000, 'Ghi chu 3', 'inactive', NOW(), NOW(), 1),
  (4, 4, 400000, 'Ghi chu 4', 'active', NOW(), NOW(), 1),
  (5, 5, 500000, 'Ghi chu 5', 'active', NOW(), NOW(), 1),
  (1, 4, 600000, 'Ghi chu 6', 'active', NOW(), NOW(), 1);


INSERT INTO hinh_thuc_thanh_toan (hinh_thuc_thanh_toan, trang_thai, created_at, updated_at, deleted_at)
VALUES
  ('Thanh toan 1', 'active', NOW(), NOW(), 1),
  ( 'Thanh toan 2', 'active', NOW(), NOW(), 1),
  ( 'Thanh toan 3', 'inactive', NOW(), NOW(), 1),
  ('Thanh toan 4', 'active', NOW(), NOW(), 1),
  ('Thanh toan 5', 'active', NOW(), NOW(), 0);


INSERT INTO lich_su_hoa_don (id_hoa_don, hanh_dong, loai_hanh_dong, ngay_tao, ngay_cap_nhat, ten_nguoi_tao, ten_nguoi_cap_nhat, so_lan_thay_doi, trang_thai, created_at, updated_at, deleted_at)
VALUES
  (1, 'Tao hoa don', 'Tao', NOW(), NOW(), 'Nguyen Van A', 'Nguyen Van A', 1, 'active', NOW(), NOW(), 1),
  (2, 'Cap nhat hoa don', 'Cap nhat', NOW(), NOW(), 'Nguyen Van B', 'Nguyen Van B', 2, 'active', NOW(), NOW(), 1),
  (3, 'Xoa hoa don', 'Xoa', NOW(), NOW(), 'Nguyen Van C', 'Nguyen Van C', 3, 'inactive', NOW(), NOW(), 1),
  (4, 'Tao hoa don', 'Tao', NOW(), NOW(), 'Nguyen Van D', 'Nguyen Van D', 4, 'active', NOW(), NOW(), 0),
  (5, 'Cap nhat hoa don', 'Cap nhat', NOW(), NOW(), 'Nguyen Van E', 'Nguyen Van E', 5, 'active', NOW(), NOW(), 0);

INSERT INTO do_thue (don_gia, image, so_luong, ten_do_thue, trang_thai, created_at, updated_at, deleted_at)
VALUES
  (100000, 'image1.jpg', 10, 'Do thue 1', 'active', NOW(), NOW(), 1),
  (200000, 'image2.jpg', 20, 'Do thue 2', 'active', NOW(), NOW(), 1),
  (300000, 'image3.jpg', 30, 'Do thue 3', 'inactive', NOW(), NOW(), 1),
  (400000, 'image4.jpg', 40, 'Do thue 4', 'active', NOW(), NOW(), 0),
  (500000, 'image5.jpg', 50, 'Do thue 5', 'active', NOW(), NOW(), 0);



INSERT INTO nuoc_uong (don_gia, image, so_luong, ten_nuoc_uong, trang_thai, created_at, updated_at, deleted_at)
VALUES
  (50000, 'image1.jpg', 10, 'Nuoc uong 1', 'active', NOW(), NOW(), 1),
  (60000, 'image2.jpg', 20, 'Nuoc uong 2', 'active', NOW(), NOW(), 1),
  (70000, 'image3.jpg', 30, 'Nuoc uong 3', 'inactive', NOW(), NOW(), 1),
  (80000, 'image4.jpg', 40, 'Nuoc uong 4', 'active', NOW(), NOW(), 0),
  (90000, 'image5.jpg', 50, 'Nuoc uong 5', 'active', NOW(), NOW(), 0);

INSERT INTO dich_vu_san_bong (id_do_thue, id_nuoc_uong, don_gia, so_luong_do_thue, so_luong_nuoc_uong, trang_thai, created_at, updated_at, deleted_at)
VALUES
  (1, 1, 150000, 10, 20, 'active', NOW(), NOW(), 1),
  (2, 2, 250000, 20, 30, 'active', NOW(), NOW(), 1),
  (3, 3, 350000, 30, 40, 'inactive', NOW(), NOW(), 1),
  (4, 4, 450000, 40, 50, 'active', NOW(), NOW(), 0),
  (5, 5, 550000, 50, 60, 'active', NOW(), NOW(), 0);


INSERT INTO chi_tiet_dich_vu_san_bong (id_dich_vu_san_bong, id_hoa_don_chi_tiet, trang_thai, created_at, updated_at, deleted_at)
VALUES
  (1, 1, 'active', NOW(), NOW(), 1),
  (2, 2, 'active', NOW(), NOW(), 1),
  (3, 3, 'inactive', NOW(), NOW(), 1),
  (4, 4, 'active', NOW(), NOW(), 0),
  (5, 5, 'active', NOW(), NOW(), 0);

INSERT INTO phu_phi (ten_phu_phi, trang_thai, created_at, updated_at, deleted_at) VALUES
('Phí vệ sinh', 'Hoạt động', '2023-06-01 10:00:00', '2023-06-01 10:00:00', 1),
('Phí bảo trì', 'Đang bảo trì', '2023-06-02 11:00:00', '2023-06-02 11:00:00', 1),
('Phí an ninh', 'Hoạt động', '2023-06-03 12:00:00', '2023-06-03 12:00:00', 1),
('Phí quản lý', 'Đóng cửa', '2023-06-04 13:00:00', '2023-06-04 13:00:00',1),
('Phí tiện ích', 'Hoạt động', '2023-06-05 14:00:00', '2023-06-05 14:00:00', 1);

INSERT INTO phu_phi_hoa_don (id_hoa_don_chi_tiet, id_phu_phi,thoi_gian_tao, trang_thai, created_at, updated_at, deleted_at)
VALUES
  (1, 1,NOW(), 'active', NOW(), NOW(), 1),
  (2, 2,NOW(), 'active', NOW(), NOW(), 1),
  (3,3, NOW(), 'inactive', NOW(), NOW(), 1),
  (4,4, NOW(), 'active', NOW(), NOW(), 0),
  (5,5, NOW(), 'active', NOW(), NOW(), 0);

INSERT INTO loai_tham_so (ten, trang_thai, created_at, updated_at, deleted_at)
VALUES
  ('Loai tham so 1', 1, NOW(), NOW(), 0),
  ('Loai tham so 2', 1, NOW(), NOW(), 0),
  ('Loai tham so 3', 0, NOW(), NOW(), 0),
  ('Loai tham so 4', 1, NOW(), NOW(), 0),
  ('Loai tham so 5', 1, NOW(), NOW(), 0);

INSERT INTO tham_so (id_loai_tham_so, ma, ten, gia_tri, mo_ta, trang_thai, created_at, updated_at, deleted_at)
VALUES
  (1, 'TS001', 'Tham so 1', 'Gia tri 1', 'Mo ta 1', 1, NOW(), NOW(), 0),
  (2, 'TS002', 'Tham so 2', 'Gia tri 2', 'Mo ta 2', 1, NOW(), NOW(), 0),
  (3, 'TS003', 'Tham so 3', 'Gia tri 3', 'Mo ta 3', 0, NOW(), NOW(), 0),
  (4, 'TS004', 'Tham so 4', 'Gia tri 4', 'Mo ta 4', 1, NOW(), NOW(), 0),
  (5, 'TS005', 'Tham so 5', 'Gia tri 5', 'Mo ta 5', 1, NOW(), NOW(), 0);


INSERT INTO giao_ca (id_nv_nhan_ca, id_nv_ban_giao, tien_phat_sinh, thoi_gian_ket_ca, tien_ban_dau, tong_tien_khac, tong_tien_mat, tong_tien_mat_ca_truoc, tong_tien_mat_rut, tong_tien_trong_ca, tien_con_lai, ghi_chu_phat_sinh, trang_thai, created_at, updated_at, deleted_at)
VALUES
  (1, 2, 100000.00, '2022-01-01 08:00:00', 200000.00, 300000.00, 400000.00, 500000.00, 600000.00, 700000.00, 800000.00, 'Ghi chu 1', 'active', NOW(), NOW(), 0),
  (2, 3, 200000.00, '2022-01-02 08:00:00', 300000.00, 400000.00, 500000.00, 600000.00, 700000.00, 800000.00, 900000.00, 'Ghi chu 2', 'active', NOW(), NOW(), 0),
  (3, 4, 300000.00, '2022-01-03 08:00:00', 400000.00, 500000.00, 600000.00, 700000.00, 800000.00, 900000.00, 1000000.00, 'Ghi chu 3', 'inactive', NOW(), NOW(), 0),
  (4, 5, 400000.00, '2022-01-04 08:00:00', 500000.00, 600000.00, 700000.00, 800000.00, 900000.00, 1000000.00, 1100000.00, 'Ghi chu 4', 'active', NOW(), NOW(), 0),
  (5, 1, 500000.00, '2022-01-05 08:00:00', 600000.00, 700000.00, 800000.00, 900000.00, 1000000.00, 1100000.00, 1200000.00, 'Ghi chu 5', 'active', NOW(), NOW(), 0);