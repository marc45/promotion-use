package com.edu.sky.promotion.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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

    @Before(value = "servicePoint()")
    public void serviceBefore(JoinPoint point) {
        paramHandle(point,null);
    }
    @AfterReturning(value = "servicePoint()",returning = "result")
    public void serviceAfterReturn(JoinPoint point,Object result) {
        paramHandle(point,result);
    }

    private void paramHandle(JoinPoint point,Object result){
        try {
            String methodName = point.getSignature().getName();

            String className = point.getTarget().getClass().getName();
            Class<?> clazz = Class.forName(className);
            Method[] methods = clazz.getMethods();
            List<Method> methodList = new ArrayList<Method>(Arrays.asList(methods));
            Method method = methodList.stream().filter(m -> m.getName().equals(methodName)).findFirst().get();
            int count = method.getParameterCount();
            if (count == 0) {
                logger.info("\n     ------> 请求接口：" + point.getTarget().getClass().getName() + "." + methodName + "()" +
                            "\n     ------> 参数为：改接口无参数");
                return;
            }
            List<String> paramNames =getParamNames(method);//参数名称
            String name = point.getTarget().getClass().getSimpleName() + "." + methodName + "{0}";
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] args = point.getArgs();
            if (paramNames.size() != parameterTypes.length) {
                logger.info("\n     ------> 请求接口：" + name +
                        "\n     ------> 参数名称为：" + JSON.toJSONString(paramNames) +
                        "\n     ------> 参数为：" + JSON.toJSONString(args)
                );
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            for (int i = 0; i < parameterTypes.length;i++) {
                if ((i+1) == parameterTypes.length) {
                    sb.append(parameterTypes[i].getSimpleName() + " " + paramNames.get(i));
                }else {
                    sb.append(parameterTypes[i].getSimpleName() + " " + paramNames.get(i) + ",");
                }
            }
            sb.append(")");
            String methodNameSb = MessageFormat.format(name, sb.toString());
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < args.length; i++) {
                jsonObject.put(paramNames.get(i),args[i]);
            }
            logger.info("\n     ------> 请求接口：" + methodNameSb +
                        "\n     ------> 参数为：" + jsonObject.toJSONString()
                            + (result != null ? "\n     ------> 返回值为：" + JSON.toJSONString(result) : "")
                    );
        } catch (Exception e) {
            logger.info("出现异常 = {}",e);
        }
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
