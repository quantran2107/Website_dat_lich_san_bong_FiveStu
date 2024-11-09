package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.HoaDonDTO;
import com.example.DATN_WebFiveTus.dto.PaymentResponse;
import com.example.DATN_WebFiveTus.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vnpay")
public class VnPayController {
    @Autowired
    private HoaDonService invoiceService;

    @PostMapping("/create-invoice")
    public HoaDonDTO createInvoice(@RequestBody HoaDonDTO invoiceRequest) {
        return invoiceService.save(invoiceRequest);
    }

    @GetMapping("/payment-confirmation")
    public PaymentResponse confirmPayment(@RequestParam String maHoaDon) {
        return invoiceService.confirmPayment(maHoaDon);
    }
}
