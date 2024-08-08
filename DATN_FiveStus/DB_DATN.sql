CREATE DATABASE DuAnTotNghiep;

USE DuAnTotNghiep;

-- Table: khach_hang
CREATE TABLE khach_hang (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ma_khach_hang VARCHAR(50) NOT NULL,
  mat_khau VARCHAR(50) NULL,
  ho_va_ten VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  gioi_tinh BOOLEAN NOT NULL,
  so_dien_thoai VARCHAR(50) NOT NULL,
  trang_thai VARCHAR(50) NOT NULL,
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit
);
-- Table: dia_chi_khach_hang
CREATE TABLE dia_chi_khach_hang (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_khach_hang INT NOT NULL,
  dia_chi_cu_the VARCHAR(255) DEFAULT '',
	thanh_pho VARCHAR(100) DEFAULT '',
  quan_huyen VARCHAR(100) DEFAULT '',
  phuong_xa VARCHAR(100) DEFAULT '',
  ghi_chu VARCHAR(100) DEFAULT '',
  trang_thai VARCHAR(100) DEFAULT '',
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at BIT,
  CONSTRAINT fk_diaChiKhachHang_khachHang FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id)

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

DELIMITER //

CREATE TRIGGER before_insert_phieu_giam_gia
BEFORE INSERT ON phieu_giam_gia
FOR EACH ROW
BEGIN
  
  -- Cập nhật trạng thái dựa trên ngày bắt đầu
  IF DATE(NEW.ngay_bat_dau) > CURDATE() THEN
    SET NEW.trang_thai = 'Sắp diễn ra';
  ELSEIF DATE(NEW.ngay_ket_thuc) < CURDATE() THEN
    SET NEW.trang_thai = 'Đã kết thúc';
  ELSE
    SET NEW.trang_thai = 'Đang diễn ra';
  END IF;
  
  -- Đặt deleted_at là false
  SET NEW.deleted_at = 0;

  -- Cập nhật thời gian tạo và cập nhật
  SET NEW.created_at = NOW();
  SET NEW.updated_at = NOW();
END //

DELIMITER ;



-- Table: phieu_giam_gia_chi_tiet
CREATE TABLE phieu_giam_gia_chi_tiet (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_khach_hang int  null,
  id_phieu_giam_gia int  null,
  trang_thai bit,
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit DEFAULT 0,
    CONSTRAINT fk_phieuGiamGiaChiTiet_khachHang FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id),
    CONSTRAINT fk_phieuGiamGiaChiTiet_phieuGiamGia FOREIGN KEY (id_phieu_giam_gia) REFERENCES phieu_giam_gia(id)
);

CREATE TRIGGER before_insert_phieu_giam_gia_chi_tiet
BEFORE INSERT ON phieu_giam_gia_chi_tiet
FOR EACH ROW
  
  -- Đặt deleted_at là false
  SET NEW.deleted_at = 0;

END //

DELIMITER ;

-- Table: hoa_don
CREATE TABLE hoa_don (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_nhan_vien INT NOT NULL,
  id_phieu_giam_gia INT NOT NULL,
  id_khach_hang INT NOT NULL,
  ma_hoa_don VARCHAR(100) NOT NULL,
  ngay_tao DATETIME,
  loai BIT,
  tong_tien FLOAT,
  tien_coc FLOAT,
  tien_con_lai FLOAT,
  tien_thua FLOAT,
  ghi_chu VARCHAR(100) DEFAULT '',
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
  ten_san_bong VARCHAR(100) ,
  gia_san_bong float,
  id_loai_san int not null,
  CONSTRAINT fk_sanBong_loaiSan FOREIGN KEY (id_loai_san) REFERENCES loai_san(id),
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit
);
-- giaSanCa=giaCa++giaSanBong
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
  deleted_at bit default 1
);

-- Table: san_ca
CREATE TABLE san_ca (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_ca INT ,
  id_san_bong INT ,
  id_ngay_trong_tuan INT  ,
  gia Float ,
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
  ghi_chu VARCHAR(100) DEFAULT '',
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
  so_luong INT,
  ten_do_thue VARCHAR(255),
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit default 0,
  image_data LONGBLOB
);


