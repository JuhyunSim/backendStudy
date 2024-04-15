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
    #historyTable tbody tr:first-child {
        height: 40px; 
        text-align: center; 
    }
    
    #historyTable {
    border-collapse: collapse; 
    width: 100%; 
	}

    #historyTable th,
    #historyTable td {
        border: 1px solid black; 
        padding: 8px; 
    }
    

    th, td {
        text-align: center;
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
    <a href="http://localhost:8080/seoul-wifi/wifi-home.jsp"> 홈 </a>
    <a href=""> 위치 히스토리 목록 </a>
	<a href="http://localhost:8080/seoul-wifi/wifi-loadData.jsp"> Open API 와이파이 정보 가져오기 </a>
	<p></p>
	
	<table id="historyTable">
		<thead>
			<tr>
				<th>ID</th>
				<th>X좌표</th>
				<th>Y좌표</th>
				<th>조회일자</th>
				<th>비고</th>
			</tr>
		</thead>
		<tbody>
			
		
        </tbody>
	</table>
	
	
		
	<script src="./js/history-data.js"></script>
</body>
</html>