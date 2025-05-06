package com.userdb.mobileapp.dto.responseDTO;

import com.userdb.mobileapp.dto.requestDTO.CreateOrderRequestDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderResponseDTO {
    private long userId;
    private List<Integer> cartItemIds;

    public static CreateOrderResponseDTO fromCreateOrderRequestDTO(CreateOrderRequestDTO requestDTO) {
        return CreateOrderResponseDTO.builder()
                .userId(requestDTO.getUserId())
                .cartItemIds(requestDTO.getCartItemIds())
                .build();
    }
}
