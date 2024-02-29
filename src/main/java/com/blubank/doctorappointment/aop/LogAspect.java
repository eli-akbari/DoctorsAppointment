package com.blubank.doctorappointment.aop;

import com.blubank.doctorappointment.exception.ApiResponse;
import com.blubank.doctorappointment.exception.HandleAppointmentException;
import com.blubank.doctorappointment.exception.ResponseStatus;
import com.blubank.doctorappointment.model.dto.*;
import com.blubank.doctorappointment.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* com.blubank.doctorappointment.controller.*.*(..))")
    public void logBeforeControllerMethodCall(JoinPoint joinPoint) {
        log.info("Entering method: " + joinPoint.getSignature().getName());
        log.debug("Arguments: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "execution(* com.blubank.doctorappointment.controller.*.*(..))", returning = "result")
    public void logAfterControllerMethodCall(JoinPoint joinPoint, Object result) {
        log.info("Exiting method: " + joinPoint.getSignature().getName());
        log.debug("Return value: " + result);
    }
}
