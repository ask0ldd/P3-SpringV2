package com.example.immo.utils;

public class UniqueFilenameGenerator {
    public static String generate(String prefix, String suffix){
        return (prefix != null ? prefix : "") + System.nanoTime() + (suffix != null ? suffix : "");
    }
}