-- Table: nuoc_uong
CREATE TABLE nuoc_uong (
  id INT AUTO_INCREMENT PRIMARY KEY,
  don_gia DECIMAL(10, 2),
  so_luong INT,
  ten_nuoc_uong VARCHAR(255),
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit default 0,
  image_data LONGBLOB
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
  ('KH001', 'password1', 'Nguyen Van A', 'lyly1062004@gmail.com', true, '0123456789', 'active', NOW(), NOW(), 1),
  ('KH002', 'password2', 'Nguyen Van B', 'nguyenvanb@gmail.com', false, '0987654321', 'active', NOW(), NOW(), 1),
  ('KH003', 'password3', 'Nguyen Van C', 'nguyenvanc@gmail.com', true, '0123456789', 'inactive', NOW(), NOW(), 1),
  ('KH004', 'password4', 'Nguyen Van D', 'nguyenvand@gmail.com', false, '0987654321', 'active', NOW(), NOW(), 0),
  ('KH005', 'password5', 'Nguyen Van E', 'nguyenvane@gmail.com', true, '0123456789', 'active', NOW(), NOW(), 0);


INSERT INTO dia_chi_khach_hang (id_khach_hang, dia_chi_cu_the, thanh_pho, quan_huyen, phuong_xa, ghi_chu, trang_thai, created_at, updated_at, deleted_at) 
VALUES
(1, 'Số 10, Đường Nguyễn Du, Phường 1', 'Hồ Chí Minh', 'Quận 1', 'Phường Bến Nghé', 'Ghi chú 1', 'Hoạt động', '2024-07-05 10:00:00', '2024-07-05 10:00:00', 0),
(2, 'Số 20, Đường Lê Lợi, Phường 2', 'Hà Nội', 'Quận Hoàn Kiếm', 'Phường Cửa Nam', 'Ghi chú 2', 'Hoạt động', '2024-07-05 11:00:00', '2024-07-05 11:00:00', 0);

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
  

INSERT INTO phieu_giam_gia (ma_phieu_giam_gia, ten_phieu_giam_gia, so_luong, muc_giam, hinh_thuc_giam_gia, dieu_kien_su_dung, gia_tri_toi_da, doi_tuong_ap_dung, ngay_bat_dau, ngay_ket_thuc, ghi_chu, created_at, updated_at)
VALUES
('PGG001', 'Phiếu giảm giá 1', 10, 10.0, 0, 100.0, 500.0, 1, '2024-07-01', '2024-07-31', 'Ghi chú 1', NOW(), NOW()),
('PGG002', 'Phiếu giảm giá 2', 20, 50.0, 1, 200.0, 1000.0, 0, '2024-08-01', '2024-08-31', 'Ghi chú 2', NOW(), NOW()),
('PGG003', 'Phiếu giảm giá 3', 30, 20.0, 0, 150.0, 750.0, 1, '2024-06-01', '2024-06-30', 'Ghi chú 3', NOW(), NOW()),
('PGG004', 'Phiếu giảm giá 4', 15, 5.0, 1, 50.0, 300.0, 0, '2024-07-05', '2024-07-25', 'Ghi chú 4', NOW(), NOW()),
('PGG005', 'Phiếu giảm giá 5', 25, 30.0, 0, 250.0, 1200.0, 1, '2024-08-05', '2024-08-20', 'Ghi chú 5', NOW(), NOW()),
('PGG006', 'Phiếu giảm giá 6', 50, 15.0, 1, 300.0, 1000.0, 0, '2024-06-05', '2024-06-25', 'Ghi chú 6', NOW(), NOW()),
('PGG007', 'Phiếu giảm giá 7', 40, 25.0, 0, 350.0, 1500.0, 1, '2024-07-10', '2024-07-20', 'Ghi chú 7', NOW(), NOW()),
('PGG008', 'Phiếu giảm giá 8', 35, 40.0, 1, 400.0, 1800.0, 0, '2024-08-10', '2024-08-30', 'Ghi chú 8', NOW(), NOW()),
('PGG009', 'Phiếu giảm giá 9', 60, 10.0, 0, 100.0, 500.0, 1, '2024-06-10', '2024-06-30', 'Ghi chú 9', NOW(), NOW()),
('PGG010', 'Phiếu giảm giá 10', 20, 35.0, 1, 450.0, 2000.0, 0, '2024-07-15', '2024-07-31', 'Ghi chú 10', NOW(), NOW()),
('PGG011', 'Phiếu giảm giá 11', 15, 12.0, 0, 120.0, 600.0, 1, '2024-08-15', '2024-08-31', 'Ghi chú 11', NOW(), NOW()),
('PGG012', 'Phiếu giảm giá 12', 55, 28.0, 1, 280.0, 1400.0, 0, '2024-06-15', '2024-06-30', 'Ghi chú 12', NOW(), NOW()),
('PGG013', 'Phiếu giảm giá 13', 45, 22.0, 0, 220.0, 1100.0, 1, '2024-07-20', '2024-07-31', 'Ghi chú 13', NOW(), NOW()),
('PGG014', 'Phiếu giảm giá 14', 30, 18.0, 1, 180.0, 900.0, 0, '2024-08-20', '2024-08-31', 'Ghi chú 14', NOW(), NOW()),
('PGG015', 'Phiếu giảm giá 15', 70, 24.0, 0, 240.0, 1200.0, 1, '2024-06-20', '2024-06-30', 'Ghi chú 15', NOW(), NOW()),
('PGG016', 'Phiếu giảm giá 16', 25, 26.0, 1, 260.0, 1300.0, 0, '2024-07-25', '2024-07-31', 'Ghi chú 16', NOW(), NOW()),
('PGG017', 'Phiếu giảm giá 17', 35, 32.0, 0, 320.0, 1600.0, 1, '2024-08-25', '2024-08-31', 'Ghi chú 17', NOW(), NOW()),
('PGG018', 'Phiếu giảm giá 18', 60, 14.0, 1, 140.0, 700.0, 0, '2024-06-25', '2024-06-30', 'Ghi chú 18', NOW(), NOW()),
('PGG019', 'Phiếu giảm giá 19', 50, 8.0, 0, 80.0, 400.0, 1, '2024-07-28', '2024-07-31', 'Ghi chú 19', NOW(), NOW()),
('PGG020', 'Phiếu giảm giá 20', 65, 27.0, 1, 270.0, 1350.0, 0, '2024-08-28', '2024-08-31', 'Ghi chú 20', NOW(), NOW());



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




INSERT INTO hoa_don (id_nhan_vien, id_phieu_giam_gia, id_khach_hang, ma_hoa_don, ngay_tao, loai, tong_tien, tien_coc, tien_con_lai, tien_thua, ghi_chu, trang_thai, ngay_den_san, ngay_thanh_toan, created_at, updated_at, deleted_at)
VALUES
  (2, 1, 3, 'HD006', DATE_SUB(NOW(), INTERVAL 3 DAY), 0, 600000, 300000, 300000, 0, 'Ghi chu 6', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), NOW(), NOW(), 0),
  (5, 3, 1, 'HD007', DATE_SUB(NOW(), INTERVAL 5 DAY), 1, 700000, 350000, 350000, 0, 'Ghi chu 7', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), NOW(), NOW(), 1),
  (4, 2, 5, 'HD008', DATE_SUB(NOW(), INTERVAL 7 DAY), 0, 800000, 400000, 400000, 0, 'Ghi chu 8', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), NOW(), NOW(), 0),
  (1, 4, 2, 'HD009', DATE_SUB(NOW(), INTERVAL 9 DAY), 1, 900000, 450000, 450000, 0, 'Ghi chu 9', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY), NOW(), NOW(), 1),
  (3, 5, 4, 'HD010', DATE_SUB(NOW(), INTERVAL 11 DAY), 1, 1000000, 500000, 500000, 0, 'Ghi chu 10', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 11 DAY), DATE_SUB(NOW(), INTERVAL 11 DAY), NOW(), NOW(), 0),
  (4, 3, 2, 'HD011', DATE_SUB(NOW(), INTERVAL 13 DAY), 0, 1100000, 550000, 550000, 0, 'Ghi chu 11', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 13 DAY), DATE_SUB(NOW(), INTERVAL 13 DAY), NOW(), NOW(), 1),
  (2, 5, 1, 'HD012', DATE_SUB(NOW(), INTERVAL 15 DAY), 0, 1200000, 600000, 600000, 0, 'Ghi chu 12', 'hủy', DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), NOW(), NOW(), 0),
  (5, 2, 4, 'HD013', DATE_SUB(NOW(), INTERVAL 17 DAY), 0, 1300000, 650000, 650000, 0, 'Ghi chu 13', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 17 DAY), DATE_SUB(NOW(), INTERVAL 17 DAY), NOW(), NOW(), 1),
  (1, 3, 5, 'HD014', DATE_SUB(NOW(), INTERVAL 19 DAY), 1, 1400000, 700000, 700000, 0, 'Ghi chu 14', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 19 DAY), DATE_SUB(NOW(), INTERVAL 19 DAY), NOW(), NOW(), 0),
  (3, 4, 1, 'HD015', DATE_SUB(NOW(), INTERVAL 21 DAY), 1, 1500000, 750000, 750000, 0, 'Ghi chu 15', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 21 DAY), DATE_SUB(NOW(), INTERVAL 21 DAY), NOW(), NOW(), 1),
  (2, 4, 3, 'HD016', DATE_SUB(NOW(), INTERVAL 23 DAY), 0, 1600000, 800000, 800000, 0, 'Ghi chu 16', 'hủy', DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 23 DAY), NOW(), NOW(), 0),
  (4, 1, 5, 'HD017', DATE_SUB(NOW(), INTERVAL 25 DAY), 1, 1700000, 850000, 850000, 0, 'Ghi chu 17', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY), NOW(), NOW(), 1),
  (5, 2, 1, 'HD018', DATE_SUB(NOW(), INTERVAL 27 DAY), 0, 1800000, 900000, 900000, 0, 'Ghi chu 18', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 27 DAY), DATE_SUB(NOW(), INTERVAL 27 DAY), NOW(), NOW(), 0),
  (3, 5, 2, 'HD019', DATE_SUB(NOW(), INTERVAL 29 DAY), 0, 1900000, 950000, 950000, 0, 'Ghi chu 19', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 29 DAY), DATE_SUB(NOW(), INTERVAL 29 DAY), NOW(), NOW(), 1),
  (1, 4, 3, 'HD020', DATE_SUB(NOW(), INTERVAL 31 DAY), 1, 2000000, 1000000, 1000000, 0, 'Ghi chu 20', 'hủy', DATE_SUB(NOW(), INTERVAL 31 DAY), DATE_SUB(NOW(), INTERVAL 31 DAY), NOW(), NOW(), 0),
  (2, 3, 5, 'HD021', DATE_SUB(NOW(), INTERVAL 33 DAY), 0, 2100000, 1050000, 1050000, 0, 'Ghi chu 21', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 33 DAY), DATE_SUB(NOW(), INTERVAL 33 DAY), NOW(), NOW(), 1),
  (4, 5, 1, 'HD022', DATE_SUB(NOW(), INTERVAL 35 DAY), 1, 2200000, 1100000, 1100000, 0, 'Ghi chu 22', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 35 DAY), DATE_SUB(NOW(), INTERVAL 35 DAY), NOW(), NOW(), 0),
  (5, 1, 3, 'HD023', DATE_SUB(NOW(), INTERVAL 37 DAY), 0, 2300000, 1150000, 1150000, 0, 'Ghi chu 23', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 37 DAY), DATE_SUB(NOW(), INTERVAL 37 DAY), NOW(), NOW(), 1),
  (3, 2, 4, 'HD024', DATE_SUB(NOW(), INTERVAL 39 DAY), 0, 2400000, 1200000, 1200000, 0, 'Ghi chu 24', 'hủy', DATE_SUB(NOW(), INTERVAL 39 DAY), DATE_SUB(NOW(), INTERVAL 39 DAY), NOW(), NOW(), 0),
  (1, 5, 2, 'HD025', DATE_SUB(NOW(), INTERVAL 41 DAY), 0, 2500000, 1250000, 1250000, 0, 'Ghi chu 25', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 41 DAY), DATE_SUB(NOW(), INTERVAL 41 DAY), NOW(), NOW(), 1),
  (1, 1, 2, 'HD026', DATE_SUB(NOW(), INTERVAL 43 DAY), 0, 2600000, 1300000, 1300000, 0, 'Ghi chu 26', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 43 DAY), DATE_SUB(NOW(), INTERVAL 43 DAY), NOW(), NOW(), 0),
  (2, 2, 3, 'HD027', DATE_SUB(NOW(), INTERVAL 45 DAY), 1, 2700000, 1350000, 1350000, 0, 'Ghi chu 27', 'hủy', DATE_SUB(NOW(), INTERVAL 45 DAY), DATE_SUB(NOW(), INTERVAL 45 DAY), NOW(), NOW(), 1),
  (3, 3, 4, 'HD028', DATE_SUB(NOW(), INTERVAL 47 DAY), 0, 2800000, 1400000, 1400000, 0, 'Ghi chu 28', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 47 DAY), DATE_SUB(NOW(), INTERVAL 47 DAY), NOW(), NOW(), 0),
  (4, 4, 5, 'HD029', DATE_SUB(NOW(), INTERVAL 49 DAY), 1, 2900000, 1450000, 1450000, 0, 'Ghi chu 29', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 49 DAY), DATE_SUB(NOW(), INTERVAL 49 DAY), NOW(), NOW(), 1),
  (5, 5, 1, 'HD030', DATE_SUB(NOW(), INTERVAL 51 DAY), 0, 3000000, 1500000, 1500000, 0, 'Ghi chu 30', 'hủy', DATE_SUB(NOW(), INTERVAL 51 DAY), DATE_SUB(NOW(), INTERVAL 51 DAY), NOW(), NOW(), 0),
  (1, 6, 2, 'HD031', DATE_SUB(NOW(), INTERVAL 53 DAY), 1, 3100000, 1550000, 1550000, 0, 'Ghi chu 31', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 53 DAY), DATE_SUB(NOW(), INTERVAL 53 DAY), NOW(), NOW(), 1),
  (2, 7, 3, 'HD032', DATE_SUB(NOW(), INTERVAL 55 DAY), 0, 3200000, 1600000, 1600000, 0, 'Ghi chu 32', 'hủy', DATE_SUB(NOW(), INTERVAL 55 DAY), DATE_SUB(NOW(), INTERVAL 55 DAY), NOW(), NOW(), 0),
  (3, 8, 4, 'HD033', DATE_SUB(NOW(), INTERVAL 57 DAY), 1, 3300000, 1650000, 1650000, 0, 'Ghi chu 33', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 57 DAY), DATE_SUB(NOW(), INTERVAL 57 DAY), NOW(), NOW(), 1),
  (4, 9, 5, 'HD034', DATE_SUB(NOW(), INTERVAL 59 DAY), 0, 3400000, 1700000, 1700000, 0, 'Ghi chu 34', 'hủy', DATE_SUB(NOW(), INTERVAL 59 DAY), DATE_SUB(NOW(), INTERVAL 59 DAY), NOW(), NOW(), 0),
  (5, 10, 1, 'HD035', DATE_SUB(NOW(), INTERVAL 61 DAY), 1, 3500000, 1750000, 1750000, 0, 'Ghi chu 35', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 61 DAY), DATE_SUB(NOW(), INTERVAL 61 DAY), NOW(), NOW(), 1),
  (1, 11, 2, 'HD036', DATE_SUB(NOW(), INTERVAL 63 DAY), 0, 3600000, 1800000, 1800000, 0, 'Ghi chu 36', 'hủy', DATE_SUB(NOW(), INTERVAL 63 DAY), DATE_SUB(NOW(), INTERVAL 63 DAY), NOW(), NOW(), 0),
  (2, 12, 3, 'HD037', DATE_SUB(NOW(), INTERVAL 65 DAY), 1, 3700000, 1850000, 1850000, 0, 'Ghi chu 37', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 65 DAY), DATE_SUB(NOW(), INTERVAL 65 DAY), NOW(), NOW(), 1),
  (3, 13, 4, 'HD038', DATE_SUB(NOW(), INTERVAL 67 DAY), 0, 3800000, 1900000, 1900000, 0, 'Ghi chu 38', 'hủy', DATE_SUB(NOW(), INTERVAL 67 DAY), DATE_SUB(NOW(), INTERVAL 67 DAY), NOW(), NOW(), 0),
  (4, 14, 5, 'HD039', DATE_SUB(NOW(), INTERVAL 69 DAY), 1, 3900000, 1950000, 1950000, 0, 'Ghi chu 39', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 69 DAY), DATE_SUB(NOW(), INTERVAL 69 DAY), NOW(), NOW(), 1),
  (5, 15, 1, 'HD040', DATE_SUB(NOW(), INTERVAL 71 DAY), 0, 4000000, 2000000, 2000000, 0, 'Ghi chu 40', 'hủy', DATE_SUB(NOW(), INTERVAL 71 DAY), DATE_SUB(NOW(), INTERVAL 71 DAY), NOW(), NOW(), 0),
  (1, 16, 2, 'HD041', DATE_SUB(NOW(), INTERVAL 73 DAY), 1, 4100000, 2050000, 2050000, 0, 'Ghi chu 41', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 73 DAY), DATE_SUB(NOW(), INTERVAL 73 DAY), NOW(), NOW(), 1),
  (2, 17, 3, 'HD042', DATE_SUB(NOW(), INTERVAL 75 DAY), 0, 4200000, 2100000, 2100000, 0, 'Ghi chu 42', 'hủy', DATE_SUB(NOW(), INTERVAL 75 DAY), DATE_SUB(NOW(), INTERVAL 75 DAY), NOW(), NOW(), 0),
  (3, 18, 4, 'HD043', DATE_SUB(NOW(), INTERVAL 77 DAY), 1, 4300000, 2150000, 2150000, 0, 'Ghi chu 43', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 77 DAY), DATE_SUB(NOW(), INTERVAL 77 DAY), NOW(), NOW(), 1),
  (4, 19, 5, 'HD044', DATE_SUB(NOW(), INTERVAL 79 DAY), 0, 4400000, 2200000, 2200000, 0, 'Ghi chu 44', 'hủy', DATE_SUB(NOW(), INTERVAL 79 DAY), DATE_SUB(NOW(), INTERVAL 79 DAY), NOW(), NOW(), 0),
  (5, 20, 1, 'HD045', DATE_SUB(NOW(), INTERVAL 81 DAY), 1, 4500000, 2250000, 2250000, 0, 'Ghi chu 45', 'đã thanh toán', DATE_SUB(NOW(), INTERVAL 81 DAY), DATE_SUB(NOW(), INTERVAL 81 DAY), NOW(), NOW(), 1),
  (1, 1, 2, 'HD046', DATE_SUB(NOW(), INTERVAL 83 DAY), 0, 4600000, 2300000, 2300000, 0, 'Ghi chu 46', 'hủy', DATE_SUB(NOW(), INTERVAL 83 DAY), DATE_SUB(NOW(), INTERVAL 83 DAY), NOW(), NOW(), 0);

