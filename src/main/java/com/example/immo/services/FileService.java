package com.example.immo.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Value("${file.path}")
    private String filePath;

    // should check if file not already exists
    // check if jpg / png
    public String save(MultipartFile file) {
        String dir = System.getProperty("user.dir") + "/src/main/resources/static/" + filePath;
        try {
            String filename = "";
            // check if png or jpg file & generate a unique filename
            if(Objects.equals(file.getContentType(), "image/jpeg")) filename = generateUniqueFileName("img-", ".jpg");
            if(Objects.equals(file.getContentType(), "image/png")) filename = generateUniqueFileName("img-", ".png");
            if(filename.isEmpty()) throw new RuntimeException("Unsupported File Type.");
            file.transferTo(new File(dir + filename));
            return filename;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Resource getImgResource(String fileName) throws IOException{
        String IMAGE_FOLDER = System.getProperty("user.dir") + "/src/main/resources/static/" + filePath;
        Path imagePath = Paths.get(IMAGE_FOLDER).resolve(fileName);
        return new UrlResource(imagePath.toUri());
    }

    public static String generateUniqueFileName(String prefix, String suffix) {
        return (prefix != null ? prefix : "") + System.nanoTime() + (suffix != null ? suffix : "");
    }

    public void deleteAll() {
        String IMAGE_FOLDER = System.getProperty("user.dir") + "/src/main/resources/static/" + filePath;
        Path imagePath = Paths.get(IMAGE_FOLDER);
        FileSystemUtils.deleteRecursively(imagePath.toFile());
        try {
            // Create the directory
            Files.createDirectories(imagePath);
            System.out.println("Directory created successfully.");
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + e.getMessage());
        }
    }
}
