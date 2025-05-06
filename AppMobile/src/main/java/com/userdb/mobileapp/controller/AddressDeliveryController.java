package com.userdb.mobileapp.controller;


import com.userdb.mobileapp.dto.requestDTO.AddressDeliveryDTO;
import com.userdb.mobileapp.dto.responseDTO.AddressDeliveryResponseDTO;
import com.userdb.mobileapp.dto.responseDTO.ResponseObject;
import com.userdb.mobileapp.entity.AddressDelivery;
import com.userdb.mobileapp.service.impl.AddressDeliveryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressDeliveryController {
    private final AddressDeliveryServiceImpl addressDeliveryService;

    @PostMapping
    public ResponseEntity<ResponseObject> createAddress(@RequestBody AddressDeliveryDTO dto) {
        try {
            AddressDelivery savedAddress = addressDeliveryService.addAddress(dto);
            return ResponseEntity.ok().body(ResponseObject.builder().status(HttpStatus.OK)
                    .data(AddressDeliveryResponseDTO.fromAddressDelivery(savedAddress))
                    .message("Add Address Delivery Successfully!")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null).message(e.getMessage())
                    .build());
        }
    }

    @PutMapping("/update/default")
    public ResponseEntity<ResponseObject> setDefaultAddress(@RequestParam Long addressId) {
        try {
            AddressDelivery updatedAddress = addressDeliveryService.setDefaultAddress(addressId);
            return ResponseEntity.ok().body(ResponseObject.builder().status(HttpStatus.OK)
                    .data(AddressDeliveryResponseDTO.fromAddressDelivery(updatedAddress))
                    .message("Update Address Default Delivery Successfully!")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null).message(e.getMessage())
                    .build());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseObject> updateAddress(
            @RequestParam Long addressId,
            @RequestBody AddressDeliveryDTO dto) {
        try {
            AddressDelivery updatedAddress = addressDeliveryService.updateAddress(dto.getUserId(), addressId, dto);
            return ResponseEntity.ok().body(ResponseObject.builder().status(HttpStatus.OK)
                    .data(AddressDeliveryResponseDTO.fromAddressDelivery(updatedAddress))
                    .message("Update Address Delivery Successfully!")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null).message(e.getMessage())
                    .build());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAddress(
            @RequestParam Long addressId,
            @RequestParam Long userId) {
        addressDeliveryService.deleteAddress(userId, addressId);
        return ResponseEntity.ok("Address updated successfully and default address set.");
    }



}
