package com.mycompany.matdongsan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    private String createFileName(String originalFileName) {
        return UUID.randomUUID() + "_" + originalFileName;
    }

    public String uploadFile(MultipartFile file) {
        String fileName = createFileName(file.getOriginalFilename());
        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();
            s3Client.putObject(objectRequest, RequestBody.fromBytes(file.getBytes()));

            return getFileUrl(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String getFileUrl(String fileName) {
        GetUrlRequest request = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        URL url = s3Client.utilities().getUrl(request);
        return url.toString();
    }
}
