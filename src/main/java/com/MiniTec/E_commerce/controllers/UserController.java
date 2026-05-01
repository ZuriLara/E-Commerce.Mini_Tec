package com.MiniTec.E_commerce.controllers;


import com.MiniTec.E_commerce.dto.user.*;
import com.MiniTec.E_commerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody CreateUserRequest request) {
        UserResponse user = userService.create(request);
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            UserResponse response = userService.findById(id);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", e.getMessage(),
                    "statusCode", HttpStatus.NOT_FOUND.value()
            ));

        }

    }

    @PutMapping(value = "/upload/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @ModelAttribute UpdateUserRequest request
            ) {
        try {
            UserResponse response = userService.updateUserWithImage(id, request);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", e.getMessage(),
                    "statusCode", HttpStatus.NOT_FOUND.value()
            ));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", e.getMessage(),
                    "statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value()
            ));
        }

    }
}
