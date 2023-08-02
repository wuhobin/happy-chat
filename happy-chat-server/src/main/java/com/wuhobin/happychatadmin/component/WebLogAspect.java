package com.wuhobin.happychatadmin.component;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wuhobin.entity.weblog.WebLog;
import com.wuhobin.utils.http.WebUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 日志切面
 *
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/7/10 15:46
 * @description
 */
@Aspect
@Component
@Slf4j
@Order(1) // 设置该类在spring 容器中的加载顺序
public class WebLogAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void requestLog() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getLog() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postLog() {
    }


    @Around("requestLog() || getLog() || postLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        try {
            //生成web日志，不影响请求结果
            generatorWebLog(joinPoint, startTime, request, result);
        } catch (Exception e) {
            log.error("web请求日志异常", e);
        }
        return result;
    }

    private void generatorWebLog(ProceedingJoinPoint joinPoint, long startTime, HttpServletRequest request, Object result) {
        String url = request.getRequestURL().toString();
        WebLog webLog =  WebLog.builder()
                .ip(WebUtils.getClientRealIp(request))
                .method(request.getMethod())
                .result(result)
                .description(getDescription(joinPoint))
                .uri(request.getRequestURI())
                .basePath(StrUtil.removeSuffix(url, URLUtil.url(url).getPath()))
                .url(url)
                .spendTime((int) (System.currentTimeMillis() - startTime))
                .parameter(getParameter(joinPoint))
                .startTime(startTime)
                .build();
        log.info("web请求日志：{}", JSONUtil.toJsonStr(webLog));
    }



    /**
     * 根据方法和传入的参数获取请求参数
     */
    private Object getParameter(ProceedingJoinPoint joinPoint) {
        //获取方法签名
        MethodSignature signature =(MethodSignature) joinPoint.getSignature();
        //获取参数名称
        String[] parameterNames = signature.getParameterNames();
        //获取所有参数
        Object[] args = joinPoint.getArgs();
        //请求参数封装
        JSONObject jsonObject = new JSONObject();
        if(parameterNames !=null && parameterNames.length >0){
            for(int i=0; i<parameterNames.length;i++){
                jsonObject.put(parameterNames[i],args[i]);
            }
        }
        return jsonObject;
    }


    /**
     * 获取方法描述
     */
    private String getDescription(ProceedingJoinPoint joinPoint) {
        //获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取方法
        Method method = signature.getMethod();
        //获取注解对象
        ApiOperation annotation = method.getAnnotation(ApiOperation.class);
        if (Objects.isNull(annotation)) {
            return "";
        }
        return annotation.value();
    }



}
