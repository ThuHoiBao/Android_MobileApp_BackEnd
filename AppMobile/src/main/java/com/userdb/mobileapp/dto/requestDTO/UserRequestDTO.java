package com.userdb.mobileapp.dto.requestDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class UserRequestDTO {
    private String fullName;
    @JsonFormat(pattern = "MMM dd, yyyy hh:mm:ss a") // Định dạng ngày tháng
    private Date dateOfBirth;
    private  int userId;
}
