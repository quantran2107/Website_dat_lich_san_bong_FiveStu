package com.example.DATN_WebFiveTus.config.VNPay;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VNPAYService {
    @Value("${vnpay.tmnCode}")
    private String vnpTmnCode;

    @Value("${vnpay.payUrl}")
    private String vnpPayUrl;

    @Value("${vnpay.returnUrl}")
    private String vnpReturnUrl;

    @Value("${vnpay.secretKey}")
    private String secretKey;

    public String createOrder(HttpServletRequest request, int amount, String orderInfo, String urlReturn) throws Exception {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";

        String vnp_TxnRef = getRandomNumber(8);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnpTmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");

        // Thêm BankCode nếu cần
        vnp_Params.put("vnp_BankCode", "VNBANK");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);

        vnp_Params.put("vnp_OrderInfo", "Thanh toan tien coc dat lich san bong " + orderInfo);

        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnpReturnUrl);

        // Lấy địa chỉ IP
        String ipAddress = VNPAYConfig.getIpAddress(request);
        vnp_Params.put("vnp_IpAddr", ipAddress);

        // Thời gian tạo giao dịch và hết hạn
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        String vnp_CreateDate = formatter.format(new Date());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        Calendar cld = Calendar.getInstance();
        cld.add(Calendar.MINUTE, 30); // Thời gian hết hạn 30 phút
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);


        // Xây dựng hash data và query
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString())).append('&');
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()))
                        .append('&');
            }
        }

        // Xóa ký tự cuối "&"
        hashData.deleteCharAt(hashData.length() - 1);
        query.deleteCharAt(query.length() - 1);

        // Tạo Secure Hash
        String vnp_SecureHash = hmacSHA512(secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        System.out.println("Generated Query: " + query.toString());
        System.out.println("Generated Secure Hash: " + vnp_SecureHash);

        return vnpPayUrl + "?" + query.toString();
    }

    private String getRandomNumber(int length) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }

    private String hmacSHA512(String key, String data) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA512");
            javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(key.getBytes(), "HmacSHA512");
            mac.init(secretKeySpec);
            byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error while hashing data", e);
        }
    }
    public int orderReturn(HttpServletRequest request){
        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = null;
            String fieldValue = null;
            try {
                fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VNPAYConfig.hashAllFields(fields);
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }
}
