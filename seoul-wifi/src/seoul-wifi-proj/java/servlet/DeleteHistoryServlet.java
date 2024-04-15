package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import seoul_wifi.HistoryInfo;
import seoul_wifi.LocationData;
import seoul_wifi.WifiInfo;
import seoul_wifi.WifiService;

@WebServlet("/DeleteHistoryServlet")
public class DeleteHistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idToDelete = request.getParameter("id");

	    // ID를 기반으로 히스토리 목록에서 해당 항목을 찾아 제거
	    for (Iterator<HistoryInfo> iterator = LocationServlet.historyList.iterator(); iterator.hasNext();) {
	        HistoryInfo history = iterator.next();
	        if (String.valueOf(history.getId()).equals(idToDelete)) {
	            iterator.remove();
	            break;
	        }
	    }

	    // 삭제가 완료되었음을 응답으로 전송
	    response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write("삭제가 완료되었습니다.");
	}

}
