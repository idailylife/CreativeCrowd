package edu.inlab.web;

import edu.inlab.models.*;
import edu.inlab.models.handler.MicroTaskHandler;
import edu.inlab.models.handler.TaskHandlerFactory;
import edu.inlab.models.json.AjaxResponseBody;
import edu.inlab.models.json.TaskClaimRequestBody;
import edu.inlab.service.*;
import edu.inlab.utils.Constants;
import edu.inlab.utils.JSON2Map;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

    @Transactional  //Avoids lazy-load problem
    @RequestMapping(value = "/tid{taskId}", method = RequestMethod.GET)
    public String showTask(
            @PathVariable("taskId") int taskId, Model model,
            HttpServletRequest request, HttpServletResponse response){
        Integer uid = (Integer) request.getSession().getAttribute(Constants.KEY_USER_UID);
        userService.loginStateParse(model, uid);

        Task task = taskService.findById(taskId);
        if(null == task || null == task.getId()){
            throw new ResourceNotFoundException();
        }

        String title = task.getTitle();
        model.addAttribute("title", title);

        String imageUrl = task.getImage();
        model.addAttribute("image", imageUrl);

        JSONObject jsonDesc = task.getDescJson();

        String descText = jsonDesc.getString(Constants.KEY_TASK_DESC);
        model.addAttribute("desc", descText);

//        if(jsonDesc.has(Constants.KEY_TASK_IMG_URL)){
//            String imageUrl = jsonDesc.getString(Constants.KEY_TASK_IMG_URL);
//            model.addAttribute("image", imageUrl);
//        }

        String descDetail = jsonDesc.getString(Constants.KEY_TASK_DESC_DETAIL);
        model.addAttribute("descDetail", descDetail);

        model.addAttribute("startEndTime", task.getDurationStr());

        //Judge if current task has expired

        //model.addAttribute("isExpiredOrFull", isExpiredOrFull);

        if(jsonDesc.has(Constants.KEY_TASK_EST_TIME)){
            String estTime = jsonDesc.getString(Constants.KEY_TASK_EST_TIME);
            model.addAttribute("estTime", estTime);
        }

        if(jsonDesc.has(Constants.KEY_TASK_REL_INFO)){
            JSONObject infoObj = jsonDesc.getJSONObject(Constants.KEY_TASK_REL_INFO);
            Map<String, String> infoMap = JSON2Map.convertJSONObjectToMap(infoObj);
            model.addAttribute("infoMap", infoMap);
        }



        model.addAttribute("task", taskService.findById(taskId));

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

    private TaskJoinState getTaskJoinState(Task task, int loginStateOrUserId){
        TaskJoinState taskJoinState = null;
        boolean isExpiredOrFull = task.isExpired() || task.isFull();



        if(loginStateOrUserId < 0){
            if(isExpiredOrFull){
                taskJoinState = TaskJoinState.EXPIRED;
            } else {
                taskJoinState =  TaskJoinState.NEED_LOGIN;
            }
        } else {
            //处理用户是否参与过/正在参与这个任务
            //User user = userService.findById(loginState);
            //Set<Task> tasks = user.getClaimedTasks();
            List<UserTask> userTasks = userTaskService.getByUserId(loginStateOrUserId, 1000);
            boolean claimedThisTask = false;
            for(UserTask userTask : userTasks){
                if(userTask.getTaskId().equals(task.getId())){
                    claimedThisTask = true;
                    if(userTask.getState() == UserTask.TYPE_FINISHED){
                    //该任务已经完成过了
                        taskJoinState = TaskJoinState.FINISHED;
                    } else {
                        taskJoinState = TaskJoinState.CLAIMED;
                    }
                    break;
                }
            }

            if(!claimedThisTask){
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
        User user = userService.findById(uid);
        AjaxResponseBody responseBody = new AjaxResponseBody();
        if(uid == null || user == null){
            responseBody.setState(500);
            responseBody.setMessage("User or id is null");
        } else {
            Task task = taskService.findById(taskClaimRequestBody.getTaskId());
            TaskJoinState taskJoinState = getTaskJoinState(task, uid);
            if(taskJoinState.equals(TaskJoinState.JOINABLE)){
                //Create UserTask
                UserTask userTask = new UserTask(uid, task.getId());
                userTaskService.saveUserTask(userTask);
                //Create UserMicroTasks
                for(Microtask microtask: task.getRelatedMictorasks()){
                    UserMicroTask userMicroTask = new UserMicroTask();
                    userMicroTask.setMicrotaskId(microtask.getId());
                    userMicroTask.setUsertaskId(userTask.getId());
                    userMicrotaskService.save(userMicroTask);
                    if(microtask.getPrevId() == null){
                        //First microtask to this task
                        userTask.setCurrUserMicrotaskId(userMicroTask.getId());
                    }
                }
                userTaskService.updateUserTask(userTask);
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

//    /**
//     * 转换任务起止时间戳信息到文字
//     * @param startTime unix时间戳
//     * @param endTime   unix时间戳
//     * @return
//     */
//    public String parseTaskDurationStr(Integer startTime, Integer endTime){
//        if(null == startTime && null == endTime){
//            return "不限";
//        }
//        String retStr = "";
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//        Calendar calendar = Calendar.getInstance();
//        Date currDate = new Date();
//        calendar.setTime(currDate);
//        int year = calendar.get(Calendar.YEAR);
//
//
//        if(null != startTime){
//            Date date = new Date((long)startTime*1000);
//            calendar.setTime(date);
//            int startYear = calendar.get(Calendar.YEAR);
//            if(startYear == year){
//                retStr += new SimpleDateFormat("MM/dd").format(date);
//            } else {
//                retStr += dateFormat.format(date);
//            }
//        }
//        if(null != endTime){
//            Date date = new Date((long)endTime*1000);
//            calendar.setTime(date);
//            int endYear = calendar.get(Calendar.YEAR);
//            retStr += " - ";
//            if(endYear == year){
//                retStr += new SimpleDateFormat("MM/dd").format(date);
//            } else {
//                retStr += dateFormat.format(date);
//            }
//        }
//        return retStr;
//    }

    @Transactional
    @RequestMapping(value = "/do/{taskId}", method = RequestMethod.GET)
    public String doTask(@PathVariable int taskId, Model model,
                         HttpServletRequest request){
        Integer uid = (Integer) request.getSession().getAttribute(Constants.KEY_USER_UID);
        userService.loginStateParse(model, uid);

        UserTask userTask = userTaskService.getByUserAndTaskId(uid, taskId);
        if(null == userTask){
            throw new ResourceNotFoundException();
        }
        if(userTask.getCurrUserMicrotaskId() == null){
            //没有正在进行中的microtask
            throw new ResourceNotFoundException();
        }
        UserMicroTask userMicroTask = userMicrotaskService.getById(userTask.getCurrUserMicrotaskId());
        Microtask microtask = microTaskService.getById(userMicroTask.getMicrotaskId());
        MicroTaskHandler microTaskHandler = TaskHandlerFactory.getHandler(microtask.getHandlerType(),
                request.getContextPath()+"/img/upload/");
        String htmlStr = microTaskHandler.parseMicrotaskToHtml(microtask.getTemplate().toString());
        model.addAttribute("htmlStr", htmlStr);
        Task task = taskService.findById(userTask.getTaskId());
        model.addAttribute("title", task.getTitle());
        return "task/do";
    }


}
