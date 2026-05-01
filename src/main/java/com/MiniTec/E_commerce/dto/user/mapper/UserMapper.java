package com.MiniTec.E_commerce.dto.user.mapper;

import com.MiniTec.E_commerce.config.APIConfig;
import com.MiniTec.E_commerce.dto.role.RoleDTO;
import com.MiniTec.E_commerce.dto.user.UserResponse;
import com.MiniTec.E_commerce.models.Role;
import com.MiniTec.E_commerce.models.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user, List<Role> roles){

        List<RoleDTO> rolesDTOS = roles.stream()
                .map(role -> new RoleDTO(role.getId(), role.getName(), role.getImage(), role.getRoute()))
                .toList();
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setLastname(user.getLastname());
        userResponse.setImage(user.getImage());
        userResponse.setEmail(user.getEmail());
        userResponse.setRoles(rolesDTOS);

        if (user.getImage() != null) {
            String imageUrl = APIConfig.BASE_URL + user.getImage();
            userResponse.setImage(imageUrl);

        }

        return userResponse;
        // Si se quiere dejar mas "limpio" el codigo, cambiar el nombre del objeto UserReponse a solo Reponse
    }
}
