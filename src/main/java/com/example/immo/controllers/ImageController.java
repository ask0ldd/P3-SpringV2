package com.example.immo.controllers;

import java.io.IOException;

import com.example.immo.services.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 127.0.0.1:3001/img/rental/griff.jpg
// 127.0.0.1:3001/img/getimage
// 127.0.0.1:3001/img/images/griff.jpg
@RestController
@RequestMapping("img")
@CrossOrigin(origins = "http://localhost:4200")
public class ImageController {

    private final FileService fileService;

    public ImageController(FileService fileService) {
        this.fileService = fileService;
    }

    @Value("${file.path}")
    private String filePath;

    @GetMapping("/rental/{imageName}")
    public ResponseEntity<Resource> serveImage(@PathVariable String imageName) {
        try {
            Resource resource = fileService.getImgResource(imageName);

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") // !!! Change content type based on your image type
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
