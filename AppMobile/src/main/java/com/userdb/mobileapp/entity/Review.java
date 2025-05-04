package com.userdb.mobileapp.entity;

//import dto.ReviewDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewID;
    private int ratingValue;
    private Date date;
    private String comment;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageReview> imageReviews = new ArrayList<>();


    @OneToOne
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;


//    public ReviewDTO toDTO() {
//        return new ReviewDTO(
//                this.reviewID,
//                this.ratingValue,
//                this.date,
//                null,
//                this.comment,
//                null,
//                null,
//                this.image
//        );
//    }
}
