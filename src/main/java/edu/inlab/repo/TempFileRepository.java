package edu.inlab.repo;

import edu.inlab.models.TempFile;

/**
 * Created by inlab-dell on 2016/5/20.
 */
public interface TempFileRepository {
    TempFile getbyId(int id);
    TempFile getByUserMicrotaskId(int userMicrotaskId);
    void save(TempFile tempFile);
    void deleteRecord(TempFile tempFile);
}
