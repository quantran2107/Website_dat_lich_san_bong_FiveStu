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
  id_nhan_vien INT  NULL,
  id_phieu_giam_gia INT  NULL,
  id_khach_hang INT  NULL,
  ma_hoa_don VARCHAR(100) NOT NULL,
  ngay_tao DATETIME,
  loai BIT,
  tong_tien FLOAT,
  tien_coc FLOAT,
  tien_con_lai FLOAT,
  tien_thua FLOAT,
  ghi_chu VARCHAR(100) DEFAULT '',
  trang_thai VARCHAR(100) NOT NULL,
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
  id_hoa_don INT NULL,
  id_san_ca INT NULL,
  ma_hoa_don_chi_tiet VARCHAR(100)  NULL,
  tien_san DECIMAL(10, 2) NULL,
  ngay_den_san date null,
  ghi_chu VARCHAR(100) DEFAULT '' null,
  trang_thai VARCHAR(50) null,
  kieu_ngay_dat VARCHAR(50) null,
  created_at DATETIME null, 
  updated_at DATETIME null,
  deleted_at bit DEFAULT 0 ,
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
  ma VARCHAR(255) NOT NULL,
  ten VARCHAR(255) NOT NULL,
  gia_tri VARCHAR(255) NOT NULL,
  type_gia_tri VARCHAR(255) NOT NULL,
  mo_ta TEXT,
  trang_thai BOOLEAN NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at BIT(1) DEFAULT 1
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
-- Table: lich_su_checkin
CREATE TABLE lich_su_checkin (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_hoa_don_chi_tiet INT NOT NULL,
  thoi_gian_checkin DATETIME NOT NULL,
  mo_ta VARCHAR(255) ,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP, 
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted_at BIT DEFAULT 0,
  CONSTRAINT fk_lichSuCheckin_hoaDonChiTiet FOREIGN KEY (id_hoa_don_chi_tiet ) REFERENCES hoa_don_chi_tiet(id)
);



-- INserttt 


INSERT INTO khach_hang (ma_khach_hang, mat_khau, ho_va_ten, email, gioi_tinh, so_dien_thoai, trang_thai, created_at, updated_at, deleted_at)
VALUES
  ('KH001', 'password1', 'Nguyen Van A', 'lyly1062004@gmail.com', true, '0987456321', 'active', NOW(), NOW(), 1),
  ('KH002', 'password2', 'Nguyen Van B', 'nguyenvanb@gmail.com', false, '0963258741', 'active', NOW(), NOW(), 1),
  ('KH003', 'password3', 'Nguyen Van C', 'nguyenvanc@gmail.com', true, '0321456987', 'inactive', NOW(), NOW(), 1),
  ('KH004', 'password4', 'Nguyen Van D', 'nguyenvand@gmail.com', false, '0369852147', 'active', NOW(), NOW(), 0),
  ('KH005', 'password5', 'Nguyen Van E', 'nguyenvane@gmail.com', true, '0147852369', 'active', NOW(), NOW(), 0);


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


