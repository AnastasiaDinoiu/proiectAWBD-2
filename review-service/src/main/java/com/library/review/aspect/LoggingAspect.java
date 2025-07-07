package com.library.review.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.library.review.controller.*.*(..)) && " +
            "!execution(* org.springframework.boot.actuate..*(..))")
    public void controllerMethods() {}

    @Pointcut("execution(* com.library.review.service.*.*(..)) && " +
            "!execution(* org.springframework.boot.actuate..*(..))")
    public void serviceMethods() {}

    @Before("controllerMethods() || serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.debug("Entering method: {}.{}(..) with arguments: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "controllerMethods() || serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.debug("Method {}.{}(..) returned: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                result);
    }
}