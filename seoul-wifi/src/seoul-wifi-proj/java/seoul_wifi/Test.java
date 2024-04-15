/* Java 1.8 샘플 코드 */
package seoul_wifi;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;

public class Test {

    private static final String SERVICE_KEY = "7741694d5373696d3132306c6d6d5341";
    private static final String URL = "http://openapi.seoul.go.kr:8088/7741694d5373696d3132306c6d6d5341/json/TbPublicWifiInfo/1/5/";
    private static final String TYPE = "json";
    private static final String SERVICE = "SeoulWifi";
    private static final String START_INDEX = "1";
    private static final String END_INDEX = "25163";
    private static final String X_SWIFI_WRDOFC = "중구";
    private static final String X_SWIFI_ADRES1 = "을지로7가 2-1";


    public static void main(String[] args) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(URL); /*URL*/
        urlBuilder.append("/" +  URLEncoder.encode(SERVICE_KEY,"UTF-8") ); /*인증키 (sample사용시에는 호출시 제한됩니다.)*/
        urlBuilder.append("/" +  URLEncoder.encode(TYPE,"UTF-8") ); /*요청파일타입 (xml,xmlf,xls,json) */
        urlBuilder.append("/" + URLEncoder.encode(SERVICE,"UTF-8")); /*서비스명 (대소문자 구분 필수입니다.)*/
        urlBuilder.append("/" + URLEncoder.encode(START_INDEX,"UTF-8")); /*요청시작위치 (sample인증키 사용시 5이내 숫자)*/
        urlBuilder.append("/" + URLEncoder.encode(END_INDEX,"UTF-8")); /*요청종료위치(sample인증키 사용시 5이상 숫자 선택 안 됨)*/
        // 상위 5개는 필수적으로 순서바꾸지 않고 호출해야 합니다.

        // 서비스별 추가 요청 인자이며 자세한 내용은 각 서비스별 '요청인자'부분에 자세히 나와 있습니다.
        urlBuilder.append("/" + URLEncoder.encode(X_SWIFI_WRDOFC,"UTF-8")); /* 서비스별 추가 요청인자들*/
        urlBuilder.append("/" + URLEncoder.encode(X_SWIFI_ADRES1,"UTF-8")); /* 서비스별 추가 요청인자들*/

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/xml");
        System.out.println("Response code: " + conn.getResponseCode()); /* 연결 자체에 대한 확인이 필요하므로 추가합니다.*/
        BufferedReader rd;

        // 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
    }
}