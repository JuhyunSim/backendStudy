<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@page import="seoul_wifi.WifiService"%>
<%@page import="java.util.List"%>
<%@page import="seoul_wifi.LocationData"%>
<%@page import="seoul_wifi.WifiInfo"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>wifi-home</title>
<style>
    #wifiTable tbody tr:first-child {
        height: 40px; 
        text-align: center; 
    }
    
    #wifiTable {
    border-collapse: collapse; 
    width: 100%; 
	}

    #wifiTable th,
    #wifiTable td {
        border: 1px solid black; 
        padding: 8px; 
    }
    
    th {
        background-color: #4CAF50;
        color: white;
    }
    
    tbody tr:nth-child(even) {
        background-color: #f2f2f2;
    }

    tbody tr:nth-child(odd) {
        background-color: white;
    }
    
    
</style>
<script src = "http://code.jquery.com/jquery-latest.min.js"></script>

</head>
<body>
	<h2>와이파이 정보 구하기</h2>
    <a href="#" onclick="goToHome();">홈</a>
    <a href="http://localhost:8080/seoul-wifi/wifi-history.jsp">위치 히스토리 목록</a> |
    <a href="http://localhost:8080/seoul-wifi/wifi-loadData.jsp">Open API 와이파이 정보 가져오기</a> |
    <p></p>
    
    <label for="latitudeInput">LAT:</label>
    <input type="text" id="latitudeInput" name="latitude">
    <label for="longitudeInput">LNT:</label>
    <input type="text" id="longitudeInput" name="longitude">
    <button id="getLocationBtn" onclick="getLocation();">내 위치 가져오기</button>
    <button id="getWifiBtn" onclick="showWifiInfo()">근처 WIFI 정보 보기</button>
	<p></p>
	<table id="wifiTable">
		<thead>
			<tr>
				<th>거리</th>
				<th>관리번호</th>
				<th>자치구</th>
				<th>와이파이명</th>
				<th>도로명주소</th>
				<th>상세주소</th>
				<th>설치위치(층)</th>
				<th>설치유형</th>
				<th>설치기관</th>
				<th>서비스구분</th>
				<th>망종류</th>
				<th>설치년도</th>
				<th>실내외구분</th>
				<th>WIFI접속환경</th>
				<th>X좌표</th>
				<th>Y좌표</th>
				<th>작업일자</th>
			</tr>
		</thead>
		<tbody>
            <tr>
                <td colspan="17">위치 정보를 입력한 후에 조회해주세요.</td>
            </tr>
        </tbody>
	</table>
	
	
		
	<script src = "./js/data2.js"></script>	
</body>
</html>