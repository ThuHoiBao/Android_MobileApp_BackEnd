package com.userdb.mobileapp.dto.requestDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.userdb.mobileapp.entity.ImageReview;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
@Getter
@Setter
public class ReviewRequestDTO {
    private int ratingValue;

    @JsonFormat(pattern = "MMM d, yyyy h:mm:ss a", timezone = "Asia/Ho_Chi_Minh")
    private Date date;

    private String comment;
    private int orderId;
    private String productName;
    private String color;
}
