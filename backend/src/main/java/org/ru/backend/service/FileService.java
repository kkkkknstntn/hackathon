package org.ru.backend.service;

import org.ru.backend.entity.File;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    File saveFile(String objectKey, String fileUrl);

    void deleteFile(Long id);

    String saveFileAndUpload(MultipartFile file);
}
