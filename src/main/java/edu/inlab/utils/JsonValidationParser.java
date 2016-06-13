package edu.inlab.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by inlab-dell on 2016/6/13.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
public class JsonValidationParser {
    @JsonProperty
    private Map<String, String> fieldErrors = new HashMap<>();

    public JsonValidationParser(BindingResult bindingResult){
        for(FieldError fieldError: bindingResult.getFieldErrors()){
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
