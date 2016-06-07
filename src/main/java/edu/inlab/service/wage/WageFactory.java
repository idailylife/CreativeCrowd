package edu.inlab.service.wage;

import edu.inlab.models.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by inlab-dell on 2016/6/7.
 */
@Service("wageFactory")
public class WageFactory {

    public static WageAssigner getAssigner(Integer type){
        if(type == Task.WAGE_TYPE_PER_TASK){
            return new TaskOneShotWageAssigner();
        }
        return null;
    }
}
