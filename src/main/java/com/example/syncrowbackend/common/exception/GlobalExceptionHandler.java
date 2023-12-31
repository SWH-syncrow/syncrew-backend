package com.example.syncrowbackend.common.exception;

import com.example.syncrowbackend.friend.service.PostServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(PostServiceImpl.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException exception) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(exception.getErrorCode(), exception.getMessage());
        LOGGER.info(errorResponseDto.toString());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCHED, exception.getMessage());
        LOGGER.info(errorResponseDto.toString());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDto> handleBindException(BindException exception) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.PARAMETER_BINDING_ERROR, exception.getMessage());
        LOGGER.info(errorResponseDto.toString());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.METHOD_NOT_ALLOWED, exception.getMessage());
        LOGGER.info(errorResponseDto.toString());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception exception) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.INTERNAL_SERVER_ERROR, exception.getMessage());
        LOGGER.info(errorResponseDto.toString());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }
}
