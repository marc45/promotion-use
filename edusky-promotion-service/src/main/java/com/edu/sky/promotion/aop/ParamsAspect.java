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
import java.util.stream.Collectors;

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
        paramHandle(point);
    }
    @AfterReturning(value = "servicePoint()",returning = "result")
    public void serviceAfterReturn(JoinPoint point,Object result) {
        paramResult(point,result);
    }

    private void paramHandle(JoinPoint point){
        try {
            String className = point.getTarget().getClass().getName();
            Class<?> clazz = Class.forName(className);
            Method[] methods = clazz.getMethods();
            List<Method> methodList = (new ArrayList<Method>(Arrays.asList(methods)));
            String methodName = point.getSignature().getName();
            List<Method> methods1 = methodList.stream()
                    .filter(m -> m.getName().equals(methodName)).collect(Collectors.toList());
            Method method = null;
            for (Method method1 : methods1) {
                if (point.getArgs().length == method1.getParameterCount()) {
                    method = method1;
                }
            }
            String targetName = className + "." + methodName;
            List<String> paramNames = getParamNames(method);
            Class<?>[] parameterTypes = method.getParameterTypes();
            String methodName1 = targetName + "{0}";
            String methodNameSb = getMethodNameSb(method.getParameterTypes(), paramNames, methodName1);
            logger.info("\n     ------> 请求接口：" + methodNameSb +
                    "\n     ------> 参数为：" + getParamToString(point.getArgs(), paramNames)
            );
        } catch (Exception e) {
            logger.info("出现异常 = {}",e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**请求结果response
     * @param point
     * @param result
     */
    private void paramResult(JoinPoint point,Object result){
        try {
            String className = point.getTarget().getClass().getName();
            Class<?> clazz = Class.forName(className);
            Method[] methods = clazz.getMethods();
            List<Method> methodList = (new ArrayList<Method>(Arrays.asList(methods)));
            String methodName = point.getSignature().getName();
            List<Method> methods1 = methodList.stream()
                    .filter(m -> m.getName().equals(methodName)).collect(Collectors.toList());
            Method method = null;
            for (Method method1 : methods1) {
                if (point.getArgs().length == method1.getParameterCount()) {
                    method = method1;
                }
            }
            List<String> paramNames = getParamNames(method);
            String name = className + "." + methodName + "{0}";
            String methodNameSb = getMethodNameSb(method.getParameterTypes(),paramNames,name);
            logger.info("\n     ------> 请求回应接口：" + methodNameSb +
                    "\n     ------> 回应参数为：" + getParamToString(point.getArgs(),paramNames)
                    + (result != null ? "\n     ------> 返回值为：" + JSON.toJSONString(result) : "")
            );
        } catch (Exception e) {
            logger.info("出现异常 = {}",e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**获取参数名称
     * @param method
     * @return
     */
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

    /**拼接请求参数为String
     * @param args
     * @param paramNames
     * @return
     */
    private String getParamToString(Object[] args,List<String> paramNames){
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < paramNames.size(); i++) {
            jsonObject.put(paramNames.get(i),args[i]);
        }
        return jsonObject.toJSONString();
    }

    /**拼接请求接口及参数名称
     * @param parameterTypes
     * @param paramNames
     * @param name
     * @return
     */
    private String getMethodNameSb(Class<?>[] parameterTypes,List<String> paramNames,String name){
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        if (parameterTypes.length == paramNames.size()) {
            for (int i = 0; i < parameterTypes.length; i++) {
                if ((i + 1) == parameterTypes.length) {
                    sb.append(parameterTypes[i].getSimpleName() + " " + paramNames.get(i));
                } else {
                    sb.append(parameterTypes[i].getSimpleName() + " " + paramNames.get(i) + ",");
                }
            }
        } else {
            sb.append("有参数没有@ParamAop注解");
        }
        sb.append(")");
        return MessageFormat.format(name, sb.toString());
    }


}
