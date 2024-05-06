package zerobase.weather.Exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zerobase.weather.domain.ErrorCode;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Exception handleAllExceptions() {
        System.out.println("error from globalExceptionHandler");
        return new Exception();
    }

    @ExceptionHandler(InvalidDateException.class)
    public ErrorCode handleAccountException(InvalidDateException e) {
        log.error("{} is occurred", e.getErrorCode());

        return ErrorCode.TOO_DISTANT_FUTURE_TO_CREATE;
    }
}
