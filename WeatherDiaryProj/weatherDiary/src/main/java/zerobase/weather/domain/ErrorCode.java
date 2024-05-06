package zerobase.weather.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("내부 서버 오류가 발생했습니다."),
    TOO_DISTANT_FUTURE_TO_CREATE("너무 먼 미래의 날짜입니다.");

    private final String description;
}
