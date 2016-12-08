package edu.inlab.web;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import edu.inlab.models.Task;
import edu.inlab.models.UserTask;
import edu.inlab.models.json.AjaxResponseBody;
import edu.inlab.models.User;
import edu.inlab.models.json.UserEchoRequestBody;
import edu.inlab.service.TaskService;
import edu.inlab.service.UserService;
import edu.inlab.service.UserTaskService;
import edu.inlab.utils.EncodeFactory;
import edu.inlab.utils.Constants;
import edu.inlab.web.exception.ResourceNotFoundException;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Version;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by inlab-dell on 2016/5/5.
 */
@Controller
@RequestMapping(value = "/user")
@ComponentScan(basePackages = "edu.inlab")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserTaskService userTaskService;

    @Autowired
    TaskService taskService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String newUser(HttpServletRequest request, Model model){
        if(request.getSession().getAttribute(Constants.KEY_USER_UID) != null){
            return "redirect:/";
        }
        model.addAttribute("allowRegister", Constants.ALLOW_REGISTER);
        return "user/register";
    }

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public AjaxResponseBody  submitNewUser(
            @RequestBody @Valid User user, BindingResult bindingResult, HttpServletRequest request){
        AjaxResponseBody responseBody = new AjaxResponseBody();
        if(bindingResult.hasErrors()){
            responseBody.setState(400);
            responseBody.setMessage("Illegal input.");
        } else {
            //String expectedCaptcha = (String)request.getSession().getAttribute(Constants.KEY_CAPTCHA_SESSION);
            //if(!Constants.DEBUG && (expectedCaptcha==null || !expectedCaptcha.equals(user.getCaptcha()))){
            if(!Constants.DEBUG && !CaptchaController.testCaptcha(user.getCaptcha(), request)){
                responseBody.setState(403);
                responseBody.setMessage("Wrong captcha.");
            } else if(!userService.isUniqueEmail(user.getEmail(), null)){
                responseBody.setState(401);
                responseBody.setMessage("Invalid Email: Duplicated.");
            } else {
                userService.setSaltPassword(user);
                userService.saveUser(user);
                responseBody.setState(200);
            }
        }
        return responseBody;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login( Model model,
            @RequestParam(value = "next", required = false) String nextStr,
            @RequestParam(value = "state", required = false) String stateStr,
                         HttpServletRequest request){
        Integer uid = (Integer)request.getSession().getAttribute(Constants.KEY_USER_UID);
        if(uid != null && !uid.equals(Constants.VAL_USER_UID_MTURK)){
            return "redirect:/";
        }

        if(nextStr != null){
            model.addAttribute("nextUrl", nextStr);
        } else {
            model.addAttribute("nextUrl", request.getContextPath());
        }
        if(stateStr != null){
            model.addAttribute("state", stateStr);
        }
        return "user/login";
    }

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public AjaxResponseBody userLogIn(
            @RequestBody @Valid User user, BindingResult bindingResult,
            HttpServletResponse response, HttpServletRequest request){
        AjaxResponseBody responseBody = new AjaxResponseBody();
        if(bindingResult.hasErrors()){
            responseBody.setState(400);
            responseBody.setMessage("Illegal input.");
        } else {
            //String expectedCaptcha = (String)request.getSession().getAttribute(Constants.KEY_CAPTCHA_SESSION);
            //if(!Constants.DEBUG && (expectedCaptcha==null || !expectedCaptcha.equals(user.getCaptcha()))){
            if(!Constants.DEBUG && !CaptchaController.testCaptcha(user.getCaptcha(), request)){
                responseBody.setState(403);
                responseBody.setMessage("Wrong captcha.");
            } else {
                int checkState = userService.verify(user.getEmail(), user.getPassword());
                switch (checkState){
                    case UserService.ERR_WRONG_PASSWORD:
                        User currUser = userService.findByEmail(user.getEmail());
                        currUser.setTokenCookie("");    //Clear token
                        userService.updateUser(currUser);
                    case UserService.ERR_NO_SUCH_USER:
                        responseBody.setState(401);
                        responseBody.setMessage("Wrong email or password.");
                        break;
                    case UserService.ERR_SALT_UNDEFINED:
                        responseBody.setState(500);
                        responseBody.setMessage("Internal error: salt not defined.");
                        break;
                    case UserService.SUCC_LOGIN:
                        responseBody.setState(200);
                        //在cookie及session中写入id及token_cookie
                        currUser = userService.findByEmail(user.getEmail());
                        currUser.setTokenCookie(EncodeFactory.getRandomUUID());
                        userService.updateUser(currUser);
                        request.getSession().setAttribute(Constants.KEY_USER_UID, currUser.getId());
                        Cookie uidCookie = new Cookie(Constants.KEY_USER_UID, currUser.getId().toString());
                        uidCookie.setMaxAge(7 * 24 * 3600); //7days
                        uidCookie.setPath(request.getContextPath());
                        response.addCookie(uidCookie);
                        Cookie tokenCookie = new Cookie(Constants.KEY_USER_TOKEN, currUser.getTokenCookie());
                        tokenCookie.setMaxAge(7 * 24 * 3600);
                        tokenCookie.setPath(request.getContextPath());
                        response.addCookie(tokenCookie);
                        break;
                    default:
                        responseBody.setState(501);
                        responseBody.setMessage("Unknown internal error");
                }
            }
        }
        return responseBody;
    }

    @RequestMapping(value = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException{
        User user = userService.getUserFromSession(request);
        if(user != null){
            user.setTokenCookie(null);
            userService.updateUser(user);
            request.getSession().removeAttribute(Constants.KEY_USER_UID);
            Cookie uidCookie = new Cookie(Constants.KEY_USER_UID, null);
            uidCookie.setPath(request.getContextPath());
            uidCookie.setMaxAge(0);
            Cookie tokenCookie = new Cookie(Constants.KEY_USER_TOKEN, null);
            tokenCookie.setMaxAge(0);
            tokenCookie.setPath(request.getContextPath());
            response.addCookie(uidCookie);
            response.addCookie(tokenCookie);
        }
        response.sendRedirect(request.getContextPath());
    }


    @RequestMapping(value = "/edit/info", method = RequestMethod.GET)
    public String edit(HttpServletRequest request, Model model){
        Integer uid = (Integer)request.getSession().getAttribute(Constants.KEY_USER_UID);
        User user = userService.findById(uid);
        model.addAttribute("user", user);
        model.addAttribute("sel_info", true);
        return "user/edit/info";
    }

    @ResponseBody
    @RequestMapping(value = "/edit/info", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public AjaxResponseBody editInfo(@Valid User user, BindingResult bindingResult,
                                     HttpServletRequest request){
        AjaxResponseBody responseBody = new AjaxResponseBody();
        if(bindingResult.hasErrors()){
            JSONArray jsonArray = new JSONArray();
            for(ObjectError error: bindingResult.getAllErrors()){
                if(error instanceof FieldError){
                    jsonArray.put(((FieldError) error).getField());
                } else {
                    jsonArray.put(error.getObjectName());
                }
            }
            responseBody.setState(400);
            responseBody.setMessage("Request user contains errors.");
            responseBody.setContent(jsonArray.toString());
        } else {
            Integer uid = (Integer)request.getSession().getAttribute(Constants.KEY_USER_UID);
            User currUser = userService.findById(uid);
            currUser.setNickname(user.getNickname());
            currUser.setGender(user.getGender());
            currUser.setPhoneNumber(user.getPhoneNumber());
            currUser.setAge(user.getAge());
            userService.updateUser(currUser);
            responseBody.setState(200);
        }
        return responseBody;
    }


    @RequestMapping(value = "/edit/payment", method = RequestMethod.GET)
    public String paymentInfo(HttpServletRequest request, Model model){
        Integer uid = (Integer)request.getSession().getAttribute(Constants.KEY_USER_UID);
        User user = userService.findById(uid);
        model.addAttribute("user", user);
        model.addAttribute("sel_pay", true);
        return "user/edit/payment";
    }

    @ResponseBody
    @RequestMapping(value = "/edit/payment", method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public AjaxResponseBody editPaymentInfo(@RequestBody Map<String,String> map, HttpServletRequest request){
        AjaxResponseBody responseBody = new AjaxResponseBody();
        User currUser = userService.getUserFromSession(request);
        if(currUser != null && map.containsKey("payMethod")){
            currUser.setPayMethod(map.get("payMethod"));
            if(map.containsKey("payAccount")){
                currUser.setPayAccount(map.get("payAccount"));
            } else {
                currUser.setPayAccount(null);
            }
            userService.updateUser(currUser);
            responseBody.setState(200);
        } else {
            responseBody.setState(400);
            responseBody.setMessage("No such user/invalid request content. Check login state.");
        }
        return  responseBody;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String userCenter(HttpServletRequest request, Model model){
//        Integer uid = (Integer)request.getSession().getAttribute(Constants.KEY_USER_UID);
//        User user = userService.findById(uid);
        User user = userService.getUserFromSession(request);
        model.addAttribute("user", user);
        model.addAttribute("claimedCount", userTaskService.getClaimedCount(user.getId()));
        model.addAttribute("finishedCount", userTaskService.getFinishedCount(user.getId()));

        model.addAttribute("sel_main", true);
        return "user";
    }



    @Transactional
    @RequestMapping(value = "task/claimed", method = RequestMethod.GET)
    public String claimedTasks(HttpServletRequest request, Model model){
        //TODO: 分页
        User user = userService.getUserFromSession(request);
        List<UserTask> userTasks = userTaskService.getByUserId(user.getId(), Constants.USER_LIST_TASK_LENGTH);
        Map<Integer, Task> mappedTasks = taskService.findMapByIds(userTaskService.getTaskIds(userTasks));
        //tasks可能会比userTasks多，因为可以重复申领
        model.addAttribute("userTasks", userTasks);
        model.addAttribute("mappedTasks", mappedTasks);
        model.addAttribute("sel_claim", true);
        return "user/task/claimed";
    }

    @Transactional
    @RequestMapping(value = "task/published", method = RequestMethod.GET)
    public String publishedTasks(HttpServletRequest request, Model model){
        //TODO: 分页
        User user = userService.getUserFromSession(request);
        List<Task> taskList = taskService.findByOwnerId(user.getId());
        model.addAttribute("taskList", taskList);
        model.addAttribute("sel_request", true);
        return "user/task/published";
    }

}
