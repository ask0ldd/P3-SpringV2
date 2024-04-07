package com.example.immo.controllers;

import java.util.Objects;

import com.example.immo.dto.responses.DefaultResponseDto;
import com.example.immo.services.FileSystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    private final FileSystemService fileSystemService;
    public ImageController(FileSystemService fileSystemService) {
        this.fileSystemService = fileSystemService;
    }

    @Value("${file.path}")
    private String filePath;

    @GetMapping("/rental/{imageName}")
    @Operation(summary = "Retrieve an Image", description = "Endpoint to retrieve an Image.", tags = {"Image"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image served.", content = { @Content(mediaType = "image/png"), @Content(mediaType = "image/jpeg") }),
            @ApiResponse(responseCode = "404", description = "Image not found."),
            @ApiResponse(responseCode = "406", description = "Not a valid Image."),
    })
    public ResponseEntity<?> serveImage(@PathVariable String imageName) {
        try {
            // get the image as a resource
            Resource resource = fileSystemService.getImgResource(imageName);
            if (resource.exists() || resource.isReadable()) {
                // check if the image is a jpg or a png
                String mimeType = resource.getURL().openConnection().getContentType();
                // if not
                if(!Objects.equals(mimeType, "image/jpeg") && !Objects.equals(mimeType, "image/png"))
                    return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("Not a valid Image."), HttpStatus.NOT_ACCEPTABLE);
                // & send back a response with a header matching the image type
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, mimeType).body(resource);
            } else {
                return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("This image can't be found."), HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            System.out.println("\u001B[31m" + exception + "\u001B[0m");
            return new ResponseEntity<DefaultResponseDto>(new DefaultResponseDto("This image can't be found."), HttpStatus.NOT_FOUND);
        }
    }
}