INSERT INTO loai_san (ten_loai_san, trang_thai, created_at, updated_at, deleted_at) VALUES
('Sân 5', 'Hoạt động', Now(), Now(), 1),
('Sân 7', 'Hoạt động', Now(),Now(), 1),
('Sân 11', 'Hoạt động', Now(), Now(), 1);



INSERT INTO san_bong (ten_san_bong, gia_san_bong, id_loai_san, trang_thai, created_at, updated_at, deleted_at)
VALUES
  ('San bong 1', 11000, 1, 'Hoạt động', NOW(), NOW(), 1),
  ('San bong 2', 22000, 1, 'Hoạt động', NOW(), NOW(), 1),
  ('San bong 3', 33000, 1, 'Không hoạt động', NOW(), NOW(), 1),
  ('San bong 4', 44000, 1, 'Hoạt động', NOW(), NOW(), 1),
  ('San bong 5', 55000, 1, 'Hoạt động', NOW(), NOW(), 1),
  ('San bong 6', 66000, 2, 'Hoạt động', NOW(), NOW(), 1),
  ('San bong 7', 77000, 2, 'Không hoạt động', NOW(), NOW(), 1),
  ('San bong 8', 88000, 2, 'Hoạt động', NOW(), NOW(), 1),
  ('San bong 9', 99000, 2, 'Hoạt động', NOW(), NOW(), 1),
  ('San bong 10', 101000, 3, 'Không hoạt động', NOW(), NOW(), 1),
  ('San bong 11', 112000, 3, 'Hoạt động', NOW(), NOW(), 1),
  ('San bong 12', 123000, 3, 'Không hoạt động', NOW(), NOW(), 1);


  


  
