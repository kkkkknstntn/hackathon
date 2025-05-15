package org.ru.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ru.backend.enums.BusinessErrorCodes;
import org.ru.backend.exception.ApiException;
import org.ru.backend.service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Client s3Client;

    @Value("${s3.bucket}")
    private String bucketName;

    @Value("${s3.url}")
    private String minioUrl;

    public String uploadFile(MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
                log.info("Uploading file {} to bucket {}", file.getOriginalFilename(), bucketName);

                PutObjectRequest putRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(file.getContentType())
                        .build();

                s3Client.putObject(putRequest, RequestBody.fromBytes(file.getBytes()));
                log.info("Successfully uploaded file: {}", file.getOriginalFilename());
                return minioUrl + "/" + bucketName + "/" + key;
            } return null;
        } catch (IOException e) {
            log.error("File upload failed: {}", file.getOriginalFilename(), e);
            throw new ApiException(
                    BusinessErrorCodes.FILE_UPLOAD_FAILED,
                    "Failed to upload file: " + file.getOriginalFilename()
            );
        }
    }

    @Override
    public void deleteFile(String objectKey) {
        try {
            log.info("Deleting file with object key: {}", objectKey);
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            s3Client.deleteObject(deleteRequest);
            log.info("Successfully deleted file with object key: {}", objectKey);
        } catch (Exception e) {
            log.error("File deletion failed for object key: {}", objectKey, e);
            throw new RuntimeException("Failed to delete file: " + objectKey, e);
        }
    }
}