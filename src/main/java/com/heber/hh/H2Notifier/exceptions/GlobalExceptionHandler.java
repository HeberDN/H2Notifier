package com.heber.hh.H2Notifier.exceptions;

import com.heber.hh.H2Notifier.dto.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception e){
        log.error("Erro interno Exception: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(new ApiResponse<>(false, e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));
        log.error("Erro de validação: {}", errors, e);
        ApiResponse <Map<String, String>> response = new ApiResponse<>(false, "Erro de validação", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolationException(ConstraintViolationException e){
        Map<String, String> errors = e.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage()
                ));
        log.error("Erro interno ConstraintViolationException: {}", errors, e);
        ApiResponse <Map<String, String>> response = new ApiResponse<>(false, "Erro de validação nos dados", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> hadleResourceNotFoundException(ResourceNotFoundException e){
        log.error("Erro interno ResourceNotFoundException: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage()));
    }

    @ExceptionHandler(ParcelaException.class)
    public ResponseEntity<ApiResponse<Void>> handleParcelaException(ParcelaException e){
        log.error("Erro interno ParcelaException: {}", e.getMessage());
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
    }

    @ExceptionHandler(PessoaException.class)
    public ResponseEntity<ApiResponse<Void>> handlePessoaException(PessoaException e){
        log.error("Erro interno PessoaException: {}", e.getMessage(), e);
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
    }

    @ExceptionHandler(NotificacaoException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotificacaoException(NotificacaoException e){
        log.error("Erro ao enviar notificação: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(new ApiResponse<>(false, e.getMessage()));
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<ApiResponse<Void>> handlerEmailException (EmailException e){
        log.error("Erro ao enviar e-mail: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError().body(new ApiResponse<>(false, "Ocorreu um erro interno no servidor. Por favor, tente novamente mais tarde." + e.getMessage()));
    }
}
