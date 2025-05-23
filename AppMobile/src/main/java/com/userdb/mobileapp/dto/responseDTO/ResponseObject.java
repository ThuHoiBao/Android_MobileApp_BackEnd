package com.userdb.mobileapp.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResponseObject {
    @JsonProperty("message")
    private String message;
    @JsonProperty("status")
    private HttpStatus status;
    @JsonProperty("data")
    private Object data;
}
