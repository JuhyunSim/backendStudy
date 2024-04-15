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
<title>wifi-loadData</title>

<style>
	h2 {
		text-align: center;
	}
</style>
<script src = "http://code.jquery.com/jquery-latest.min.js"></script>
</head>
	<body>
		
		<h2 id="wifiCountMessage" > <%= request.getAttribute("wifiCount") %>개의 와이파이 정보를 정상적으로 저장했습니다. </h2>
		
		<p></p>
		<a href="http://localhost:8080/seoul-wifi/wifi-home.jsp">홈으로 가기</a>
		
		<script>
	    $(document).ready(function() {
	        $.ajax({
	            url: "./OpenApiLoadServlet",
	            type: "GET",
	            success: function(data) {
	                // 서버로부터 받은 데이터를 화면에 표시
	                $("#wifiCountMessage").html(data);
	              
	            },
	            error: function(xhr, status, error) {
	                console.error("Error fetching wifi count:", error);
	            }
	        });
	    });
	    </script>
	</body>
</html>