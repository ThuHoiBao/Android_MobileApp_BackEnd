package com.userdb.mobileapp.service;

import com.userdb.mobileapp.dto.payment.PaymentDTO;
import com.userdb.mobileapp.dto.payment.PaymentQueryDTO;
import com.userdb.mobileapp.dto.payment.PaymentRefundDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;


public interface IVNPayService {
    String createPaymentUrl(PaymentDTO paymentRequest, HttpServletRequest request);
    String queryTransaction(PaymentQueryDTO paymentQueryDTO, HttpServletRequest request) throws  IOException;
    String refundTransaction(PaymentRefundDTO refunDTO) throws IOException;
}
