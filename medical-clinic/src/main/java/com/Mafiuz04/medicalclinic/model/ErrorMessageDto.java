package com.Mafiuz04.medicalclinic.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class ErrorMessageDto {
    private final HttpStatus status;
    private final String message;
}
