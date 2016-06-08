package edu.inlab.web;

import edu.inlab.models.*;
import edu.inlab.models.handler.TaskHandlerFactory;
import edu.inlab.models.json.AjaxResponseBody;
import edu.inlab.models.json.MTurkIdValidationRequestBody;
import edu.inlab.models.json.TaskClaimRequestBody;
import edu.inlab.service.*;
import edu.inlab.service.assignment.MicroTaskAssigner;
import edu.inlab.service.assignment.MicroTaskAssignerFactory;
import edu.inlab.service.wage.WageAssigner;
import edu.inlab.service.wage.WageFactory;
import edu.inlab.utils.Constants;
import edu.inlab.utils.JSON2Map;
import edu.inlab.web.exception.ResourceNotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

/**
 * Created by inlab-dell on 2016/5/10.
 */
@Controller
@RequestMapping("/task")
@ComponentScan(basePackages = "edu.inlab")
public class TaskController {
    public enum TaskJoinState {NEED_LOGIN, JOINABLE, CLAIMED, FINISHED, EXPIRED };

    @Autowired
    TaskService taskService;

    @Autowired
    MicroTaskService microTaskService;

    @Autowired
    UserService userService;

    @Autowired
    UserTaskService userTaskService;

    @Autowired
    UserMicrotaskService userMicrotaskService;

    @Autowired
    TempFileService tempFileService;

    @Autowired
    MicroTaskAssignerFactory microTaskAssignerFactory;

    @Autowired
    WageFactory wageFactory;

    @Transactional  //Avoids lazy-load problem
    @RequestMapping(value = "/tid{taskId}", method = RequestMethod.GET)
    public String showTask(
            @PathVariable("taskId") int taskId, Model model,
            HttpServletRequest request, HttpServletResponse response){

        Integer uid = (Integer) request.getSession().getAttribute(Constants.KEY_USER_UID);
        if(uid == null){
            try{
                uid = userService.maintainLoginState(request, response);
            } catch (IOException e){
                e.printStackTrace();
            }

        }
        userService.loginStateParse(model, uid);

        Task task = taskService.findById(taskId);
        if(null == task || null == task.getId()){
            throw new ResourceNotFoundException();
        }

        model.addAttribute("tid", taskId);

        String title = task.getTitle();
        model.addAttribute("title", title);

        String imageUrl = task.getImage();
        model.addAttribute("image", imageUrl);

        JSONObject jsonDesc = task.getDescJson();

        String descText = jsonDesc.getString(Constants.KEY_TASK_DESC);
        model.addAttribute("desc", descText);

        String descDetail = jsonDesc.getString(Constants.KEY_TASK_DESC_DETAIL);
        model.addAttribute("descDetail", descDetail);

        model.addAttribute("startEndTime", task.getDurationStr());


        if(jsonDesc.has(Constants.KEY_TASK_EST_TIME)){
            String estTime = jsonDesc.getString(Constants.KEY_TASK_EST_TIME);
            model.addAttribute("estTime", estTime);
        }

        if(jsonDesc.has(Constants.KEY_TASK_REL_INFO)){
            JSONObject infoObj = jsonDesc.getJSONObject(Constants.KEY_TASK_REL_INFO);
            Map<String, String> infoMap = JSON2Map.convertJSONObjectToMap(infoObj);
            model.addAttribute("infoMap", infoMap);
        }

        model.addAttribute("task", task);
        if(task.getType() == Task.TYPE_MTURK){
            model.addAttribute("isMTurkTask", true);
        }

        int loginState = -400;
        try{
            loginState = userService.maintainLoginState(request, response);
        } catch (IOException e){
            e.printStackTrace();
        }
        TaskJoinState taskJoinState = getTaskJoinState(task, loginState);
        model.addAttribute("taskState", taskJoinState);

        return "task/show";
    }

    /**
     * Get MTurk task join state
     * @param task
     * @param mturkId
     * @return
     */
    private TaskJoinState getTaskJoinState(Task task, String mturkId){
        if(mturkId == null){
            return TaskJoinState.NEED_LOGIN;
        }
        if(task.isExpired() || task.isFull()){
            return TaskJoinState.EXPIRED;
        }
        UserTask claimedUnfinishedUT = userTaskService.getUnfinishedByMTurkIdAndTaskId(mturkId, task.getId());
        if(null != claimedUnfinishedUT){
            return  TaskJoinState.CLAIMED;
        }
        List<UserTask> finiteStateUserTasks = userTaskService.getFinishedOrExpired(mturkId, task.getId());
        if(finiteStateUserTasks.isEmpty()){
            return TaskJoinState.JOINABLE;
        } else {
            if(task.getRepeatable() == 0){
                return TaskJoinState.FINISHED;
            } else {
                return TaskJoinState.JOINABLE;
            }
        }
    }

