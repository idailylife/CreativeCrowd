package edu.inlab.web;

import edu.inlab.models.Task;
import edu.inlab.service.TaskService;
import edu.inlab.service.UserService;
import edu.inlab.utils.Constants;
import edu.inlab.utils.JSON2Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by inlab-dell on 2016/5/10.
 */
@Controller
@RequestMapping("/task")
@ComponentScan(basePackages = "edu.inlab")
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;

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
        model.addAttribute("loginState", loginState);

        return "task/show";
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if(null != startTime){
            Date date = new Date((long)startTime*1000);
            retStr += dateFormat.format(date);
        }
        if(null != endTime){
            Date date = new Date((long)endTime*1000);
            retStr += " - " + dateFormat.format(date);
        }
        return retStr;
    }
}
