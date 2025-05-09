package com.userdb.mobileapp.service;

import com.userdb.mobileapp.dto.requestDTO.AddressDeliveryDTO;
import com.userdb.mobileapp.dto.responseDTO.AddressDeliveryResponseDTO;
import com.userdb.mobileapp.entity.AddressDelivery;
import com.userdb.mobileapp.exception.DataNotFoundException;

import java.util.List;

public interface IAddressDeliveryService {
    AddressDelivery addAddress(AddressDeliveryDTO dto);
    AddressDelivery setDefaultAddress(Long addressId);
    AddressDelivery updateAddress(Long userId, Long addressId, AddressDeliveryDTO dto);
    void deleteAddress(Long userId, Long addressId);
    AddressDeliveryResponseDTO getDefaultAddressByUserId(Long userId) throws DataNotFoundException;
    List<AddressDeliveryResponseDTO> getAllAddressDelivery(long userId);

    boolean updateDefaultAddress(long userId, long addressId);
}
