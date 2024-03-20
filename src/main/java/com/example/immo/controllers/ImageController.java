package com.example.immo.controllers;

import java.io.IOException;
import java.io.InputStream;

import com.example.immo.services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 127.0.0.1:3001/img/rental/griff.jpg
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

    @GetMapping(value = "/rental/{imageName}", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    public void getImage(HttpServletResponse response, @PathVariable String imageName) throws IOException {
        InputStream stream = fileService.getResource(imageName);
        StreamUtils.copy(stream, response.getOutputStream());
    }
}
