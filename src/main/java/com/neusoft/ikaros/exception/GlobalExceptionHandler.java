package com.neusoft.ikaros.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidException(MethodArgumentNotValidException e) {

        Map<String, Object> res = new HashMap<>();

        String msg = e.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        res.put("code", 400);
        res.put("msg", msg);

        return res;
    }
}