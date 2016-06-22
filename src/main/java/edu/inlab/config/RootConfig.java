package edu.inlab.config;

import edu.inlab.service.LoggingService;
import edu.inlab.service.ScheduledTaskService;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by inlab-dell on 2016/5/4.
 */
@Configuration
@EnableScheduling
@ComponentScan(basePackages = "edu.inlab",
        excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)
})
@EnableAspectJAutoProxy
public class RootConfig {

    @Bean
    public LoggingService loggingService(){
        return new LoggingService();
    }

    @Bean
    public ScheduledTaskService scheduledTaskService(){
        return new ScheduledTaskService();
    }
}
