package edu.inlab.service;

import edu.inlab.models.Microtask;
import edu.inlab.models.Task;

import java.util.List;

/**
 * Created by inlab-dell on 2016/5/16.
 */
public interface MicroTaskService {
    Microtask getById(int id);
    void save(Microtask microtask);
    void delete(Microtask microtask);
    void saveOrUpdate(Microtask microtask);
    Microtask getUniqueByTask(Task task);
}
