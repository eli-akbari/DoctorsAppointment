package com.blubank.doctorappointment.aop;
import com.blubank.doctorappointment.exception.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionHandlerAspect {

    @Around("@annotation(com.blubank.doctorappointment.exception.HandleAppointmentException)")
    public ResponseEntity<ApiResponse<?>> handleAppointmentExceptions(ProceedingJoinPoint joinPoint) {
        try {
            return (ResponseEntity<ApiResponse<?>>) joinPoint.proceed();
        } catch (EndTimeBeforeStartTimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ResponseStatus.END_TIME_BEFORE_START_TIME, e.getMessage()));
        } catch (AppointmentDurationTooShortException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ResponseStatus.DURATION_TOO_SHORT, e.getMessage()));
        } catch (DoctorDoesntExists e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(ResponseStatus.DOCTOR_NOT_FOUND, e.getMessage()));
        } catch (AppointmentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(ResponseStatus.APPOINTMENT_NOT_FOUND, e.getMessage()));
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new ApiResponse<>(ResponseStatus.CONFLICT, e.getMessage()));
        } catch (MissingPatientInfoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ResponseStatus.MISSING_PATIENT_INFO, e.getMessage()));
        } catch (PatientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(ResponseStatus.PATIENT_NOT_FOUND, e.getMessage()));
        } catch (TakenAppointmentException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new ApiResponse<>(ResponseStatus.APPOINTMENT_ALREADY_TAKEN, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        } catch (Throwable e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ResponseStatus.INTERNAL_SERVER_ERROR, null));
        }
    }
}
