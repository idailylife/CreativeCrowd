package edu.inlab.models;

import javax.persistence.Transient;

/**
 * Created by inlab-dell on 2016/5/17.
 */
public class CaptchaCapableModel {
    @Transient
    protected String captcha;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
