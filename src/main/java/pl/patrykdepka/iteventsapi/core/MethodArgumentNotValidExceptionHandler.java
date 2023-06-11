package pl.patrykdepka.iteventsapi.core;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errorMap = new HashMap<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            if (errorMap.containsKey(fieldError.getField())) {
                errorMap.get(fieldError.getField()).add(fieldError.getDefaultMessage());
            } else {
                List<String> newErrorList = new ArrayList<>();
                newErrorList.add(fieldError.getDefaultMessage());
                errorMap.put(fieldError.getField(), newErrorList);
            }
        }

        return errorMap;
    }
}
