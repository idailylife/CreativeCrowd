package edu.inlab.web;

import edu.inlab.models.Headline;
import edu.inlab.models.Task;
import edu.inlab.models.User;
import edu.inlab.models.json.AjaxResponseBody;
import edu.inlab.models.json.TaskPageCountRequestBody;
import edu.inlab.service.HeadlineService;
import edu.inlab.service.TaskService;
import edu.inlab.service.UserService;
import edu.inlab.utils.Constants;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by inlab-dell on 2016/5/4.
 * 首页部分的Controller
 */
@Controller
@RequestMapping("/")
public class HomeController {
    private static int TASK_PAGE_SIZE = 8;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Autowired
    HeadlineService headlineService;

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model, HttpServletRequest request, HttpServletResponse response)
        throws IOException{
        //return "pages/home";
        userService.maintainLoginState(request, response);
        Integer uid = (Integer) request.getSession().getAttribute(Constants.KEY_USER_UID);
//        if(null != uid){
//            User user = userService.findById(uid);
//            String displayName = user.getEmail();
//            if(user.getNickname() != null)
//                displayName = user.getNickname();
//            model.addAttribute("loginState", true);
//            model.addAttribute("displayName", displayName);
//        } else {
//            model.addAttribute("loginState", false);
//        }
        userService.loginStateParse(model, uid);

        //Fetch latest tasks
        List<Task> taskList = taskService.getPagedTasks(1, TASK_PAGE_SIZE);
        model.addAttribute("tasks", taskList);

        //Fetch headlines
        List<Headline> headlineList = headlineService.getAll();
        model.addAttribute("headlines", headlineList);

        return "home";
    }

    @ResponseBody
    @RequestMapping(value = "more", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public AjaxResponseBody more(@RequestBody TaskPageCountRequestBody requestBody){
        Integer pageSize = TASK_PAGE_SIZE;
        if(requestBody.getCount() != null){
            pageSize = requestBody.getCount();
        }
        List<Task> taskList = taskService.getPagedTasks(requestBody.getPage(),
                pageSize);
        AjaxResponseBody responseBody = new AjaxResponseBody();
        if(taskList == null || taskList.isEmpty()){
            responseBody.setState(201);
            responseBody.setMessage("End of list");
        } else {
            responseBody.setState(200);
            JSONArray array = new JSONArray(taskList);
            responseBody.setContent(array.toString());
        }
        return responseBody;
    }

    @RequestMapping(value = "agreement")
    public String agreement(){
        return "agreement";
    }

//    @RequestMapping(value = "/register", method = RequestMethod.GET)
//    public String register(){
//        return "redirect:/user/register";
//    }
}
