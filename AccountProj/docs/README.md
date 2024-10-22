# SpringProject1(계좌이체 시스템) 

## Overview
- 계좌를 개설, 조회, 삭제할 수 있다.
- 계좌의 잔액을 이체 및 취소, 확인할 수 있다. 

## 패키지 구성
- aop : AOP 설정 파일(AccountLock 어노테이션 설정-TransactionController에서 사용) 
- config : 설정 파일
- controller : AccountController(계좌 개설, 조회, 삭제 API), TransactionController(잔액사용, 취소, 거래확인 API)
- service :
  - AccountService (AccountController의 비즈니스 코드)
    : 테이블에서 사용자 조회, 계좌번호생성, 계좌저장 로직 및 검증로직 구현
  - LockAopAspect
    : LockService의 메서드가 AccountLock 어노테이션에서 lock 취득, 해제가 이루어지도록 구현
  - LockService
    : Redis를 사용해 락을 설정하고 해제하는 메서드(메서드가 동시에 동작하여 충돌하는 것을 방지)
  - TransactionService (TransactionController의 비즈니스 코드)
    : 검증로직과 함께 계좌잔액 사용, 취소, 거래내역 저장 로직 구현
    
     >   **계좌이체 실패응답 케이스**
     > - 사용자가 없는 경우
     > - 사용자 아이디와 계좌 소유주가 다른 경우
     > - 계좌가 이미 해지된 상태인 경우
     > - 거래 금액이 잔액보다 큰 경우
     > - 거래 금액이 너무 작거나 큰 경우
    
- domain : Account(계좌), AccountUser(계좌명의자), BaseEntity(생성일자, 수정일자), Transaction(거래정보)
- dto: controller 계층에서 요청/응답 시에 사용 
- exception
  - AccountException(RuntimeException을 상속받는 커스텀 Exception)
  - GlobalExceptionHandler(예외 메세지를 응답에 포함시키도록 설정)
- repository : 영속성 컨텍스트 계층 관리
- type : enum 타입 파일

## 기능
### 계좌 CRUD
- 계좌 생성: 계좌개설 가능여부 확인 후 계좌번호 생성하며 저장
- 계좌 목록조회: 특정 사용자가 소유한 계좌 목록 조회
- 계좌 개별조회: 사용자의 특정 계좌 개별 조회
- 계좌 삭제: 자기계좌 여부, 계좌 잔액 상태 등 확인 후 삭제
### 계좌 이체
- 잔액사용: 계좌이체 실패 케이스 참고하여 잔액 사용하여 결과 저장 및 반환
- 잔액사용 취소: 계좌취소 조건을 참고하여 사용내역 취소
- 거래확인: 거래내역 반환
