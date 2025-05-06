package com.userdb.mobileapp.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private String userName;
    private Date reviewDate;
    private int ratingValue;
    private String comment;
    private List<String> imageReviews;

}
