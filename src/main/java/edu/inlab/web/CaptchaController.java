package edu.inlab.web;

import com.google.code.kaptcha.Producer;
import edu.inlab.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

/**
 * Created by inlab-dell on 2016/5/17.
 */
@Controller
@RequestMapping("/captcha")
public class CaptchaController {

    private Producer captchaProducer;

    @Autowired
    public void setCaptchaProducer(Producer captchaProducer) {
        this.captchaProducer = captchaProducer;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView handleRequest(HttpServletRequest request,
                  HttpServletResponse response) throws Exception{
        // Set to expire far in the past.
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        // return a jpeg
        response.setContentType("image/jpeg");

        // create the text for the image
        String capText = captchaProducer.createText();

        // store the text in the session
        request.getSession().setAttribute(Constants.KEY_CAPTCHA_SESSION, capText);

        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);

        ServletOutputStream out = response.getOutputStream();

        // write the data out
        ImageIO.write(bi, "jpg", out);
        try
        {
            out.flush();
        }
        finally
        {
            out.close();
        }
        return null;
    }

    public static boolean testCaptcha(String userInput, HttpServletRequest request){
        String sessionCaptcha = (String) request.getSession().getAttribute(Constants.KEY_CAPTCHA_SESSION);
        if(null == sessionCaptcha)
            return false;
        sessionCaptcha = sessionCaptcha.toLowerCase();
        userInput = userInput.toLowerCase();
        return userInput.equals(sessionCaptcha);
    }
}
