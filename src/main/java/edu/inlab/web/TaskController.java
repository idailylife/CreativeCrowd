package edu.inlab.web;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import edu.inlab.models.*;
import edu.inlab.models.handler.MicroTaskHandler;
import edu.inlab.models.handler.SimpleMicroTaskHandler;
import edu.inlab.models.handler.TaskHandlerFactory;
import edu.inlab.models.json.AjaxResponseBody;
import edu.inlab.models.json.TaskClaimRequestBody;
import edu.inlab.repo.usertype.JSONArrayUserType;
import edu.inlab.service.*;
import edu.inlab.utils.Constants;
import edu.inlab.utils.JSON2Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
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

    @Autowired
    TempFileService tempFileService;

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
        TaskJoinState taskJoinState = TaskJoinState.EXPIRED;
        boolean isExpiredOrFull = task.isExpired() || task.isFull();


        if(loginStateOrUserId < 0){
            if(!isExpiredOrFull){
                taskJoinState = TaskJoinState.NEED_LOGIN;
            }
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

//            UserTask userTask = userTaskService.getByUserAndTaskId(loginStateOrUserId, task.getId());
//            boolean claimedThisTask = false;
//            if(userTask != null){
//                claimedThisTask = true;
//                if(userTask.getState() == UserTask.TYPE_FINISHED){
//                    taskJoinState = TaskJoinState.FINISHED;
//                } else {
//                    taskJoinState = TaskJoinState.CLAIMED;
//                }
//            }
//
//            if(!claimedThisTask && !isExpiredOrFull){
//                taskJoinState = TaskJoinState.JOINABLE;
//            }
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


    @Transactional
    @RequestMapping(value = "/do/{taskId}", method = RequestMethod.GET)
    public String doTask(@PathVariable int taskId, Model model,
                         HttpServletRequest request){
        Integer uid = (Integer) request.getSession().getAttribute(Constants.KEY_USER_UID);
        //userService.loginStateParse(model, uid);

        UserTask userTask = userTaskService.getUnfinishedByUserIdAndTaskId(uid, taskId); //userTaskService.getByUserAndTaskId(uid, taskId);
        if(null == userTask){
            //TODO: 跳转到任务页?
            return "redirect:/task/tid" + taskId;
        }
        if(userTask.getCurrUserMicrotaskId() == null){
            //没有正在进行中的microtask
            return "redirect:/task/tid" + taskId;
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
        Integer umtId = (Integer) request.getSession().getAttribute(Constants.KEY_FILE_UPLOAD);
        if(umtId == null){
            responseBody.setState(401);
            responseBody.setMessage("Auth failure. Session does not exist");
        } else if(userMicrotaskService.getById(umtId) == null){
            responseBody.setState(402);
            responseBody.setMessage("Auth failure. umt_id is not valid.");
        } else {
            JSONObject rcvJsonObj = new JSONObject(jsonStr);    //For safety
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
            UserTask userTask = userTaskService.getById(userMicroTask.getUsertaskId());
            userTask.setState(1);
            if(userMicroTask.getId().equals(userTask.getCurrUserMicrotaskId())){
                userTask.setState(UserTask.TYPE_FINISHED);
                userTask.setCurrUserMicrotaskId(null);
                userTaskService.updateUserTask(userTask);
                Task task = taskService.findById(userTask.getTaskId());
                task.setFinishedCount(task.getFinishedCount() + 1);
                taskService.updateTask(task);
                request.getSession().removeAttribute(Constants.KEY_FILE_UPLOAD);
                responseBody.setState(200);
            } else {
                responseBody.setState(405);
                responseBody.setMessage("Task not finished");
            }

        }
        return responseBody;
    }

    @RequestMapping(value = "/done", method = RequestMethod.GET)
    public String taskDone(){
        return "task/done";
    }
}
