package org.wm.data.aspect;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.wm.data.annotation.UsePage;
import org.wm.commons.dto.MyPageParam;
import org.wm.commons.exception.PageParamException;

/**
 * 功能描述：<功能描述>
 *     解析分页参数注解@UsePage
 *
 *
 * @author dove 
 * @date 2023/07/19 22:20
 * @since 1.0
**/

@Slf4j
@Aspect
@Component
public class UsePageAspect {

    /**
     *  切入点
     */
    private static final String POINTCUT_SIGN = "@annotation(org.wm.data.annotation.UsePage)";

    @Pointcut(POINTCUT_SIGN)
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 注解鉴权
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        var method = signature.getMethod();
        var userPage = method.getAnnotation(UsePage.class);

        if (userPage != null) {
            MyPageParam myPageParam = null;
            var args = joinPoint.getArgs();
            for (Object arg : args) {
                if (MyPageParam.class.isAssignableFrom(arg.getClass())) {
                    myPageParam = (MyPageParam) arg;
                    break;
                }
            }
            if (myPageParam == null) {
                log.error("使用注解@UsePage开启分页时，需要传递MyPageParam类型参数，参数列表{}不存在MyPageParam", args);
                throw new PageParamException("分页参数MyPageParam类型不存在");
            }
            PageHelper.startPage(myPageParam.getCurrent(), myPageParam.getPageSize());
        }
        // 执行原有逻辑
        return joinPoint.proceed();
    }



}
