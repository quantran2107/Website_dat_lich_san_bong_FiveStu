package com.example.DATN_WebFiveTus.config.VNPay;

import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;
import com.example.DATN_WebFiveTus.dto.HoaDonDTO;
import com.example.DATN_WebFiveTus.service.HoaDonChiTietService;
import com.example.DATN_WebFiveTus.service.HoaDonService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
//@RestController
@RequestMapping("/api/vnpay")
public class VNPAYController {

    @Autowired
    private VNPAYService vnPayService;

    @Autowired
    @Lazy
    private HoaDonService hoaDonService;

    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;



    // Chuyển hướng người dùng đến cổng thanh toán VNPAY
    @PostMapping("/submitOrder")
    public ResponseEntity<?> submitOrder(@RequestParam("amount") int orderTotal,
                                         @RequestParam("orderInfo") String orderInfo,
                                         HttpServletRequest request) {
        try {
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl);
            return ResponseEntity.ok(Map.of("redirectUrl", vnpayUrl));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error creating payment", "details", e.getMessage()));
        }
    }

    @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request, Model model) {
        int paymentStatus = vnPayService.orderReturn(request);

        // Lấy thông tin vnp_OrderInfo
        String orderInfoRaw = request.getParameter("vnp_OrderInfo"); // Dạng "Thanh toan tien coc dat lich san bong <id>"
        Integer idHoaDon = null;

        try {
            // Tách id từ chuỗi orderInfoRaw
            if (orderInfoRaw != null && orderInfoRaw.contains(" ")) {
                String[] parts = orderInfoRaw.split(" ");
                idHoaDon = Integer.parseInt(parts[parts.length - 1]); // Lấy phần cuối cùng
            }
        } catch (NumberFormatException e) {
//            System.err.println("Error parsing idHoaDon from vnp_OrderInfo: " + e.getMessage());
        }
//        System.out.println(idHoaDon);

        // Nếu không lấy được id hóa đơn, trả về lỗi
        if (idHoaDon == null) {
//            System.err.println("Invalid vnp_OrderInfo format: " + orderInfoRaw);
            return "/orderFail";
        }

        // Xử lý các thông tin khác
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        Long totalAmount = 0L;
        try {
            if (totalPrice != null) {
                totalAmount = Long.parseLong(totalPrice) / 100; // Chia để đưa về đơn vị "đồng"
            }
        } catch (NumberFormatException e) {
//            System.err.println("Error parsing totalPrice: " + e.getMessage());
        }

        String formattedDate = "";
        try {
            if (paymentTime != null) {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = inputFormat.parse(paymentTime);
                formattedDate = outputFormat.format(date);
            }
        } catch (ParseException e) {
//            System.err.println("Error parsing paymentTime: " + e.getMessage());
        }

        // Cập nhật trạng thái hóa đơn và hóa đơn chi tiết
        if (paymentStatus == 1) { // Thanh toán thành công
            hoaDonService.updateTrangThaiHoaDon(idHoaDon, "Chờ thanh toán");
            List<HoaDonChiTietDTO> hoaDonChiTietList = hoaDonChiTietService.searchFromHoaDon(idHoaDon);

            HoaDonDTO hoaDonDTO = hoaDonService.getOne(idHoaDon);
            if (hoaDonDTO.getHoVaTenKhachHang() == null || hoaDonDTO.getEmailKhachHang() == null) {
                throw new RuntimeException("Thông tin khách hàng không tồn tại.");
            }
            hoaDonService.sendInvoiceEmail(hoaDonDTO, hoaDonChiTietList);
            for (HoaDonChiTietDTO chiTiet : hoaDonChiTietList) {
                hoaDonChiTietService.updateTrangThaiHoaDonChiTiet(chiTiet.getId(), "Chờ nhận sân");
            }
        } else { // Thanh toán thất bại
            hoaDonService.updateTrangThaiHoaDon(idHoaDon, "Đã huỷ");
            List<HoaDonChiTietDTO> hoaDonChiTietList = hoaDonChiTietService.searchFromHoaDon(idHoaDon);
            for (HoaDonChiTietDTO chiTiet : hoaDonChiTietList) {
                hoaDonChiTietService.updateTrangThaiHoaDonChiTiet(chiTiet.getId(), "Đã huỷ");
            }
        }

        // Thêm dữ liệu vào model
        model.addAttribute("orderId", idHoaDon);
        model.addAttribute("totalPrice", totalAmount);
        model.addAttribute("paymentTime", formattedDate);
        model.addAttribute("transactionId", transactionId);

        return paymentStatus == 1 ? "/orderSuccess" : "/orderFail";
    }


}
