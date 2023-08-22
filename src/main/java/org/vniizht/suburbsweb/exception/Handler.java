package org.vniizht.suburbsweb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.LoginException;
import java.util.concurrent.RejectedExecutionException;

@ControllerAdvice
public class Handler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) throws Exception {
        return new ResponseEntity<>(ex.getLocalizedMessage(), getHttpStatus(ex));
    }

    private HttpStatus getHttpStatus(Exception ex) throws Exception {
        if (ex instanceof RejectedExecutionException) {
            return HttpStatus.IM_USED;
        } else if (ex instanceof LoginException) {
            return HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof UserCheckException) {
            return HttpStatus.FORBIDDEN;
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            return HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        } else if (ex instanceof HttpMessageNotReadableException || ex instanceof MissingRequestHeaderException) {
            return HttpStatus.UNPROCESSABLE_ENTITY;
        } else {
            throw ex;
        }
    }
}
