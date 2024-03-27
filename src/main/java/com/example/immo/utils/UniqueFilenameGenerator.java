package com.example.immo.utils;

public class UniqueFilenameGenerator {
    // generates a unique filename for the uploaded picture before storage
    public static String generate(String prefix, String suffix){
        return (prefix != null ? prefix : "") + System.nanoTime() + (suffix != null ? suffix : "");
    }
}
