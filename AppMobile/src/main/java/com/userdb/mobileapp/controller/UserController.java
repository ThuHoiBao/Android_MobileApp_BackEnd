package com.userdb.mobileapp.controller;

import com.userdb.mobileapp.component.JwtTokenUtil;
import com.userdb.mobileapp.component.LocalizationUtils;
import com.userdb.mobileapp.dto.requestDTO.SocialLoginDTO;
import com.userdb.mobileapp.dto.requestDTO.UserDTO;
import com.userdb.mobileapp.dto.requestDTO.UserLoginDTO;
import com.userdb.mobileapp.dto.requestDTO.UserUpdatePasswordDTO;
import com.userdb.mobileapp.dto.responseDTO.ResponseObject;
import com.userdb.mobileapp.dto.responseDTO.TokenResponse;
import com.userdb.mobileapp.dto.responseDTO.UserResponse;
import com.userdb.mobileapp.entity.User;
import com.userdb.mobileapp.exception.DataNotFoundException;
import com.userdb.mobileapp.exception.InvalidParamException;
import com.userdb.mobileapp.service.IUserService;
import com.userdb.mobileapp.utils.MessageKeys;
import com.userdb.mobileapp.utils.ValidationUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final LocalizationUtils localizationUtils;

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest()
                        .body(ResponseObject.builder()
                                .status(HttpStatus.BAD_REQUEST)
                                .data(null)
                                .message(errorMessages.toString())
                                .build());
            }
            if(userDTO.getEmail() == null || userDTO.getEmail().trim().isBlank()){
                if(userDTO.getPhoneNumber() == null || userDTO.getPhoneNumber().isBlank()){
                    return ResponseEntity.badRequest().body(
                            ResponseObject.builder().status(HttpStatus.BAD_REQUEST)
                                    .data(null)
                                    .message("At least email or phone number is required")
                                    .build());
                } else {
                    //phone number not blank
                    if(!ValidationUtils.isValidPhoneNumber(userDTO.getPhoneNumber())){
                        throw new Exception("Invalid phone number");
                    }
                }
            } else {
                //Email not blank
                if(!ValidationUtils.isValidEmail(userDTO.getEmail())){
                    throw new Exception("Invalid email format");
                }
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest()
                        .body(ResponseObject.builder()
                                .status(HttpStatus.BAD_REQUEST)
                                .data(null)
                                .message(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH))
                                .build());
            }
            User user = userService.createUser(userDTO);
            return ResponseEntity.ok().body(ResponseObject.builder().status(HttpStatus.CREATED)
                    .data(UserResponse.fromUser(user))
                    .message("Accout registration successful")
                    .build());
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null).message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
//            @Valid
            @RequestBody UserLoginDTO userLoginDTO){
        try {
            String token = userService.login(userLoginDTO.getPhone_number(), userLoginDTO.getPassword());
            TokenResponse tokenResponse = new TokenResponse(token);
            System.out.println("Login Successfully");
            System.out.println(tokenResponse.getToken());
            return ResponseEntity.ok().body(tokenResponse);
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvalidParamException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        //Lay token tu header Authorization
        String token = request.getHeader("Authorization");
        if(token == null || !token.startsWith("Bearer ")){
            return ResponseEntity.badRequest().body("Token invalid or not have in request.");
        }
        token = token.substring(7); //Get token follow "Bearer ";
        //Call service to handle logout
        String reponse = userService.logout(token);
        return ResponseEntity.ok(reponse);
    }

    @GetMapping("/verify-token")
    public ResponseEntity<?> veryfyToken(@RequestHeader("Authorization") String token){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Token invalid");
        }

        if(token != null && token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        //Kiem tra token
            String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
            User user = userService.findByPhoneNumber(phoneNumber);
            UserLoginDTO userLoginDTO = UserLoginDTO.builder()
                    .phone_number(user.getPhoneNumber())
                    .password("******")
                    .build();
            return ResponseEntity.ok(userLoginDTO);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("email") String email){
        boolean exists = userService.verifyEmail(email);
        return ResponseEntity.ok(exists ? "exists" : "available");
    }

    @GetMapping("/verify-phone")
    public ResponseEntity<?> verifyPhone(@RequestParam("phone") String phone){
        boolean exists = userService.verifyPhone(phone);
        return ResponseEntity.ok(exists ? "exists" : "available");
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam("to") String to){
        String otp = userService.generateOTP();
        boolean result = userService.sendEmail(to, otp);
        if(result){
            return ResponseEntity.ok().body(otp);
        }
        return (ResponseEntity<String>) ResponseEntity.badRequest();
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(
            @Valid
            @RequestBody UserUpdatePasswordDTO userUpdatePasswordDTO) {
        System.out.println(userUpdatePasswordDTO.toString());
        boolean result = userService.updatePassword(userUpdatePasswordDTO);
        if (result) {
            return ResponseEntity.ok("Password updated successfully!");
        } else {
            return ResponseEntity.badRequest().body("User with provided email not found!");
        }
    }

    @PostMapping("/login-social")
    private ResponseEntity<TokenResponse> loginSocial(@Valid @RequestBody SocialLoginDTO socialLoginDTO, HttpServletRequest request) throws Exception{
        try {
        // Gọi hàm loginSocial từ UserService cho đăng nhập mạng xã hội
        String token = userService.loginSocial(socialLoginDTO);
        TokenResponse tokenResponse = new TokenResponse(token);
        System.out.println("Login Successfully");
        System.out.println(tokenResponse.getToken());
        return ResponseEntity.ok().body(tokenResponse);
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvalidParamException e) {
            throw new RuntimeException(e);
        }
    }
}
