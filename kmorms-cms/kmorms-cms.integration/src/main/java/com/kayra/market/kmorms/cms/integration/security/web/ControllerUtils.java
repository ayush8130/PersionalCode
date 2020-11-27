package com.kayra.market.kmorms.cms.integration.security.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.kayra.market.kmorms.model.validation.ValidationErrorMessages;

public class ControllerUtils {
    public static ResponseEntity<Object> responseEntityFor(String errorMessage, String errorCode, HttpStatus status) {
        ValidationErrorMessages errors = new ValidationErrorMessages();
        errors.addError(null, errorCode, errorMessage);
        return new ResponseEntity<>((Object) errors, new HttpHeaders(), status);
    }
}
