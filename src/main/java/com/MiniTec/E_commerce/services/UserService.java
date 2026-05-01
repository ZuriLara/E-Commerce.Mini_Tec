package com.MiniTec.E_commerce.services;

import com.MiniTec.E_commerce.dto.user.*;
import com.MiniTec.E_commerce.dto.role.RoleDTO;
import com.MiniTec.E_commerce.dto.user.mapper.UserMapper;
import com.MiniTec.E_commerce.models.Role;
import com.MiniTec.E_commerce.models.User;
import com.MiniTec.E_commerce.models.UserHasRoles;
import com.MiniTec.E_commerce.repositories.RoleRepository;
import com.MiniTec.E_commerce.repositories.UserHasRolesRepository;
import com.MiniTec.E_commerce.repositories.UserRepository;
import com.MiniTec.E_commerce.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

// TODO: Quitar los espacios sobrantes

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

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email)){
            throw new RuntimeException("El correo ya esta registrado");
        }
        User user = new User();
        user.setName(request.name);
        user.setLastname(request.lastname);
        user.setEmail(request.email);
        String encryptedPassword = passwordEncoder.encode(request.password);
        user.setPassword(encryptedPassword);

        //ESTE CAMPO SE USA CUANDO HAYA FALLAS EN EL POSTMAN
        user.setNotificationToken("default-token");

        User savedUser = userRepository.save(user);
        Role clientRole = roleRepository.findById("CLIENT").orElseThrow(
                () -> new RuntimeException("El rol de cliente no existe")
        );
        UserHasRoles userHasRoles = new UserHasRoles(savedUser, clientRole);
        userHasRolesRepository.save(userHasRoles);

        List<Role> roles = roleRepository.findAllByUserHasRoles_User_Id(savedUser.getId());

        return userMapper.toUserResponse(user, roles);
    }
    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("el Email o Password no son validos"));
        if (!passwordEncoder.matches(request.getPassword(),user.getPassword())) {
            throw new RuntimeException("El Email o Password no son validos");
        }
        String token = jwtUtil.generateToken(user);
        List<Role> roles = roleRepository.findAllByUserHasRoles_User_Id(user.getId());
        LoginResponse response = new LoginResponse();
        response.setToken("Bearer "+ token);
        response.setUser(userMapper.toUserResponse(user, roles));

        return response;

    }

    @Transactional
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("el Email o Password no son validos"));

        List<Role> roles = roleRepository.findAllByUserHasRoles_User_Id(user.getId());


        return userMapper.toUserResponse(user, roles);

    }


    @Transactional
    public UserResponse updateUserWithImage(Long id, UpdateUserRequest request) throws IOException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("el Email o Password no son validos"));

        if (request.getName() != null) {
            user.setName(request.getName());
        }

        if (request.getLastname() != null) {
            user.setLastname(request.getLastname());
        }

        if (request.getFile() != null && !request.getFile().isEmpty()) {
            String uploadDir = "uploads/users/" + user.getId();
            String filename = request.getFile().getOriginalFilename();
            String filePath = Paths.get(uploadDir, filename).toString();

            Files.createDirectories(Paths.get(uploadDir));
            Files.copy(request.getFile().getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            user.setImage("/" + filePath.replace("\\", "/"));
        }

        userRepository.save(user);

        List<Role> roles = roleRepository.findAllByUserHasRoles_User_Id(user.getId());


        return userMapper.toUserResponse(user, roles);

    }
}
