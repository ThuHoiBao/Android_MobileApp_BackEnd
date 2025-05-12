package com.userdb.mobileapp.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentDTO {
    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("amount")
    private long amount; // Số tiền cần thanh toán

    @JsonProperty("bankCode")
    private String bankCode;  // Mã ngân hàng

    @JsonProperty("language")
    private String language; // Ngôn ngữ giao diện thanh toán (vd: "vn", "en")
}
