function goToHome() {
    // 홈으로 이동하는 함수
    window.location.href = "http://localhost:8080/seoul-wifi/wifi-home.jsp";
}

function getLocation() {
    // 현재 위치를 가져오는 함
    // 위치 정보를 가져오는 HTML5 Geolocation API 사용
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            // 현재 위치의 위도와 경도 값을 가져와서 입력 필드에 설정
            document.getElementById("latitudeInput").value = position.coords.latitude;
            document.getElementById("longitudeInput").value = position.coords.longitude;
        }, function(error) {
            console.error("Error getting current location:", error);
        });
    } else {
        alert("Geolocation is not supported by this browser.");
    }
}


function showWifiInfo() {
    // 근처 WIFI 정보를 보여주는 함수

    // LAT, LNT 값 가져오기
    var latitude = document.getElementById("latitudeInput").value;
    var longitude = document.getElementById("longitudeInput").value;
// AJAX를 사용하여 서버로 데이터 전송
    $.ajax({
        url: "./LocationServlet",
        type: "GET",
        data: {
            latitude: latitude,
            longitude: longitude
        },
        success: function(response) {
			console.log(response);
            // 서버로부터 받은 데이터를 처리하여 테이블에 출력하는 함수 호출
            displayWifiInfo(response);
          
        },
        error: function(xhr, status, error) {
            console.error("Error fetching WIFI info:", error);
        }
    });
}


function displayWifiInfo(wifiData) {
    // 서버로부터 받은 WIFI 정보를 테이블에 출력하는 함수

    try {
		
        var wifiList = wifiData;

        // tbody 요소 선택
        var tbody = document.querySelector("#wifiTable tbody");

        // tbody 초기화
        tbody.innerHTML = "";

        // 받은 데이터를 테이블에 추가
        wifiList.forEach(function(wifi) {
            var row = "<tr>" +
	                "<td>" + (wifi.distance !== '' ? wifi.distance : '') + "</td>" +
				    "<td>" + (wifi.manageNo !== '' ? wifi.manageNo : '') + "</td>" +
				    "<td>" + (wifi.region !== '' ? wifi.region : '') + "</td>" +
				    "<td>" + (wifi.wifiName !== '' ? wifi.wifiName : '') + "</td>" +
				    "<td>" + (wifi.adress1 !== '' ? wifi.adress1 : '') + "</td>" +
				    "<td>" + (wifi.adress2 !== '' ? wifi.adress2 : '') + "</td>" +
				    "<td>" + (wifi.instlFloor !== '' ? wifi.instlFloor : '') + "</td>" +
				    "<td>" + (wifi.instlType !== '' ? wifi.instlType : '') + "</td>" +
				    "<td>" + (wifi.instlBy !== '' ? wifi.instlBy : '') + "</td>" +
				    "<td>" + (wifi.serviceSe !== '' ? wifi.serviceSe : '') + "</td>" +
				    "<td>" + (wifi.wifiCMCWR !== '' ? wifi.wifiCMCWR : '') + "</td>" +
				    "<td>" + (wifi.instlYear !== '' ? wifi.instlYear : '') + "</td>" +
				    "<td>" + (wifi.inoutDoor !== '' ? wifi.inoutDoor : '') + "</td>" +
				    "<td>" + (wifi.wifiEnviron !== '' ? wifi.wifiEnviron : '') + "</td>" +
				    "<td>" + (wifi.wifiLnt !== '' ? wifi.wifiLnt : '') + "</td>" +
				    "<td>" + (wifi.wifiLat !== '' ? wifi.wifiLat : '') + "</td>" +
				    "<td>" + (wifi.workDate !== '' ? wifi.workDate : '') + "</td>" +
                "</tr>";

            // 테이블에 행 추가
            tbody.innerHTML += row;
        });

        console.log("JSON 파싱 성공:");
    } catch (error) {
        console.error("JSON 파싱 실패:", error);
        console.error("받은 데이터:",wifiData);
    }
}