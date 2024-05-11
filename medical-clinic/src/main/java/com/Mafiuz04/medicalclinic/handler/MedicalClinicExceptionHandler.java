package com.Mafiuz04.medicalclinic.handler;

import com.Mafiuz04.medicalclinic.exception.MedicalClinicException;
import com.Mafiuz04.medicalclinic.model.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice// kiedy mamy RestControllera nie musimy używać ResponseEntity<T>
public class MedicalClinicExceptionHandler {

    @ExceptionHandler(MedicalClinicException.class)
    public ResponseEntity<ErrorMessageDto> MedicalClinicException(MedicalClinicException ex) {
        return ResponseEntity.status(ex.getStatus()).body(new ErrorMessageDto(ex.getStatus(), ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String anyExceptionResponse() {
        return "Unknown Error";
    }
}
