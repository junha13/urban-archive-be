package archive.backend.global.exception;

import archive.backend.global.exception.errorcode.ErrorCode;

public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorcode, ErrorCode errorCode) {
        super(errorcode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
