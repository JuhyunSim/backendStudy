package com.example.demo.dto;

import lombok.*;


//클라이언트와 주고받는 데이터 객체
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountInfo {
    private String accountNumber;
    private Long balance;

}
