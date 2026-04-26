package com.MiniTec.E_commerce.services;

import com.MiniTec.E_commerce.dto.user.CreateUserRequest;
import com.MiniTec.E_commerce.models.User;
import com.MiniTec.E_commerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email)){
            throw new RuntimeException("El correo ya esta registrado");
        }
        User user = new User();
        user.setName(request.name);
        user.setLastname(request.lastname);
        user.setEmail(request.email);
        user.setPassword(request.password);
        return userRepository.save(user);
    }

}
