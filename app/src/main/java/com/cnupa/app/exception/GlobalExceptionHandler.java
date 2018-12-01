package com.cnupa.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler()
    @ResponseBody
    String handleException(Exception e){
     	System.out.println(e);
        return "Exception:" + e.getMessage();
        
    }


    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(value=HttpStatus.UNAUTHORIZED,reason = "用户名和密码不匹配!")
    @ResponseBody
    public String handlerResponseBankException(Exception e){
    	ResponseStatus t;
    	
    	return e.getMessage();
    }
}