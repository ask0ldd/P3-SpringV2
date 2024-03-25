package com.example.immo.services.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileSystemService {

    String saveImg(MultipartFile file);
    Resource getImgResource(String fileName) throws IOException;
    void deleteAllFiles();
}
