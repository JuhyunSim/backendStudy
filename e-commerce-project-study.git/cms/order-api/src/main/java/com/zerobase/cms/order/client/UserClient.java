package com.zerobase.cms.order.client;

import com.zerobase.cms.user.domain.customer.ChangeBalanceForm;
import com.zerobase.cms.user.domain.customer.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-api", url = "${user-api.feign.client.url}")
public interface UserClient {
    @GetMapping("/customer/getinfo")
    public ResponseEntity<CustomerDto> getinfo(
            @RequestHeader(name = "X-Auth-Token") String token
    );

    @PutMapping("/customer/balance")
    public ResponseEntity<Integer> changeBalance(
            @RequestHeader(name = "X-Auth-Token") String token,
            @RequestBody ChangeBalanceForm changeBalanceForm
    );
}
