package edu.inlab.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by inlab-dell on 2016/5/31.
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Task can not be assigned.")
public class TaskAssignException extends RuntimeException {
}
