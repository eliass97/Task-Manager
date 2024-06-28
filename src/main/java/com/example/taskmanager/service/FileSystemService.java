package com.example.taskmanager.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.taskmanager.exception.CustomException;

@Service
public class FileSystemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemService.class);

    @Value("${file-system.path}")
    private String fileSystemPath;

    public String storeUserImage(Integer userId, MultipartFile image) throws CustomException {
        String userImagesPath = constructUserImagesPath(userId);
        createDirectories(userImagesPath);

        try {
            String imagePath = userImagesPath + image.getOriginalFilename();
            LOGGER.info("Storing image on path: {}", imagePath);
            File file = new File(imagePath);
            image.transferTo(file);
            return file.getAbsolutePath();
        } catch (IOException exception) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store the image in the file system");
        }
    }

    private String constructUserImagesPath(Integer userId) {
        return fileSystemPath + "/user_" + userId + "/images/";
    }

    private void createDirectories(String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException exception) {
            LOGGER.error("Failed to create initial directories for {}", fileSystemPath);
        }
    }

    public byte[] loadUserImage(String filePath) throws CustomException {
        try {
            Path path = new File(filePath).toPath();
            return Files.readAllBytes(path);
        } catch (IOException exception) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to load the image from the file system");
        }
    }
}
