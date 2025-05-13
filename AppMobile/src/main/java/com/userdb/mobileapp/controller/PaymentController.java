package com.userdb.mobileapp.controller;

import com.userdb.mobileapp.dto.payment.PaymentDTO;
import com.userdb.mobileapp.dto.payment.PaymentQueryDTO;
import com.userdb.mobileapp.dto.payment.PaymentRefundDTO;
import com.userdb.mobileapp.dto.responseDTO.ResponseObject;
import com.userdb.mobileapp.exception.DataNotFoundException;
import com.userdb.mobileapp.service.impl.OrderServiceImpl;
import com.userdb.mobileapp.service.impl.VNPayServiceImpl;
import feign.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/payments")
public class PaymentController {
    private final VNPayServiceImpl vnPayService;
    private final OrderServiceImpl orderService;

    @PostMapping("/create_payment_url")
    public ResponseEntity<String> createPayment(@RequestBody PaymentDTO paymentRequest, HttpServletRequest request){
        try {
            String paymentUrl = vnPayService.createPaymentUrl(paymentRequest, request);
            return ResponseEntity.ok(paymentUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/vnpay/return")
        public ResponseEntity<String> handleVnPayReturn(@RequestParam Map<String, String> allParams) throws DataNotFoundException {
        String vnp_OrderInfo = allParams.get("vnp_OrderInfo");
        String vnp_ResponseCode = allParams.get("vnp_ResponseCode");
        String vnp_Amount = allParams.get("vnp_Amount");
        String vnp_BankCode = allParams.get("vnp_BankCode");
        String vnp_PayDate = allParams.get("vnp_CreateDate");


        String orderInfoPrefix = "Thanh toan don hang:";
        if(!vnp_OrderInfo.startsWith(orderInfoPrefix)){
            return ResponseEntity.badRequest().body("Invalid order info format");
        }
        int orderId = Integer.parseInt(vnp_OrderInfo.substring(orderInfoPrefix.length()).trim());

        if("00".equals(vnp_ResponseCode)){
            orderService.updateOrderStatus(orderId, vnp_Amount, vnp_BankCode, vnp_PayDate);
            return ResponseEntity.ok("Payment confirmed");
        } else {
            return ResponseEntity.ok("Payment failed or cancelled");
        }
    }

    @PostMapping("/query")
    public ResponseEntity<ResponseObject> queryTransaction(@RequestBody PaymentQueryDTO paymentQueryDTO, HttpServletRequest request) {
        try {
            String result = vnPayService.queryTransaction(paymentQueryDTO, request);
            return ResponseEntity.ok(ResponseObject.builder()
                    .status(HttpStatus.OK)
                    .message("Query successful")
                    .data(result)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error querying transaction: " + e.getMessage())
                    .build());
        }
    }

    @PostMapping("/refund")
    public ResponseEntity<ResponseObject> refundTransaction(
            @Valid @RequestBody PaymentRefundDTO paymentRefundDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(String.join(", ", errorMessages))
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        }

        try {
            String response = vnPayService.refundTransaction(paymentRefundDTO);
            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message("Refund processed successfully")
                    .status(HttpStatus.OK)
                    .data(response)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                    .message("Failed to process refund: " + e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .build());
        }
    }

}
