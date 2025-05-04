package com.userdb.mobileapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ImageProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String imageProduct;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
