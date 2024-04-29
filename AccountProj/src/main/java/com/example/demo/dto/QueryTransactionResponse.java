package com.example.demo.dto;

import com.example.demo.type.TransactionResultType;
import com.example.demo.type.TransactionType;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryTransactionResponse {
        private String accountNumber;
        private TransactionType transactionType;
        private TransactionResultType transactionResult;
        private LocalDateTime transactedAt;
        private Long amount;
        private String transactionId;

        public static QueryTransactionResponse from(TransactionDto transactionDto) {
            return QueryTransactionResponse.builder()
                    .accountNumber(transactionDto.getAccountNumber())
                    .transactionType(transactionDto.getTransactionType())
                    .transactionResult(transactionDto.getTransactionResultType())
                    .transactedAt(transactionDto.getTransactedAt())
                    .amount(transactionDto.getAmount())
                    .transactionId(transactionDto.getTransactionId())
                    .build();
        }
}
