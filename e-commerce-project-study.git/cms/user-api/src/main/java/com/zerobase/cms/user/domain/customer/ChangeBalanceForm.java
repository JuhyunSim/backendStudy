package com.zerobase.cms.user.domain.customer;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeBalanceForm {

    private Long customerId;
    private String from;
    private String message;
    private Integer changeAmount;
}
