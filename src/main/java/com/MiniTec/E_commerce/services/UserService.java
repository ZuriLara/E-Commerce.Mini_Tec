package com.MiniTec.E_commerce.services;

import com.MiniTec.E_commerce.dto.user.CreateUserRequest;
import com.MiniTec.E_commerce.models.Role;
import com.MiniTec.E_commerce.models.User;
import com.MiniTec.E_commerce.models.UserHasRoles;
import com.MiniTec.E_commerce.repositories.RoleRepository;
import com.MiniTec.E_commerce.repositories.UserHasRolesRepository;
import com.MiniTec.E_commerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserHasRolesRepository userHasRolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email)){
            throw new RuntimeException("El correo ya esta registrado");
        }
        User user = new User();
        user.setName(request.name);
        user.setLastname(request.lastname);
        user.setEmail(request.email);
        String encryptedPassword = passwordEncoder.encode(request.password);
        user.setPassword(encryptedPassword);


        user.setNotificationToken("default-token");

        User savedUser = userRepository.save(user);
        Role clientRole = roleRepository.findById("CLIENT").orElseThrow(
                () -> new RuntimeException("El rol de cliente no existe")
        );
        UserHasRoles userHasRoles = new UserHasRoles(savedUser, clientRole);
        userHasRolesRepository.save(userHasRoles);

        return savedUser;
    }

}
