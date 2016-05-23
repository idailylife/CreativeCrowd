package edu.inlab.repo;

import edu.inlab.models.TempFile;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by inlab-dell on 2016/5/20.
 */
@Repository("tempFileRepository")
public class TempFileRepoImpl extends AbstractDao<Integer, TempFile>
    implements TempFileRepository{
    public TempFile getbyId(int id){
        return getByKey(id);
    }

    public TempFile getByUserMicrotaskId(int userMicrotaskId) {
        List<TempFile> tempFiles = getSession().createCriteria(TempFile.class)
                .add(Restrictions.eq("usermicrotaskId", userMicrotaskId))
                .setMaxResults(1)
                .list();
        if(tempFiles!=null && !tempFiles.isEmpty()){
            return tempFiles.get(0);
        } else {
            return null;
        }
    }

    public void save(TempFile tempFile) {
        persist(tempFile);
    }

    public void deleteRecord(TempFile tempFile) {
        delete(tempFile);
    }
}
