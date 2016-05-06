package edu.inlab.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by inlab-dell on 2016/5/4.
 * 首页部分的Controller
 */
@Controller
@RequestMapping("/")
public class HomeController {
    @RequestMapping(method = RequestMethod.GET)
    public String home(){
        return "home";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(){
        return "redirect:/user/register";
    }
}