    /**
     * Get task join state for normal tasks,
     * If the task is a MTurk task, return JOINABLE
     * @param task
     * @param loginStateOrUserId
     * @return TaskJoinState
     */
    private TaskJoinState getTaskJoinState(Task task, int loginStateOrUserId){
        TaskJoinState taskJoinState = TaskJoinState.EXPIRED;
        boolean isExpiredOrFull = task.isExpired() || task.isFull();


        if(loginStateOrUserId < 0){
            if(!isExpiredOrFull){
                taskJoinState = TaskJoinState.NEED_LOGIN;
            }
        } else if(loginStateOrUserId == 0){
            //MTurk task
            return TaskJoinState.JOINABLE;
        } else {
            //处理用户是否参与过/正在参与这个任务

            //List<UserTask> claimedButNotFinishedTasks = userTaskService.getUnfinishedTasks(loginStateOrUserId);

            UserTask unfinishedTask = userTaskService.getUnfinishedByUserIdAndTaskId(loginStateOrUserId, task.getId());
            if(unfinishedTask != null){
                taskJoinState = TaskJoinState.CLAIMED;
            } else if(userTaskService.getByUserAndTaskId(loginStateOrUserId, task.getId()).size() > 0){
                //已有任务完成记录
                if(task.getRepeatable() == 1){
                    taskJoinState = TaskJoinState.JOINABLE;
                } else {
                    taskJoinState = taskJoinState.FINISHED;
                }
            } else {
                taskJoinState = TaskJoinState.JOINABLE;
            }

        }

        return taskJoinState;
    }

