package com.edu.sky.promotion.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.edu.sky.core.exception.ResultBean;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**获取接口请求信息
 * @author songwei
 */
@Aspect
@Component
@EnableAspectJAutoProxy
public class ParamsAspect {
    private Logger logger = LoggerFactory.getLogger(ParamsAspect.class);

    @Pointcut(value = "execution(* com.edu.sky.promotion.service.*.*(..))")
    public void servicePoint(){}

    @Around(value = "servicePoint()")
    public Object serviceAround(ProceedingJoinPoint pjp){
        try {
            String methodName = pjp.getSignature().getName();
            String className = pjp.getTarget().getClass().getName();
            Class<?> clazz = Class.forName(className);
            Method[] methods = clazz.getMethods();
            List<Method> methodList = new ArrayList<Method>(Arrays.asList(methods));
            Method method = methodList.stream().filter(m -> m.getName().equals(methodName)).findFirst().get();
            int count = method.getParameterCount();
            if (count == 0) {
                logger.info("\n     ------> 请求接口：" + pjp.getTarget().getClass().getName() + "." + methodName + "()" +
                        "\n     ------> 参数为：接口无参数");
            } else {
                List<String> paramNames =getParamNames(method);//参数名称
                StringBuilder sb = new StringBuilder();
                Class<?>[] parameterTypes = method.getParameterTypes();
                sb.append("(");
                for (int i = 0; i < parameterTypes.length;i++) {
                    if ((i+1) == parameterTypes.length) {
                        sb.append(parameterTypes[i].getSimpleName() + " " + paramNames.get(i));
                    }else {
                        sb.append(parameterTypes[i].getSimpleName() + " " + paramNames.get(i) + ",");
                    }
                }
                sb.append(")");
                String name = pjp.getTarget().getClass().getSimpleName() + "." + methodName + "{0}";
                String methodNameSb = MessageFormat.format(name, sb.toString());
                Object[] args = pjp.getArgs();
                logger.info("\n     ------> 请求接口：" + methodNameSb +
                            "\n     ------> 参数为：" + JSON.toJSONString(args)
                );
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < args.length; i++) {
                    jsonObject.put(paramNames.get(i),args[i]);
                }
                Object result = pjp.proceed(args);
                logger.info("\n     ------> 请求接口：" + methodNameSb +
                        "\n     ------> 参数为：" + jsonObject.toJSONString()
                        + (result != null ? "\n     ------> 返回值为：" + JSON.toJSONString(result) : "接口无返回值")
                );
                return result;
            }
        } catch (Throwable throwable) {
            logger.error("出现异常 = {}",throwable);
            throw new RuntimeException(ResultBean.getFailResultString(500,throwable.getMessage()));
        }
        return null;
    }

    //获取参数名称
    private List<String> getParamNames(Method method){
        List<String> paramNames = new ArrayList<>();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                paramNames.add(((ParamAsp)annotation).value());
            }
        }
        return paramNames;
    }


}
