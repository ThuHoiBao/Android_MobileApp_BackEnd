package com.userdb.mobileapp.service;


import com.userdb.mobileapp.dto.requestDTO.SocialLoginDTO;
import com.userdb.mobileapp.dto.requestDTO.UserDTO;
import com.userdb.mobileapp.dto.requestDTO.UserUpdatePasswordDTO;
import com.userdb.mobileapp.entity.User;
import com.userdb.mobileapp.exception.DataNotFoundException;
import com.userdb.mobileapp.exception.InvalidParamException;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber, String password) throws DataNotFoundException, InvalidParamException;
    String logout(String token);
    boolean verifyToken(String token);
    User findByPhoneNumber(String phoneNumber);
    boolean verifyEmail(String email);
    boolean verifyPhone(String phone);
    String generateOTP();
    boolean sendEmail(String to, String code);
    boolean updatePassword(UserUpdatePasswordDTO userUpdatePasswordDTO);
    String loginSocial(SocialLoginDTO socialLoginDTO) throws Exception;
    User getUserDetailsFromToken(String token) throws  Exception;
}
