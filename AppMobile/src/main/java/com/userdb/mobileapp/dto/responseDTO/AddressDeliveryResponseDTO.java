    package com.userdb.mobileapp.dto.responseDTO;

    import com.userdb.mobileapp.entity.AddressDelivery;
    import lombok.*;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class AddressDeliveryResponseDTO {
        //address, full_name, phone_number, is_default, user_id, status
        private Long id;
        private String fullName;
        private String phoneNumber;
        private String address;
        private Boolean isDefault;
        private Long userId;
        private Boolean status;

        public static AddressDeliveryResponseDTO fromAddressDelivery(AddressDelivery addressDelivery) {
            return AddressDeliveryResponseDTO.builder()
                    .id(addressDelivery.getId())
                    .fullName(addressDelivery.getFullName())
                    .phoneNumber(addressDelivery.getPhoneNumber())
                    .address(addressDelivery.getAddress())
                    .isDefault(addressDelivery.getIsDefault())
                    .userId(addressDelivery.getUser().getId())
                    .status(addressDelivery.getStatus())
                    .build();
        }
    }
