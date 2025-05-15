package org.ru.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.ru.backend.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(
            summary = "Upload a file",
            description = "This endpoint allows users to upload a file. It saves the file to S3 and stores its metadata in the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "File uploaded successfully"),
                    @ApiResponse(responseCode = "400", description = "File upload failed due to invalid input"),
                    @ApiResponse(responseCode = "500", description = "Internal server error during file upload")
            }
    )
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = fileService.saveFileAndUpload(file);
            if (fileUrl != null) {
                return ResponseEntity.ok(fileUrl);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File upload failed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Delete a file",
            description = "This endpoint allows users to delete a file by its ID. It removes the file from both the database and S3.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "File deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "File deletion failed due to invalid ID"),
                    @ApiResponse(responseCode = "500", description = "Internal server error during file deletion")
            }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) {
        try {
            fileService.deleteFile(id);
            return ResponseEntity.ok("File deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete file: " + e.getMessage());
        }
    }
}

