package com.trivia.persistence.dto.server;

import java.sql.Timestamp;

public class Log {
    private Timestamp timestamp;
    private String message;

    public Log() {

    }

    public Log(String rawLog) {
        this.message = rawLog;
    }

    public Log(String message, Timestamp timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
