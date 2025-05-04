package com.userdb.mobileapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ImageReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String imageReview;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

}
