package edu.inlab.repo;

import edu.inlab.models.Headline;

import java.util.List;

/**
 * Created by inlab-dell on 2016/5/27.
 */
public interface HeadlineRepository {
    List<Headline> getAll();
}
