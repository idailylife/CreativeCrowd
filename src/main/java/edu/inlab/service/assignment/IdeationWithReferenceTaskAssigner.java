package edu.inlab.service.assignment;

import edu.inlab.models.*;
import edu.inlab.service.MicroTaskService;
import edu.inlab.service.TaskService;
import edu.inlab.service.UserMicrotaskService;
import edu.inlab.utils.BlobObjectConv;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.sql.Blob;
import java.util.List;
import java.util.Map;

/**
 * Created by hebowei on 2017/1/19.
 * 可附带方案参考的创意设计方案提交任务
 *
 * params = {
 *     "enableRef" : true/false,
 *     "showParent": true/false,                    (valid iff enabRef==true)
 *     "refSize": size of reference item (set),     (valid iff enabRef==true)
 *     "topSize": size of
 * }
 *
 *
 * 如果enableRef==true，则新建的UserMicrotask实例中，results属性会预先填入参考项
 *
 * results = {
 *     *refItems: [
 *          {
 *              umtId : 参考项的UserMicrotask ID,
 *              *parentUmtId: 参考项父节点的UserMicrotask ID (可选)
 *          }, ...
 *
 *     ]    //可选，如果有参考项
 * }
 *
 * Reference的解析使用渲染器里的方法渲染，此处只负责分发
 */
public class IdeationWithReferenceTaskAssigner implements MicroTaskAssigner {
    @Autowired
    TaskService taskService;

    @Autowired
    MicroTaskService microTaskService;

    @Autowired
    UserMicrotaskService userMicrotaskService;

    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public boolean supportAssignUserMicroTask() {
        return true;
    }

    @Override
    public Microtask assignNext(UserTask userTask) throws RuntimeException {
        return null;
    }

    @Override
    public Microtask assignCurrent(UserTask userTask) throws RuntimeException {
        return null;
    }

    @Override
    public UserMicroTask assignNextUMT(UserTask userTask) throws RuntimeException {
        Task task = taskService.findById(userTask.getTaskId());
        Microtask microtask = microTaskService.getUniqueByTask(task);

        JSONObject taskParams = new JSONObject(task.getParams());
        UserMicroTask userMicroTask = new UserMicroTask(userTask.getId(), microtask.getId());
        if(taskParams.getBoolean("enableRef")){
            // Reference enabled
            int refSize = taskParams.getInt("refSize");
            IdeationTaskConfig taskConfig = IdeationTaskConfig.ReadFromBlob(task.getConfigBlob());
            List<Integer> topRankedUmtIds = taskConfig.getTopRankedItems()
        }
        return userMicroTask;
    }

    @Override
    public void onUserMicrotaskSubmit(UserMicroTask userMicroTask, Task task) {

    }



}
