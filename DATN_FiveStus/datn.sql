Create DATABASE DuAnTotNghiep;

USE DuAnTotNghiep;

-- Table: khach_hang
CREATE TABLE khach_hang (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ma_khach_hang VARCHAR(50) NOT NULL,
  mat_khau VARCHAR(255) NULL,
  ho_va_ten VARCHAR(100) NULL,
  email VARCHAR(100) NULL,
  gioi_tinh BOOLEAN NULL,
  so_dien_thoai VARCHAR(50) NULL,
  trang_thai VARCHAR(50) NULL,
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
  ngay_sinh Date NULL,
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
  deleted_at bit DEFAULT 0
);



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

-- Table: hoa_don
CREATE TABLE hoa_don (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_nhan_vien INT  NULL,
  id_khach_hang INT  NULL,
  ma_hoa_don VARCHAR(100) NOT NULL,
  ngay_tao DATETIME,
  loai BIT,
  tong_tien_san DECIMAL(10, 2) NULL, 
  tien_coc DECIMAL(10, 2) NULL, 
  tong_tien DECIMAL(10, 2) NULL, 
  ghi_chu VARCHAR(100) DEFAULT '',
  trang_thai VARCHAR(100) NOT NULL,
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit DEFAULT 0,
CONSTRAINT fk_hoaDon_khachHang FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id),
  CONSTRAINT fk_hoaDon_nhanVien FOREIGN KEY (id_nhan_vien) REFERENCES nhan_vien(id)
);

-- loai_san
CREATE TABLE loai_san (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ten_loai_san VARCHAR(100) NOT NULL,
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit DEFAULT 0
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
  deleted_at bit DEFAULT 0
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
  deleted_at bit DEFAULT 0
);


-- ngay
CREATE TABLE ngay_trong_tuan (
  id INT AUTO_INCREMENT PRIMARY KEY,
  thu_trong_tuan VARCHAR(100) NOT NULL,
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit default 0
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
  deleted_at BIT(1) DEFAULT 0,
  CONSTRAINT fk_sanCa_ca FOREIGN KEY (id_ca) REFERENCES ca(id),
  CONSTRAINT fk_sanCa_sanBong FOREIGN KEY (id_san_bong) REFERENCES san_bong(id),
  CONSTRAINT fk_sanCa_ngay_trong_tuan FOREIGN KEY (id_ngay_trong_tuan) REFERENCES ngay_trong_tuan(id)
);

-- Table: hoa_don_chi_tiet
CREATE TABLE hoa_don_chi_tiet (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_hoa_don INT NULL,
  id_san_ca INT NULL,
  id_nhan_vien INT  NULL,
  id_phieu_giam_gia INT  NULL,
  ma_hoa_don_chi_tiet VARCHAR(100)  NULL,
  tong_tien DECIMAL(10, 2) NULL,
  tien_coc_hdct DECIMAL(10, 2) NULL,
  tien_giam_gia DECIMAL(10, 2) NULL,
  tong_tien_thuc_te DECIMAL(10, 2) NULL,
  ngay_den_san date null,
  ghi_chu VARCHAR(100) DEFAULT '' null,
  trang_thai VARCHAR(50) null,
  kieu_ngay_dat VARCHAR(50) null,
  created_at DATETIME null, 
  updated_at DATETIME null,
  deleted_at bit DEFAULT 0 ,
  CONSTRAINT fk_hoaDonChiTiet_hoaDon FOREIGN KEY (id_hoa_don) REFERENCES hoa_don(id),
  CONSTRAINT fk_hoaDonChiTiet_sanCa FOREIGN KEY (id_san_ca) REFERENCES san_ca(id),
   CONSTRAINT fk_hoaDonChiTiet_phieuGiamGia FOREIGN KEY (id_phieu_giam_gia) REFERENCES phieu_giam_gia(id)
);

