package com.poyraz.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.poyraz.controller..*(..))")
    public void controllerMethods() {
    }

    @Pointcut("execution(* com.poyraz.service..*(..))")
    public void serviceMethods() {
    }

    @Pointcut("execution(* com.poyraz.repository..*(..))")
    public void repositoryMethods() {
    }

    @Before("controllerMethods()")
    public void logBeforeController(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();

        log.info(">>>> Entering Controller: {}.{}() with args: {}",
                className,
                methodName,
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterReturningController(JoinPoint joinPoint, Object result) {
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();

        log.info("<<<< Exiting Controller: {}.{}() with result: {}",
                signature.getDeclaringTypeName(),
                methodName,
                (result != null ? result : "void"));
    }

    @AfterThrowing(pointcut = "controllerMethods() || serviceMethods() || repositoryMethods()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();

        log.error("!!!! Exception in {}.{}() with cause: {}",
                className,
                methodName,
                e.getMessage(),
                e);
    }
    
    @Around("serviceMethods() || repositoryMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();

        log.debug("--> Starting execution of {}.{}() with args: {}", className, methodName, Arrays.toString(args));

        try {
            Object result = joinPoint.proceed();

            long executionTime = System.currentTimeMillis() - start;

            log.debug("<-- Finished execution of {}.{}() in {}ms. Result: {}",
                    className,
                    methodName,
                    executionTime,
                    result);

            return result;
        } catch (Throwable t) {
            long executionTime = System.currentTimeMillis() - start;
            log.error("!!! Execution of {}.{}() failed in {}ms. Exception will be logged by AfterThrowing.",
                    className,
                    methodName,
                    executionTime);
            throw t;
        }
    }

}
