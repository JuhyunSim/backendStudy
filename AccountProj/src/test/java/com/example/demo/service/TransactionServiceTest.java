package com.example.demo.service;

import com.example.demo.domain.Account;
import com.example.demo.domain.AccountUser;
import com.example.demo.domain.Transaction;
import com.example.demo.dto.TransactionDto;
import com.example.demo.exception.AccountException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AccountUserRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.type.AccountStatus;
import com.example.demo.type.ErrorCode;
import com.example.demo.type.TransactionResultType;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.demo.type.AccountStatus.IN_USE;
import static com.example.demo.type.AccountStatus.UNREGISTERED;
import static com.example.demo.type.ErrorCode.*;
import static com.example.demo.type.TransactionResultType.F;
import static com.example.demo.type.TransactionResultType.S;
import static com.example.demo.type.TransactionType.CANCEL;
import static com.example.demo.type.TransactionType.USE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    private final long USE_AMOUNT = 200L;
    private final long CANCEL_AMOUNT = 200L;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountUserRepository accountUserRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void successUseBalance() {
        //given
        AccountUser simson = AccountUser.builder()
                .id(10L)
                .name("Simson")
                .build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(simson));
        Account account = Account.builder()
                .accountUser(simson)
                .accountStatus(AccountStatus.IN_USE)
                .accountNumber("1234512345")
                .balance(100000L)
                .build();
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(account));
        given(transactionRepository.save(any()))
                .willReturn(Transaction.builder()
                        .account(account)
                        .transactionType(USE)
                        .transactionResultType(S)
                        .transactionId("transactionId")
                        .transactedAt(LocalDateTime.now())
                        .amount(10000L)
                        .balanceSnapShot(90000L)
                        .build());

        ArgumentCaptor<Transaction> captor
                = ArgumentCaptor.forClass(Transaction.class);

        //when
        //파라미터가 실제 테스트 결과에 영향을 주지는 않음
        // (테스트 대상은 transactionRepository에 save한 값(mock.given)이기 때문
        //파라미터 내용이 잘 적용되는지 확인하려면 captor 사용
        TransactionDto transactionDto
                = transactionService.useBalance(
                1L,
                "1000000000",
                1000L
        );


        //then
        verify(transactionRepository).save(captor.capture());
        assertEquals(1000L, captor.getValue().getAmount());
        assertEquals(99000L, captor.getValue().getBalanceSnapShot());

        assertEquals("1234512345", transactionDto.getAccountNumber());
        assertEquals(USE, transactionDto.getTransactionType());
        assertEquals(90000L, transactionDto.getBalanceSnapShot());
        assertEquals(S, transactionDto.getTransactionResultType());
    }

    @Test
    @DisplayName("해당 유저 없음 - 잔액 사용 실패")
    void useBalance_UserNotFound() {
        //given
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        AccountException exception = assertThrows(AccountException.class,
                () -> transactionService.useBalance(
                        1L, "1000000000", 1000L)
        );
        //then
        assertEquals(USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("해당 계좌 없음 - 잔액 사용 실패")
    void useBalance_AccountNotFound() {
        //given
        AccountUser simson = AccountUser.builder()
                .id(10L)
                .name("Simson")
                .build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(simson));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.empty());
        //when
        AccountException exception = assertThrows(AccountException.class,
                () -> transactionService.useBalance(
                        1L, "1000000000", 1000L)
        );
        //then
        assertEquals(ACCOUNT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("사용자 아이디와 계좌 소유주가 다름 - 잔액 사용 실패")
    void useBalance_UnMatch() {
        //given
        AccountUser simson = AccountUser.builder()
                .id(10L)
                .name("Simson")
                .build();
        AccountUser kims = AccountUser.builder()
                .id(11L)
                .name("Kims")
                .build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(simson));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(Account.builder()
                        .accountUser(kims)
                        .build()));
        //when
        AccountException exception = assertThrows(AccountException.class, () -> transactionService.useBalance(
                1L, "1000000000", 1000L
        ));
        //then
        assertEquals(USER_ACCOUNT_UN_MATCH, exception.getErrorCode());
    }

    @Test
    @DisplayName(" * 3. 계좌가 이미 해지된 상태인 경우")
    void useBalance_Already_Unregistered() {
        //given
        AccountUser simson = AccountUser.builder()
                .id(10L)
                .name("Simson")
                .build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(simson));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(
                        Account.builder()
                                .accountUser(simson)
                                .accountStatus(UNREGISTERED)
                                .accountNumber("1234512345")
                                .balance(0L)
                                .build()
                ));

        //when
        AccountException exception = assertThrows(AccountException.class, () -> transactionService.useBalance(
                1L, "1000000000", 1000L
        ));
        //then
        assertEquals(ACCOUNT_ALREADY_UNREGISTERED, exception.getErrorCode());
    }

    @Test
    @DisplayName(" * 4. 거래 금액이 잔액보다 큰 경우")
    void useBalance_Amount_Exceed() {
        //given
        AccountUser simson = AccountUser.builder()
                .id(10L)
                .name("Simson")
                .build();
        Account account = Account.builder()
                .accountUser(simson)
                .accountStatus(IN_USE)
                .accountNumber("1234512345")
                .balance(100L)
                .build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(simson));

        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(account));

        //when
        AccountException exception = assertThrows(AccountException.class, () -> transactionService.useBalance(
                1L, "1000000000", 1000L
        ));
        //then
        assertEquals(AMOUNT_EXCEED_ACCOUNT_BALANCE, exception.getErrorCode());
    }

    @Test
    @DisplayName("실패 transaction 저장")
    void saveFailedUseTransaction() {
        //given
        AccountUser simson = AccountUser.builder()
                .id(10L)
                .name("Simson")
                .build();
        Account account = Account.builder()
                .accountUser(simson)
                .accountStatus(AccountStatus.IN_USE)
                .accountNumber("1234512345")
                .balance(100000L)
                .build();

        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(account));
        given(transactionRepository.save(any()))
                .willReturn(Transaction.builder()
                        .account(account)
                        .transactionType(USE)
                        .transactionResultType(S)
                        .transactionId("transactionId")
                        .transactedAt(LocalDateTime.now())
                        .amount(10000L)
                        .balanceSnapShot(90000L)
                        .build());

        ArgumentCaptor<Transaction> captor
                = ArgumentCaptor.forClass(Transaction.class);

        //when
        transactionService.saveFailedUseTransaction("1000000000", 1000L);

        //then
        verify(transactionRepository, times(1)).save(captor.capture());
        assertEquals(1000L, captor.getValue().getAmount());
        assertEquals(100000L, captor.getValue().getBalanceSnapShot());
        assertEquals(F, captor.getValue().getTransactionResultType());
    }


    @Test
    void successCancelBalance() {
        //given
        AccountUser simson = AccountUser.builder()
                .id(10L)
                .name("Simson")
                .build();

        Account account = Account.builder()
                .accountUser(simson)
                .accountStatus(AccountStatus.IN_USE)
                .accountNumber("1234512345")
                .balance(100000L)
                .build();

        Transaction transaction = Transaction.builder()
                .account(account)
                .transactionType(USE)
                .transactionResultType(S)
                .transactionId("transactionIdForUse")
                .transactedAt(LocalDateTime.now())
                .amount(USE_AMOUNT)
                .balanceSnapShot(100000L - USE_AMOUNT)
                .build();

        given(transactionRepository.findByTransactionId(anyString()))
                .willReturn(Optional.of(transaction));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(account));
        given(transactionRepository.save(any()))
                .willReturn(Transaction.builder()
                        .account(account)
                        .transactionType(CANCEL)
                        .transactionResultType(S)
                        .transactionId("transactionIdForCancel")
                        .transactedAt(LocalDateTime.now())
                        .amount(CANCEL_AMOUNT)
                        .balanceSnapShot(
                                transaction.getBalanceSnapShot()
                                        + CANCEL_AMOUNT)
                        .build()
                );

        ArgumentCaptor<Transaction> captor
                = ArgumentCaptor.forClass(Transaction.class);

        //when
        TransactionDto transactionDto
                = transactionService.cancelBalance(
                "transactionId",
                "1000000000",
                CANCEL_AMOUNT
        );


        //then
        verify(transactionRepository).save(captor.capture());
        assertEquals(CANCEL_AMOUNT, captor.getValue().getAmount());
        assertEquals(100000L + CANCEL_AMOUNT, captor.getValue().getBalanceSnapShot());

        assertEquals(S, transactionDto.getTransactionResultType());
        assertEquals(CANCEL, transactionDto.getTransactionType());
        assertEquals(100000L, transactionDto.getBalanceSnapShot());
        assertEquals(CANCEL_AMOUNT, transactionDto.getAmount());
    }

    @Test
    @DisplayName("거래와 관련한 해당 계좌 없음 - 잔액 사용 취소 실패")
    void cancelBalance_AccountNotFound() {
        //given
        given(transactionRepository.findByTransactionId(anyString()))
                .willReturn(Optional.of(Transaction.builder().build()));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.empty());
        //when
        AccountException exception = assertThrows(AccountException.class,
                () -> transactionService.cancelBalance(
                        "transactionId",
                        "1000000000", 1000L)
        );
        //then
        assertEquals(ACCOUNT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("원 사용 거래 없음 - 잔액 사용 취소 실패")
    void cancelBalance_TransactionNotFound() {
        //given
        given(transactionRepository.findByTransactionId(anyString()))
                .willReturn(Optional.empty());

        //when
        AccountException exception = assertThrows(AccountException.class,
                () -> transactionService.cancelBalance(
                        "transactionId",
                        "1000000000", 1000L)
        );
        //then
        assertEquals(TRANSACTION_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("거래와 계좌 매칭 실패 - 잔액 사용 취소 실패")
    void cancelBalance_UnMatchAccount() {
        //given
        AccountUser simson = AccountUser.builder()
                .id(10L)
                .name("Simson")
                .build();

        Account account1 = Account.builder()
                .accountUser(simson)
                .id(1L)
                .accountStatus(IN_USE)
                .accountNumber("1234512345")
                .build();
        Account account2 = Account.builder()
                .accountUser(simson)
                .id(2L)
                .accountStatus(IN_USE)
                .accountNumber("5432154321")
                .build();
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(account1));
        given(transactionRepository.findByTransactionId(anyString()))
                .willReturn(Optional.of(Transaction.builder()
                        .account(account2)
                        .amount(CANCEL_AMOUNT)
                        .build()));
        //when
        AccountException exception = assertThrows(AccountException.class,
                () -> transactionService.cancelBalance(
                        "transactionId",
                        "1000000000", 1000L)
        );
        //then
        assertEquals(TRANSACTION_ACCOUNT_UN_MATCH, exception.getErrorCode());
    }

    @Test
    @DisplayName("거래금액과 취소금액 불일치 - 잔액 사용 취소 실패")
    void cancelBalance_CancelMustFully() {
        //given
        AccountUser simson = AccountUser.builder()
                .id(10L)
                .name("Simson")
                .build();

        Account account1 = Account.builder()
                .accountUser(simson)
                .id(1L)
                .accountStatus(IN_USE)
                .accountNumber("1234512345")
                .balance(10000L)
                .build();
        Transaction transaction = Transaction.builder()
                .account(account1)
                .transactionType(CANCEL)
                .transactionResultType(S)
                .transactionId("transactionIdForAll")
                .transactedAt(LocalDateTime.now())
                .amount(CANCEL_AMOUNT)
                .balanceSnapShot(10000L - USE_AMOUNT)
                .build();
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(account1));
        given(transactionRepository.findByTransactionId(anyString()))
                .willReturn(Optional.of(transaction));
        //when
        AccountException exception = assertThrows(AccountException.class,
                () -> transactionService.cancelBalance(
                        "transactionId",
                        "1000000000", 1000L)
        );
        //then
        assertEquals(CANCEL_MUST_FULLY, exception.getErrorCode());
    }


    @Test
    @DisplayName("1년 넘은 거래 - 잔액 사용 취소 실패")
    void cancelBalance_TooOldToCancel() {
        //given
        AccountUser simson = AccountUser.builder()
                .id(10L)
                .name("Simson")
                .build();

        Account account1 = Account.builder()
                .accountUser(simson)
                .id(1L)
                .accountStatus(IN_USE)
                .accountNumber("1234512345")
                .balance(10000L)
                .build();
        Transaction transaction = Transaction.builder()
                .account(account1)
                .transactionType(USE)
                .transactionResultType(S)
                .transactionId("transactionId")
                .transactedAt(LocalDateTime.now().minusYears(1))
                .amount(CANCEL_AMOUNT)
                .balanceSnapShot(9000L)
                .build();
        given(transactionRepository.findByTransactionId(anyString()))
                .willReturn(Optional.of(transaction));
        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(account1));

        //when
        AccountException exception = assertThrows(AccountException.class,
                () -> transactionService.cancelBalance(
                        "transactionId",
                        "1000000000", CANCEL_AMOUNT)
        );
        //then
        assertEquals(TOO_OLD_ORDER_TO_CANCEL, exception.getErrorCode());
    }

    @Test
    void successGetTransaction() {
        //given
        AccountUser simson = AccountUser.builder()
                .id(10L)
                .name("Simson")
                .build();

        Account account1 = Account.builder()
                .accountUser(simson)
                .id(1L)
                .accountStatus(IN_USE)
                .accountNumber("1234512345")
                .balance(10000L)
                .build();
        Transaction transaction = Transaction.builder()
                .account(account1)
                .transactionType(USE)
                .transactionResultType(S)
                .transactionId("transactionId")
                .transactedAt(LocalDateTime.now().minusYears(1))
                .amount(USE_AMOUNT)
                .balanceSnapShot(10000L - USE_AMOUNT)
                .build();
        given(transactionRepository.findByTransactionId(anyString()))
                .willReturn(Optional.of(transaction));

        //when
        TransactionDto transactionDto
                = transactionService.queryTransaction("transactionId");

        //then
        assertEquals(USE, transactionDto.getTransactionType());
        assertEquals(S, transactionDto.getTransactionResultType());
        assertEquals("transactionId", transactionDto.getTransactionId());
        assertEquals(USE_AMOUNT, transactionDto.getAmount());
    }

    @Test
    @DisplayName("원 거래 없음 - 거래 조회 실패")
    void queryTransaction_TransactionNotFound() {
        //given
        given(transactionRepository.findByTransactionId(anyString()))
                .willReturn(Optional.empty());
        //when

        AccountException exception = assertThrows(AccountException.class,
                () -> transactionService.queryTransaction("transactionId"));
        //then

        assertEquals(TRANSACTION_NOT_FOUND, exception.getErrorCode());
    }

}