INSERT INTO ca (ten_ca, gia_ca, thoi_gian_bat_dau, thoi_gian_ket_thuc, trang_thai, created_at, updated_at, deleted_at)
VALUES
  ('Ca 1 (06:00 - 08:00)', 100000, '2024-07-10 06:00:00', '2024-07-10 08:00:00', 'Hoạt động', NOW(), NOW(), 1),
  ('Ca 2 (08:00 - 10:00)', 200000, '2024-07-10 08:00:00', '2024-07-10 10:00:00', 'Không hoạt động', NOW(), NOW(), 1),
  ('Ca 3 (10:00 - 12:00)', 150000, '2024-07-10 10:00:00', '2024-07-10 12:00:00', 'Hoạt động', NOW(), NOW(), 1),
  ('Ca 4 (12:00 - 14:00)', 180000, '2024-07-10 12:00:00', '2024-07-10 14:00:00', 'Không hoạt động', NOW(), NOW(), 1),
  ('Ca 5 (14:00 - 16:00)', 160000, '2024-07-10 14:00:00', '2024-07-10 16:00:00', 'Hoạt động', NOW(), NOW(), 1),
  ('Ca 6 (16:00 - 18:00)', 220000, '2024-07-10 16:00:00', '2024-07-10 18:00:00', 'Không hoạt động', NOW(), NOW(), 1),
  ('Ca 7 (18:00 - 20:00)', 250000, '2024-07-10 18:00:00', '2024-07-10 20:00:00', 'Hoạt động', NOW(), NOW(), 1),
  ('Ca 8 (20:00 - 22:00)', 200000, '2024-07-10 20:00:00', '2024-07-10 22:00:00', 'Không hoạt động', NOW(), NOW(), 1);