-- Table: hinh_thuc_thanh_toan
CREATE TABLE hinh_thuc_thanh_toan (
  id INT AUTO_INCREMENT PRIMARY KEY,
  hinh_thuc_thanh_toan VARCHAR(100) NOT NULL,
  trang_thai VARCHAR(100),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit DEFAULT 0
);

-- Table: chi_tiet_hinh_thuc_thanh_toan
CREATE TABLE chi_tiet_hinh_thuc_thanh_toan (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_hoa_don_chi_tiet INT,
  id_hinh_thuc_thanh_toan INT,
  so_tien DECIMAL(10, 2),
  ma_giao_dich varchar(100) null,
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit DEFAULT 0,
  CONSTRAINT fk_chiTietHinhThucThanhToan_hoaDonChiTiet FOREIGN KEY (id_hoa_don_chi_tiet) REFERENCES hoa_don_chi_tiet(id),
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
  image_data VARCHAR(255)default'https://res.cloudinary.com/dsuehugin/image/upload/v1728565365/g7xqdec1fnavzvacfu2k.jpg'
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
  image_data VARCHAR(255) 
);

-- Table: dich_vu_san_bong
CREATE TABLE dich_vu_san_bong (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_do_thue INT,
  id_nuoc_uong INT,
  id_hoa_don_chi_tiet INT,
  tong_tien DECIMAL(10, 2),
  so_luong INT,
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit,
  CONSTRAINT fk_dichVuSanBong_doThue FOREIGN KEY (id_do_thue) REFERENCES do_thue(id),
  CONSTRAINT fk_dichVuSanBong_nuocUong FOREIGN KEY (id_nuoc_uong) REFERENCES nuoc_uong(id),
  CONSTRAINT fk_chiTietDichVuSanBong_hoaDonChiTiet FOREIGN KEY (id_hoa_don_chi_tiet) REFERENCES hoa_don_chi_tiet(id)
);

-- phu-phi

-- Table: phu_phi_hoa_don
CREATE TABLE phu_phi_hoa_don (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_hoa_don_chi_tiet INT,
  ma VARCHAR(100),
  ten VARCHAR(100),
  tien_phu_phi DECIMAL(10, 2),
  ghi_chu VARCHAR(50),
  trang_thai VARCHAR(50),
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit,
  CONSTRAINT fk_phuPhiHoaDon_hoaDonChiTiet FOREIGN KEY (id_hoa_don_chi_tiet) REFERENCES hoa_don_chi_tiet(id)
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
  deleted_at BIT(1) DEFAULT 1,
  is_active BIT(1) DEFAULT 1
);



-- Table: giao_ca
CREATE TABLE giao_ca (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_nhan_vien INT,
  tien_mat_ca_truoc DECIMAL(10,2),
  tien_mat_trong_ca DECIMAL(10,2),
  tien_ck_trong_ca DECIMAL(10,2),
  tong_tien_trong_ca DECIMAL(10,2),
  tong_tien_mat_thuc_te DECIMAL(10,2),
  tong_tien_phat_sinh DECIMAL(10,2),
  ghi_chu VARCHAR(255),
  trang_thai bit,
  created_at DATETIME, 
  updated_at DATETIME,
  deleted_at bit,
  CONSTRAINT fk_giaoCa_nvGiaoCa FOREIGN KEY ( id_nhan_vien) REFERENCES nhan_vien(id)
);


CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `enabled` BOOLEAN NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`username`),
    UNIQUE (`email`)
);


CREATE TABLE `roles` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` ENUM('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MANAGER','ROLE_EMPLOYEE') NOT NULL, -- Thay 'ROLE_OTHER' bằng các vai trò khác nếu cần
    PRIMARY KEY (`id`)
);
CREATE TABLE `user_roles` (
    `user_id` BIGINT NOT NULL,
    `role_id` INT NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`role_id`) REFERENCES `roles`(`id`) ON DELETE CASCADE
);
