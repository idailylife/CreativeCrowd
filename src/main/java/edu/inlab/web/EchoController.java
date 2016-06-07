package edu.inlab.web;

import edu.inlab.models.Task;
import edu.inlab.models.UserTask;
import edu.inlab.models.json.AjaxResponseBody;
import edu.inlab.models.json.UserEchoRequestBody;
import edu.inlab.service.TaskService;
import edu.inlab.service.UserService;
import edu.inlab.service.UserTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.io.IOException;

/**
 * Created by inlab-dell on 2016/6/2.
 * 维护用户、任务状态
 */

@Controller
@RequestMapping(value = "/echo")
public class EchoController {

    @Autowired
    UserService userService;

    @Autowired
    UserTaskService userTaskService;

    @Autowired
    TaskService taskService;

    /**
     * Maintain user login / task validaty state during mturk task.
     * @param echoRequestBody
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/mturk", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public AjaxResponseBody userEchoMTurk(@Valid @RequestBody UserEchoRequestBody echoRequestBody,
                                     BindingResult bindingResult) throws IOException {
        AjaxResponseBody responseBody = new AjaxResponseBody();
        if(bindingResult.hasErrors()){
            responseBody.setState(400);
            responseBody.setMessage(bindingResult.getAllErrors().toString());
            return responseBody;
        }
        if (echoRequestBody.getTaskType() == 1){
            //MTurk user
            Task task = taskService.findById(echoRequestBody.getTaskId());
            if(task == null){
                responseBody.setState(401);
                responseBody.setMessage("Cannot get specified task #" + echoRequestBody.getTaskId());
                return responseBody;
            }
            if(task.getTimeLimit() == null){
                //Unlimited time
                responseBody.setState(200);
                return responseBody;
            }

            UserTask userTask = userTaskService.getUnfinishedByMTurkIdAndTaskId(
                    echoRequestBody.getMturkId(), echoRequestBody.getTaskId()
            );
            if(userTask == null){
                responseBody.setState(402);
                responseBody.setMessage("Cannot find unfinished usertask");
                return responseBody;
            }
            long remainingTime = getRemainingTime(task, userTask);
            if(remainingTime < 0){
                userTask.setState(UserTask.STATE_EXPIRED);
                userTaskService.updateUserTask(userTask);
                responseBody.setState(403);
                responseBody.setMessage("ERROR: Time is up");
            } else {
                responseBody.setState(200);
                responseBody.setContent(String.valueOf(remainingTime));
            }
        } else {
            responseBody.setState(415);
            responseBody.setMessage("Unknown request user type");
        }
        return responseBody;
    }

    /**
     * Return the remaining time in seconds for some UserTask
     * @param task
     * @param userTask
     * @return if negative, the usertask is expired
     */
    public static long getRemainingTime(Task task, UserTask userTask){
        if(task.getTimeLimit() == null){
            return Integer.MAX_VALUE;
        }
        Long expiration = task.getTimeLimit() * (long)60; //Minutes to seconds
        Long startTime = userTask.getStartTime();
        Long currentTime = System.currentTimeMillis() / 1000;
        return startTime + expiration - currentTime;
    }
}
