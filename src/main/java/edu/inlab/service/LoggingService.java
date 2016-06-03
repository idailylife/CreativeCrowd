package edu.inlab.service;

import edu.inlab.models.json.MTurkIdValidationRequestBody;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by inlab-dell on 2016/6/3.
 */
@Aspect
public class LoggingService {
    final static Logger logger = Logger.getLogger(LoggingService.class);

    /* User */

    @Pointcut("execution(* edu.inlab.web.UserController.userLogIn(..))")
    public void userLoginPost(){}

    @Before("userLoginPost()")
    public void logUserLoginAttempt(){
        logger.debug("user login attempt.");
    }

    /* Task */
    @Pointcut(value = "execution(* edu.inlab.web.TaskController.checkMturkId(..))" +
    "&& args(request, body,..)", argNames = "request, body")
    public void checkMTurkId(HttpServletRequest request, MTurkIdValidationRequestBody body){}

    @Before(value = "checkMTurkId(request, body)", argNames = "request, body")
    public void logCheckMtAttempt(HttpServletRequest request, MTurkIdValidationRequestBody body){
        logger.debug("Check mturk id, Remote IP= " + getRemoteIP(request) +
                ", RequestBody= " + body);
    }

    String getRemoteIP(HttpServletRequest request){
        String ip = request.getHeader("X-FORWARDED-FOR");
        if(ip == null){
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
