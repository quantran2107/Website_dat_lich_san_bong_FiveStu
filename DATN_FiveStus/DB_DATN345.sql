CREATE DATABASE DuAnTotNghiep1;

USE DuAnTotNghiep1;

-- DB update 26/06/2024
-- Table: khach_hang
CREATE TABLE khach_hang (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ma_khach_hang VARCHAR(50) NOT NULL,
  mat_khau VARCHAR(50) NOT NULL,
  ho_va_ten VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  gioi_tinh BOOLEAN NOT NULL,
  so_dien_thoai VARCHAR(50) NOT NULL,
  trang_thai VARCHAR(50) NOT NULL

);

-- Table: dia_chi
CREATE TABLE dia_chi (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ten_dia_chi VARCHAR(100) DEFAULT '',
  ghi_chu VARCHAR(100) DEFAULT '',
  id_khach_hang INT NOT NULL,
  CONSTRAINT fk_diaChi_khachHang FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id)
);

-- Table: chuc_vu
CREATE TABLE chuc_vu (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ten_chuc_vu VARCHAR(100) NOT NULL,
  ma_nhan_vien VARCHAR(100) NOT NULL,
  trang_thai VARCHAR(100) NOT NULL  
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
  trang_thai VARCHAR(100) NOT NULL  
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
  CONSTRAINT fk_diemDanh_nhanVien FOREIGN KEY (id_nhan_vien) REFERENCES nhan_vien(id)
);

-- Table: phieu_giam_gia
CREATE TABLE phieu_giam_gia (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ma_phieu_giam_gia VARCHAR(100),
  ten_phieu_giam_gia VARCHAR(100),
  so_luong INT,
  muc_giam float,
  hinh_thuc_giam_gia FLOAT,
  dieu_kien_su_dung VARCHAR(100),
  ngay_bat_dau DATETIME,
  ngay_ket_thuc DATETIME,
  trang_thai VARCHAR(100)
);

-- Table: hoa_don
CREATE TABLE hoa_don (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_nhan_vien INT NOT NULL,
  id_phieu_giam_gia INT NOT NULL,
  id_khach_hang INT NOT NULL,
  ma_hoa_don VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  ngay_tao DATETIME,
  so_dien_thoai_nguoi_dat VARCHAR(100) NOT NULL,
  ten_nguoi_dat VARCHAR(100) NOT NULL,
  tong_tien FLOAT,
  tien_coc FLOAT,
  tien_con_lai FLOAT,
  tien_thua FLOAT,
  ghi_chu VARCHAR(100) NOT NULL,
  trang_thai VARCHAR(100) NOT NULL,
  ngay_den_san DATETIME,
  ngay_thanh_toan DATETIME,
  CONSTRAINT fk_hoaDon_khachHang FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id),
  CONSTRAINT fk_hoaDon_nhanVien FOREIGN KEY (id_nhan_vien) REFERENCES nhan_vien(id),
  CONSTRAINT fk_hoaDon_phieuGiamGia FOREIGN KEY (id_phieu_giam_gia) REFERENCES phieu_giam_gia(id)
);

-- Table: san_bong
CREATE TABLE san_bong (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ten_san_bong VARCHAR(100) NOT NULL,
  gia_san FLOAT,
  trang_thai VARCHAR(50)
);

-- Table: ca
CREATE TABLE ca (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ten_ca VARCHAR(100) NOT NULL,
  gia_ca FLOAT,
  thoi_gian_bat_dau DATETIME,
  thoi_gian_ket_thuc DATETIME,
  trang_thai VARCHAR(50)
);


-- ngay
CREATE TABLE ngay_trong_tuan (
  id INT AUTO_INCREMENT PRIMARY KEY,
  thu_trong_tuan VARCHAR(100) NOT NULL,
  gia_ca FLOAT,
  he_so float,
  trang_thai VARCHAR(50)
);

