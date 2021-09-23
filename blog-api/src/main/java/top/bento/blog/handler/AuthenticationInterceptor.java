package top.bento.blog.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.bento.blog.dao.pojo.SysUser;
import top.bento.blog.exception.AuthenticationException;
import top.bento.blog.service.AuthenticationService;
import top.bento.blog.utils.UserThreadLocal;
import top.bento.blog.vo.ErrorCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j // log - lombok
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * 1. check whether request url belongs to authentication controller
     * 2. check whether token is valid
     * 3. if no exception, forward the request
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            // possible handler: RequestResourceHandler
            return true;
        }
        String token = request.getHeader("Authorization");

        // log sout
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        if (StringUtils.isBlank(token)) {
            throw new AuthenticationException(ErrorCode.USER_NOT_LOGIN.getCode(), ErrorCode.USER_NOT_LOGIN.getMsg());
        }
        SysUser user = authenticationService.validateToken(token);
        if (user == null) {
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN_ERROR.getCode(), ErrorCode.INVALID_TOKEN_ERROR.getMsg());
        }
        UserThreadLocal.put(user);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
