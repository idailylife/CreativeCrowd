package edu.inlab.web;

import edu.inlab.models.User;
import edu.inlab.service.UserService;
import edu.inlab.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by inlab-dell on 2016/5/9.
 * 在需要登录的地方处理登录验证、Session/Cookie维护
 */
@Component
@ComponentScan(basePackages = "edu.inlab")
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        int state = userService.maintainLoginState(request, response);
        if(state >= 0) {
            return true;
        }
        else{
            response.sendRedirect("/user/login?next=" + request.getRequestURI() + "&state=" + state);
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView != null){ //防止渲染json对象出错
            Integer uid = (Integer) request.getSession().getAttribute(Constants.KEY_USER_UID);
            if(uid != null){
                User user = userService.findById(uid);
                String displayName = user.getEmail();
                if(user.getNickname() != null)
                    displayName = user.getNickname();
                modelAndView.getModel().put("loginState", true);
                //model.addAttribute("loginState", true);
                modelAndView.getModel().put("displayName", displayName);
                //model.addAttribute("displayName", displayName);
            } else {
                modelAndView.getModel().put("loginState", false);
            }
        }
        super.postHandle(request, response, handler, modelAndView);
    }
}
