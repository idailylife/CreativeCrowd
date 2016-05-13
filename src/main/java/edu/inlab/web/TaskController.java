package edu.inlab.web;

import edu.inlab.models.AjaxResponseBody;
import edu.inlab.models.Task;
import edu.inlab.models.User;
import edu.inlab.models.UserTask;
import edu.inlab.service.TaskService;
import edu.inlab.service.UserService;
import edu.inlab.service.UserTaskService;
import edu.inlab.utils.Constants;
import edu.inlab.utils.JSON2Map;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    UserService userService;

    @Autowired
    UserTaskService userTaskService;

    @Transactional  //Avoids lazy-load problem
    @RequestMapping(value = "/tid{taskId}", method = RequestMethod.GET)
    public String showTask(
            @PathVariable("taskId") int taskId, Model model,
            HttpServletRequest request, HttpServletResponse response){
        Task task = taskService.findById(taskId);
        if(null == task || null == task.getId()){
            throw new ResourceNotFoundException();
        }

        String title = task.getTitle();
        model.addAttribute("title", title);

        JSONObject jsonDesc = task.getDescJson();

        String descText = jsonDesc.getString(Constants.KEY_TASK_DESC);
        model.addAttribute("desc", descText);

        if(jsonDesc.has(Constants.KEY_TASK_IMG_URL)){
            String imageUrl = jsonDesc.getString(Constants.KEY_TASK_IMG_URL);
            model.addAttribute("image", imageUrl);
        }

        String descDetail = jsonDesc.getString(Constants.KEY_TASK_DESC_DETAIL);
        model.addAttribute("descDetail", descDetail);

        model.addAttribute("startEndTime",
                parseTaskDurationStr(task.getStartTime(), task.getEndTime()));

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

        TaskJoinState taskJoinState = getTaskJoinState(task, request, response);
        model.addAttribute("taskState", taskJoinState);

        return "task/show";
    }

    private TaskJoinState getTaskJoinState(Task task,
                                           HttpServletRequest request, HttpServletResponse response){
        TaskJoinState taskJoinState = null;
        boolean isExpiredOrFull = task.isExpired() || task.isFull();

        int loginState = -400;
        try{
            loginState = userService.maintainLoginState(request, response);
        } catch (IOException e){
            e.printStackTrace();
        }

        if(loginState < 0){
            if(isExpiredOrFull){
                taskJoinState = TaskJoinState.EXPIRED;
            } else {
                taskJoinState =  TaskJoinState.NEED_LOGIN;
            }
        } else {
            //处理用户是否参与过/正在参与这个任务
            //User user = userService.findById(loginState);
            //Set<Task> tasks = user.getClaimedTasks();
            List<UserTask> userTasks = userTaskService.getByUserId(loginState, 1000);
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

//            if(tasks.contains(task)){
//                UserTask userTask = userTaskService.getByUserAndTaskId(user.getId(), task.getId());
//                if(userTask.getState() == UserTask.TYPE_FINISHED){
//                    //该任务已经完成过了
//                    taskJoinState = TaskJoinState.FINISHED;
//                } else {
//                    taskJoinState = TaskJoinState.CLAIMED;
//                }
//            } else {
//                taskJoinState = TaskJoinState.JOINABLE;
//            }
        }

        return taskJoinState;
    }

    @RequestMapping(value = "/claim", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public AjaxResponseBody claimTask(){

    }

    /**
     * 转换任务起止时间戳信息到文字
     * @param startTime unix时间戳
     * @param endTime   unix时间戳
     * @return
     */
    public String parseTaskDurationStr(Integer startTime, Integer endTime){
        if(null == startTime && null == endTime){
            return "不限";
        }
        String retStr = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        Date currDate = new Date();
        calendar.setTime(currDate);
        int year = calendar.get(Calendar.YEAR);


        if(null != startTime){
            Date date = new Date((long)startTime*1000);
            calendar.setTime(date);
            int startYear = calendar.get(Calendar.YEAR);
            if(startYear == year){
                retStr += new SimpleDateFormat("MM/dd").format(date);
            } else {
                retStr += dateFormat.format(date);
            }
        }
        if(null != endTime){
            Date date = new Date((long)endTime*1000);
            calendar.setTime(date);
            int endYear = calendar.get(Calendar.YEAR);
            retStr += " - ";
            if(endYear == year){
                retStr += new SimpleDateFormat("MM/dd").format(date);
            } else {
                retStr += dateFormat.format(date);
            }
        }
        return retStr;
    }
}
