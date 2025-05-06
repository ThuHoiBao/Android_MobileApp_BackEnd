package com.userdb.mobileapp.service;

import com.userdb.mobileapp.dto.requestDTO.AddressDeliveryDTO;
import com.userdb.mobileapp.entity.AddressDelivery;

public interface IAddressDeliveryService {
    AddressDelivery addAddress(AddressDeliveryDTO dto);
    AddressDelivery setDefaultAddress(Long addressId);
    AddressDelivery updateAddress(Long userId, Long addressId, AddressDeliveryDTO dto);
    void deleteAddress(Long userId, Long addressId);
}
