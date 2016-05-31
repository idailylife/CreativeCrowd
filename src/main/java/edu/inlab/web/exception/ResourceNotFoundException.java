package edu.inlab.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by inlab-dell on 2016/5/11.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Cannot find target resource :(")
public final class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
