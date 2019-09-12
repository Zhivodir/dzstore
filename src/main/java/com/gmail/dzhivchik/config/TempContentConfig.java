package com.gmail.dzhivchik.config;

public class TempContentConfig {
    final static public long MAX_SIZE_OF_FILE = 5120;

    public static boolean isExceedMaximumFileSize(long fileSize) {
        return fileSize > TempContentConfig.MAX_SIZE_OF_FILE;
    }
}
