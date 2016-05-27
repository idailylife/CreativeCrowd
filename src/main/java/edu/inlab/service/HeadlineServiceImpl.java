package edu.inlab.service;

import edu.inlab.models.Headline;
import edu.inlab.repo.HeadlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by inlab-dell on 2016/5/27.
 */
@Transactional
@Service("headlineService")
public class HeadlineServiceImpl implements HeadlineService {

    @Autowired
    HeadlineRepository headlineRepository;

    public List<Headline> getAll() {
        return headlineRepository.getAll();
    }
}
