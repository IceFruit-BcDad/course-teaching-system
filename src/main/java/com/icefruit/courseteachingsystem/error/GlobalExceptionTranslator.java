package com.icefruit.courseteachingsystem.error;

import com.icefruit.courseteachingsystem.api.Response;
import com.icefruit.courseteachingsystem.api.ResultCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Set;


@RestControllerAdvice
public class GlobalExceptionTranslator {

    static final Logger logger = LoggerFactory.getLogger(GlobalExceptionTranslator.class);

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Response handleError(MissingServletRequestParameterException e) {
        logger.warn("Missing Request Parameter", e);
        String message = String.format("Missing Request Parameter: %s", e.getParameterName());
        return Response
                .builder()
                .code(ResultCode.PARAM_MISS)
                .message(message)
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Response handleError(MethodArgumentTypeMismatchException e) {
        logger.warn("Method Argument Type Mismatch", e);
        String message = String.format("Method Argument Type Mismatch: %s", e.getName());
        return Response
                .builder()
                .code(ResultCode.PARAM_TYPE_ERROR)
                .message(message)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleError(MethodArgumentNotValidException e) {
        logger.warn("Method Argument Not Valid", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String message = "";
        if (error != null){
            message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        }
        return Response
                .builder()
                .code(ResultCode.PARAM_VALID_ERROR)
                .message(message)
                .build();
    }

    @ExceptionHandler(BindException.class)
    public Response handleError(BindException e) {
        logger.warn("Bind Exception", e);
        FieldError error = e.getFieldError();
        String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        return Response
                .builder()
                .code(ResultCode.PARAM_BIND_ERROR)
                .message(message)
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Response handleError(ConstraintViolationException e) {
        logger.warn("Constraint Violation", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String path = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String message = String.format("%s:%s", path, violation.getMessage());
        return Response
                .builder()
                .code(ResultCode.PARAM_VALID_ERROR)
                .message(message)
                .build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Response handleError(NoHandlerFoundException e) {
        logger.error("404 Not Found", e);
        return Response
                .builder()
                .code(ResultCode.NOT_FOUND)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response handleError(HttpMessageNotReadableException e) {
        logger.error("Message Not Readable", e);
        return Response
                .builder()
                .code(ResultCode.MSG_NOT_READABLE)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response handleError(HttpRequestMethodNotSupportedException e) {
        logger.error("Request Method Not Supported", e);
        return Response
                .builder()
                .code(ResultCode.METHOD_NOT_SUPPORTED)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Response handleError(HttpMediaTypeNotSupportedException e) {
        logger.error("Media Type Not Supported", e);
        return Response
                .builder()
                .code(ResultCode.MEDIA_TYPE_NOT_SUPPORTED)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(ServiceException.class)
    public Response handleError(ServiceException e) {
        logger.error("Service Exception", e);
        return Response
                .builder()
                .code(e.getResultCode())
                .message(e.getMessage())
                .build();
    }

//    @ExceptionHandler(PermissionDeniedException.class)
//    public Response handleError(PermissionDeniedException e) {
//        logger.error("Permission Denied", e);
//        return Response
//                .builder()
//                .code(e.getResultCode())
//                .message(e.getMessage())
//                .build();
//    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Response handleError(MaxUploadSizeExceededException e) {
        logger.error("Upload file too large", e);
        return Response
                .builder()
                .code(ResultCode.REQUEST_ENTITY_TOO_LARGE)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(Throwable.class)
    public Response handleError(Throwable e) {
        logger.error("Internal Server Error", e);
        return Response
                .builder()
                .code(ResultCode.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();
    }
}
