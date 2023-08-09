package org.wm.data.annotation;

import java.lang.annotation.*;

/**
 * 功能描述：<功能描述>
 *     开启分页，标注在Mybatis Mapper接口方法上
 * 
 * @author dove 
 * @date 2023/07/19 22:16
 * @since 1.0
**/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UsePage {
}
