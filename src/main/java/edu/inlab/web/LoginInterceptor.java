package edu.inlab.web;

import edu.inlab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
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

}