-- Table: san_ca
 CREATE TABLE san_ca (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_ca INT NOT NULL,
  id_san_bong INT NOT NULL,
  id_ngay_trong_tuan INT NOT NULL,
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit,
  CONSTRAINT fk_sanCa_ca FOREIGN KEY (id_ca) REFERENCES ca(id),
  CONSTRAINT fk_sanCa_sanBong FOREIGN KEY (id_san_bong) REFERENCES san_bong(id),
  CONSTRAINT fk_sanCa_ngay_trong_tuan FOREIGN KEY (id_ngay_trong_tuan) REFERENCES ngay_trong_tuan(id)
);



-- Table: hoa_don_chi_tiet
CREATE TABLE hoa_don_chi_tiet (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_hoa_don INT NOT NULL,
  id_san_ca INT NOT NULL,
  ngay_den_san DATE NOT NULL,
  muay_thanh_toan VARCHAR(50) NOT NULL,
  thoi_gian_checkin VARCHAR(50) NOT NULL,
  tien_san DECIMAL(10, 2) NOT NULL,
  ghi_chu TEXT NOT NULL,
  CONSTRAINT fk_hoaDonChiTiet_hoaDon FOREIGN KEY (id_hoa_don) REFERENCES hoa_don(id),
  CONSTRAINT fk_hoaDonChiTiet_sanCa FOREIGN KEY (id_san_ca) REFERENCES san_ca(id)
);

-- Table: hinh_thuc_thanh_toan
CREATE TABLE hinh_thuc_thanh_toan (
  id INT AUTO_INCREMENT PRIMARY KEY,
  hinh_thuc_thanh_toan VARCHAR(100) NOT NULL,
  trang_thai VARCHAR(100)
);

-- Table: chi_tiet_hinh_thuc_thanh_toan
CREATE TABLE chi_tiet_hinh_thuc_thanh_toan (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_hoa_don INT,
  id_hinh_thuc_thanh_toan INT,
  trang_thai VARCHAR(50),
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
  CONSTRAINT fk_lichSuHoaDon_hoaDon FOREIGN KEY (id_hoa_don) REFERENCES hoa_don(id)
);

-- Table: do_thue
CREATE TABLE do_thue (
  id INT AUTO_INCREMENT PRIMARY KEY,
  don_gia DECIMAL(10, 2),
  image VARCHAR(255),
  so_luong INT,
  ten_do_thue VARCHAR(255),
  trang_thai VARCHAR(50)
);

-- Table: nuoc_uong
CREATE TABLE nuoc_uong (
  id INT AUTO_INCREMENT PRIMARY KEY,
  don_gia DECIMAL(10, 2),
  image VARCHAR(255),
  so_luong INT,
  ten_nuoc_uong VARCHAR(255),
  trang_thai VARCHAR(50)
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
  CONSTRAINT fk_dichVuSanBong_doThue FOREIGN KEY (id_do_thue) REFERENCES do_thue(id),
  CONSTRAINT fk_dichVuSanBong_nuocUong FOREIGN KEY (id_nuoc_uong) REFERENCES nuoc_uong(id)
);

-- Table: chi_tiet_dich_vu_san_bong
CREATE TABLE chi_tiet_dich_vu_san_bong (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_dich_vu_san_bong INT,
  id_hoa_don_chi_tiet INT,
  trang_thai VARCHAR(50),
  CONSTRAINT fk_chiTietDichVuSanBong_dichVuSanBong FOREIGN KEY (id_dich_vu_san_bong) REFERENCES dich_vu_san_bong(id),
  CONSTRAINT fk_chiTietDichVuSanBong_hoaDonChiTiet FOREIGN KEY (id_hoa_don_chi_tiet) REFERENCES hoa_don_chi_tiet(id)
);

-- Table: phu_phi_hoa_don
CREATE TABLE phu_phi_hoa_don (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_hoa_don_chi_tiet INT,
  thoi_gian_tao DATETIME,
  trang_thai VARCHAR(50),
  CONSTRAINT fk_phuPhiHoaDon_hoaDonChiTiet FOREIGN KEY (id_hoa_don_chi_tiet) REFERENCES hoa_don_chi_tiet(id)
);

