package com.userdb.mobileapp.entity;


import com.userdb.mobileapp.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private long amount;
    private String paymentDate;
    private boolean status;
    private String momoBillId;
}
