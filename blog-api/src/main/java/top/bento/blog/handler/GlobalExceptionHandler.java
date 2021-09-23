package top.bento.blog.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.bento.blog.exception.AuthenticationException;
import top.bento.blog.exception.TokenValidationException;
import top.bento.blog.vo.Result;

// globally intercept and process @Controller methods (AOP)
@ControllerAdvice
public class GlobalExceptionHandler {

    //handle LoginException class
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public Result handleAuthenticationException(AuthenticationException ex) {
        ex.printStackTrace();
        return Result.fail(ex.getCode(), ex.getMsg());
    }

    //handle TokenValidationException.class
    @ExceptionHandler(TokenValidationException.class)
    public Result handleTokenValidationException(TokenValidationException ex) {
        ex.printStackTrace();
        return Result.fail(ex.getCode(), ex.getMsg());
    }

    //handle Exception.class
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleException(Exception ex) {
        ex.printStackTrace();
        return Result.fail(-999, "System Exception");
    }
}
