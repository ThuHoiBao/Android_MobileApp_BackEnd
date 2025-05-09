package com.userdb.mobileapp.repository;

import com.userdb.mobileapp.dto.responseDTO.AddressDeliveryResponseDTO;
import com.userdb.mobileapp.entity.AddressDelivery;
import feign.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AddressDeliveryRepository extends JpaRepository<AddressDelivery, Long> {
    List<AddressDelivery> findByUserId(Long userId);
    Optional<AddressDelivery> findByUser_IdAndIsDefaultTrue(long userId);
        @Query(value = """
        SELECT id, full_name, phone_number, address, is_default, user_id, status 
        FROM address_delivery 
        WHERE user_id = :userId AND is_default = true AND status = true 
    """, nativeQuery = true)
    Optional<AddressDeliveryResponseDTO> findByUser_IdAndIsDefaultTrueAndStatusTrue(@Param("userId") Long userId);

    @Query(value = """
        SELECT id, full_name, phone_number, address, is_default, user_id, status
        FROM address_delivery
        WHERE user_id = :userId AND status = true
    
    """, nativeQuery = true)
   List<AddressDeliveryResponseDTO> findByUser_Id(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE AddressDelivery a SET a.isDefault = false WHERE a.user.id = :userId")
    void updateIsDefaultForUser(long userId);

    @Modifying
    @Transactional
    @Query("UPDATE AddressDelivery a SET a.isDefault = :isDefault WHERE a.id = :addressId")
    void updateIsDefault(long addressId, boolean isDefault);
}
