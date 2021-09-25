package top.bento.blog.common.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import top.bento.blog.utils.HttpContextUtils;
import top.bento.blog.utils.IpUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Component
@Aspect
@Slf4j
public class LogAspect {

    // reusable pointcut - cut-into method custom annotated with "@LogAnnotation"
    @Pointcut("@annotation(top.bento.blog.common.aop.LogAnnotation)")
    public void pt() {
    }

    //    Around notification
    @Around("pt()")
    public Object log(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // invoke target method
        Object result = point.proceed();
        // execution time usage
        long time = System.currentTimeMillis() - beginTime;
        // record the log
        recordLog(point, time);
        return result;
    }

    private void recordLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        log.info("=====================log begins================================");
        log.info("module:{}", logAnnotation.module());
        log.info("operation:{}", logAnnotation.operator());

        // request method name
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("request method:{}", className + "." + methodName + "()");

        // request method args
        Object[] args = joinPoint.getArgs();
        String params = JSON.toJSONString(args[0]);
        log.info("params:{}", params);

        // parse request & get ip addr
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.info("ip:{}", IpUtils.getIpAddr(request));


        log.info("execution time : {} ms", time);
        log.info("=====================log ends================================");
    }

}
