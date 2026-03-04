package archive.backend.global.exception.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getCode();

    HttpStatus getHttpStatus();

    String getMessage();
}
