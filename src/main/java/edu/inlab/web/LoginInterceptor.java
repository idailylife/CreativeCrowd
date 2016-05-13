package edu.inlab.web;

import edu.inlab.models.User;
import edu.inlab.service.UserService;
import edu.inlab.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
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
        if(state >= 0)
            return true;
        else{
            response.sendRedirect("/user/login?next=" + request.getRequestURI() + "&state=" + state);
            return false;
        }

        //MOVED TO UserService.CLASS
//        Integer uid = (Integer) request.getSession().getAttribute(Constants.KEY_USER_UID);
//        String errorCode = "";
//        if(uid != null){
//            User user = userService.findById(uid);
//            if(null != user)
//                return true;
//            errorCode += "userNotFound ";
//        } else {
//            Cookie[] cookies = request.getCookies();
//            String token = null;
//            for(Cookie cookie: cookies){
//                if(cookie.getName().equals(Constants.KEY_USER_UID)){
//                    uid = Integer.valueOf(cookie.getValue());
//                    if(uid != null && userService.findById(uid) != null){
//                        continue;
//                    } else {
//                        cookie.setMaxAge(0);
//                        response.addCookie(cookie);
//                        errorCode += "cookieInvalid ";
//                    }
//                } else if(cookie.getName().equals(Constants.KEY_USER_TOKEN)){
//                    token = cookie.getValue();
//                }
//            }
//            if(uid != null && token != null){
//                int verifyState = userService.verify(uid, token);
//                if(verifyState == UserService.SUCC_LOGIN){
//                    request.getSession().setAttribute(Constants.KEY_USER_UID, uid);
//                    return true;
//                }
//                errorCode += "loginExpired ";
//            }
//        }
//        response.sendRedirect("/user/login?next=" + request.getRequestURI() + "&state=" + errorCode);
//        return false;
    }

}
