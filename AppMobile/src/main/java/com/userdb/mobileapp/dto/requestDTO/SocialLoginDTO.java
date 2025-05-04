package com.userdb.mobileapp.dto.requestDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialLoginDTO extends SocialAccountDTO{
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("email")
    @NotBlank(message = "Email is required")
    private String email;

    @JsonProperty("profile_image")
    private String profileImage;
}
