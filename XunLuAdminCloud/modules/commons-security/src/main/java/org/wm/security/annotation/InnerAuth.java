package org.wm.security.annotation;

import java.lang.annotation.*;

/**
 * 功能描述：<功能描述>
 * 内部认证注解、例如，定时器调用其他服务不需要用户信息，但有不能让外部调用接口时，使用该注解
 *
 * @author dove
 * @date 2023/7/12 23:28
 * @since 1.0
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InnerAuth {
    /**
     * 是否校验用户信息
     */
    boolean isUser() default false;
}