-- Table: loai_tham_so
CREATE TABLE loai_tham_so (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ten VARCHAR(255),
  trang_thai INT
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
  CONSTRAINT fk_giaoCa_nvNhanCa FOREIGN KEY (id_nv_nhan_ca) REFERENCES nhan_vien(id),
  CONSTRAINT fk_giaoCa_nvBanGiao FOREIGN KEY (id_nv_ban_giao) REFERENCES nhan_vien(id)
);




-- Insert dữ liệu: USE DuAnTotNghiep1;

-- Insert 5 records into khach_hang
INSERT INTO khach_hang (ma_khach_hang, mat_khau, ho_va_ten, email, gioi_tinh, so_dien_thoai, trang_thai)
VALUES 
('KH001', 'password1', 'Nguyen Van A', 'a@gmail.com', 1, '0123456789', 'active'),
('KH002', 'password2', 'Tran Thi B', 'b@gmail.com', 0, '0987654321', 'active'),
('KH003', 'password3', 'Le Van C', 'c@gmail.com', 1, '0112233445', 'inactive'),
('KH004', 'password4', 'Pham Thi D', 'd@gmail.com', 0, '0998877665', 'active'),
('KH005', 'password5', 'Hoang Van E', 'e@gmail.com', 1, '0123344556', 'inactive');

-- Insert 5 records into dia_chi
INSERT INTO dia_chi (ten_dia_chi, ghi_chu, id_khach_hang)
VALUES 
('Address 1', 'Note 1', 1),
('Address 2', 'Note 2', 2),
('Address 3', 'Note 3', 3),
('Address 4', 'Note 4', 4),
('Address 5', 'Note 5', 5);

-- Insert 5 records into chuc_vu
INSERT INTO chuc_vu (ten_chuc_vu, ma_nhan_vien, trang_thai)
VALUES 
('Manager', 'NV001', 'active'),
('Assistant', 'NV002', 'active'),
('Clerk', 'NV003', 'inactive'),
('Supervisor', 'NV004', 'active'),
('Technician', 'NV005', 'inactive');

-- Insert 5 records into nhan_vien
INSERT INTO nhan_vien (ten_nhan_vien, ma_nhan_vien, mat_khau, ho_ten, email, gioi_tinh, so_dien_thoai, dia_chi, trang_thai)
VALUES 
('Nguyen Van A', 'NV001', 'password1', 'Nguyen Van A', 'a@gmail.com', 'Nam', '0123456789', 'Address 1', 'active'),
('Tran Thi B', 'NV002', 'password2', 'Tran Thi B', 'b@gmail.com', 'Nu', '0987654321', 'Address 2', 'active'),
('Le Van C', 'NV003', 'password3', 'Le Van C', 'c@gmail.com', 'Nam', '0112233445', 'Address 3', 'inactive'),
('Pham Thi D', 'NV004', 'password4', 'Pham Thi D', 'd@gmail.com', 'Nu', '0998877665', 'Address 4', 'active'),
('Hoang Van E', 'NV005', 'password5', 'Hoang Van E', 'e@gmail.com', 'Nam', '0123344556', 'Address 5', 'inactive');

-- Insert 5 records into diem_danh
INSERT INTO diem_danh (ten_chuc_vu, gio_vao, gio_ra, ghi_chu, trang_thai, id_nhan_vien)
VALUES 
('Manager', '2024-01-01 08:00:00', '2024-01-01 17:00:00', 'Note 1', 'present', 1),
('Assistant', '2024-01-02 09:00:00', '2024-01-02 18:00:00', 'Note 2', 'present', 2),
('Clerk', '2024-01-03 10:00:00', '2024-01-03 19:00:00', 'Note 3', 'absent', 3),
('Supervisor', '2024-01-04 07:00:00', '2024-01-04 16:00:00', 'Note 4', 'present', 4),
('Technician', '2024-01-05 11:00:00', '2024-01-05 20:00:00', 'Note 5', 'absent', 5);

