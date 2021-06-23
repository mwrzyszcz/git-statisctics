package com.gitstatisctics.exception;

import lombok.Getter;

@Getter
public class GitUserException extends RuntimeException {
    private final int errorCode;

    public GitUserException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
