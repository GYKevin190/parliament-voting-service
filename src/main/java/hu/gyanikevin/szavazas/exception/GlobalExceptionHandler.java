package hu.gyanikevin.szavazas.exception;

import hu.gyanikevin.szavazas.dto.response.HibaResponseDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SzavazasException.class)
    public ResponseEntity<HibaResponseDto> szavazasHiba(SzavazasException exception) {
        return ResponseEntity.status(exception.getStatus()).body(new HibaResponseDto(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HibaResponseDto> validaciosHiba(MethodArgumentNotValidException exception) {
        String hiba = exception.getBindingResult().getAllErrors().stream().map(error -> error.getDefaultMessage()).distinct().collect(Collectors.joining(" "));
        return ResponseEntity.badRequest().body(new HibaResponseDto(hiba));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HibaResponseDto> jsonHiba(HttpMessageNotReadableException exception) {
        return ResponseEntity.badRequest().body(new HibaResponseDto("Hibás JSON-struktúra, időpont vagy érvénytelen szavazás-, eljárás-, illetve szavazatkód."));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<HibaResponseDto> egyedisegiHiba(DataIntegrityViolationException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new HibaResponseDto("Az időpont vagy a képviselő szavazata már szerepel az adatbázisban."));
    }
}
