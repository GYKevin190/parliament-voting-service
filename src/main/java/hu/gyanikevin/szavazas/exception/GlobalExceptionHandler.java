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
        return new ResponseEntity<>(new HibaResponseDto(exception.getMessage()), exception.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HibaResponseDto> validaciosHiba(MethodArgumentNotValidException exception) {
        String hiba = exception.getBindingResult().getAllErrors().stream().map(error -> error.getDefaultMessage()).distinct().collect(Collectors.joining(" "));
        return new ResponseEntity<>(new HibaResponseDto(hiba), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HibaResponseDto> jsonHiba(HttpMessageNotReadableException exception) {
        return new ResponseEntity<>(new HibaResponseDto("Hibás JSON-struktúra, időpont vagy érvénytelen szavazás-, eljárás-, illetve szavazatkód."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<HibaResponseDto> egyedisegiHiba(DataIntegrityViolationException exception) {
        return new ResponseEntity<>(new HibaResponseDto("Az időpont vagy a képviselő szavazata már szerepel az adatbázisban."), HttpStatus.CONFLICT);
    }
}
