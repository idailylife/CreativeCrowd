package edu.inlab.web;

import edu.inlab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * Created by inlab-dell on 2016/5/5.
 */
@Controller
@RequestMapping(value = "/user")
@ComponentScan(basePackages = "edu.inlab.service")
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newUser(Model model){
        return "user/new";
    }
}
