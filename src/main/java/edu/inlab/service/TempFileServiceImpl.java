package edu.inlab.service;

import edu.inlab.models.FileBucket;
import edu.inlab.models.TempFile;
import edu.inlab.repo.TempFileRepository;
import edu.inlab.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by inlab-dell on 2016/5/20.
 */
@Service("tempFileService")
@Transactional
public class TempFileServiceImpl implements TempFileService {
    @Autowired
    TempFileRepository tempFileRepository;

    public void save(TempFile tempFile) {
        tempFileRepository.save(tempFile);
    }

    public TempFile getByUsermicrotaskId(int usermicrotaskId) {
        return tempFileRepository.getByUserMicrotaskId(usermicrotaskId);
    }

    public void update(TempFile tempFile) {
        TempFile entity = tempFileRepository.getbyId(tempFile.getId());
        if(null != entity){
            entity.setFilename(tempFile.getFilename());
            entity.setUsermicrotaskId(tempFile.getUsermicrotaskId());
        }
    }

    public void delete(TempFile tempFile) {
        tempFileRepository.deleteRecord(tempFile);
    }
}
