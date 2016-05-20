package edu.inlab.models;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by inlab-dell on 2016/5/20.
 */
public class FileBucket {
    MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