INSERT INTO ngay_trong_tuan (thu_trong_tuan, trang_thai, created_at, updated_at)
VALUES
  ('Thứ 2', 'Hoạt động', NOW(), NOW()),
  ('Thứ 3', 'Hoạt động', NOW(), NOW()),
  ('Thứ 4', 'Hoạt động', NOW(), NOW()),
  ('Thứ 5', 'Hoạt động', NOW(), NOW()),
  ('Thứ 6', 'Hoạt động', NOW(), NOW()),
  ('Thứ 7', 'Hoạt động', NOW(), NOW()),
  ('Chủ Nhật', 'Hoạt động', NOW(), NOW());


INSERT INTO san_ca (id_ca, id_san_bong, id_ngay_trong_tuan, gia, trang_thai, created_at, updated_at, deleted_at)
VALUES
  (1, 1, 1, 11000, 'Hoạt động', NOW(), NOW(), 1),
  (2, 1, 2, 12000, 'Hoạt động', NOW(), NOW(), 1),
  (3, 1, 3, 13000, 'Hoạt động', NOW(), NOW(), 1),
  (3, 1, 1, 21000, 'Hoạt động', NOW(), NOW(), 1),
  (2, 1, 2, 22000, 'Hoạt động', NOW(), NOW(), 1),
  (3, 2, 3, 23000, 'Hoạt động', NOW(), NOW(), 1),
  (1, 2, 1, 31000, 'Hoạt động', NOW(), NOW(), 1),
  (2, 2, 2, 32000, 'Hoạt động', NOW(), NOW(), 1),
  (3, 2, 3, 33000, 'Hoạt động', NOW(), NOW(), 1),
  (1, 2, 1, 41000, 'Hoạt động', NOW(), NOW(), 1),
  (2, 3, 2, 42000, 'Hoạt động', NOW(), NOW(), 1),
  (3, 3, 3, 43000, 'Hoạt động', NOW(), NOW(), 1);


