package org.ru.backend.service.impl;

import org.ru.backend.entity.File;
import org.ru.backend.repository.FileRepository;
import org.ru.backend.service.FileService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.ru.backend.service.S3Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final S3Service s3Service;

    @Override
    public File saveFile(String objectKey, String fileUrl) {

        File file =  File.builder()
                .objectKey(objectKey)
                .fileUrl(fileUrl)
                .createdAt(Instant.now())
                .build();
        return fileRepository.save(file);
    }

    @Override
    public void deleteFile(Long id) {
        File file = fileRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
        s3Service.deleteFile(file.getObjectKey());
        fileRepository.delete(file);
    }

    @Override
    public String saveFileAndUpload(MultipartFile file) {
        String fileUrl = s3Service.uploadFile(file);
        if (fileUrl != null) {
            String objectKey = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

            saveFile(objectKey, fileUrl);
            return fileUrl;
        }
        return null;
    }
}
