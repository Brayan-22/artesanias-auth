package com.artesanias.authenticationservice.advice;

import com.artesanias.authenticationservice.exceptions.NotValidTokenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(NotValidTokenException.class)
    public ResponseEntity<String> handleNotValidTokenException(NotValidTokenException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
