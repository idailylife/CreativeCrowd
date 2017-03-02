package edu.inlab.service.assignment;

import edu.inlab.models.Microtask;
import edu.inlab.models.Task;
import edu.inlab.models.UserMicroTask;
import edu.inlab.models.UserTask;
import edu.inlab.service.MicroTaskService;
import edu.inlab.service.TaskService;
import edu.inlab.service.UserMicrotaskService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * Created by inlab-dell on 2017/3/2.
 * Suited for pairwise comparison tasks
 *
 * params = {
 *     "comparison_size": size of comparisons per assignment,
 *     "trap_question_size": size of trap questions per LEVEL,
 *     "gold_std_q_size": size of golden standard questions per LEVEL,
 *     "max_cmp_size": size of maximum comparisons per assignment,
 *     "trap_threshold": threshold of correct ratio on trap questions, double value,
 *     "gs_threshold": threshold of correct ratio on gold-std questions, double value,
 *     "data" : [
 *          [image_id, text_description],
 *          [image_id, text_description],
 *          ...
 *     ],
 *     "gs_questions": [
 *          [idA, idB],
 *          [idA, idB],...
 *     ]
 * }
 *
 * In-situ status will be put to UserMicroTask.metaInfo, which is a JSONObject:
 * metaInfo = {
 *     "end_of_level": true/false,
 *     "allow_next_level": true/false,
 *     "itemA": {
 *         "image": internal image url/id,
 *         "text": text
 *     },
 *     "itemB": {
 *         "image": internal image url/id,
 *         "text": text
 *     }
 * }
 *
 * UserTask object will hold user quality status in
 * metainfo = {
 *     "g_answered" : num. of answered gold standard questions,
 *     "g_correct" : ...,
 *     "t_answered": num. of answered trap questions,
 *     "t_correct": ...
 * }
 *
 */

@Component
@ComponentScan(basePackages = "edu.inlab")
public class PairwiseQuestionTaskAssigner extends MicroTaskAssigner{
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
    public UserMicroTask assignNextUMT(UserTask userTask) throws RuntimeException {
        //Implement here
        Task task  = taskService.findById(userTask.getTaskId());
        Microtask microtask = microTaskService.getUniqueByTask(task);
        JSONObject taskParams = new JSONObject(task.getParams());

        UserMicroTask userMicroTask = new UserMicroTask(userTask.getId(), microtask.getId());
        JSONObject umtMetaInfo = new JSONObject();

        JSONArray candidates = taskParams.getJSONArray("data");


        return null;
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
    public void onUserMicrotaskSubmit(UserMicroTask userMicroTask, Task task) {

    }
}