-- Insert 5 records into phieu_giam_gia
INSERT INTO phieu_giam_gia (ma_phieu_giam_gia, ten_phieu_giam_gia, so_luong, muc_giam, hinh_thuc_giam_gia, dieu_kien_su_dung, ngay_bat_dau, ngay_ket_thuc, trang_thai)
VALUES 
('PGG001', 'Discount 1', 100, 10000, 0.10, 'Condition 1', '2024-01-01 00:00:00', '2024-12-31 23:59:59', 'active'),
('PGG002', 'Discount 2', 50, 20000, 0.20, 'Condition 2', '2024-01-01 00:00:00', '2024-12-31 23:59:59', 'active'),
('PGG003', 'Discount 3', 200, 30000, 0.15, 'Condition 3', '2024-01-01 00:00:00', '2024-12-31 23:59:59', 'inactive'),
('PGG004', 'Discount 4', 30, 30000, 0.25, 'Condition 4', '2024-01-01 00:00:00', '2024-12-31 23:59:59', 'active'),
('PGG005', 'Discount 5', 70, 30000, 0.30, 'Condition 5', '2024-01-01 00:00:00', '2024-12-31 23:59:59', 'inactive');

-- Insert 5 records into hoa_don
INSERT INTO hoa_don (id_nhan_vien, id_phieu_giam_gia, id_khach_hang, ma_hoa_don, email, ngay_tao, so_dien_thoai_nguoi_dat, ten_nguoi_dat, tong_tien, tien_coc, tien_con_lai, tien_thua, ghi_chu, trang_thai, ngay_den_san, ngay_thanh_toan)
VALUES 
(1, 1, 1, 'HD001', 'a@gmail.com', '2024-01-01 12:00:00', '0123456789', 'Nguyen Van A', 100000, 50000, 50000, 0, 'Note 1', 'paid', '2024-01-05 12:00:00', '2024-01-01 15:00:00'),
(2, 2, 2, 'HD002', 'b@gmail.com', '2024-02-01 12:00:00', '0987654321', 'Tran Thi B', 200000, 100000, 100000, 0, 'Note 2', 'paid', '2024-02-05 12:00:00', '2024-02-01 15:00:00'),
(3, 3, 3, 'HD003', 'c@gmail.com', '2024-03-01 12:00:00', '0112233445', 'Le Van C', 300000, 150000, 150000, 0, 'Note 3', 'unpaid',  '2024-03-05 12:00:00', '2024-03-01 15:00:00'),
(4, 4, 4, 'HD004', 'd@gmail.com', '2024-04-01 12:00:00', '0998877665', 'Pham Thi D', 400000, 200000, 200000, 0, 'Note 4', 'paid',  '2024-04-05 12:00:00', '2024-04-01 15:00:00'),
(5, 5, 5, 'HD005', 'e@gmail.com', '2024-05-01 12:00:00', '0123344556', 'Hoang Van E', 500000, 250000, 250000, 0, 'Note 5', 'unpaid',  '2024-05-05 12:00:00', '2024-05-01 15:00:00');

-- Insert 5 records into san_bong
INSERT INTO san_bong (ten_san_bong, gia_san, trang_thai)
VALUES 
('San 1', 50000, 'available'),
('San 2', 60000, 'available'),
('San 3', 70000, 'occupied'),
('San 4', 80000, 'available'),
('San 5', 90000, 'occupied');

-- Insert 5 records into ca
INSERT INTO ca (ten_ca, gia_ca, thoi_gian_bat_dau, thoi_gian_ket_thuc, trang_thai)
VALUES 
('Ca 1', 100000, '2024-01-01 08:00:00', '2024-01-01 10:00:00', 'available'),
('Ca 2', 200000, '2024-01-01 10:00:00', '2024-01-01 12:00:00', 'available'),
('Ca 3', 300000, '2024-01-01 12:00:00', '2024-01-01 14:00:00', 'booked'),
('Ca 4', 400000, '2024-01-01 14:00:00', '2024-01-01 16:00:00', 'available'),
('Ca 5', 500000, '2024-01-01 16:00:00', '2024-01-01 18:00:00', 'booked');

