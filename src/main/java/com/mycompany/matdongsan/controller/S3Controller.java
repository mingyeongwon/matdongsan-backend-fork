package com.mycompany.matdongsan.controller;

import com.mycompany.matdongsan.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/S3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public void uploadFile(@RequestParam("file")MultipartFile file) {
        try {
            s3Service.uploadFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
