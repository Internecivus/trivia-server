package com.trivia.persistence.dto.client;

import java.util.Date;

public class ImageData {
    private String path;
    private Date dateCreated;

    public ImageData(String path, Date dateCreated) {
        this.path = path;
        this.dateCreated = dateCreated;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
