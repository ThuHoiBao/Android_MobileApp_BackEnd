package com.userdb.mobileapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartID;

    @OneToOne
    @JoinColumn(name = "userID")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.REMOVE)
    private List<CartItem> cardItems;

}
