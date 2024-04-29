package com.example.demo.dto;

import com.example.demo.aop.AccountLockIdInterface;
import com.example.demo.type.TransactionResultType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Request Type
 * {
 *    "transactionId": "abcds~~~"
 *    "accountNumber":"1000000000",
 *    "amount": 1000L
 * }
 */

public class CancelBalance {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request implements AccountLockIdInterface {
        @NotBlank
        private String transactionId;

        @NotBlank
        @Size(min = 10, max = 10)
        private String accountNumber;

        @NotNull
        @Min(10)
        @Max(1000_000_000)
        private Long amount;
    }


    /**
     * Response Type
     * {
     *    "accountNumber":"1234567890",
     *    "transactionResult":"S",
     *    "transactionId":"c2033bb6d82a4250aecf8e27c49b63f6",
     *    "amount":1000,
     *    "transactedAt":"2022-06-01T23:26:14.671859"
     * }
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String accountNumber;
        private TransactionResultType transactionResult;
        private LocalDateTime transactedAt;
        private Long amount;
        private String transactionId;

        public static Response from(TransactionDto transactionDto) {
            return Response.builder()
                    .accountNumber(transactionDto.getAccountNumber())
                    .transactionResult(transactionDto.getTransactionResultType())
                    .transactedAt(transactionDto.getTransactedAt())
                    .amount(transactionDto.getAmount())
                    .transactionId(transactionDto.getTransactionId())
                    .build();
        }
    }
}
