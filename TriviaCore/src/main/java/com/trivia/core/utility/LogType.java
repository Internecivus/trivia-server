package com.trivia.core.utility;

public enum LogType {
    ADMIN("admin.log"),
    SERVER("server.log");

    private String fileName;

    LogType(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}