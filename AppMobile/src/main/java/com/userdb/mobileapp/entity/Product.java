package com.userdb.mobileapp.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    private String productName;
    private double price;

    private String color;
    private int size;
    private boolean status;
    private String description;

//    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private Review review;

    @OneToMany(mappedBy = "product") // tham chieu den "product" trong cardItem
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "categoryID", nullable = true)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageProduct> imageProducts = new ArrayList<>();

    // Phương thức ánh xạ từ Product sang ProductDTO
//    public ProductDTO toDTO() {
//        return new ProductDTO(
//                productID,
//                productName,
//                price,
//                0,
//                image,
//                color,
//                size,
//                status,
//                description,
//                createDate,
//                null,
//                null,
//                null,
//                promotionProducts != null ? promotionProducts.stream().map(PromotionProduct::toDTO).collect(Collectors.toList()) : null,
//                0,
//                this.review != null ? this.review.toDTO() : null,
//                image == null ? null : "data:image/png;base64," + Base64.getEncoder().encodeToString(image)
//        );
//    }
}