-- Insert 5 records into ngay_trong_tuaan
INSERT INTO ngay_trong_tuan (thu_trong_tuan, gia_ca, he_so, trang_thai)
VALUES 
('Monday', 100000, 1.0, 'available'),
('Tuesday', 200000, 1.1, 'available'),
('Wednesday', 300000, 1.2, 'booked'),
('Thursday', 400000, 1.3, 'available'),
('Friday', 500000, 1.4, 'booked');

-- Insert 5 records into san_ca
INSERT INTO san_ca (id_ca, id_san_bong, id_ngay_trong_tuan, trang_thai, created_at, updated_at, deleted_at) VALUES
(1, 1, 1, 'available', NOW(), NOW(), 1),
(2, 2, 2, 'reserved', NOW(), NOW(), 1),
(3, 3, 3, 'maintenance', NOW(), NOW(), 1),
(4, 1, 4, 'available', NOW(), NOW(), 0),
(5, 2, 5, 'reserved', NOW(), NOW(), 0);



-- Insert 5 records into hoa_don_chi_tiet
INSERT INTO hoa_don_chi_tiet (id_hoa_don, id_san_ca, ngay_den_san, muay_thanh_toan, thoi_gian_checkin, tien_san, ghi_chu)
VALUES 
(1, 1, '2024-01-05', 'cash', '08:00', 100000, 'Note 1'),
(2, 2, '2024-02-05', 'card', '10:00', 200000, 'Note 2'),
(3, 3, '2024-03-05', 'cash', '12:00', 300000, 'Note 3'),
(4, 4, '2024-04-05', 'card', '14:00', 400000, 'Note 4'),
(5, 5, '2024-05-05', 'cash', '16:00', 500000, 'Note 5');

-- Insert 5 records into hinh_thuc_thanh_toan
INSERT INTO hinh_thuc_thanh_toan ( hinh_thuc_thanh_toan, trang_thai)
VALUES 
( 'cash', 'completed'),
( 'card', 'completed'),
( 'cash', 'pending'),
( 'card', 'completed'),
('cash', 'pending');

-- Insert 5 records into chi_tiet_hinh_thuc_thanh_toan
INSERT INTO chi_tiet_hinh_thuc_thanh_toan (id_hoa_don, id_hinh_thuc_thanh_toan, trang_thai)
VALUES 
(1, 1, 'completed'),
(2, 2, 'completed'),
(3, 3, 'pending'),
(4, 4, 'completed'),
(5, 5, 'pending');

-- Insert 5 records into lich_su_hoa_don
INSERT INTO lich_su_hoa_don (id_hoa_don, hanh_dong, loai_hanh_dong, ngay_tao, ngay_cap_nhat, ten_nguoi_tao, ten_nguoi_cap_nhat, so_lan_thay_doi, trang_thai)
VALUES 
(1, 'created', 'create', '2024-01-01 12:00:00', '2024-01-01 12:00:00', 'Admin', 'Admin', 0, 'active'),
(2, 'updated', 'update', '2024-02-01 12:00:00', '2024-02-01 12:00:00', 'Admin', 'Admin', 1, 'active'),
(3, 'deleted', 'delete', '2024-03-01 12:00:00', '2024-03-01 12:00:00', 'Admin', 'Admin', 1, 'inactive'),
(4, 'created', 'create', '2024-04-01 12:00:00', '2024-04-01 12:00:00', 'Admin', 'Admin', 0, 'active'),
(5, 'updated', 'update', '2024-05-01 12:00:00', '2024-05-01 12:00:00', 'Admin', 'Admin', 1, 'active');

-- Insert 5 records into do_thue
INSERT INTO do_thue (don_gia, image, so_luong, ten_do_thue, trang_thai)
VALUES 
(10000, 'image1.jpg', 10, 'Do Thue 1', 'available'),
(20000, 'image2.jpg', 20, 'Do Thue 2', 'available'),
(30000, 'image3.jpg', 30, 'Do Thue 3', 'rented'),
(40000, 'image4.jpg', 40, 'Do Thue 4', 'available'),
(50000, 'image5.jpg', 50, 'Do Thue 5', 'rented');

