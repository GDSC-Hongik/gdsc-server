package com.gdschongik.gdsc.global.exception;

public record ErrorResponse(String errorCodeName, String errorMessage) {
    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.name(), errorCode.getMessage());
    }

    public static ErrorResponse of(String errorCodeName, String errorMessage) {
        return new ErrorResponse(errorCodeName, errorMessage);
    }
}
