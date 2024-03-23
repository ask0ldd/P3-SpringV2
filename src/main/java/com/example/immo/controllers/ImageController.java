package com.example.immo.controllers;

import java.util.Objects;

import com.example.immo.dto.responses.DefaultResponseDto;
import com.example.immo.services.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> serveImage(@PathVariable String imageName) {
        try {
            // get the image as a resource
            Resource resource = fileService.getImgResource(imageName);
            // check if the image is a jpg or a png
            String contentType = "image/jpeg";
            if(Objects.requireNonNull(resource.getFilename()).contains(".png")) contentType = "image/png";
            // send back a response with a header matching the image type
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, contentType)
                        .body(resource);
            } else {
                return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("This image can't be found."), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("This image can't be found."), HttpStatus.NOT_FOUND);
        }
    }
}
