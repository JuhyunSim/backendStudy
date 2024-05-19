# SpringProject3(배당금 조회 어플리케이션) 

## Overview
- 회원가입 및 로그인을 하여 저장되어 있는 회사 리스트를 조회할 수 있고, 회사 이름을 검색하면 배당금 정보를 조회할 수 있는 기능 구현
- 회원가입 시 관리자 권한을 부여받게 되면 회사 정보를 저장, 삭제할 수 있음(회사 정보는 야후 파이낸스에서 스크래핑해온 정보)

## 패키지 구성
- config : 설정 파일
- controller : CompanyController(회사 저장, 조회, 검색어 자동완성 등 API), FiananceController(배당금 정보 조회 API)
- service : CompanyService (CompanyController 비즈니스 코드 작성), FinanceService(FinanceController 비즈니스 코드 작성)
- model_constant: final 타입 모음
- model : domain 데이터 
- persist_entity : 데이터베이스에 직접적으로 저장하는 데이터
- persist_repository : CompanyRepository(회사 정보 저장), DividendRepository(배당금 정보 저장), 
                        MemberEntityRepository(회원 정보 저장)
- exception : custom exception, exception handler
- security : 회원가입 및 로그인 시 필요한 인증정보 (JWT 라이브러리 사용)
- scrapper : 야후 사이트의 기업 정보 스크랩

### 기능
- 회원가입 및 로그인
- 저장 : 스크랩하여 회사 정보 저장 (매월 1일 scheduling)
- 조회 : 회사 리스트, 회사별 배당금 조회 기능 * 배당금 조회 시, 캐싱(인메모리 데이터베이스 redis) 사용
- 삭제 : 회사 정보 삭제
- 검색어 자동완성 : appache 라이브러리의 Trie를 활용하여 자동완성 구현
