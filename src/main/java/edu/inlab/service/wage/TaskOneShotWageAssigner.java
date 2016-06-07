package edu.inlab.service.wage;

import edu.inlab.models.Task;
import edu.inlab.models.UserTask;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by inlab-dell on 2016/6/7.
 * 直接根据Task中wage的数值发钱
 */

public class TaskOneShotWageAssigner implements WageAssigner {
    public Double assignWage(Task task, UserTask userTask) {
        return assignWage(task);
    }

    public Double assignWage(Task task) {
        Double retVal = null;
        try{
            retVal = Double.valueOf(task.getWage());
        } catch (NullPointerException e){
            e.printStackTrace();
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
        return retVal;
    }
}
