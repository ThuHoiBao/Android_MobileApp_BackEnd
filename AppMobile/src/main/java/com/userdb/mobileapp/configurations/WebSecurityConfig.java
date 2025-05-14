package com.userdb.mobileapp.configurations;

import com.userdb.mobileapp.entity.Role;
import com.userdb.mobileapp.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor  // annotation cua Lombok, tu dong tao constructor
//cho các thuoc tinh final, giup giam bot ma lenh lap.
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    /*
        Khi gui request GET /api/v1/products
        1.Request di vao SecurityFilterChain
        2. JwtTokenFilter se kiem tra token JWT trong header Authorization
        3. Neu token hop le:
            - Lay role trong token (USEER hoac ADMIN)
            - Kiem tra xem role do co quyen GET /product/** khong
        4.Neu khong du quyen hoac token sau, se tra loi 403 hoac 401.
     */
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                /*
                    * Bo loc jwtTokenFilter se chay truoc bo loc mac dinh cua Spring Security (UsernamePasswordAuthenticationFilter)
                    * Muc dich: Kiem tra JWT token truoc khi xu ly xac thuc nguoi dung
                 */
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> {
                    request
                            .requestMatchers(
                                    String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix),
                                    String.format("%s/users/verify-email", apiPrefix),
                                    String.format("%s/users/verify-phone", apiPrefix),
                                    String.format("%s/users/send-otp", apiPrefix),
                                    String.format("%s/users/update-password", apiPrefix),
                                    String.format("%s/payments/**", apiPrefix),
                                    String.format("%s/users/login-social", apiPrefix),
                                    String.format("%s/payments/create_payment_url", apiPrefix),
                                    "/api/product/summary",

                                    "/api/products/**",
                                    "/api/orders/**",
                                    "/api/reviews/**",
                                    "/api/review/**",


                                    "api/review/productName",
                                    "/api/cart/items",
                                    "/api/cart/update",
                                    "/api/cart/add",
                                    "/api/address/default",
                                    "/api/address/delete" ,
                                    "/api/cart/item",
                                    "/api/address/items",
                                    "/api/address/update",
                                    "/api/address/setDefault",
                                    "/api/address/add",
                                    "/api/cart/update-cart",
                                    "/api/orders/from-cart",
                                    "/api/categories/**",
                                    "/api/v1/users/profile/**",
                                    "/api/v1/users/profile/update/**"

                            ).permitAll()

//                            //Categories
//                            .requestMatchers("POST",
//                                    String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
//                            .requestMatchers("PUT",
//                                    String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
//                            .requestMatchers("GET",
//                                    String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
//                            .requestMatchers("DELETE",
//                                    String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
//
//
//                            //Products
//                            .requestMatchers("POST",
//                                    String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
//                            .requestMatchers("PUT",
//                                    String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
//                            .requestMatchers("GET",
//                                    String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
//                            .requestMatchers("DELETE",
//                                    String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
//
//                            //Orders
//                            .requestMatchers(POST,
//                                    String.format("%s/orders/**", apiPrefix)).hasAnyRole(Role.USER)
//                            .requestMatchers(GET,
//                                    String.format("%s/orders/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.USER)
//                            .requestMatchers(PUT,
//                                    String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
//                            .requestMatchers(DELETE,
//                                    String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
//
//                            //Order_detail
//                            .requestMatchers(POST,
//                                    String.format("%s/order_details/**", apiPrefix)).hasAnyRole(Role.USER)
//                            .requestMatchers(GET,
//                                    String.format("%s/order_details/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.USER)
//                            .requestMatchers(PUT,
//                                    String.format("%s/order_details/**", apiPrefix)).hasRole(Role.ADMIN)
//                            .requestMatchers(DELETE,
//                                    String.format("%s/order_details/**", apiPrefix)).hasRole(Role.ADMIN)

                            //verify-token
                            .requestMatchers(GET, String.format("%s/users/verify-token", apiPrefix)).hasAnyRole(Role.ADMIN, Role.USER)
                            .anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
