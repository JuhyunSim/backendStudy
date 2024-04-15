package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import seoul_wifi.WifiService;

@WebServlet("/OpenApiLoadServlet")
public class OpenApiLoadServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // WifiService 클래스의 인스턴스 생성
        WifiService wifiService = new WifiService();
        
        // WifiService의 countData() 메서드 호출하여 와이파이 정보 개수를 가져옴
        int wifiCount = wifiService.countData();
        
        // JSP 페이지로 와이파이 정보 개수를 전달하기 위해 request 객체에 저장
        request.setAttribute("wifiCount", wifiCount);
        
        // JSP 페이지로 포워딩
        request.getRequestDispatcher("/wifi-loadData.jsp").forward(request, response);
    }
}