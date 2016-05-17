package edu.inlab.web;

import edu.inlab.models.User;
import edu.inlab.service.UserService;
import edu.inlab.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by inlab-dell on 2016/5/4.
 * 首页部分的Controller
 */
@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model, HttpServletRequest request){
        //return "pages/home";
        Integer uid = (Integer) request.getSession().getAttribute(Constants.KEY_USER_UID);
        if(null != uid){
            User user = userService.findById(uid);
            String displayName = user.getEmail();
            if(user.getNickname() != null)
                displayName = user.getNickname();
            model.addAttribute("loginState", true);
            model.addAttribute("displayName", displayName);
        } else {
            model.addAttribute("loginState", false);
        }
        return "home";
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
