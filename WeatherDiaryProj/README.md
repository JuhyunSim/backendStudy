# SpringProject2(날씨 일기 어플리케이션) 

## 패키지 구성
- config : 설정 파일
- controller : DiaryController (다이어리 생성/생성, 특정 날짜 다이어리 조회, 기간 다이어리 조회, 텍스트 업데이트, 다이어리 삭제 API)
- service : DiaryService (위 내용의 비즈니스 코드 작성)
- Exception : InvalidDateException(너무 먼 미래의 날짜 데이터 저장 시 에러)

### 사용 API
- OpenWeatherMap API 사용

### 기능
- 데이터 저장
: 매일 오전 1시에 OpenWeatherMap API 가져와서 저장
: /create/diary 요청 시 저장된 weather 데이터를 가져와서 저장함
- 조회 
: 기간 조회 가능(/read/diaries)
- 수정
: 해당 날짜의 첫 번째 데이터를 가져와서 텍스트 수정
- 삭제
: 해당 날짜의 모든 데이터 삭제 

### API document
- http://localhost:8080/swagger-ui/index.html#/
- springdoc 활용하여 작성

