$(document).ready(function() {
    fetchHistory();
});

function fetchHistory() {
    $.ajax({
        url: './LocationServlet',
        type: 'POST',
        dataType: 'json',
        success: function(data) {
            displayHistory(data);
        },
        error: function(xhr, status, error) {
            console.error('Error:', error);
        }
    });
}

function displayHistory(historyData) {
    try {
        var historyList = historyData;

        // ID를 기준으로 내림차순 정렬
        historyList.sort(function(a, b) {
            return b.id - a.id;
        });

        var tbody = document.querySelector("#historyTable tbody");

        // tbody 초기화
        tbody.innerHTML = "";
        
        if (historyData.length === 0) {
            var noDataMessage = "<tr><td colspan='5'>조회 내역이 없습니다.</td></tr>";
            tbody.innerHTML = noDataMessage;
            return;
        }

        // 받은 데이터를 테이블에 추가
        historyList.forEach(function(history) {
            var row = "<tr>" +
                "<td>" + (history.id !== '' ? history.id : '') + "</td>" +
                "<td>" + (history.myLat !== '' ? history.myLat : '') + "</td>" +
                "<td>" + (history.myLnt !== '' ? history.myLnt : '') + "</td>" +
                "<td>" + (history.timeStamp !== '' ? history.timeStamp : '') + "</td>" +
                "<td><button onclick=\"deleteRow(this)\">삭제</button></td>" +
                "</tr>";

            // 테이블에 행 추가
            tbody.innerHTML += row;
        });

        console.log("JSON 파싱 성공:");
    } catch (error) {
        console.error("JSON 파싱 실패:", error);
        console.error("받은 데이터:", historyData);
    }
}


// 삭제 버튼 클릭 시 해당 행 삭제
function deleteRow(button) {
    var row = button.parentNode.parentNode; 
    row.parentNode.removeChild(row); 
    
    
    
    var idToDelete = row.cells[0].innerText; 
    $.ajax({
        url: './DeleteHistoryServlet', // 삭제를 처리할 서블릿 주소
        type: 'POST',
        data: { id: idToDelete }, // 삭제할 데이터의 ID를 서버로 전달
        success: function(response) {
            console.log('삭제 요청이 성공적으로 처리되었습니다.');
        },
        error: function(xhr, status, error) {
            console.error('삭제 요청 중 오류가 발생하였습니다:', error);
        }
    });
    
     // 삭제 후 히스토리 리스트의 길이 확인
    if (historyList.length === 0) {
        var tbody = document.querySelector("#historyTable tbody");
        tbody.innerHTML = "<tr><td colspan='4'>조회 내역이 없습니다.</td></tr>";
    }

}
    