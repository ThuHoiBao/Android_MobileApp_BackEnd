package com.userdb.mobileapp.repository;

import com.userdb.mobileapp.entity.AddressDelivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressDeliveryRepository extends JpaRepository<AddressDelivery, Long> {
    List<AddressDelivery> findByUserId(Long userId);
    Optional<AddressDelivery> findByUser_IdAndIsDefaultTrue(long userId);
    Optional<AddressDelivery> findTopByUserIdAndStatusTrueOrderByIdAsc(Long userId);
}