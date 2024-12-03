package com.example.DATN_WebFiveTus.config.VNPay;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Controller
//@RestController
@RequestMapping("/api/vnpay")
public class VNPAYController {
    @Autowired
    private VNPAYService vnPayService;

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

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        Long totalAmount = 0L;
        try {
            if (totalPrice != null) {
                totalAmount = Long.parseLong(totalPrice) / 100; // Chia để đưa về đơn vị "đồng"
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing totalPrice: " + e.getMessage());
        }

        // Chuyển đổi `vnp_PayDate` từ chuỗi sang định dạng ngày tháng năm
        String formattedDate = "";
        try {
            if (paymentTime != null) {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = inputFormat.parse(paymentTime);
                formattedDate = outputFormat.format(date);
            }
        } catch (ParseException e) {
            System.err.println("Error parsing paymentTime: " + e.getMessage());
        }

        // Thêm dữ liệu vào model
        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalAmount);
        model.addAttribute("paymentTime", formattedDate); // Truyền thời gian đã được format
        model.addAttribute("transactionId", transactionId);
        return paymentStatus == 1 ? "/orderSuccess" : "/orderFail";
    }


}
