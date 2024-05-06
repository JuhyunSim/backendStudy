package zerobase.weather.Exception;

import lombok.*;
import org.springframework.web.bind.annotation.ResponseStatus;
import zerobase.weather.domain.ErrorCode;

import static zerobase.weather.domain.ErrorCode.TOO_DISTANT_FUTURE_TO_CREATE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvalidDateException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public InvalidDateException(ErrorCode errorCode) {
        this.errorCode = TOO_DISTANT_FUTURE_TO_CREATE;
        this.errorMessage = errorCode.getDescription();
    }
}
