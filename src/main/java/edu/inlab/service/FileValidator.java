package edu.inlab.service;

import edu.inlab.models.FileBucket;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by inlab-dell on 2016/5/20.
 */
@Component
public class FileValidator implements Validator{
    public boolean supports(Class<?> clazz) {
        return FileBucket.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        FileBucket fileBucket = (FileBucket)target;
        if(fileBucket.getFile() != null){
            if(fileBucket.getFile().getSize() == 0){
                errors.rejectValue("file", "missing.file");
            }
        } else {
            errors.rejectValue("file", "missing.null");
        }
    }
}
