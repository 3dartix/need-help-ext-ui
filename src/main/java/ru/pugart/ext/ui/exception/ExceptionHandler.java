package ru.pugart.ext.ui.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import reactor.core.publisher.Mono;
import ru.pugart.ext.ui.dto.ResponseDto;

@ControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(CreateException.class)
    public Mono<ResponseEntity<?>> methodCreateException(CreateException ex) {
        ResponseDto response = ResponseDto.builder()
                .error(ex.getLocalizedMessage())
                .success(false)
                .build();
        return Mono.just(new ResponseEntity<>(response, HttpStatus.BAD_REQUEST));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthException.class)
    public Mono<ResponseEntity<?>> methodAuthException(AuthException ex) {
        ResponseDto response = ResponseDto.builder()
                .error(ex.getLocalizedMessage())
                .success(false)
                .build();
        return Mono.just(new ResponseEntity<>(response, HttpStatus.BAD_REQUEST));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<?>> methodNotFoundException(NotFoundException ex) {
        ResponseDto response = ResponseDto.builder()
                .error(ex.getLocalizedMessage())
                .success(false)
                .build();
        return Mono.just(new ResponseEntity<>(response, HttpStatus.BAD_REQUEST));
    }
}
