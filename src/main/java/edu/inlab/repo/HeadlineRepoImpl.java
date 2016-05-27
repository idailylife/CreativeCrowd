package edu.inlab.repo;

import edu.inlab.models.Headline;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by inlab-dell on 2016/5/27.
 */
@Repository("headlineRepo")
public class HeadlineRepoImpl extends AbstractDao<Integer, Headline> implements HeadlineRepository {
    public List<Headline> getAll() {
        return getSession().createCriteria(Headline.class).list();
    }
}
