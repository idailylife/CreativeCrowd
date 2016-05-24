package edu.inlab.web;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import edu.inlab.models.json.AjaxResponseBody;
import edu.inlab.models.User;
import edu.inlab.service.UserService;
import edu.inlab.utils.EncodeFactory;
import edu.inlab.utils.Constants;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by inlab-dell on 2016/5/5.
 */
@Controller
@RequestMapping(value = "/user")
@ComponentScan(basePackages = "edu.inlab")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String newUser(HttpServletRequest request){
        if(request.getSession().getAttribute(Constants.KEY_USER_UID) != null){
            return "redirect:/";
        }
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
            String expectedCaptcha = (String)request.getSession().getAttribute(Constants.KEY_CAPTCHA_SESSION);
            if(!Constants.DEBUG && (expectedCaptcha==null || !expectedCaptcha.equals(user.getCaptcha()))){
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
        if(request.getSession().getAttribute(Constants.KEY_USER_UID) != null){
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
            String expectedCaptcha = (String)request.getSession().getAttribute(Constants.KEY_CAPTCHA_SESSION);
            if(!Constants.DEBUG && (expectedCaptcha==null || !expectedCaptcha.equals(user.getCaptcha()))){
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
        request.getSession().removeAttribute(Constants.KEY_USER_UID);
        Cookie uidCookie = new Cookie(Constants.KEY_USER_UID, null);
        uidCookie.setPath(request.getContextPath());
        uidCookie.setMaxAge(0);
        Cookie tokenCookie = new Cookie(Constants.KEY_USER_TOKEN, null);
        tokenCookie.setMaxAge(0);
        tokenCookie.setPath(request.getContextPath());
        response.addCookie(uidCookie);
        response.addCookie(tokenCookie);
        response.sendRedirect(request.getContextPath());
    }


    @RequestMapping(value = "/edit/info", method = RequestMethod.GET)
    public String edit(HttpServletRequest request, Model model){
        Integer uid = (Integer)request.getSession().getAttribute(Constants.KEY_USER_UID);
        User user = userService.findById(uid);
        model.addAttribute("user", user);
        return "user/edit/info";
    }

    @ResponseBody
    @RequestMapping(value = "/edit/info", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public AjaxResponseBody editInfo(@Valid @RequestBody User user, BindingResult bindingResult,
                                     HttpServletRequest request){
        AjaxResponseBody responseBody = new AjaxResponseBody();
        if(bindingResult.hasErrors()){
            JSONArray jsonArray = new JSONArray();
            for(ObjectError error: bindingResult.getAllErrors()){
                jsonArray.put(error.getObjectName());
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

}