INSERT INTO hoa_don_chi_tiet (id_hoa_don, id_san_ca, tien_san, ghi_chu, trang_thai, created_at, updated_at, deleted_at)
VALUES
  (1, 2, 150000, 'Ghi chu 2', 'Đã thanh toán', NOW(), NOW(), 0),
  (1, 3, 200000, 'Ghi chu 3', 'Đã thanh toán', NOW(), NOW(), 0),
  (1, 4, 250000, 'Ghi chu 4', 'Đã thanh toán', NOW(), NOW(), 0),
  (2, 3, 180000, 'Ghi chu 5', 'Đã thanh toán', NOW(), NOW(), 0),
  (2, 5, 220000, 'Ghi chu 6', 'Đã thanh toán', NOW(), NOW(), 0),
  (3, 1, 120000, 'Ghi chu 7', 'Đã thanh toán', NOW(), NOW(), 0),
  (3, 4, 150000, 'Ghi chu 8', 'Đã thanh toán', NOW(), NOW(), 0),
  (3, 5, 180000, 'Ghi chu 9', 'Đã thanh toán', NOW(), NOW(), 0),
  (4, 2, 200000, 'Ghi chu 10', 'Đã thanh toán', NOW(), NOW(), 0),
  (4, 3, 250000, 'Ghi chu 11', 'Đã thanh toán', NOW(), NOW(), 0),
  (5, 1, 180000, 'Ghi chu 12', 'Đã thanh toán', NOW(), NOW(), 0),
  (5, 4, 220000, 'Ghi chu 13', 'Đã thanh toán', NOW(), NOW(), 0),
  (5, 5, 250000, 'Ghi chu 14', 'Đã thanh toán', NOW(), NOW(), 0),
  (6, 3, 150000, 'Ghi chu 15', 'Đã thanh toán', NOW(), NOW(), 0),
  (7, 2, 120000, 'Ghi chu 16', 'Đã thanh toán', NOW(), NOW(), 0),
  (7, 4, 180000, 'Ghi chu 17', 'Đã thanh toán', NOW(), NOW(), 0),
  (7, 5, 200000, 'Ghi chu 18', 'Đã thanh toán', NOW(), NOW(), 0),
  (8, 1, 250000, 'Ghi chu 19', 'Đã thanh toán', NOW(), NOW(), 0),
  (9, 2, 180000, 'Ghi chu 20', 'Đã thanh toán', NOW(), NOW(), 0),
  (9, 3, 220000, 'Ghi chu 21', 'Đã thanh toán', NOW(), NOW(), 0),
  (10, 1, 250000, 'Ghi chu 22', 'Đã thanh toán', NOW(), NOW(), 0),
  (10, 4, 150000, 'Ghi chu 23', 'Đã thanh toán', NOW(), NOW(), 0),
  (11, 3, 200000, 'Ghi chu 24', 'Đã thanh toán', NOW(), NOW(), 0),
  (12, 1, 120000, 'Ghi chu 25', 'Đã thanh toán', NOW(), NOW(), 0),
  (12, 2, 180000, 'Ghi chu 26', 'Đã thanh toán', NOW(), NOW(), 0),
  (12, 5, 220000, 'Ghi chu 27', 'Đã thanh toán', NOW(), NOW(), 0),
  (13, 3, 250000, 'Ghi chu 28', 'Đã thanh toán', NOW(), NOW(), 0),
  (14, 1, 180000, 'Ghi chu 29', 'Đã thanh toán', NOW(), NOW(), 0),
  (14, 4, 200000, 'Ghi chu 30', 'Đã thanh toán', NOW(), NOW(), 0),
  (15, 2, 150000, 'Ghi chu 31', 'Đã thanh toán', NOW(), NOW(), 0),
  (15, 3, 180000, 'Ghi chu 32', 'Đã thanh toán', NOW(), NOW(), 0),
  (15, 4, 200000, 'Ghi chu 33', 'Đã thanh toán', NOW(), NOW(), 0),
  (16, 1, 120000, 'Ghi chu 34', 'Đã thanh toán', NOW(), NOW(), 0),
  (17, 2, 250000, 'Ghi chu 35', 'Đã thanh toán', NOW(), NOW(), 0),
  (17, 3, 180000, 'Ghi chu 36', 'Đã thanh toán', NOW(), NOW(), 0),
  (18, 1, 220000, 'Ghi chu 37', 'Đã thanh toán', NOW(), NOW(), 0),
  (19, 2, 150000, 'Ghi chu 38', 'Đã thanh toán', NOW(), NOW(), 0),
  (19, 3, 200000, 'Ghi chu 39', 'Đã thanh toán', NOW(), NOW(), 0),
  (19, 4, 250000, 'Ghi chu 40', 'Đã thanh toán', NOW(), NOW(), 0),
  (20, 1, 120000, 'Ghi chu 41', 'Đã thanh toán', NOW(), NOW(), 0),
  (20, 2, 180000, 'Ghi chu 42', 'Đã thanh toán', NOW(), NOW(), 0),
  (20, 3, 220000, 'Ghi chu 43', 'Đã thanh toán', NOW(), NOW(), 0),
  (21, 4, 200000, 'Ghi chu 44', 'Đã thanh toán', NOW(), NOW(), 0),
  (21, 5, 180000, 'Ghi chu 45', 'Đã thanh toán', NOW(), NOW(), 0),
  (22, 1, 250000, 'Ghi chu 46', 'Đã thanh toán', NOW(), NOW(), 0),
  (22, 2, 220000, 'Ghi chu 47', 'Đã thanh toán', NOW(), NOW(), 0),
  (23, 3, 150000, 'Ghi chu 48', 'Đã thanh toán', NOW(), NOW(), 0),
  (23, 4, 180000, 'Ghi chu 49', 'Đã thanh toán', NOW(), NOW(), 0),
  (24, 5, 200000, 'Ghi chu 50', 'Đã thanh toán', NOW(), NOW(), 0),
  (24, 1, 220000, 'Ghi chu 51', 'Đã thanh toán', NOW(), NOW(), 0),
  (25, 2, 180000, 'Ghi chu 52', 'Đã thanh toán', NOW(), NOW(), 0),
  (25, 3, 150000, 'Ghi chu 53', 'Đã thanh toán', NOW(), NOW(), 0),
  (26, 4, 200000, 'Ghi chu 54', 'Đã thanh toán', NOW(), NOW(), 0),
  (26, 5, 250000, 'Ghi chu 55', 'Đã thanh toán', NOW(), NOW(), 0),
  (27, 1, 150000, 'Ghi chu 56', 'Đã thanh toán', NOW(), NOW(), 0),
  (27, 2, 200000, 'Ghi chu 57', 'Đã thanh toán', NOW(), NOW(), 0),
  (28, 3, 220000, 'Ghi chu 58', 'Đã thanh toán', NOW(), NOW(), 0),
  (28, 4, 250000, 'Ghi chu 59', 'Đã thanh toán', NOW(), NOW(), 0),
  (29, 5, 180000, 'Ghi chu 60', 'Đã thanh toán', NOW(), NOW(), 0),
  (29, 1, 200000, 'Ghi chu 61', 'Đã thanh toán', NOW(), NOW(), 0),
  (30, 2, 220000, 'Ghi chu 62', 'Đã thanh toán', NOW(), NOW(), 0),
  (30, 3, 250000, 'Ghi chu 63', 'Đã thanh toán', NOW(), NOW(), 0),
  (31, 4, 150000, 'Ghi chu 64', 'Đã thanh toán', NOW(), NOW(), 0),
  (31, 5, 200000, 'Ghi chu 65', 'Đã thanh toán', NOW(), NOW(), 0),
  (32, 1, 220000, 'Ghi chu 66', 'Đã thanh toán', NOW(), NOW(), 0),
  (32, 2, 250000, 'Ghi chu 67', 'Đã thanh toán', NOW(), NOW(), 0),
  (33, 3, 180000, 'Ghi chu 68', 'Đã thanh toán', NOW(), NOW(), 0),
  (33, 4, 200000, 'Ghi chu 69', 'Đã thanh toán', NOW(), NOW(), 0),
  (34, 5, 220000, 'Ghi chu 70', 'Đã thanh toán', NOW(), NOW(), 0);


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

