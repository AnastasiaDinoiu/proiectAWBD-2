package com.library.apigateway.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.library.apigateway.controller.*.*(..))")
    public void controllerMethods() {}

    @Pointcut("execution(* com.library.apigateway.filter.*.*(..))")
    public void filterMethods() {}

    @Pointcut("execution(* com.library.apigateway.config.*.*(..))")
    public void configMethods() {}

    @Around("controllerMethods() || filterMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info("API-Gateway: Executing {}.{}() with arguments: {}",
                className, methodName, Arrays.toString(args));

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();

            logger.info("API-Gateway: {}.{}() executed successfully in {} ms",
                    className, methodName, (endTime - startTime));

            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();

            logger.error("API-Gateway: {}.{}() failed in {} ms with error: {}",
                    className, methodName, (endTime - startTime), e.getMessage());
            throw e;
        }
    }

    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        logger.debug("API-Gateway: Before executing {}.{}()", className, methodName);
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        logger.debug("API-Gateway: {}.{}() returned: {}", className, methodName, result);
    }

    @AfterThrowing(pointcut = "controllerMethods() || filterMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        logger.error("API-Gateway: Exception in {}.{}(): {}",
                className, methodName, exception.getMessage(), exception);
    }
}