package edu.inlab.config;

import edu.inlab.utils.Constants;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by inlab-dell on 2016/5/4.
 */

public class CreCrowdAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{RootConfig.class};
    }

    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }

    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setMultipartConfig(getMultipartConfigElement());
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        XssFilter xssFilter = new XssFilter();
        encodingFilter.setEncoding("UTF-8");
        return new Filter[]{encodingFilter, xssFilter};
    }

    private MultipartConfigElement getMultipartConfigElement(){
        MultipartConfigElement multipartConfigElement;
        try {
            Properties properties = getPropertiesFromResource();
            multipartConfigElement = new MultipartConfigElement(
                    properties.getProperty("upload.location"),
                    Long.valueOf(properties.getProperty("upload.max_size")),
                    Long.valueOf(properties.getProperty("upload.max_request_size")),
                    Integer.valueOf(properties.getProperty("upload.file_size_threshold"))
            );
        } catch (IOException e){
            System.err.println("Cannot read file-upload properties from file, use the default setting.");
            multipartConfigElement = new MultipartConfigElement(
                    Constants.UPLOAD_FILE_STORE_LOCATION,
                    Constants.MAX_FILE_SIZE,
                    Constants.MAX_REQUEST_SIZE,
                    Constants.FILE_SIZE_THRESHOLD);
        }

        return multipartConfigElement;
    }

    private Properties getPropertiesFromResource() throws IOException{
        //Ref http://crunchify.com/java-properties-file-how-to-read-config-properties-values-in-java/
        Properties properties = new Properties();
        String propFileName = "application.properties";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        if(inputStream != null){
            properties.load(inputStream);
        } else {
            throw new FileNotFoundException("Cannot read properties file " + propFileName);
        }
        return properties;
    }

}
