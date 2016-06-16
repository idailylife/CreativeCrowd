package edu.inlab.service;

import edu.inlab.models.Microtask;
import edu.inlab.repo.MicroTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by inlab-dell on 2016/5/16.
 */
@Service("microTaskService")
public class MicroTaskServiceImpl implements MicroTaskService {
    @Autowired
    MicroTaskRepository microTaskRepository;

    public Microtask getById(int id) {
        return microTaskRepository.getById(id);
    }

    public void save(Microtask microtask) {
        microTaskRepository.save(microtask);
    }

    @Override
    public void delete(Microtask microtask) {
        microTaskRepository.remove(microtask);
    }

    @Override
    public void saveOrUpdate(Microtask microtask) {
        microTaskRepository.update(microtask);
    }
}
