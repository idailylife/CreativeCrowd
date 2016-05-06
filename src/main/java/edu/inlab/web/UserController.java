package edu.inlab.web;

import edu.inlab.models.AjaxResponseBody;
import edu.inlab.models.User;
import edu.inlab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
            @RequestBody User user
    ){
        AjaxResponseBody responseBody = new AjaxResponseBody();
        responseBody.setState(1);
        responseBody.setMessage("WOW");
        return responseBody;
    }
}