-- Chèn dữ liệu vào bảng do_thue
INSERT INTO do_thue (don_gia, so_luong, ten_do_thue, trang_thai, created_at, updated_at, deleted_at, image_data)
VALUES
  (100000, 10, 'Do thue 1', 'Còn', NOW(), NOW(), 0, NULL),
  (200000, 20, 'Do thue 2', 'Còn', NOW(), NOW(), 0, NULL),
  (300000, 0, 'Do thue 3', 'Hết', NOW(), NOW(), 0, NULL),
  (400000, 40, 'Do thue 4', 'Còn', NOW(), NOW(), 0, NULL),
  (500000, 50, 'Do thue 5', 'Còn', NOW(), NOW(), 0, NULL),
  (600000, 0, 'Do thue 6', 'Hết', NOW(), NOW(), 0, NULL),
  (700000, 70, 'Do thue 7', 'Còn', NOW(), NOW(), 0, NULL),
  (800000, 80, 'Do thue 8', 'Còn', NOW(), NOW(), 0, NULL),
  (900000, 0, 'Do thue 9', 'Hết', NOW(), NOW(), 0, NULL),
  (1000000, 0, 'Do thue 10', 'Hết', NOW(), NOW(), 0, NULL),
  (1100000, 110, 'Do thue 11', 'Còn', NOW(), NOW(), 0, NULL),
  (1200000, 120, 'Do thue 12', 'Còn', NOW(), NOW(), 0, NULL),
  (1300000, 0, 'Do thue 13', 'Hết', NOW(), NOW(), 0, NULL),
  (1400000, 140, 'Do thue 14', 'Còn', NOW(), NOW(), 0, NULL),
  (1500000, 150, 'Do thue 15', 'Còn', NOW(), NOW(), 0, NULL),
  (1600000, 0, 'Do thue 16', 'Hết', NOW(), NOW(), 0, NULL),
  (1700000, 170, 'Do thue 17', 'Còn', NOW(), NOW(), 0, NULL),
  (1800000, 0, 'Do thue 18', 'Hết', NOW(), NOW(), 0, NULL),
  (1900000, 190, 'Do thue 19', 'Còn', NOW(), NOW(), 0, NULL),
  (2000000, 200, 'Do thue 20', 'Còn', NOW(), NOW(), 0, NULL);






