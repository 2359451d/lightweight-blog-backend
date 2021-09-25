package top.bento.blog.common.aop;

import java.lang.annotation.*;

// support annotated on method
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String module() default "";

    String operator() default "";
}
