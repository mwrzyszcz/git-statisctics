package com.gitstatisctics.exception;

import com.gitstatisctics.dto.ErrorResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(value = GitUserException.class)
    ResponseEntity<ErrorResponseDTO> onGitUserException(GitUserException ex) {
        return ResponseEntity
                .status(ex.getErrorCode())
                .body(ErrorResponseDTO.builder()
                        .message(ex.getMessage())
                        .build());
    }
}
