package com.MiniTec.E_commerce.dto.user;


import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private UserResponse user;


}
