package com.userdb.mobileapp.dto.responseDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@Getter
@Setter
public class ReviewResponseDTO {
    private int ratingValue;
    private Date date;
    private String comment;
    private List<String> images;
}
