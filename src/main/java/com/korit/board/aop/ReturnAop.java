package com.korit.board.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ReturnAop {

    @Pointcut("@annotation(com.korit.board.aop.annotation.ReturnAop)")
    private void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Signature signature = proceedingJoinPoint.getSignature();
        CodeSignature codeSignature = (CodeSignature) signature;

        String className = codeSignature.getDeclaringTypeName();
        String methodName = codeSignature.getName();

        Object target = proceedingJoinPoint.proceed();

        System.out.println("==========================================================================================");
        System.out.println("클래스명: " + className + ", 메소드명: " + methodName);
        System.out.println("return: " + target);
        System.out.println("==========================================================================================");

        return target;
    }
}
