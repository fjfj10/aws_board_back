package com.korit.board.aop;

import com.korit.board.exception.ValidException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Aop는 필터와 같은 역할을 한다(필터는 요청과 응답 모두 걸림)
 */

@Aspect
@Component
public class ValidAop {

//    @Pointcut("execution(* com.korit.board.controller.*.*(..))")
    @Pointcut("@annotation(com.korit.board.aop.annotation.ValidAop)")
    private void pointCut() {};

    @Around("pointCut()")/* 포인트컷 */
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    // getArgs(): 매개변수, getSignature(): 클래스정보
        Object[] args = proceedingJoinPoint.getArgs();
        BeanPropertyBindingResult bindingResult = null;
        for (Object arg: args) {
            if (arg.getClass() == BeanPropertyBindingResult.class) {
                // 있으면 다운캐스팅 해서 대입
                bindingResult = (BeanPropertyBindingResult) arg;
                break;
            }
        }

        if (bindingResult == null) {
            return proceedingJoinPoint.proceed();   // null일경우 그냥 통과 시켜버림
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            });
            throw new ValidException(errorMap);
        }


        // .proceed() 메소드가 호출되기 전이 전처리
        Object target = proceedingJoinPoint.proceed();  // 메소드의 body
        // .proceed() 메소드가 리턴된 후가 후처리
        return target;
    }
}
