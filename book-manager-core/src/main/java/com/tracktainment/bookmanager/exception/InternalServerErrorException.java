package com.tracktainment.bookmanager.exception;

public class InternalServerErrorException extends BusinessException {

    public InternalServerErrorException(String message) {
        super(
                ExceptionCode.INTERNAL_SERVER_ERROR,
                message
        );
    }
}
