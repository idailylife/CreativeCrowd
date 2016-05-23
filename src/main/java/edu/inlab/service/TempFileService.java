package edu.inlab.service;

import edu.inlab.models.FileBucket;
import edu.inlab.models.TempFile;

/**
 * Created by inlab-dell on 2016/5/20.
 */
public interface TempFileService {
    void save(TempFile tempFile);
    void update(TempFile tempFile);
    TempFile getByUsermicrotaskId(int usermicrotaskId);
}