INSERT INTO hoa_don (id_nhan_vien, id_phieu_giam_gia, id_khach_hang, ma_hoa_don, ngay_tao, loai, tong_tien, tien_coc, tien_con_lai, tien_thua, ghi_chu, trang_thai, ngay_thanh_toan, created_at, updated_at, deleted_at)
VALUES
(1, 1, 1, 'HD001', NOW(), 1, 500000, 0, 500000, 0, 'Ghi chú HD001', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(1, 2, 2, 'HD002', NOW() + INTERVAL 1 DAY, 1, 600000, 0, 600000, 0, 'Ghi chú HD002', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(2, 3, 3, 'HD003', NOW() + INTERVAL 2 DAY, 0, 550000, 0, 550000, 0, 'Ghi chú HD003', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(2, 4, 4, 'HD004', NOW() + INTERVAL 3 DAY, 0, 650000, 0, 650000, 0, 'Ghi chú HD004', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(3, 5, 1, 'HD005', NOW() + INTERVAL 4 DAY, 1, 700000, 0, 700000, 0, 'Ghi chú HD005', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(3, 1, 2, 'HD006', NOW() + INTERVAL 5 DAY, 1, 750000, 0, 750000, 0, 'Ghi chú HD006', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(4, 2, 3, 'HD007', NOW() + INTERVAL 6 DAY, 0, 800000, 0, 800000, 0, 'Ghi chú HD007', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(4, 3, 4, 'HD008', NOW() + INTERVAL 7 DAY, 0, 850000, 0, 850000, 0, 'Ghi chú HD008', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(5, 4, 5, 'HD009', NOW() + INTERVAL 8 DAY, 1, 900000, 0, 900000, 0, 'Ghi chú HD009', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(5, 5, 1, 'HD010', NOW() + INTERVAL 9 DAY, 1, 950000, 0, 950000, 0, 'Ghi chú HD010', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(1, 1, 2, 'HD011', NOW() + INTERVAL 10 DAY, 1, 500000, 0, 500000, 0, 'Ghi chú HD011', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(1, 2, 3, 'HD012', NOW() + INTERVAL 11 DAY, 1, 600000, 0, 600000, 0, 'Ghi chú HD012', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(2, 3, 4, 'HD013', NOW() + INTERVAL 12 DAY, 0, 550000, 0, 550000, 0, 'Ghi chú HD013', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(2, 4, 5, 'HD014', NOW() + INTERVAL 13 DAY, 0, 650000, 0, 650000, 0, 'Ghi chú HD014', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(3, 5, 1, 'HD015', NOW() + INTERVAL 14 DAY, 1, 700000, 0, 700000, 0, 'Ghi chú HD015', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(3, 1, 2, 'HD016', NOW() + INTERVAL 15 DAY, 1, 750000, 0, 750000, 0, 'Ghi chú HD016', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(4, 2, 3, 'HD017', NOW() + INTERVAL 16 DAY, 0, 800000, 0, 800000, 0, 'Ghi chú HD017', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(4, 3, 4, 'HD018', NOW() + INTERVAL 17 DAY, 0, 850000, 0, 850000, 0, 'Ghi chú HD018', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(5, 4, 5, 'HD019', NOW() + INTERVAL 18 DAY, 1, 900000, 0, 900000, 0, 'Ghi chú HD019', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(5, 5, 1, 'HD020', NOW() + INTERVAL 19 DAY, 1, 950000, 0, 950000, 0, 'Ghi chú HD020', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(1, 1, 1, 'HD021', NOW(), 1, 500000, 500000, 0, 0, 'Ghi chú HD021', 'Đã thanh toán', '2024-08-01', NOW(), NOW(), 0),
(1, 2, 2, 'HD022', NOW() + INTERVAL 1 DAY, 1, 600000, 600000, 0, 0, 'Ghi chú HD022', 'Đã thanh toán', '2024-08-02', NOW(), NOW(), 0),
(2, 3, 3, 'HD023', NOW() + INTERVAL 2 DAY, 0, 550000, 550000, 0, 0, 'Ghi chú HD023', 'Đã thanh toán', '2024-08-03', NOW(), NOW(), 0),
(2, 4, 4, 'HD024', NOW() + INTERVAL 3 DAY, 0, 650000, 650000, 0, 0, 'Ghi chú HD024', 'Đã thanh toán', '2024-08-04', NOW(), NOW(), 0),
(3, 5, 1, 'HD025', NOW() + INTERVAL 4 DAY, 1, 700000, 700000, 0, 0, 'Ghi chú HD025', 'Đã thanh toán', '2024-08-05', NOW(), NOW(), 0),
(3, 1, 2, 'HD026', NOW() + INTERVAL 5 DAY, 1, 750000, 750000, 0, 0, 'Ghi chú HD026', 'Đã thanh toán', '2024-08-06', NOW(), NOW(), 0),
(4, 2, 3, 'HD027', NOW() + INTERVAL 6 DAY, 0, 800000, 800000, 0, 0, 'Ghi chú HD027', 'Đã thanh toán', '2024-08-07', NOW(), NOW(), 0),
(4, 3, 4, 'HD028', NOW() + INTERVAL 7 DAY, 0, 850000, 850000, 0, 0, 'Ghi chú HD028', 'Đã thanh toán', '2024-08-08', NOW(), NOW(), 0),
(5, 4, 5, 'HD029', NOW() + INTERVAL 8 DAY, 1, 900000, 900000, 0, 0, 'Ghi chú HD029', 'Đã thanh toán', '2024-08-09', NOW(), NOW(), 0),
(5, 5, 1, 'HD030', NOW() + INTERVAL 9 DAY, 1, 950000, 950000, 0, 0, 'Ghi chú HD030', 'Đã thanh toán', '2024-08-10', NOW(), NOW(), 0),
(1, 1, 2, 'HD031', NOW() + INTERVAL 10 DAY, 1, 500000, 500000, 0, 0, 'Ghi chú HD031', 'Đã thanh toán', '2024-08-11', NOW(), NOW(), 0),
(1, 2, 3, 'HD032', NOW() + INTERVAL 11 DAY, 1, 600000, 600000, 0, 0, 'Ghi chú HD032', 'Đã thanh toán', '2024-08-12', NOW(), NOW(), 0),
(2, 3, 4, 'HD033', NOW() + INTERVAL 12 DAY, 0, 550000, 550000, 0, 0, 'Ghi chú HD033', 'Đã thanh toán', '2024-08-13', NOW(), NOW(), 0),
(2, 4, 5, 'HD034', NOW() + INTERVAL 13 DAY, 0, 650000, 650000, 0, 0, 'Ghi chú HD034', 'Đã thanh toán', '2024-08-14', NOW(), NOW(), 0),
(3, 5, 1, 'HD035', NOW() + INTERVAL 14 DAY, 1, 700000, 700000, 0, 0, 'Ghi chú HD035', 'Đã thanh toán', '2024-08-15', NOW(), NOW(), 0),
(3, 1, 2, 'HD036', NOW() + INTERVAL 15 DAY, 1, 750000, 750000, 0, 0, 'Ghi chú HD036', 'Đã thanh toán', '2024-08-16', NOW(), NOW(), 0),
(4, 2, 3, 'HD037', NOW() + INTERVAL 16 DAY, 0, 800000, 800000, 0, 0, 'Ghi chú HD037', 'Đã thanh toán', '2024-08-17', NOW(), NOW(), 0),
(4, 3, 4, 'HD038', NOW() + INTERVAL 17 DAY, 0, 850000, 850000, 0, 0, 'Ghi chú HD038', 'Đã thanh toán', '2024-08-18', NOW(), NOW(), 0),
(5, 4, 5, 'HD039', NOW() + INTERVAL 18 DAY, 1, 900000, 900000, 0, 0, 'Ghi chú HD039', 'Đã thanh toán', '2024-08-19', NOW(), NOW(), 0),
(5, 5, 1, 'HD040', NOW() + INTERVAL 19 DAY, 1, 950000, 950000, 0, 0, 'Ghi chú HD040', 'Đã thanh toán', '2024-08-20', NOW(), NOW(), 0),
(1, 1, 1, 'HD041', NOW(), 1, 520000, 0, 520000, 0, 'Ghi chú HD041', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(1, 2, 2, 'HD042', NOW() + INTERVAL 1 DAY, 1, 630000, 0, 630000, 0, 'Ghi chú HD042', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(2, 3, 3, 'HD043', NOW() + INTERVAL 2 DAY, 0, 570000, 0, 570000, 0, 'Ghi chú HD043', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(2, 4, 4, 'HD044', NOW() + INTERVAL 3 DAY, 0, 670000, 0, 670000, 0, 'Ghi chú HD044', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(3, 5, 1, 'HD045', NOW() + INTERVAL 4 DAY, 1, 720000, 0, 720000, 0, 'Ghi chú HD045', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(3, 1, 2, 'HD046', NOW() + INTERVAL 5 DAY, 1, 770000, 0, 770000, 0, 'Ghi chú HD046', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(4, 2, 3, 'HD047', NOW() + INTERVAL 6 DAY, 0, 820000, 0, 820000, 0, 'Ghi chú HD047', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(4, 3, 4, 'HD048', NOW() + INTERVAL 7 DAY, 0, 870000, 0, 870000, 0, 'Ghi chú HD048', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(5, 4, 5, 'HD049', NOW() + INTERVAL 8 DAY, 1, 920000, 0, 920000, 0, 'Ghi chú HD049', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(5, 5, 1, 'HD050', NOW() + INTERVAL 9 DAY, 1, 970000, 0, 970000, 0, 'Ghi chú HD050', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(1, 1, 2, 'HD051', NOW() + INTERVAL 10 DAY, 1, 520000, 520000, 0, 0, 'Ghi chú HD051', 'Đã thanh toán', '2024-08-21', NOW(), NOW(), 0),
(1, 2, 3, 'HD052', NOW() + INTERVAL 11 DAY, 1, 630000, 630000, 0, 0, 'Ghi chú HD052', 'Đã thanh toán', '2024-08-22', NOW(), NOW(), 0),
(2, 3, 4, 'HD053', NOW() + INTERVAL 12 DAY, 0, 570000, 570000, 0, 0, 'Ghi chú HD053', 'Đã thanh toán', '2024-08-23', NOW(), NOW(), 0),
(2, 4, 5, 'HD054', NOW() + INTERVAL 13 DAY, 0, 670000, 670000, 0, 0, 'Ghi chú HD054', 'Đã thanh toán', '2024-08-24', NOW(), NOW(), 0),
(3, 5, 1, 'HD055', NOW() + INTERVAL 14 DAY, 1, 720000, 720000, 0, 0, 'Ghi chú HD055', 'Đã thanh toán', '2024-08-25', NOW(), NOW(), 0),
(3, 1, 2, 'HD056', NOW() + INTERVAL 15 DAY, 1, 770000, 770000, 0, 0, 'Ghi chú HD056', 'Đã thanh toán', '2024-08-26', NOW(), NOW(), 0),
(4, 2, 3, 'HD057', NOW() + INTERVAL 16 DAY, 0, 820000, 820000, 0, 0, 'Ghi chú HD057', 'Đã thanh toán', '2024-08-27', NOW(), NOW(), 0),
(4, 3, 4, 'HD058', NOW() + INTERVAL 17 DAY, 0, 870000, 870000, 0, 0, 'Ghi chú HD058', 'Đã thanh toán', '2024-08-28', NOW(), NOW(), 0),
(5, 4, 5, 'HD059', NOW() + INTERVAL 18 DAY, 1, 920000, 920000, 0, 0, 'Ghi chú HD059', 'Đã thanh toán', '2024-08-29', NOW(), NOW(), 0),
(5, 5, 1, 'HD060', NOW() + INTERVAL 19 DAY, 1, 970000, 970000, 0, 0, 'Ghi chú HD060', 'Đã thanh toán', '2024-08-30', NOW(), NOW(), 0),
(1, 1, 1, 'HD041', NOW() + INTERVAL 20 DAY, 1, 500000, 500000, 0, 0, 'Ghi chú HD041', 'Đã thanh toán', '2024-08-21', NOW(), NOW(), 0),
(1, 2, 3, 'HD042', NOW() + INTERVAL 21 DAY, 0, 510000, 0, 510000, 0, 'Ghi chú HD042', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(2, 3, 4, 'HD043', NOW() + INTERVAL 22 DAY, 1, 620000, 0, 620000, 0, 'Ghi chú HD043', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(3, 4, 5, 'HD044', NOW() + INTERVAL 23 DAY, 0, 630000, 0, 630000, 0, 'Ghi chú HD044', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(4, 5, 1, 'HD045', NOW() + INTERVAL 24 DAY, 1, 640000, 0, 640000, 0, 'Ghi chú HD045', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(5, 1, 2, 'HD046', NOW() + INTERVAL 25 DAY, 0, 650000, 0, 650000, 0, 'Ghi chú HD046', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(1, 2, 3, 'HD047', NOW() + INTERVAL 26 DAY, 1, 660000, 0, 660000, 0, 'Ghi chú HD047', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(2, 3, 4, 'HD048', NOW() + INTERVAL 27 DAY, 0, 670000, 0, 670000, 0, 'Ghi chú HD048', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(3, 4, 5, 'HD049', NOW() + INTERVAL 28 DAY, 1, 680000, 0, 680000, 0, 'Ghi chú HD049', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(4, 5, 1, 'HD050', NOW() + INTERVAL 29 DAY, 0, 690000, 0, 690000, 0, 'Ghi chú HD050', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(5, 1, 2, 'HD051', NOW() + INTERVAL 30 DAY, 1, 700000, 0, 700000, 0, 'Ghi chú HD051', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(1, 3, 4, 'HD052', NOW() + INTERVAL 31 DAY, 0, 710000, 0, 710000, 0, 'Ghi chú HD052', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(2, 4, 5, 'HD053', NOW() + INTERVAL 32 DAY, 1, 720000, 0, 720000, 0, 'Ghi chú HD053', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(3, 5, 1, 'HD054', NOW() + INTERVAL 33 DAY, 0, 730000, 0, 730000, 0, 'Ghi chú HD054', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(4, 1, 2, 'HD055', NOW() + INTERVAL 34 DAY, 1, 740000, 0, 740000, 0, 'Ghi chú HD055', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(5, 2, 3, 'HD056', NOW() + INTERVAL 35 DAY, 0, 750000, 0, 750000, 0, 'Ghi chú HD056', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(1, 4, 5, 'HD057', NOW() + INTERVAL 36 DAY, 1, 760000, 0, 760000, 0, 'Ghi chú HD057', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(2, 5, 1, 'HD058', NOW() + INTERVAL 37 DAY, 0, 770000, 0, 770000, 0, 'Ghi chú HD058', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(3, 1, 2, 'HD059', NOW() + INTERVAL 38 DAY, 1, 780000, 0, 780000, 0, 'Ghi chú HD059', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(4, 2, 3, 'HD060', NOW() + INTERVAL 39 DAY, 0, 790000, 0, 790000, 0, 'Ghi chú HD060', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(5, 3, 4, 'HD061', NOW() + INTERVAL 40 DAY, 1, 800000, 0, 800000, 0, 'Ghi chú HD061', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(1, 5, 1, 'HD062', NOW() + INTERVAL 41 DAY, 0, 810000, 0, 810000, 0, 'Ghi chú HD062', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(2, 1, 2, 'HD063', NOW() + INTERVAL 42 DAY, 1, 820000, 0, 820000, 0, 'Ghi chú HD063', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(3, 2, 3, 'HD064', NOW() + INTERVAL 43 DAY, 0, 830000, 0, 830000, 0, 'Ghi chú HD064', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(4, 3, 4, 'HD065', NOW() + INTERVAL 44 DAY, 1, 840000, 0, 840000, 0, 'Ghi chú HD065', 'Chờ thanh toán', NULL, NOW(), NOW(), 0),
(5, 4, 5, 'HD066', NOW() + INTERVAL 45 DAY, 0, 850000, 0, 850000, 0, 'Ghi chú HD066', 'Chờ thanh toán', NULL, NOW(), NOW(), 0);



  
INSERT INTO loai_san (ten_loai_san, trang_thai, created_at, updated_at, deleted_at) VALUES
('Sân 5', 'Hoạt động', Now(), Now(), 0),
('Sân 7', 'Hoạt động', Now(),Now(), 0),
('Sân 11', 'Hoạt động', Now(), Now(), 0);



INSERT INTO san_bong (ten_san_bong, gia_san_bong, id_loai_san, trang_thai, created_at, updated_at, deleted_at)
VALUES
  ('San bong 13', 11500, 1, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 14', 22500, 1, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 15', 33500, 1, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 16', 44500, 1, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 17', 55500, 1, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 18', 66500, 2, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 19', 77500, 2, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 20', 88500, 2, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 21', 99500, 2, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 22', 105000, 3, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 23', 116000, 3, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 24', 127000, 3, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 25', 138000, 3, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 26', 149000, 1, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 27', 160000, 1, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 28', 171000, 1, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 29', 182000, 1, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 30', 193000, 1, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 31', 204000, 2, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 32', 215000, 2, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 33', 226000, 2, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 34', 237000, 2, 'Hoạt động', NOW(), NOW(), 0),
  ('San bong 35', 248000, 3, 'Hoạt động', NOW(), NOW(), 0);


  
INSERT INTO ca (ten_ca, gia_ca, thoi_gian_bat_dau, thoi_gian_ket_thuc, trang_thai, created_at, updated_at, deleted_at)
VALUES
  ('Ca 1', 100000, '2024-08-18 06:00:00', '2024-08-18 08:00:00', 'Hoạt động', NOW(), NOW(), 0),
  ('Ca 2', 200000, '2024-08-18 08:00:00', '2024-08-18 10:00:00', 'Hoạt động', NOW(), NOW(), 0),
  ('Ca 3', 150000, '2024-08-18 10:00:00', '2024-08-18 12:00:00', 'Hoạt động', NOW(), NOW(), 0),
  ('Ca 4', 180000, '2024-08-18 12:00:00', '2024-08-18 14:00:00', 'Hoạt động', NOW(), NOW(), 0),
  ('Ca 5', 160000, '2024-08-18 14:00:00', '2024-08-18 16:00:00', 'Hoạt động', NOW(), NOW(), 0),
  ('Ca 6', 220000, '2024-08-18 16:00:00', '2024-08-18 18:00:00', 'Hoạt động', NOW(), NOW(), 0),
  ('Ca 7', 250000, '2024-08-18 18:00:00', '2024-08-18 20:00:00', 'Hoạt động', NOW(), NOW(), 0),
  ('Ca 8', 200000, '2024-08-18 20:00:00', '2024-08-18 22:00:00', 'Hoạt động', NOW(), NOW(), 0);




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
  (1, 1, 1, 110000, 'Còn trống', NOW(), NOW(), 0),
  (1, 1, 2, 120000, 'Còn trống', NOW(), NOW(), 0),
  (1, 1, 3, 130000, 'Còn trống', NOW(), NOW(), 0),
  (1, 1, 4, 140000, 'Còn trống', NOW(), NOW(), 0),
  (1, 1, 5, 150000, 'Còn trống', NOW(), NOW(), 0),
  (1, 1, 6, 160000, 'Còn trống', NOW(), NOW(), 0),
  (1, 1, 7, 170000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 2, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 2, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 2, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 2, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 2, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 2, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 2, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 3, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 3, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 3, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 3, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 3, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 3, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 3, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 4, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 4, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 4, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 4, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 4, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 4, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 4, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 5, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 5, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 5, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 5, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 5, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 5, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 5, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 6, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 6, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 6, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 6, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 6, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 6, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 6, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 7, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 7, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 7, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 7, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 7, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 7, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 7, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 8, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 8, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 8, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 8, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 8, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 8, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 8, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 9, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 9, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 9, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 9, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 9, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 9, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 9, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 10, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 10, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 10, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 10, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 10, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 10, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 10, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 11, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 11, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 11, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 11, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 11, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 11, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 11, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 12, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 12, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 12, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 12, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 12, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 12, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 12, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 13, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 13, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 13, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 13, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 13, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 13, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 13, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 14, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 14, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 14, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 14, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 14, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 14, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 14, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 15, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 15, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 15, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 15, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 15, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 15, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 15, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 16, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 16, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 16, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 16, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 16, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 16, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 16, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 17, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 17, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 17, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 17, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 17, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 17, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 17, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 18, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 18, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 18, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 18, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 18, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 18, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 18, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 19, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 19, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 19, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 19, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 19, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 19, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 19, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 20, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 20, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 20, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 20, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 20, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 20, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 20, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 21, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 21, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 21, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 21, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 21, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 21, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 21, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 22, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 22, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 22, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 22, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 22, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 22, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 22, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (1, 23, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (1, 23, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (1, 23, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 23, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 23, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 23, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (1, 23, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (2, 1, 1, 110000, 'Còn trống', NOW(), NOW(), 0),
  (2, 1, 2, 120000, 'Còn trống', NOW(), NOW(), 0),
  (2, 1, 3, 130000, 'Còn trống', NOW(), NOW(), 0),
  (2, 1, 4, 140000, 'Còn trống', NOW(), NOW(), 0),
  (2, 1, 5, 150000, 'Còn trống', NOW(), NOW(), 0),
  (2, 1, 6, 160000, 'Còn trống', NOW(), NOW(), 0),
  (2, 1, 7, 170000, 'Còn trống', NOW(), NOW(), 0),
  
  (2, 2, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 2, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 2, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 2, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 2, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 2, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 2, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (2, 3, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 3, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 3, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 3, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 3, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 3, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 3, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (2, 4, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 4, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 4, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 4, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 4, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 4, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 4, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (2, 5, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 5, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 5, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 5, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 5, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 5, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 5, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (2, 6, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 6, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 6, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 6, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 6, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 6, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 6, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (2, 7, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 7, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 7, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 7, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 7, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 7, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 7, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (2, 8, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 8, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 8, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 8, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 8, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 8, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 8, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (2, 9, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 9, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 9, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 9, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 9, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 9, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 9, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (2, 10, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 10, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 10, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 10, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 10, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 10, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 10, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (2, 11, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 11, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 11, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 11, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 11, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 11, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 11, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (2, 12, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 12, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 12, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 12, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 12, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 12, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 12, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (2, 13, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 13, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 13, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 13, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 13, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 13, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 13, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (2, 14, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 14, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 14, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 14, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 14, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 14, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 14, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (2, 15, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 15, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 15, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 15, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 15, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 15, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 15, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (2, 16, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 16, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 16, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 16, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 16, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 16, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 16, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (2, 17, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 17, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 17, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 17, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 17, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 17, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 17, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (2, 18, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 18, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 18, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 18, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 18, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 18, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 18, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (2, 19, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 19, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 19, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 19, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 19, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 19, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 19, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (2, 20, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 20, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 20, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 20, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 20, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 20, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 20, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (2, 21, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 21, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 21, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 21, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 21, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 21, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 21, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (2, 22, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 22, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 22, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 22, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 22, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 22, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 22, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (2, 23, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (2, 23, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (2, 23, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 23, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 23, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 23, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (2, 23, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (3, 1, 1, 110000, 'Còn trống', NOW(), NOW(), 0),
  (3, 1, 2, 120000, 'Còn trống', NOW(), NOW(), 0),
  (3, 1, 3, 130000, 'Còn trống', NOW(), NOW(), 0),
  (3, 1, 4, 140000, 'Còn trống', NOW(), NOW(), 0),
  (3, 1, 5, 150000, 'Còn trống', NOW(), NOW(), 0),
  (3, 1, 6, 160000, 'Còn trống', NOW(), NOW(), 0),
  (3, 1, 7, 170000, 'Còn trống', NOW(), NOW(), 0),
  
  (3, 2, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 2, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 2, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 2, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 2, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 2, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 2, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (3, 3, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 3, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 3, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 3, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 3, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 3, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 3, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (3, 4, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 4, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 4, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 4, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 4, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 4, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 4, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (3, 5, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 5, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 5, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 5, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 5, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 5, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 5, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (3, 6, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 6, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 6, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 6, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 6, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 6, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 6, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (3, 7, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 7, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 7, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 7, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 7, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 7, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 7, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (3, 8, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 8, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 8, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 8, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 8, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 8, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 8, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (3, 9, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 9, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 9, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 9, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 9, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 9, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 9, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (3, 10, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 10, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 10, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 10, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 10, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 10, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 10, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (3, 11, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 11, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 11, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 11, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 11, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 11, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 11, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (3, 12, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 12, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 12, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 12, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 12, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 12, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 12, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (3, 13, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 13, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 13, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 13, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 13, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 13, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 13, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (3, 14, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 14, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 14, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 14, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 14, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 14, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 14, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (3, 15, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 15, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 15, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 15, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 15, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 15, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 15, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (3, 16, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 16, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 16, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 16, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 16, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 16, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 16, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (3, 17, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 17, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 17, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 17, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 17, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 17, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 17, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (3, 18, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 18, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 18, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 18, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 18, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 18, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 18, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (3, 19, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 19, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 19, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 19, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 19, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 19, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 19, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (3, 20, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 20, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 20, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 20, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 20, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 20, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 20, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (3, 21, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 21, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 21, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 21, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 21, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 21, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 21, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (3, 22, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (3, 22, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (3, 22, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 22, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 22, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 22, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (3, 22, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (3, 23, 1, 180000, 'Hoạt động', NOW(), NOW(), 0),
  (3, 23, 2, 190000, 'Hoạt động', NOW(), NOW(), 0),
  (3, 23, 3, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (3, 23, 4, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (3, 23, 5, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (3, 23, 6, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (3, 23, 7, 200000, 'Hoạt động', NOW(), NOW(), 0),
  
  (4, 1, 1, 110000, 'Còn trống', NOW(), NOW(), 0),
  (4, 1, 2, 120000, 'Còn trống', NOW(), NOW(), 0),
  (4, 1, 3, 130000, 'Còn trống', NOW(), NOW(), 0),
  (4, 1, 4, 140000, 'Còn trống', NOW(), NOW(), 0),
  (4, 1, 5, 150000, 'Còn trống', NOW(), NOW(), 0),
  (4, 1, 6, 160000, 'Còn trống', NOW(), NOW(), 0),
  (4, 1, 7, 170000, 'Còn trống', NOW(), NOW(), 0),
  
  (4, 2, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 2, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 2, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 2, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 2, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 2, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 2, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (4, 3, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 3, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 3, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 3, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 3, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 3, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 3, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (4, 4, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 4, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 4, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 4, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 4, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 4, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 4, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (4, 5, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 5, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 5, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 5, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 5, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 5, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 5, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (4, 6, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 6, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 6, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 6, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 6, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 6, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 6, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (4, 7, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 7, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 7, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 7, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 7, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 7, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 7, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (4, 8, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 8, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 8, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 8, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 8, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 8, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 8, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (4, 9, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 9, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 9, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 9, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 9, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 9, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 9, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (4, 10, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 10, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 10, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 10, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 10, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 10, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 10, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (4, 11, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 11, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 11, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 11, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 11, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 11, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 11, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (4, 12, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 12, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 12, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 12, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 12, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 12, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 12, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (4, 13, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 13, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 13, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 13, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 13, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 13, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 13, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (4, 14, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 14, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 14, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 14, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 14, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 14, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 14, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (4, 15, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 15, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 15, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 15, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 15, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 15, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 15, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (4, 16, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 16, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 16, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 16, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 16, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 16, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 16, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (4, 17, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 17, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 17, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 17, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 17, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 17, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 17, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (4, 18, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 18, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 18, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 18, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 18, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 18, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 18, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (4, 19, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 19, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 19, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 19, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 19, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 19, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 19, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (4, 20, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 20, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 20, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 20, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 20, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 20, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 20, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (4, 21, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 21, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 21, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 21, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 21, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 21, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 21, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (4, 22, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (4, 22, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (4, 22, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 22, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 22, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 22, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (4, 22, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (4, 23, 1, 180000, 'Hoạt động', NOW(), NOW(), 0),
  (4, 23, 2, 190000, 'Hoạt động', NOW(), NOW(), 0),
  (4, 23, 3, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (4, 23, 4, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (4, 23, 5, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (4, 23, 6, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (4, 23, 7, 200000, 'Hoạt động', NOW(), NOW(), 0),
  
  (5, 1, 1, 110000, 'Còn trống', NOW(), NOW(), 0),
  (5, 1, 2, 120000, 'Còn trống', NOW(), NOW(), 0),
  (5, 1, 3, 130000, 'Còn trống', NOW(), NOW(), 0),
  (5, 1, 4, 140000, 'Còn trống', NOW(), NOW(), 0),
  (5, 1, 5, 150000, 'Còn trống', NOW(), NOW(), 0),
  (5, 1, 6, 160000, 'Còn trống', NOW(), NOW(), 0),
  (5, 1, 7, 170000, 'Còn trống', NOW(), NOW(), 0),
  
  (5, 2, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 2, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 2, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 2, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 2, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 2, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 2, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (5, 3, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 3, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 3, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 3, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 3, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 3, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 3, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (5, 4, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 4, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 4, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 4, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 4, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 4, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 4, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (5, 5, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 5, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 5, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 5, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 5, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 5, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 5, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (5, 6, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 6, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 6, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 6, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 6, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 6, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 6, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (5, 7, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 7, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 7, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 7, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 7, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 7, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 7, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (5, 8, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 8, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 8, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 8, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 8, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 8, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 8, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (5, 9, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 9, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 9, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 9, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 9, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 9, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 9, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (5, 10, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 10, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 10, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 10, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 10, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 10, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 10, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (5, 11, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 11, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 11, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 11, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 11, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 11, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 11, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (5, 12, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 12, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 12, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 12, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 12, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 12, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 12, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (5, 13, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 13, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 13, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 13, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 13, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 13, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 13, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (5, 14, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 14, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 14, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 14, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 14, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 14, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 14, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (5, 15, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 15, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 15, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 15, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 15, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 15, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 15, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (5, 16, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 16, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 16, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 16, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 16, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 16, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 16, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (5, 17, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 17, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 17, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 17, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 17, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 17, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 17, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (5, 18, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 18, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 18, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 18, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 18, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 18, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 18, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (5, 19, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 19, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 19, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 19, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 19, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 19, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 19, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (5, 20, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 20, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 20, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 20, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 20, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 20, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 20, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (5, 21, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 21, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 21, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 21, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 21, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 21, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 21, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (5, 22, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (5, 22, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (5, 22, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 22, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 22, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 22, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (5, 22, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (5, 23, 1, 180000, 'Hoạt động', NOW(), NOW(), 0),
  (5, 23, 2, 190000, 'Hoạt động', NOW(), NOW(), 0),
  (5, 23, 3, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (5, 23, 4, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (5, 23, 5, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (5, 23, 6, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (5, 23, 7, 200000, 'Hoạt động', NOW(), NOW(), 0),
  
  (6, 1, 1, 110000, 'Còn trống', NOW(), NOW(), 0),
  (6, 1, 2, 120000, 'Còn trống', NOW(), NOW(), 0),
  (6, 1, 3, 130000, 'Còn trống', NOW(), NOW(), 0),
  (6, 1, 4, 140000, 'Còn trống', NOW(), NOW(), 0),
  (6, 1, 5, 150000, 'Còn trống', NOW(), NOW(), 0),
  (6, 1, 6, 160000, 'Còn trống', NOW(), NOW(), 0),
  (6, 1, 7, 170000, 'Còn trống', NOW(), NOW(), 0),
  
  (6, 2, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 2, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 2, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 2, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 2, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 2, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 2, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (6, 3, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 3, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 3, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 3, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 3, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 3, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 3, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (6, 4, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 4, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 4, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 4, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 4, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 4, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 4, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (6, 5, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 5, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 5, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 5, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 5, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 5, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 5, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (6, 6, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 6, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 6, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 6, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 6, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 6, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 6, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (6, 7, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 7, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 7, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 7, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 7, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 7, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 7, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (6, 8, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 8, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 8, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 8, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 8, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 8, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 8, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (6, 9, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 9, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 9, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 9, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 9, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 9, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 9, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (6, 10, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 10, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 10, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 10, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 10, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 10, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 10, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (6, 11, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 11, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 11, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 11, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 11, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 11, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 11, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (6, 12, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 12, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 12, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 12, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 12, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 12, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 12, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (6, 13, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 13, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 13, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 13, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 13, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 13, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 13, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (6, 14, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 14, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 14, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 14, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 14, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 14, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 14, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (6, 15, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 15, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 15, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 15, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 15, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 15, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 15, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (6, 16, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 16, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 16, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 16, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 16, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 16, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 16, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (6, 17, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 17, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 17, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 17, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 17, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 17, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 17, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (6, 18, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 18, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 18, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 18, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 18, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 18, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 18, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (6, 19, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 19, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 19, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 19, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 19, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 19, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 19, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (6, 20, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 20, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 20, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 20, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 20, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 20, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 20, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (6, 21, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 21, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 21, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 21, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 21, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 21, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 21, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (6, 22, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (6, 22, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (6, 22, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 22, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 22, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 22, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (6, 22, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (6, 23, 1, 180000, 'Hoạt động', NOW(), NOW(), 0),
  (6, 23, 2, 190000, 'Hoạt động', NOW(), NOW(), 0),
  (6, 23, 3, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (6, 23, 4, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (6, 23, 5, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (6, 23, 6, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (6, 23, 7, 200000, 'Hoạt động', NOW(), NOW(), 0),
  
  (7, 1, 1, 110000, 'Còn trống', NOW(), NOW(), 0),
  (7, 1, 2, 120000, 'Còn trống', NOW(), NOW(), 0),
  (7, 1, 3, 130000, 'Còn trống', NOW(), NOW(), 0),
  (7, 1, 4, 140000, 'Còn trống', NOW(), NOW(), 0),
  (7, 1, 5, 150000, 'Còn trống', NOW(), NOW(), 0),
  (7, 1, 6, 160000, 'Còn trống', NOW(), NOW(), 0),
  (7, 1, 7, 170000, 'Còn trống', NOW(), NOW(), 0),
  
  (7, 2, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 2, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 2, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 2, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 2, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 2, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 2, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (7, 3, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 3, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 3, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 3, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 3, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 3, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 3, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (7, 4, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 4, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 4, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 4, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 4, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 4, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 4, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (7, 5, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 5, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 5, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 5, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 5, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 5, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 5, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (7, 6, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 6, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 6, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 6, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 6, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 6, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 6, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (7, 7, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 7, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 7, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 7, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 7, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 7, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 7, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (7, 8, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 8, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 8, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 8, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 8, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 8, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 8, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (7, 9, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 9, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 9, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 9, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 9, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 9, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 9, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (7, 10, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 10, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 10, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 10, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 10, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 10, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 10, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (7, 11, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 11, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 11, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 11, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 11, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 11, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 11, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (7, 12, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 12, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 12, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 12, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 12, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 12, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 12, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (7, 13, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 13, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 13, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 13, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 13, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 13, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 13, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (7, 14, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 14, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 14, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 14, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 14, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 14, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 14, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (7, 15, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 15, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 15, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 15, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 15, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 15, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 15, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (7, 16, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 16, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 16, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 16, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 16, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 16, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 16, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (7, 17, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 17, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 17, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 17, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 17, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 17, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 17, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (7, 18, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 18, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 18, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 18, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 18, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 18, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 18, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (7, 19, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 19, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 19, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 19, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 19, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 19, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 19, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (7, 20, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 20, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 20, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 20, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 20, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 20, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 20, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (7, 21, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 21, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 21, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 21, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 21, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 21, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 21, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (7, 22, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (7, 22, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (7, 22, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 22, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 22, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 22, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (7, 22, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (7, 23, 1, 180000, 'Hoạt động', NOW(), NOW(), 0),
  (7, 23, 2, 190000, 'Hoạt động', NOW(), NOW(), 0),
  (7, 23, 3, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (7, 23, 4, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (7, 23, 5, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (7, 23, 6, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (7, 23, 7, 200000, 'Hoạt động', NOW(), NOW(), 0),
  
  (8, 1, 1, 110000, 'Còn trống', NOW(), NOW(), 0),
  (8, 1, 2, 120000, 'Còn trống', NOW(), NOW(), 0),
  (8, 1, 3, 130000, 'Còn trống', NOW(), NOW(), 0),
  (8, 1, 4, 140000, 'Còn trống', NOW(), NOW(), 0),
  (8, 1, 5, 150000, 'Còn trống', NOW(), NOW(), 0),
  (8, 1, 6, 160000, 'Còn trống', NOW(), NOW(), 0),
  (8, 1, 7, 170000, 'Còn trống', NOW(), NOW(), 0),
  
  (8, 2, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 2, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 2, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 2, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 2, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 2, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 2, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (8, 3, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 3, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 3, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 3, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 3, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 3, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 3, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (8, 4, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 4, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 4, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 4, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 4, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 4, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 4, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (8, 5, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 5, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 5, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 5, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 5, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 5, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 5, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (8, 6, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 6, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 6, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 6, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 6, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 6, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 6, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (8, 7, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 7, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 7, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 7, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 7, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 7, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 7, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (8, 8, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 8, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 8, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 8, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 8, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 8, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 8, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (8, 9, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 9, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 9, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 9, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 9, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 9, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 9, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (8, 10, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 10, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 10, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 10, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 10, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 10, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 10, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (8, 11, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 11, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 11, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 11, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 11, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 11, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 11, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (8, 12, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 12, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 12, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 12, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 12, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 12, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 12, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (8, 13, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 13, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 13, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 13, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 13, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 13, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 13, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (8, 14, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 14, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 14, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 14, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 14, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 14, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 14, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (8, 15, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 15, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 15, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 15, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 15, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 15, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 15, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (8, 16, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 16, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 16, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 16, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 16, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 16, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 16, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (8, 17, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 17, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 17, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 17, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 17, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 17, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 17, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (8, 18, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 18, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 18, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 18, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 18, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 18, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 18, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (8, 19, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 19, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 19, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 19, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 19, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 19, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 19, 7, 200000, 'Còn trống', NOW(), NOW(), 0),

  (8, 20, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 20, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 20, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 20, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 20, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 20, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 20, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (8, 21, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 21, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 21, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 21, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 21, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 21, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 21, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (8, 22, 1, 180000, 'Còn trống', NOW(), NOW(), 0),
  (8, 22, 2, 190000, 'Còn trống', NOW(), NOW(), 0),
  (8, 22, 3, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 22, 4, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 22, 5, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 22, 6, 200000, 'Còn trống', NOW(), NOW(), 0),
  (8, 22, 7, 200000, 'Còn trống', NOW(), NOW(), 0),
  
  (8, 23, 1, 180000, 'Hoạt động', NOW(), NOW(), 0),
  (8, 23, 2, 190000, 'Hoạt động', NOW(), NOW(), 0),
  (8, 23, 3, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (8, 23, 4, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (8, 23, 5, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (8, 23, 6, 200000, 'Hoạt động', NOW(), NOW(), 0),
  (8, 23, 7, 200000, 'Hoạt động', NOW(), NOW(), 0);





INSERT INTO hoa_don_chi_tiet (id_hoa_don, id_san_ca, ma_hoa_don_chi_tiet, tien_san, ngay_den_san, ghi_chu, trang_thai, kieu_ngay_dat, created_at, updated_at, deleted_at)
VALUES
(61, 1, 'HDCT061-1', 500000, NOW() - INTERVAL 2 DAY, 'Ghi chú HD061-1', 'Chờ nhận sân', 'Theo ngày', NOW(), NOW(), 0),
(62, 2, 'HDCT062-1', 600000, NOW() - INTERVAL 1 DAY, 'Ghi chú HD062-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(63, 3, 'HDCT063-1', 550000, NOW(), 'Ghi chú HD063-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(64, 4, 'HDCT064-1', 650000, NOW() + INTERVAL 1 DAY, 'Ghi chú HD064-1', 'Chờ nhận sân', 'Theo ngày', NOW(), NOW(), 0),
(65, 5, 'HDCT065-1', 700000, NOW() + INTERVAL 2 DAY, 'Ghi chú HD065-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(66, 6, 'HDCT066-1', 750000, NOW() - INTERVAL 2 DAY, 'Ghi chú HD066-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(67, 7, 'HDCT067-1', 800000, NOW() - INTERVAL 1 DAY, 'Ghi chú HD067-1', 'Chờ nhận sân', 'Theo ngày', NOW(), NOW(), 0),
(68, 8, 'HDCT068-1', 850000, NOW(), 'Ghi chú HD068-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(69, 9, 'HDCT069-1', 900000, NOW() + INTERVAL 1 DAY, 'Ghi chú HD069-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(70, 10, 'HDCT070-1', 950000, NOW() + INTERVAL 2 DAY, 'Ghi chú HD070-1', 'Chờ nhận sân', 'Theo ngày', NOW(), NOW(), 0),
(71, 11, 'HDCT071-1', 500000, NOW() - INTERVAL 2 DAY, 'Ghi chú HD071-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(72, 12, 'HDCT072-1', 600000, NOW() - INTERVAL 1 DAY, 'Ghi chú HD072-1', 'Chờ nhận sân', 'Theo ngày', NOW(), NOW(), 0),
(73, 13, 'HDCT073-1', 550000, NOW(), 'Ghi chú HD073-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(74, 14, 'HDCT074-1', 650000, NOW() + INTERVAL 1 DAY, 'Ghi chú HD074-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(75, 15, 'HDCT075-1', 700000, NOW() + INTERVAL 2 DAY, 'Ghi chú HD075-1', 'Chờ nhận sân', 'Theo ngày', NOW(), NOW(), 0),
(76, 16, 'HDCT076-1', 750000, NOW() - INTERVAL 2 DAY, 'Ghi chú HD076-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(77, 17, 'HDCT077-1', 800000, NOW() - INTERVAL 1 DAY, 'Ghi chú HD077-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(78, 18, 'HDCT078-1', 850000, NOW(), 'Ghi chú HD078-1', 'Chờ nhận sân', 'Theo ngày', NOW(), NOW(), 0),
(79, 19, 'HDCT079-1', 900000, NOW() + INTERVAL 1 DAY, 'Ghi chú HD079-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(80, 20, 'HDCT080-1', 950000, NOW() + INTERVAL 2 DAY, 'Ghi chú HD080-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(21, 21, 'HDCT021-1', 500000, NOW() - INTERVAL 2 DAY, 'Ghi chú HD021-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(22, 22, 'HDCT022-1', 600000, NOW() - INTERVAL 1 DAY, 'Ghi chú HD022-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(23, 23, 'HDCT023-1', 550000, NOW(), 'Ghi chú HD023-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(24, 24, 'HDCT024-1', 650000, NOW() + INTERVAL 1 DAY, 'Ghi chú HD024-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(25, 25, 'HDCT025-1', 700000, NOW() + INTERVAL 2 DAY, 'Ghi chú HD025-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(26, 26, 'HDCT026-1', 750000, NOW() - INTERVAL 2 DAY, 'Ghi chú HD026-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(27, 27, 'HDCT027-1', 800000, NOW() - INTERVAL 1 DAY, 'Ghi chú HD027-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(28, 28, 'HDCT028-1', 850000, NOW(), 'Ghi chú HD028-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(29, 29, 'HDCT029-1', 900000, NOW() + INTERVAL 1 DAY, 'Ghi chú HD029-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(30, 30, 'HDCT030-1', 950000, NOW() + INTERVAL 2 DAY, 'Ghi chú HD030-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(31, 31, 'HDCT031-1', 500000, NOW() - INTERVAL 2 DAY, 'Ghi chú HD031-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(32, 32, 'HDCT032-1', 600000, NOW() - INTERVAL 1 DAY, 'Ghi chú HD032-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(33, 33, 'HDCT033-1', 550000, NOW(), 'Ghi chú HD033-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(34, 34, 'HDCT034-1', 650000, NOW() + INTERVAL 1 DAY, 'Ghi chú HD034-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(35, 35, 'HDCT035-1', 700000, NOW() + INTERVAL 2 DAY, 'Ghi chú HD035-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(36, 36, 'HDCT036-1', 750000, NOW() - INTERVAL 2 DAY, 'Ghi chú HD036-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(37, 37, 'HDCT037-1', 800000, NOW() - INTERVAL 1 DAY, 'Ghi chú HD037-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(38, 38, 'HDCT038-1', 850000, NOW(), 'Ghi chú HD038-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(39, 39, 'HDCT039-1', 900000, NOW() + INTERVAL 1 DAY, 'Ghi chú HD039-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(40, 40, 'HDCT040-1', 950000, NOW() + INTERVAL 2 DAY, 'Ghi chú HD040-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(41, 1, 'HDCT041-1', 500000, NOW() - INTERVAL 2 DAY, 'Ghi chú HD041-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(42, 2, 'HDCT042-1', 600000, NOW() - INTERVAL 1 DAY, 'Ghi chú HD042-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(43, 3, 'HDCT043-1', 550000, NOW(), 'Ghi chú HD043-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(44, 4, 'HDCT044-1', 650000, NOW() + INTERVAL 1 DAY, 'Ghi chú HD044-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(45, 5, 'HDCT045-1', 700000, NOW() + INTERVAL 2 DAY, 'Ghi chú HD045-1', 'Đã thanh toán', 'Theo tuần', NOW(), NOW(), 0),
(46, 6, 'HDCT046-1', 750000, NOW() - INTERVAL 2 DAY, 'Ghi chú HD046-1', 'Đã thanh toán', 'Nhiều ngày', NOW(), NOW(), 0),
(47, 7, 'HDCT047-1', 800000, NOW() - INTERVAL 1 DAY, 'Ghi chú HD047-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(48, 8, 'HDCT048-1', 850000, NOW(), 'Ghi chú HD048-1', 'Đã thanh toán', 'Theo tuần', NOW(), NOW(), 0),
(49, 9, 'HDCT049-1', 900000, NOW() + INTERVAL 1 DAY, 'Ghi chú HD049-1', 'Đã thanh toán', 'Nhiều ngày', NOW(), NOW(), 0),
(50, 10, 'HDCT050-1', 950000, NOW() + INTERVAL 2 DAY, 'Ghi chú HD050-1', 'Đã thanh toán', 'Theo ngày', NOW(), NOW(), 0),
(51, 1, 'HDCT051-1', 500000, NOW() - INTERVAL 1 DAY, 'Ghi chú HD051-1', 'Đã thanh toán', 'Theo tuần', NOW(), NOW(), 0),
(52, 2, 'HDCT052-1', 600000, NOW(), 'Ghi chú HD052-1', 'Chờ nhận sân', 'Nhiều ngày', NOW(), NOW(), 0),
(53, 3, 'HDCT053-1', 550000, NOW() + INTERVAL 1 DAY, 'Ghi chú HD053-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(54, 4, 'HDCT054-1', 650000, NOW() + INTERVAL 2 DAY, 'Ghi chú HD054-1', 'Chờ nhận sân', 'Theo tuần', NOW(), NOW(), 0),
(55, 5, 'HDCT055-1', 700000, NOW() - INTERVAL 2 DAY, 'Ghi chú HD055-1', 'Đang hoạt động', 'Nhiều ngày', NOW(), NOW(), 0),
(56, 6, 'HDCT056-1', 750000, NOW() - INTERVAL 1 DAY, 'Ghi chú HD056-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(57, 7, 'HDCT057-1', 800000, NOW(), 'Ghi chú HD057-1', 'Chờ nhận sân', 'Theo tuần', NOW(), NOW(), 0),
(58, 8, 'HDCT058-1', 850000, NOW() + INTERVAL 1 DAY, 'Ghi chú HD058-1', 'Chờ nhận sân', 'Nhiều ngày', NOW(), NOW(), 0),
(59, 9, 'HDCT059-1', 900000, NOW() + INTERVAL 2 DAY, 'Ghi chú HD059-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(60, 10, 'HDCT060-1', 950000, NOW() - INTERVAL 2 DAY, 'Ghi chú HD060-1', 'Đang hoạt động', 'Theo tuần', NOW(), NOW(), 0),
(61, 11, 'HDCT061-1', 500000, NOW() - INTERVAL 1 DAY, 'Ghi chú HD061-1', 'Chờ nhận sân', 'Nhiều ngày', NOW(), NOW(), 0),
(62, 12, 'HDCT062-1', 600000, NOW(), 'Ghi chú HD062-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(63, 13, 'HDCT063-1', 550000, NOW() + INTERVAL 1 DAY, 'Ghi chú HD063-1', 'Chờ nhận sân', 'Theo tuần', NOW(), NOW(), 0),
(64, 14, 'HDCT064-1', 650000, NOW() + INTERVAL 2 DAY, 'Ghi chú HD064-1', 'Chờ nhận sân', 'Nhiều ngày', NOW(), NOW(), 0),
(65, 15, 'HDCT065-1', 700000, NOW() - INTERVAL 2 DAY, 'Ghi chú HD065-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(66, 16, 'HDCT066-1', 750000, NOW() - INTERVAL 1 DAY, 'Ghi chú HD066-1', 'Chờ nhận sân', 'Theo tuần', NOW(), NOW(), 0),
(67, 17, 'HDCT067-1', 800000, NOW(), 'Ghi chú HD067-1', 'Đang hoạt động', 'Nhiều ngày', NOW(), NOW(), 0),
(68, 18, 'HDCT068-1', 850000, NOW() + INTERVAL 1 DAY, 'Ghi chú HD068-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(69, 19, 'HDCT069-1', 900000, NOW() + INTERVAL 2 DAY, 'Ghi chú HD069-1', 'Chờ nhận sân', 'Theo tuần', NOW(), NOW(), 0),
(70, 20, 'HDCT070-1', 950000, NOW() - INTERVAL 2 DAY, 'Ghi chú HD070-1', 'Chờ nhận sân', 'Nhiều ngày', NOW(), NOW(), 0),
(71, 21, 'HDCT071-1', 500000, NOW() - INTERVAL 1 DAY, 'Ghi chú HD071-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(72, 22, 'HDCT072-1', 600000, NOW(), 'Ghi chú HD072-1', 'Chờ nhận sân', 'Theo tuần', NOW(), NOW(), 0),
(73, 23, 'HDCT073-1', 550000, NOW() + INTERVAL 1 DAY, 'Ghi chú HD073-1', 'Đang hoạt động', 'Nhiều ngày', NOW(), NOW(), 0),
(74, 24, 'HDCT074-1', 650000, NOW() + INTERVAL 2 DAY, 'Ghi chú HD074-1', 'Chờ nhận sân', 'Theo ngày', NOW(), NOW(), 0),
(75, 25, 'HDCT075-1', 700000, NOW() - INTERVAL 2 DAY, 'Ghi chú HD075-1', 'Chờ nhận sân', 'Theo tuần', NOW(), NOW(), 0),
(76, 26, 'HDCT076-1', 750000, NOW() - INTERVAL 1 DAY, 'Ghi chú HD076-1', 'Đang hoạt động', 'Nhiều ngày', NOW(), NOW(), 0),
(77, 27, 'HDCT077-1', 800000, NOW(), 'Ghi chú HD077-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(78, 28, 'HDCT078-1', 850000, NOW() + INTERVAL 1 DAY, 'Ghi chú HD078-1', 'Chờ nhận sân', 'Theo tuần', NOW(), NOW(), 0),
(79, 29, 'HDCT079-1', 900000, NOW() + INTERVAL 2 DAY, 'Ghi chú HD079-1', 'Chờ nhận sân', 'Nhiều ngày', NOW(), NOW(), 0),
(8, 1, 'HDCT008-1', 500000, NOW() - INTERVAL 1 DAY, 'Ghi chú HD008-1', 'Chờ nhận sân', 'Theo ngày', NOW(), NOW(), 0),
(9, 2, 'HDCT009-1', 600000, NOW() + INTERVAL 1 DAY, 'Ghi chú HD009-1', 'Đang hoạt động', 'Theo ngày', NOW(), NOW(), 0),
(10, 3, 'HDCT010-1', 550000, NOW() + INTERVAL 2 DAY, 'Ghi chú HD010-1', 'Chờ nhận sân', 'Theo ngày', NOW(), NOW(), 0),

-- Theo tuần
(11, 4, 'HDCT011-1', 650000, NOW() - INTERVAL 1 WEEK, 'Ghi chú HD011-1', 'Đang hoạt động', 'Theo tuần', NOW(), NOW(), 0),
(12, 5, 'HDCT012-1', 700000, NOW() + INTERVAL 1 WEEK, 'Ghi chú HD012-1', 'Chờ nhận sân', 'Theo tuần', NOW(), NOW(), 0),
(13, 1, 'HDCT013-1', 750000, NOW() + INTERVAL 2 WEEK, 'Ghi chú HD013-1', 'Đang hoạt động', 'Theo tuần', NOW(), NOW(), 0),

-- Nhiều ngày
(14, 2, 'HDCT014-1', 800000, NOW() - INTERVAL 5 DAY, 'Ghi chú HD014-1', 'Chờ nhận sân', 'Nhiều ngày', NOW(), NOW(), 0),
(15, 3, 'HDCT015-1', 850000, NOW() + INTERVAL 5 DAY, 'Ghi chú HD015-1', 'Đang hoạt động', 'Nhiều ngày', NOW(), NOW(), 0),
(16, 4, 'HDCT016-1', 900000, NOW() + INTERVAL 10 DAY, 'Ghi chú HD016-1', 'Chờ nhận sân', 'Nhiều ngày', NOW(), NOW(), 0),
(17, 5, 'HDCT017-1', 950000, NOW() - INTERVAL 10 DAY, 'Ghi chú HD017-1', 'Đang hoạt động', 'Nhiều ngày', NOW(), NOW(), 0);







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


INSERT INTO giao_ca (id_nv_nhan_ca, id_nv_ban_giao, tien_phat_sinh, thoi_gian_ket_ca, tien_ban_dau, tong_tien_khac, tong_tien_mat, tong_tien_mat_ca_truoc, tong_tien_mat_rut, tong_tien_trong_ca, tien_con_lai, ghi_chu_phat_sinh, trang_thai, created_at, updated_at, deleted_at)
VALUES
  (1, 2, 100000.00, '2022-01-01 08:00:00', 200000.00, 300000.00, 400000.00, 500000.00, 600000.00, 700000.00, 800000.00, 'Ghi chu 1', 'active', NOW(), NOW(), 0),
  (2, 3, 200000.00, '2022-01-02 08:00:00', 300000.00, 400000.00, 500000.00, 600000.00, 700000.00, 800000.00, 900000.00, 'Ghi chu 2', 'active', NOW(), NOW(), 0),
  (3, 4, 300000.00, '2022-01-03 08:00:00', 400000.00, 500000.00, 600000.00, 700000.00, 800000.00, 900000.00, 1000000.00, 'Ghi chu 3', 'inactive', NOW(), NOW(), 0),
  (4, 5, 400000.00, '2022-01-04 08:00:00', 500000.00, 600000.00, 700000.00, 800000.00, 900000.00, 1000000.00, 1100000.00, 'Ghi chu 4', 'active', NOW(), NOW(), 0),
  (5, 1, 500000.00, '2022-01-05 08:00:00', 600000.00, 700000.00, 800000.00, 900000.00, 1000000.00, 1100000.00, 1200000.00, 'Ghi chu 5', 'active', NOW(), NOW(), 0);
  
  INSERT INTO lich_su_checkin (id_hoa_don_chi_tiet, thoi_gian_checkin, mo_ta)
VALUES
  (21, '2024-08-25 08:30:00', 'Khách hàng check-in vào sáng sớm'),
  (22, '2024-08-25 09:45:00', 'Khách hàng check-in vào buổi sáng'),
  (23, '2024-08-25 10:15:00', 'Khách hàng check-in trước giờ trưa'),
  (24, '2024-08-25 12:00:00', 'Khách hàng check-in vào trưa'),
  (25, '2024-08-25 13:30:00', 'Khách hàng check-in sau bữa trưa'),
  (26, '2024-08-25 15:00:00', 'Khách hàng check-in vào giữa chiều'),
  (27, '2024-08-25 16:00:00', 'Khách hàng check-in vào cuối chiều'),
  (28, '2024-08-25 17:30:00', 'Khách hàng check-in vào buổi tối sớm'),
  (29, '2024-08-25 18:45:00', 'Khách hàng check-in gần giờ đóng cửa'),
  (30, '2024-08-25 19:15:00', 'Khách hàng check-in vào tối muộn');
  
  INSERT INTO tham_so (ma, ten, gia_tri, type_gia_tri, mo_ta, trang_thai, deleted_at)
VALUES
('TS001', 'Giờ Mở Cửa', '06:00', 'giờ', 'Thời gian mở cửa sân bóng', 1, 0),
('TS002', 'Giờ Đóng Cửa', '22:00', 'giờ', 'Thời gian đóng cửa sân bóng', 1, 0),
('TS003', 'Giá Sân Nhỏ', '200000', 'VND', 'Giá thuê sân nhỏ trong 1 giờ', 1, 0),
('TS004', 'Giá Sân Lớn', '400000', 'VND', 'Giá thuê sân lớn trong 1 giờ', 1, 0),
('TS005', 'Số Người Tối Đa Sân Nhỏ', '10', 'người', 'Số người tối đa cho sân nhỏ', 1, 0),
('TS006', 'Số Người Tối Đa Sân Lớn', '20', 'người', 'Số người tối đa cho sân lớn', 1, 0),
('TS007', 'Thời Gian Hủy Đặt Sân', '2', 'giờ', 'Thời gian tối thiểu trước giờ đá để hủy đặt sân', 1, 0),
('TS008', 'Thời Gian Đặt Trước', '1', 'ngày', 'Thời gian tối thiểu để đặt sân trước ngày đá', 1, 0),
('TS009', 'Giá Thuê Bóng', '50000', 'VND', 'Giá thuê bóng trong 1 trận', 1, 0),
('TS010', 'Phí Huấn Luyện Viên', '300000', 'VND', 'Phí thuê huấn luyện viên theo giờ', 1, 0);
