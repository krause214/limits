package ru.bbcv.exception.handler;

import ru.bbcv.exception.CommonErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.bbcv.controller.LimitsController;

@Slf4j
@RestControllerAdvice(assignableTypes = LimitsController.class)
public class LimitsControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonErrorResponse> handleError(Exception e) {
        log.error("Ошибка - {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonErrorResponse("UNKNOWN_ERROR", e.getMessage()));
    }
}
