# Mission1(주변 공공와이파이 조회-서울) 

## 소스파일
- Java, Java Servlet, js, jsp 소스파일
-----
### jsp
- home, history, load-openapi 파일 각각 구성
- wifi-home.jsp:  "현재위치 가져오기", "근처WIFI정보 조회하기" 기능 웹페이지에 출력, a태그 통해 위치조회 페이지로 이동
- wifi-history.jsp: wifi-home.jsp 에서 조회한 내용 출력. 조회 내역 삭제 기능 구현. 
- wifi-loadData.jsp: 오픈에이피아이로부터 불러온 데이터 개수 출력(저장 실패 시, null개 저장하였다는 내용으로 출력)

### js
- data2.js, history-data.js
- data2: wifi-home.jsp 연결 / ajax로 Java 서버에 주변 20개 데이터 요청 (*LocationDataServlet / GET)
- history-data: -> wifi-history.jsp에 연결  / ajax로 Java 서버에 조회내역 데이터 요청 (*LocationDataServlet / POST)
- wifi-loadData.jsp에는 별도 파일 없이 script 태그로 직접 입력

### Servlet
- LocationDataServlet / DeleteHistory / OpenApiLoadServlet
- LocationDataServlet(doGet) : 클라이언트의 GET 요청에 대해 주변 20개 공공와이파이 데이터를 전송(historyList에 조회요청 내역을 저장함)
- LocationDataServlet(doPost) : 클라이언트의 POST 요청에 대해 조회내역(historyList) 전송
- DeleteHistoryServlet : 클라이언트의 POST 요청(조회데이터 삭제)에 대해 처리  
- OpenApiLoadServlet : 클라이언트의 GET 요청에 대해 불러올 수 있는 openapi 데이터 개수를 전송

### java
- WifiService
- wifi정보 리스트 반환 메서드(getWifiList(LocationData)) : 조회 요청 시, 조회자의 위치정보를 받아 데이터를 업데이트한 후 리스트 반환  / WifiInfo 객체 활용
- wifi정보 조회내역 업데이트 메서드(historyUpdate(LocationData)) 
- wifi정보 조회내역 리스트 반환 메서드(getHistory()) / HistoryInfo 객체 활용
- wifi정보 데이터 개수 반환 메서드(countData())
