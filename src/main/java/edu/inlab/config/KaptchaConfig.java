package edu.inlab.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Properties;

/**
 * Created by inlab-dell on 2016/5/17.
 */
@Configuration
@PropertySource(value = {"classpath:application.properties"})
public class KaptchaConfig {

    @Autowired
    private Environment environment;

    private DefaultKaptcha kaptcha;

    @Bean(name = "captchaProducer")
    public DefaultKaptcha getKaptchaProducer(){
        if(null == kaptcha) {
            kaptcha = new DefaultKaptcha();
            kaptcha.setConfig(getKaptchaConfig());
        }
        return kaptcha;
    }


    @Bean
    public Config getKaptchaConfig(){
        return new Config(kaptchaProperties());
    }

    private Properties kaptchaProperties(){
        Properties properties = new Properties();
        properties.put("kaptcha.image.width",
                environment.getRequiredProperty("kaptcha.image.width"));
        properties.put("kaptcha.image.height",
                environment.getRequiredProperty("kaptcha.image.height"));
        properties.put("kaptcha.textproducer.char.string",
                environment.getRequiredProperty("kaptcha.textproducer.char.string"));
        properties.put("kaptcha.textproducer.char.length",
                environment.getRequiredProperty("kaptcha.textproducer.char.length"));
        return properties;
    }
}
