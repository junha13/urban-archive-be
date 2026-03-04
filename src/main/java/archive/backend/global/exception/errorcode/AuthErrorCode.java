package archive.backend.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    AUTH_NOT_FOUND("", HttpStatus.NOT_FOUND, "");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
