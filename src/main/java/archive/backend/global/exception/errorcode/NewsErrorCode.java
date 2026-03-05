package archive.backend.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NewsErrorCode implements ErrorCode {

    NEWS_NOT_FOUND("", HttpStatus.INTERNAL_SERVER_ERROR, "");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
