package com.gmail.dzhivchik.config;

public class TempContentConfig {
    public final static long MAX_SIZE_OF_FILE = 5120;
    public final static int BUFFER_SIZE = 1024;

    public final static String SYMBOL_FOR_ARCHIEVE_NAME = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static boolean isExceedMaximumFileSize(long fileSize) {
        return fileSize > TempContentConfig.MAX_SIZE_OF_FILE;
    }
}
