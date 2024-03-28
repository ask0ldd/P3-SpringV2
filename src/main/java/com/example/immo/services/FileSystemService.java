package com.example.immo.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import com.example.immo.exceptions.UnsupportedFileTypeException;
import com.example.immo.services.interfaces.IFileSystemService;
import com.example.immo.utils.UniqueFilenameGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemService implements IFileSystemService {

    @Value("${file.path}")
    private String filePath;

    // should check if file not already exists
    // check if jpg / png
    public String saveImg(MultipartFile file) {
        String dir = System.getProperty("user.dir") + "/src/main/resources/static/" + filePath;
        try {
            String filename = "";
            // check if png or jpg file & generate a unique filename
            if(Objects.equals(file.getContentType(), "image/jpeg")) filename = UniqueFilenameGenerator.generate("img-", ".jpg");
            if(Objects.equals(file.getContentType(), "image/png")) filename = UniqueFilenameGenerator.generate("img-", ".png");
            // if no filename has been assigned, then the file type is not supported
            if(filename.isEmpty()) throw new UnsupportedFileTypeException("Unsupported File Type.");
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

    public void deleteAllFiles() {
        String IMAGE_FOLDER = System.getProperty("user.dir") + "/src/main/resources/static/" + filePath;
        Path imagePath = Paths.get(IMAGE_FOLDER);
        try {
            FileSystemUtils.deleteRecursively(imagePath.toFile());
            // Create the directory
            Files.createDirectories(imagePath);
            System.out.println("Directory created successfully.");
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + e.getMessage());
        }
    }
}
