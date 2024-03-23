package com.example.ratelimitor.ratelimitor_app.exception;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class AppExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<Object> handleRequestNotPermitted(RequestNotPermitted ex, HttpServletRequest request) {
        logger.warn("Request to path '{}' is blocked due to rate-limiting. {}",
                request.getRequestURI(), ex.getMessage());

        return new ResponseEntity<>("Too many requests", HttpStatus.TOO_MANY_REQUESTS);
    }
}
