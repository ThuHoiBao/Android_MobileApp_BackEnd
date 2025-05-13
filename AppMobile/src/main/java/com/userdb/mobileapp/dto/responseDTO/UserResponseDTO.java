package com.userdb.mobileapp.dto.responseDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Setter
@Getter
public class UserResponseDTO {
    private String fullName;
    private String phoneNumber;
    private String email;
    private Date dateOfBirth;
    private String profileImage;
}
