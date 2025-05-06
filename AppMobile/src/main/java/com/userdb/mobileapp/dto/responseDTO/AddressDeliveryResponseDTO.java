package com.userdb.mobileapp.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.userdb.mobileapp.entity.AddressDelivery;
import com.userdb.mobileapp.entity.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDeliveryResponseDTO {
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("address")
    private String address;
    @JsonProperty("is_default")
    private Boolean isDefault;
    @JsonProperty("user_id")
    private Long userId;

    public static AddressDeliveryResponseDTO fromAddressDelivery(AddressDelivery addressDelivery) {
        return AddressDeliveryResponseDTO.builder()
                .fullName(addressDelivery.getFullName())
                .phoneNumber(addressDelivery.getPhoneNumber())
                .address(addressDelivery.getAddress())
                .isDefault(addressDelivery.getIsDefault())
                .userId(addressDelivery.getUser().getId())
                .build();
    }
}
