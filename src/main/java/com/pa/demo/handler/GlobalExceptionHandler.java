package com.pa.demo.handler;

import com.pa.demo.dto.APIResponse;
import com.pa.demo.dto.ErrorDto;
import com.pa.demo.exception.CustomerNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<APIResponse> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        List<ErrorDto> errors = new ArrayList<>();
        ErrorDto error = ErrorDto.builder()
                .field("CustomerNotFound")
                .errorMessage(ex.getMessage())
                .build();
        errors.add(error);
        APIResponse apiResponse = APIResponse.builder()
                .status("FAILED")
                .errors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex) {
        List<ErrorDto> errors = new ArrayList<>();
        ErrorDto error = ErrorDto.builder()
                .field("ExpiredJwtException")
                .errorMessage(ex.getMessage())
                .build();
        errors.add(error);
        APIResponse apiResponse = APIResponse.builder()
                .status("FAILED")
                .errors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Object> handleSignatureException(SignatureException ex) {
        List<ErrorDto> errors = new ArrayList<>();
        ErrorDto error = ErrorDto.builder()
                .field("SignatureException")
                .errorMessage(ex.getMessage())
                .build();
        errors.add(error);
        APIResponse apiResponse = APIResponse.builder()
                .status("FAILED")
                .errors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<APIResponse> handleMalformedJwtException(MalformedJwtException ex) {
        List<ErrorDto> errors = new ArrayList<>();
        ErrorDto error = ErrorDto.builder()
                .field("MalformedJwtException")
                .errorMessage(ex.getMessage())
                .build();
        errors.add(error);
        APIResponse apiResponse = APIResponse.builder()
                .status("FAILED")
                .errors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<APIResponse> handleBadCredentialsException(BadCredentialsException ex) {
        List<ErrorDto> errors = new ArrayList<>();
        ErrorDto error = ErrorDto.builder()
                .field("BadCredentialsException")
                .errorMessage(ex.getMessage())
                .build();
        errors.add(error);
        APIResponse apiResponse = APIResponse.builder()
                .status("FAILED")
                .errors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIResponse> handleBadCredentialsException(AccessDeniedException ex) {
        log.error("error: ", ex);
        List<ErrorDto> errors = new ArrayList<>();
        ErrorDto error = ErrorDto.builder()
                .field("AccessDeniedException")
                .errorMessage(ex.getMessage())
                .build();
        errors.add(error);
        APIResponse apiResponse = APIResponse.builder()
                .status("FAILED")
                .errors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<APIResponse> handleBadCredentialsException(AuthenticationException ex) {
        List<ErrorDto> errors = new ArrayList<>();
        ErrorDto error = ErrorDto.builder()
                .field("AuthenticationException")
                .errorMessage(ex.getMessage())
                .build();
        errors.add(error);
        APIResponse apiResponse = APIResponse.builder()
                .status("FAILED")
                .errors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<APIResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        return errorResponseBuilder(ex, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleGeneralException(Exception ex) {
        log.debug("Get error named " + ex.getClass().getSimpleName() + ": ", ex);
        log.error("Get error named " + ex.getClass().getSimpleName() + ": ", ex);
        return errorResponseBuilder(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<APIResponse> errorResponseBuilder (Exception ex, HttpStatus httpStatus) {
        String exceptionName = ex.getClass().getSimpleName();
        List<ErrorDto> errors = new ArrayList<>();
        ErrorDto error = ErrorDto.builder()
                .field(exceptionName)
                .errorMessage(ex.getMessage())
                .build();
        errors.add(error);
        APIResponse apiResponse = APIResponse.builder()
                .status("FAILED")
                .errors(errors)
                .build();
        return ResponseEntity.status(httpStatus).body(apiResponse);
    }
}

