package com.MiniTec.E_commerce.dto.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserRequest {

    private String name;
    private String lastname;
    private MultipartFile file;
}
