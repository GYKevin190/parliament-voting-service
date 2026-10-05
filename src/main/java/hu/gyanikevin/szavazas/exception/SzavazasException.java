package hu.gyanikevin.szavazas.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SzavazasException extends RuntimeException {

    private final HttpStatus status;

    public SzavazasException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
