package com.example.demo.controller;

import com.example.demo.domain.Account;
import com.example.demo.dto.AccountDto;
import com.example.demo.dto.CreateAccount;
import com.example.demo.dto.DeleteAccount;
import com.example.demo.exception.AccountException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.type.AccountStatus;
import com.example.demo.service.AccountService;
import com.example.demo.type.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.example.demo.type.ErrorCode.ACCOUNT_NOT_FOUND;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void successCreateAccount() throws Exception {
        //given
        given(accountService.createAccount(anyLong(), anyLong()))
                .willReturn(AccountDto.builder()
                        .userId(1L)
                        .accountNumber("1234567890")
                        .registeredAT(LocalDateTime.now())
                        .unRegisteredAT(LocalDateTime.now())
                        .build()
                );
        //when
        //then
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new CreateAccount.Request(122L, 1111L)
                )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId")
                        .value(1))
                .andExpect(jsonPath("$.accountNumber")
                        .value("1234567890"))
                .andDo(print());
    }

    @Test
    void successGetAccount() throws Exception {
        //given
        given(accountService.getAccount(anyLong()))
                .willReturn(Account.builder()
                                .accountNumber("3456")
                                .accountStatus(AccountStatus.IN_USE)
                                .build());
        //when
        //then
        mockMvc.perform(get("/account/876"))
                .andDo(print())
                .andExpect(jsonPath("$.accountNumber").value("3456"))
                .andExpect(jsonPath("$.accountStatus").value("IN_USE"))
                .andExpect(status().isOk());
    }

    /**
     *
     * 계좌 삭제 테스트
     */
    @Test
    void successDeleteAccount() throws Exception {
        //given
        given(accountService.deleteAccount(anyLong(), anyString()))
                .willReturn(AccountDto.builder()
                        .userId(1L)
                        .accountNumber("1234567890")
                        .registeredAT(LocalDateTime.now())
                        .unRegisteredAT(LocalDateTime.now())
                        .build()
                );
        //when
        //then
        mockMvc.perform(delete("/account?userId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new DeleteAccount.Request(122L, "1234512345")
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId")
                        .value(1))
                .andExpect(jsonPath("$.accountNumber")
                        .value("1234567890"))
                .andDo(print());
    }


    @Test
    void successGetAccountsByUserId() throws Exception {
        //given
        List<AccountDto> accountDtos = Arrays.asList(
                AccountDto.builder()
                        .accountNumber("1111111111")
                        .balance(1000L)
                        .build(),
                AccountDto.builder()
                        .accountNumber("2222222222")
                        .balance(2000L)
                        .build(),
                AccountDto.builder()
                        .accountNumber("3333333333")
                        .balance(3000L)
                        .build()
        );

        given(accountService.getAccountsByUserId(anyLong()))
                .willReturn(accountDtos);
        //when
        //then
        mockMvc.perform(get("/account?user_Id=1"))
                .andDo(print())
                .andExpect(jsonPath("$[0].accountNumber").value("1111111111"))
                .andExpect(jsonPath("$[0].balance").value(1000L))
                .andExpect(jsonPath("$[1].accountNumber").value("2222222222"))
                .andExpect(jsonPath("$[1].balance").value(2000L))
                .andExpect(jsonPath("$[2].accountNumber").value("3333333333"))
                .andExpect(jsonPath("$[2].balance").value(3000L));
    }

    @Test
    void failGetAccount() throws Exception {
        //given
        given(accountService.getAccount(anyLong()))
                .willThrow(new AccountException(ACCOUNT_NOT_FOUND));
        //when
        //then
        mockMvc.perform(get("/account/876"))
                .andDo(print())
                .andExpect(jsonPath("$.errorCode").value("ACCOUNT_NOT_FOUND"))
                .andExpect(jsonPath("$.errorMessage").value(ACCOUNT_NOT_FOUND.getDescription()))
                .andExpect(status().isOk());
    }
}