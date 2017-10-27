package com.edu.sky.promotion.aop;

import java.lang.annotation.*;

/**参数注解：获取参数名称
 * @author songwei
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamAsp {

    String value();
}
