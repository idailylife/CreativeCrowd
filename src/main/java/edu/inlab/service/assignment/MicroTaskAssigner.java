package edu.inlab.service.assignment;

import edu.inlab.models.Microtask;

import edu.inlab.models.Task;
import edu.inlab.models.UserMicroTask;
import edu.inlab.models.UserTask;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.sql.Blob;

/**
 * Created by inlab-dell on 2016/5/31.
 * 该接口负责实时分配某一个task中的某个microtask给指定用户
 */


public abstract class MicroTaskAssigner {

    /**
     * Does this microtaskAssigner support configure file (downloading/uploading)?
     * @return
     */
    public boolean supportConfigFile(){
        return false;
    }

    public Blob updateConfigFile(Blob taskBlob, Object... inputs){
        return null;
    }

    public ByteArrayOutputStream exportConfigFile(Task task){
        return null;
    }

    /**
     *
     * @return Set to true if the microtask template will be filled upon user request (not stored constantly)
     */
    public abstract boolean isTransient();

    /**
     *
     * @return Set to true if assigning UserMicrotask instance directly, instead of assigning Microtask.
     *
     */
    public abstract boolean supportAssignUserMicroTask();


    /**
     * 分配下一个UserMicrotask
     * supportAssignUserMicroTask()==true时使用本方法
     * @param userTask
     * @return
     * @throws RuntimeException
     */
    public abstract UserMicroTask assignNextUMT(UserTask userTask) throws RuntimeException;

    /**
     * 分配下一个microtask
     * @param userTask
     * @return 下一项microtask，如果用户任务结束，可以为null
     */
    public abstract Microtask assignNext(UserTask userTask) throws RuntimeException;

    /**
     * FOR TRANSIENT TASK ONLY
     * 分配当前的microtask (给随机任务用)
     * @param userTask
     * @return
     * @throws RuntimeException
     */
    public abstract Microtask assignCurrent(UserTask userTask) throws RuntimeException;

    public abstract void onUserMicrotaskSubmit(UserMicroTask userMicroTask, Task task);

    public final static int TASK_ASSIGN_SINGLE = 0;
    public final static int TASK_ASSIGN_RANDOM = 1;
    public final static int TASK_ASSIGN_SEQUENCE = 2;
    public final static int TASK_ASSIGN_SINGLE_RANDOM = 3;
    public final static int TASK_ASSIGN_SINGLE_WITH_REF = 4;
}
