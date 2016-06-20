package edu.inlab.models;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by inlab-dell on 2016/6/20.
 */
public class SingleFileBucket {
    MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
