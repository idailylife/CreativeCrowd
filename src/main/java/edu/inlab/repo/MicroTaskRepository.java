package edu.inlab.repo;


import edu.inlab.models.Microtask;

import java.util.List;

/**
 * Created by hebowei on 16/5/14.
 */
public interface MicroTaskRepository{
    Microtask getById(int id);
    void save(Microtask microtask);
    void update(Microtask microtask);
    void remove(Microtask microtask);
    List<Microtask> getByTaskId(int taskId);
}