    @ResponseBody
    @Transactional
    @RequestMapping(value = "/claim", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public AjaxResponseBody claimTask(
            @RequestBody TaskClaimRequestBody taskClaimRequestBody,
            HttpServletRequest request){
        Integer uid = (Integer) request.getSession().getAttribute(Constants.KEY_USER_UID);
        String mturkId = (String) request.getSession().getAttribute(Constants.KEY_MTURK_ID);
        User user = userService.findById(uid);
        AjaxResponseBody responseBody = new AjaxResponseBody();

        if(user == null && mturkId == null){
            responseBody.setState(500);
            responseBody.setMessage("User or id is null");
        } else {
            Task task = taskService.findById(taskClaimRequestBody.getTaskId());
            TaskJoinState taskJoinState;
            if(uid == 0){
                taskJoinState = getTaskJoinState(task, mturkId);
            } else {
                taskJoinState = getTaskJoinState(task, uid);
            }

            if(taskJoinState.equals(TaskJoinState.JOINABLE)){
                //Create UserTask

                UserTask userTask;
                if(uid > 0)
                    userTask = new UserTask(uid, task.getId());
                else
                    userTask = new UserTask(mturkId, task.getId());
                userTask.setStartTime(System.currentTimeMillis()/1000);
                userTaskService.saveUserTask(userTask);

                //Set task claimed count
                task.setClaimedCount(task.getClaimedCount()+1);
                taskService.updateTask(task);

                responseBody.setState(200);
                responseBody.setMessage("userTask created");
                responseBody.setContent(userTask.getId().toString());
            }else if(taskJoinState.equals(TaskJoinState.CLAIMED)){
                responseBody.setState(300);
                responseBody.setMessage("userTask already claimed");
            }else {
                responseBody.setState(400);
                responseBody.setMessage("Wrong task state.");
            }
        }
        return responseBody;
    }


    @Transactional
    @RequestMapping(value = "/do/{taskId}", method = RequestMethod.GET)
    public String doTask(@PathVariable int taskId, Model model,
                         HttpServletRequest request){
        Integer uid = (Integer) request.getSession().getAttribute(Constants.KEY_USER_UID);
        String mturkId = (String) request.getSession().getAttribute(Constants.KEY_MTURK_ID);
        //userService.loginStateParse(model, uid);


        UserTask userTask;
        if(uid != 0){
            //Normal task
            userTask = userTaskService.getUnfinishedByUserIdAndTaskId(uid, taskId); //userTaskService.getByUserAndTaskId(uid, taskId);
        } else {
            //MTurk task
            userTask = userTaskService.getUnfinishedByMTurkIdAndTaskId(mturkId, taskId);
        }

        if(null == userTask){
            //找不到符合条件的任务
            return "redirect:/task/tid" + taskId;
        }
        if(userTask.getCurrUserMicrotaskId() == null){
            //没有正在进行中的microtask，根据设定的Microtask Coordinator来分配新任务
            Task task = taskService.findById(userTask.getTaskId());

            MicroTaskAssigner taskAssigner = microTaskAssignerFactory.getAssigner(task.getMode());
            Microtask nextMt = taskAssigner.assignNext(userTask);
            if(nextMt == null){
                return "redirect:/task/tid" + taskId;
            }
            UserMicroTask userMicroTask = new UserMicroTask(userTask.getId(), nextMt.getId());
            userMicrotaskService.save(userMicroTask);
            userTask.setCurrUserMicrotaskId(userMicroTask.getId());
            userTaskService.updateUserTask(userTask);
        }
        UserMicroTask userMicroTask = userMicrotaskService.getById(userTask.getCurrUserMicrotaskId());
        Microtask microtask = microTaskService.getById(userMicroTask.getMicrotaskId());
        String handlerType = microtask.getHandlerType();
        JSONArray handlerContent = microtask.getTemplate();
        model.addAttribute("handlerType", handlerType);
        model.addAttribute("handlerContent", TaskHandlerFactory.parseMicrotaskToItemLists(handlerContent));

        //Find related file that was uploaded by user
        TempFile oldFile = tempFileService.getByUsermicrotaskId(userMicroTask.getId());
        if(oldFile != null){
            model.addAttribute("file", oldFile.getFilename());
        }
        //Pass saved data records
        JSONObject savedResults = userMicroTask.getResults();
        Map<String, String> resultMap = new HashMap<String, String>();
        if(savedResults != null){
            Iterator<String> keys = savedResults.keys();
            while(keys.hasNext()){
                String key = keys.next();
                if(!key.equals("file"))
                    resultMap.put(key, savedResults.getString(key));
                else
                    model.addAttribute("file", savedResults.getString(key));
            }
        }
        model.addAttribute("savedResults", resultMap);

        Task task = taskService.findById(userTask.getTaskId());
        model.addAttribute("task", task);

        model.addAttribute("prev", microtask.getPrevId());
        model.addAttribute("next", microtask.getNextId());

        //Add session to authorize file upload
        request.getSession().setMaxInactiveInterval(60*60);
        request.getSession().setAttribute(Constants.KEY_FILE_UPLOAD, userMicroTask.getId());

        return "task/do";
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "/do", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public AjaxResponseBody saveTask(@RequestBody String jsonStr,
                                       HttpServletRequest request){
        AjaxResponseBody responseBody = new AjaxResponseBody();
        //Integer umtId = (Integer) request.getSession().getAttribute(Constants.KEY_FILE_UPLOAD);
        JSONObject rcvJsonObj = new JSONObject(jsonStr);    //For safety
        Integer taskId = rcvJsonObj.getInt("tid");
        Integer uid = (Integer) request.getSession().getAttribute(Constants.KEY_USER_UID);
        UserTask userTask = null;
        Integer umtId = null;
        if(taskId != null && uid != null){

            if(uid != 0)
                userTask = userTaskService.getUnfinishedByUserIdAndTaskId(uid, taskId);
            else {
                String mturkId = (String) request.getSession().getAttribute(Constants.KEY_MTURK_ID);
                userTask = userTaskService.getUnfinishedByMTurkIdAndTaskId(mturkId, taskId);
            }
            umtId = userTask.getCurrUserMicrotaskId();
        }


        if(umtId == null){
            responseBody.setState(401);
            responseBody.setMessage("Auth failure. umt_id does not exist");
        } else if(userMicrotaskService.getById(umtId) == null){
            responseBody.setState(402);
            responseBody.setMessage("Auth failure. umt_id is not valid.");
        } else {

            TempFile tempFile = tempFileService.getByUsermicrotaskId(umtId);
            UserMicroTask userMicroTask = userMicrotaskService.getById(umtId);
            if(userMicroTask.getResults() != null && userMicroTask.getResults().has("file")){
                rcvJsonObj.put("file", userMicroTask.getResults().getString("file"));
            }
            if(tempFile!= null){
                rcvJsonObj.put("file", tempFile.getFilename());
            }

            userMicroTask.setResults(rcvJsonObj);
            userMicrotaskService.update(userMicroTask);

            //Remove temp file db record
            if(tempFile != null){
                tempFileService.delete(tempFile);
            }

            responseBody.setState(200);
        }

        return responseBody;
    }


    @Transactional
    @ResponseBody
    @RequestMapping(value = "/submit", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public AjaxResponseBody submitTask(HttpServletRequest request){
        AjaxResponseBody responseBody = new AjaxResponseBody();
        Integer umtId = (Integer) request.getSession().getAttribute(Constants.KEY_FILE_UPLOAD);
        if(umtId == null || userMicrotaskService.getById(umtId)==null){
            responseBody.setState(401);
            responseBody.setMessage("Auth check failure");
        } else {
            UserMicroTask userMicroTask = userMicrotaskService.getById(umtId);
            if(userMicroTask.getResults() == null){
                responseBody.setState(405);
                responseBody.setMessage("MicroTask not finished");
            } else {
                UserTask userTask = userTaskService.getById(userMicroTask.getUsertaskId());
                Task task = taskService.findById(userTask.getTaskId());

                //Check if the task is overtime
                long timeLeft = EchoController.getRemainingTime(task, userTask);
                if(timeLeft < 0){
                    userTask.setState(UserTask.STATE_EXPIRED);
                    userTaskService.updateUserTask(userTask);
                    responseBody.setState(403);
                    responseBody.setMessage("Time is over, sorry :<");
                    return responseBody;
                }

                Microtask microtask = microTaskAssignerFactory.getAssigner(task.getMode())
                        .assignNext(userTask);
                if(microtask == null){
                    //Calculate wage for normal tasks
                    if(task.getType() == Task.TYPE_NORMAL){
                        WageAssigner wageAssigner = WageFactory.getAssigner(task.getWageType());
                        userTask.setRemuneration(wageAssigner.assignWage(task, userTask));
                    }

                    //Task finished
                    userTask.setState(UserTask.STATE_FINISHED);
                    userTask.setCurrUserMicrotaskId(null);
                    userTask.generateRefCode();
                    userTaskService.updateUserTask(userTask);
                    task.setFinishedCount(task.getFinishedCount() + 1);
                    taskService.updateTask(task);
                    responseBody.setState(200);
                    responseBody.setMessage("Task finished.");
                    responseBody.setContent(userTask.getRefCode());
                } else {
                    //Task not finished, navigate to the next microtask
                    UserMicroTask nextUserMT = new UserMicroTask(userTask.getId(), microtask.getId());
                    userMicrotaskService.save(nextUserMT);
                    userTask.setCurrUserMicrotaskId(nextUserMT.getId());
                    userTaskService.updateUserTask(userTask);
                    responseBody.setState(201);
                    responseBody.setMessage("New userMicroTask created.");
                    responseBody.setContent(userMicroTask.getId().toString());
                }
                request.getSession().removeAttribute(Constants.KEY_FILE_UPLOAD);
            }

        }
        return responseBody;
    }


    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createTask(HttpServletRequest request, Model model){

        return "task/create";
    }

    /*MTurk Support*/
    @ResponseBody
    @RequestMapping(value = "/check_mt", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public AjaxResponseBody checkMturkId(HttpServletRequest request,
                                         @Valid MTurkIdValidationRequestBody body,
                                         BindingResult bindingResult){
        AjaxResponseBody responseBody = new AjaxResponseBody();
        if(bindingResult.hasErrors()) {
            responseBody.setState(400);
            responseBody.setMessage("Your form contains errors, fail to process.");
            responseBody.setContent(bindingResult.toString());
            //} else if (!body.getCaptcha().equals(request.getSession().getAttribute(Constants.KEY_CAPTCHA_SESSION))) {
        } else if(!CaptchaController.testCaptcha(body.getCaptcha(), request)){
            responseBody.setState(401);
            responseBody.setMessage("Invalid captcha:(");
        } else {
            Integer tid = Integer.parseInt(body.getTaskId());
            List<UserTask> claimedUserTasks = userTaskService.getByMturkIdAndTaskId(body.getMturkId(), tid);
            UserTask unfinishedUserTask = userTaskService.getUnfinishedByMTurkIdAndTaskId(body.getMturkId(), tid);
            if(claimedUserTasks.isEmpty()){
                //Fresh new
                responseBody.setState(200);
            } else {
                Task task = taskService.findById(tid);

                if(unfinishedUserTask != null){
                    //Resume unfinished one
                    responseBody.setState(201);
                    responseBody.setMessage("Find a claimed but not finished task");
                } else if(task.getRepeatable() == 0){
                    //Cannot do more...
                    responseBody.setState(402);
                    responseBody.setMessage("Task has already been finished or is expired.");
                } else {

                    responseBody.setState(200);
                }
            }
        }

        request.getSession().removeAttribute(Constants.KEY_CAPTCHA_SESSION);
        if(responseBody.getState() < 300){
            request.getSession().setAttribute(Constants.KEY_USER_UID, Constants.VAL_USER_UID_MTURK);
            request.getSession().setAttribute(Constants.KEY_MTURK_ID, body.getMturkId());
        } else {
            request.getSession().removeAttribute(Constants.KEY_USER_UID);
            request.getSession().removeAttribute(Constants.KEY_MTURK_ID);
        }

        return responseBody;
    }


    @RequestMapping(value = "/done", method = RequestMethod.GET)
    public String taskDone(@RequestParam(value = "refCode", required = false) String refCode, Model model){
        model.addAttribute("refCode", refCode);
        return "task/done";
    }
}
