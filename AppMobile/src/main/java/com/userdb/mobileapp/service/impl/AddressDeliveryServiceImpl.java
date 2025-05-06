package com.userdb.mobileapp.service.impl;

import com.userdb.mobileapp.dto.requestDTO.AddressDeliveryDTO;
import com.userdb.mobileapp.entity.AddressDelivery;
import com.userdb.mobileapp.entity.User;
import com.userdb.mobileapp.repository.AddressDeliveryRepository;
import com.userdb.mobileapp.repository.UserRepository;
import com.userdb.mobileapp.service.IAddressDeliveryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressDeliveryServiceImpl implements IAddressDeliveryService {
    private final AddressDeliveryRepository addressDeliveryRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public AddressDelivery addAddress(AddressDeliveryDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));

        // Nếu isDefault = true, cập nhật tất cả địa chỉ khác của user về false
        if (Boolean.TRUE.equals(dto.getIsDefault())) {
            user.getAddressDeliveries().forEach(address -> address.setIsDefault(false));
        }

        AddressDelivery address = AddressDelivery.builder()
                .fullName(dto.getFullName())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .isDefault(dto.getIsDefault() != null ? dto.getIsDefault() : false)
                .user(user)
                .status(true)
                .build();

        return addressDeliveryRepository.save(address);
    }

    @Override
    public AddressDelivery setDefaultAddress(Long addressId) {
        AddressDelivery targetAddress = addressDeliveryRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with ID: " + addressId));

        Long userId = targetAddress.getUser().getId();

        // Bỏ đánh dấu mặc định của tất cả địa chỉ của user này
        List<AddressDelivery> userAddresses = addressDeliveryRepository.findByUserId(userId);
        for (AddressDelivery addr : userAddresses) {
            addr.setIsDefault(false);
        }

        // Đánh dấu lại địa chỉ mặc định
        targetAddress.setIsDefault(true);

        // Lưu tất cả thay đổi
        addressDeliveryRepository.saveAll(userAddresses);
        return addressDeliveryRepository.save(targetAddress);
    }

    @Override
    public AddressDelivery updateAddress(Long userId, Long addressId, AddressDeliveryDTO dto) {
        // Kiểm tra xem User có tồn tại không
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Kiểm tra xem AddressDelivery có thuộc về user không
        AddressDelivery address = addressDeliveryRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with ID: " + addressId));

        // Kiểm tra nếu địa chỉ không phải của user thì báo lỗi
        if (!address.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Address does not belong to the specified user");
        }

        // Cập nhật thông tin địa chỉ
        address.setFullName(dto.getFullName());
        address.setPhoneNumber(dto.getPhoneNumber());
        address.setAddress(dto.getAddress());
        address.setStatus(true);

        return addressDeliveryRepository.save(address);
    }

    @Override
    public void deleteAddress(Long userId, Long addressId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        AddressDelivery address = addressDeliveryRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with ID: " + addressId));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Address does not belong to the specified user");
        }

        address.setStatus(false);
        addressDeliveryRepository.save(address);

        AddressDelivery defaultAddress = addressDeliveryRepository
                .findTopByUserIdAndStatusTrueOrderByIdAsc(userId)
                .orElseThrow(() -> new RuntimeException("No available addresses to set as default"));

        defaultAddress.setIsDefault(true);
        addressDeliveryRepository.save(defaultAddress);
    }

}
