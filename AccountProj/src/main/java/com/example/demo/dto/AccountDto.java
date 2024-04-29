package com.example.demo.dto;

import com.example.demo.domain.Account;
import lombok.*;
import net.bytebuddy.implementation.bind.annotation.BindingPriority;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    private Long userId;
    private String accountNumber;
    private Long balance;
    private LocalDateTime registeredAT;
    private LocalDateTime unRegisteredAT;

    public static AccountDto fromEntity(Account account) {
        return AccountDto.builder()
                .userId(account.getAccountUser().getId())
                .balance(account.getBalance())
                .accountNumber(account.getAccountNumber())
                .registeredAT(account.getRegisteredAt())
                .unRegisteredAT(account.getUnRegisteredAt())
                .build();
    }
}