INSERT INTO nuoc_uong (don_gia, so_luong, ten_nuoc_uong, trang_thai, created_at, updated_at, image_data)
VALUES
(50.00, 20, 'Nước Uống A', 'Còn', NOW(), NOW(), NULL),
(55.00, 5, 'Nước Uống B', 'Còn', NOW(), NOW(), NULL),
(60.00, 0, 'Nước Uống C', 'Hết', NOW(), NOW(), NULL),
(65.00, 10, 'Nước Uống D', 'Còn', NOW(), NOW(), NULL),
(70.00, 3, 'Nước Uống E', 'Còn', NOW(), NOW(), NULL),
(75.00, 0, 'Nước Uống F', 'Hết', NOW(), NOW(), NULL),
(80.00, 25, 'Nước Uống G', 'Còn', NOW(), NOW(), NULL),
(85.00, 0, 'Nước Uống H', 'Hết', NOW(), NOW(), NULL),
(90.00, 7, 'Nước Uống I', 'Còn', NOW(), NOW(), NULL),
(95.00, 15, 'Nước Uống J', 'Còn', NOW(), NOW(), NULL),
(100.00, 2, 'Nước Uống K', 'Còn', NOW(), NOW(), NULL),
(105.00, 0, 'Nước Uống L', 'Hết', NOW(), NOW(), NULL),
(110.00, 8, 'Nước Uống M', 'Còn', NOW(), NOW(), NULL),
(115.00, 0, 'Nước Uống N', 'Hết', NOW(), NOW(), NULL),
(120.00, 4, 'Nước Uống O', 'Còn', NOW(), NOW(), NULL),
(125.00, 0, 'Nước Uống P', 'Hết', NOW(), NOW(), NULL),
(130.00, 6, 'Nước Uống Q', 'Còn', NOW(), NOW(), NULL),
(135.00, 0, 'Nước Uống R', 'Hết', NOW(), NOW(), NULL),
(140.00, 9, 'Nước Uống S', 'Còn', NOW(), NOW(), NULL),
(145.00, 0, 'Nước Uống T', 'Hết', NOW(), NOW(), NULL),
(150.00, 11, 'Nước Uống U', 'Còn', NOW(), NOW(), NULL),
(155.00, 0, 'Nước Uống V', 'Hết', NOW(), NOW(), NULL);



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