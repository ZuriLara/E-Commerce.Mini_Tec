package com.MiniTec.E_commerce.services;

import com.MiniTec.E_commerce.dto.user.CreateUserRequest;
import com.MiniTec.E_commerce.dto.user.CreateUserResponse;
import com.MiniTec.E_commerce.dto.role.RoleDTO;
import com.MiniTec.E_commerce.dto.user.LoginRequest;
import com.MiniTec.E_commerce.dto.user.LoginResponse;
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

import java.util.List;


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

    @Transactional
    public CreateUserResponse create(CreateUserRequest request) {
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

        CreateUserResponse response = new CreateUserResponse();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setLastname(savedUser.getLastname());
        response.setImage(savedUser.getImage());
        response.setEmail(savedUser.getEmail());

        List<Role> roles = roleRepository.findAllByUserHasRoles_User_Id(savedUser.getId());
        List<RoleDTO> rolesDTOS = roles.stream()
                        .map(role -> new RoleDTO(role.getId(), role.getName(), role.getImage(), role.getRoute()))
                                .toList();

        response.setRoles(rolesDTOS);
        return response;
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
        List<RoleDTO> rolesDTOS = roles.stream()
                .map(role -> new RoleDTO(role.getId(), role.getName(), role.getImage(), role.getRoute()))
                .toList();
        CreateUserResponse createUserResponse = new CreateUserResponse();
        createUserResponse.setId(user.getId());
        createUserResponse.setName(user.getName());
        createUserResponse.setLastname(user.getLastname());
        createUserResponse.setImage(user.getImage());
        createUserResponse.setEmail(user.getEmail());
        createUserResponse.setRoles(rolesDTOS);

        LoginResponse response = new LoginResponse();
        response.setToken("Bearer "+ token);
        response.setUser(createUserResponse);

        return response;

    }

    @Transactional
    public CreateUserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("el Email o Password no son validos"));

        List<Role> roles = roleRepository.findAllByUserHasRoles_User_Id(user.getId());
        List<RoleDTO> rolesDTOS = roles.stream()
                .map(role -> new RoleDTO(role.getId(), role.getName(), role.getImage(), role.getRoute()))
                .toList();
        CreateUserResponse createUserResponse = new CreateUserResponse();
        createUserResponse.setId(user.getId());
        createUserResponse.setName(user.getName());
        createUserResponse.setLastname(user.getLastname());
        createUserResponse.setImage(user.getImage());
        createUserResponse.setEmail(user.getEmail());
        createUserResponse.setRoles(rolesDTOS);

        return  createUserResponse;
    }
}
