package com.library.user.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void serviceLayer() {}

    @Pointcut("@within(org.springframework.stereotype.Repository)")
    public void repositoryLayer() {}

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController) || " +
            "@within(org.springframework.stereotype.Controller)")
    public void controllerLayer() {}

    @Before("serviceLayer() || repositoryLayer() || controllerLayer()")
    public void logBefore(JoinPoint joinPoint) {
        log.debug("Entering method: {} with arguments: {}",
                joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "serviceLayer() || repositoryLayer() || controllerLayer()",
            returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.debug("Method {} returned: {}",
                joinPoint.getSignature().toShortString(),
                result);
    }

    @AfterThrowing(pointcut = "serviceLayer() || repositoryLayer() || controllerLayer()",
            throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("Exception in method: {} with message: {}",
                joinPoint.getSignature().toShortString(),
                exception.getMessage());
    }

    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    public Object logTransactionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            log.info("Transaction {} executed in {} ms",
                    joinPoint.getSignature().toShortString(),
                    elapsedTime);
            return result;
        } catch (Throwable throwable) {
            long elapsedTime = System.currentTimeMillis() - start;
            log.error("Transaction {} failed after {} ms",
                    joinPoint.getSignature().toShortString(),
                    elapsedTime);
            throw throwable;
        }
    }
}