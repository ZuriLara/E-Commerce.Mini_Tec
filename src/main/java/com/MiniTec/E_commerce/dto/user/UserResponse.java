package com.MiniTec.E_commerce.dto.user;

import com.MiniTec.E_commerce.dto.role.RoleDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class UserResponse {

    public Long id;
    public String name;
    public String lastname;
    public String email;
    public String image;

    @JsonProperty("notification_token")
    public String notificationToken;

    List<RoleDTO> roles;

}
