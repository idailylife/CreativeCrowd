package edu.inlab.web;

import edu.inlab.models.json.AjaxResponseBody;
import edu.inlab.models.User;
import edu.inlab.service.UserService;
import edu.inlab.utils.EncodeFactory;
import edu.inlab.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
    public String newUser(){
        return "user/register";
    }

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public AjaxResponseBody  submitNewUser(
            @RequestBody @Valid User user, BindingResult bindingResult){
        AjaxResponseBody responseBody = new AjaxResponseBody();
        if(bindingResult.hasErrors()){
            responseBody.setState(400);
            responseBody.setMessage("Illegal input.");
        } else {
            if(!userService.isUniqueEmail(user.getEmail(), null)){
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
            @RequestParam(value = "state", required = false) String stateStr ){
        //TODO: 如果已经登录，则跳转到主页
        if(nextStr != null){
            model.addAttribute("nextUrl", nextStr);
        } else {
            model.addAttribute("nextUrl", "/");
        }
        if(stateStr != null){
            model.addAttribute("state", stateStr);
        }
        return "pages/user/login";
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
                    response.addCookie(uidCookie);
                    Cookie tokenCookie = new Cookie(Constants.KEY_USER_TOKEN, currUser.getTokenCookie());
                    tokenCookie.setMaxAge(7 * 24 * 3600);
                    response.addCookie(tokenCookie);
                    break;
                default:
                    responseBody.setState(501);
                    responseBody.setMessage("Unknown internal error");
            }
        }
        return responseBody;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET,
    produces = MediaType.TEXT_PLAIN_VALUE)
    public String test(){
        return "blablabla...";
    }
}
