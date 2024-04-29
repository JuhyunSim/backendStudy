package com.example.demo.controller;

import com.example.demo.domain.Account;
import com.example.demo.dto.CancelBalance;
import com.example.demo.dto.TransactionDto;
import com.example.demo.dto.UseBalance;
import com.example.demo.service.TransactionService;
import com.example.demo.type.AccountStatus;
import com.example.demo.type.TransactionResultType;
import com.example.demo.type.TransactionType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.demo.type.TransactionResultType.S;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void successUseBalance() throws Exception {
        //given
        given(transactionService.useBalance(anyLong(), anyString(), anyLong()))
                .willReturn(TransactionDto.builder()
                        .accountNumber("1234512345")
                        .transactionResultType(S)
                        .transactedAt(LocalDateTime.now())
                        .amount(100000L)
                        .transactionId("transactionId")
                        .build());

        //when
        //then
        mockMvc.perform(post("/transaction/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                        new UseBalance.Request(
                                1L,
                                "5432154321",
                                1000L
                                )
                            )
                        )
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber")
                        .value("1234512345"))
                .andExpect(jsonPath("$.transactionResult")
                        .value("S"))
                .andExpect(jsonPath("$.transactionId")
                        .value("transactionId"))
                .andExpect(jsonPath("$.amount")
                        .value(100000L))
                ;
    }


    @Test
    void successCancelBalance() throws Exception {
        //given
        given(transactionService.cancelBalance(anyString(), anyString(), anyLong()))
                .willReturn(TransactionDto.builder()
                        .accountNumber("1234512345")
                        .transactionResultType(S)
                        .transactedAt(LocalDateTime.now())
                        .amount(100000L)
                        .transactionId("transactionIdForCancel")
                        .build());

        //when
        //then
        mockMvc.perform(post("/transaction/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                        new CancelBalance.Request(
                                                "transactionId",
                                                "5432154321",
                                                1000L
                                        )
                                )
                        )
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber")
                        .value("1234512345"))
                .andExpect(jsonPath("$.transactionResult")
                        .value("S"))
                .andExpect(jsonPath("$.transactionId")
                        .value("transactionIdForCancel"))
                .andExpect(jsonPath("$.amount")
                        .value(100000L))
        ;
    }


    @Test
    void successGetTransaction() throws Exception {
        //given
        given(transactionService.queryTransaction(anyString()))
                .willReturn(TransactionDto.builder()
                        .accountNumber("1234512345")
                        .transactionType(TransactionType.USE)
                        .transactionResultType(S)
                        .transactedAt(LocalDateTime.now())
                        .amount(1000L)
                        .transactionId("transactionId")
                        .balanceSnapShot(9000L)
                        .build());
        //when
        //then
        mockMvc.perform(get("/transaction/transactionId"))
                .andDo(print())
                .andExpect(jsonPath("$.accountNumber")
                        .value("1234512345"))
                .andExpect(jsonPath("$.transactionType").value("USE"))
                .andExpect(jsonPath("$.transactionResult").value("S"))
                .andExpect(jsonPath("$.amount").value(1000L))
                .andExpect(jsonPath("$.transactionId").value("transactionId"))
                .andExpect(status().isOk());
    }

}