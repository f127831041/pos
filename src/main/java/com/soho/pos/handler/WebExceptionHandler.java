package com.soho.pos.handler;


import com.soho.pos.ex.PortalException;
import com.soho.pos.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class WebExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        return handleExceptionInternal(ex, Result.failure(HttpStatus.OK, errors.get(0)), headers, HttpStatus.BAD_REQUEST, request);
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>(Result.failure(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }
    
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, Result.failure(HttpStatus.OK,ex.getMessage()), headers, HttpStatus.BAD_REQUEST, request);
    }
    
    @ExceptionHandler(PortalException.class)
    public ResponseEntity<?> handlePortalException(PortalException ex) {
        return new ResponseEntity<>(Result.failure(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOtherException(Exception ex) {
        return new ResponseEntity<>(Result.failure(HttpStatus.INTERNAL_SERVER_ERROR, "執行此操作時發生錯誤！請測試相關環境或洽詢服務廠商。"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
