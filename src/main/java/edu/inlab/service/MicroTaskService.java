package edu.inlab.service;

import edu.inlab.models.Microtask;

/**
 * Created by inlab-dell on 2016/5/16.
 */
public interface MicroTaskService {
    Microtask getById(int id);
    void save(Microtask microtask);
}