-- Insert 5 records into nuoc_uong
INSERT INTO nuoc_uong (don_gia, image, so_luong, ten_nuoc_uong, trang_thai)
VALUES 
(10000, 'image1.jpg', 100, 'Nuoc Uong 1', 'available'),
(20000, 'image2.jpg', 200, 'Nuoc Uong 2', 'available'),
(30000, 'image3.jpg', 300, 'Nuoc Uong 3', 'sold out'),
(40000, 'image4.jpg', 400, 'Nuoc Uong 4', 'available'),
(50000, 'image5.jpg', 500, 'Nuoc Uong 5', 'sold out');

-- Insert 5 records into dich_vu_san_bong
INSERT INTO dich_vu_san_bong (id_do_thue, id_nuoc_uong, don_gia, so_luong_do_thue, so_luong_nuoc_uong, trang_thai)
VALUES 
(1, 1, 100000, 10, 100, 'available'),
(2, 2, 200000, 20, 200, 'available'),
(3, 3, 300000, 30, 300, 'sold out'),
(4, 4, 400000, 40, 400, 'available'),
(5, 5, 500000, 50, 500, 'sold out');

-- Insert 5 records into chi_tiet_dich_vu_san_bong
INSERT INTO chi_tiet_dich_vu_san_bong (id_dich_vu_san_bong, id_hoa_don_chi_tiet, trang_thai)
VALUES 
(1, 1, 'completed'),
(2, 2, 'completed'),
(3, 3, 'pending'),
(4, 4, 'completed'),
(5, 5, 'pending');

-- Insert 5 records into phu_phi_hoa_don
INSERT INTO phu_phi_hoa_don (id_hoa_don_chi_tiet, thoi_gian_tao, trang_thai)
VALUES 
(1, '2024-01-01 12:00:00', 'active'),
(2, '2024-02-01 12:00:00', 'active'),
(3, '2024-03-01 12:00:00', 'inactive'),
(4, '2024-04-01 12:00:00', 'active'),
(5, '2024-05-01 12:00:00', 'inactive');

-- Insert 5 records into loai_tham_so
INSERT INTO loai_tham_so (ten, trang_thai)
VALUES 
('Type 1', 1),
('Type 2', 1),
('Type 3', 0),
('Type 4', 1),
('Type 5', 0);

-- Insert 5 records into tham_so
INSERT INTO tham_so (id_loai_tham_so, ma, ten, gia_tri, mo_ta, trang_thai)
VALUES 
(1, 'Code 1', 'Param 1', 'Value 1', 'Description 1', 1),
(2, 'Code 2', 'Param 2', 'Value 2', 'Description 2', 1),
(3, 'Code 3', 'Param 3', 'Value 3', 'Description 3', 0),
(4, 'Code 4', 'Param 4', 'Value 4', 'Description 4', 1),
(5, 'Code 5', 'Param 5', 'Value 5', 'Description 5', 0);

-- Insert 5 records into giao_ca
INSERT INTO giao_ca (id_nv_nhan_ca, id_nv_ban_giao, tien_phat_sinh, thoi_gian_ket_ca, tien_ban_dau, tong_tien_khac, tong_tien_mat, tong_tien_mat_ca_truoc, tong_tien_mat_rut, tong_tien_trong_ca, tien_con_lai, ghi_chu_phat_sinh, trang_thai)
VALUES 
(1, 2, 1000, '2024-01-01 12:00:00', 5000, 6000, 7000, 8000, 9000, 10000, 11000, 'Note 1', 'completed'),
(2, 3, 2000, '2024-02-01 12:00:00', 6000, 7000, 8000, 9000, 10000, 11000, 12000, 'Note 2', 'completed'),
(3, 4, 3000, '2024-03-01 12:00:00', 7000, 8000, 9000, 10000, 11000, 12000, 13000, 'Note 3', 'pending'),
(4, 5, 4000, '2024-04-01 12:00:00', 8000, 9000, 10000, 11000, 12000, 13000, 14000, 'Note 4', 'completed'),
(5, 1, 5000, '2024-05-01 12:00:00', 9000, 10000, 11000, 12000, 13000, 14000, 15000, 'Note 5', 'pending');





