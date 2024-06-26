package zerobase.stockdividend.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.stockdividend.exception.AbstractException;

public class NotFoundUserIdException extends AbstractException {
    @Override
    public int getStatusCode() {

        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 ID 입니다.";
    }
}
