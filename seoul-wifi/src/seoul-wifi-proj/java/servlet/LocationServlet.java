package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import seoul_wifi.HistoryInfo;
import seoul_wifi.LocationData;
import seoul_wifi.WifiInfo;
import seoul_wifi.WifiService;

@WebServlet("/LocationServlet")
public class LocationServlet extends HttpServlet {
	
	public static List<HistoryInfo> historyList = new ArrayList<>();
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        // WifiService 클래스의 메서드를 호출하여 거리정보 업데이트
        WifiService wifiService = new WifiService();
        
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(historyList);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush(); 

//        response.getWriter().write("Location data received and processed successfully.");
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	request.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html; charset=UTF-8");
    	
        double latitude = Double.parseDouble(request.getParameter("latitude"));
        double longitude = Double.parseDouble(request.getParameter("longitude"));
        
        // 위치 정보를 LocationData 객체에 저장
        LocationData locationData = new LocationData();
        locationData.setLatitude(latitude);
        locationData.setLongitude(longitude);
    	
        // WifiService 클래스의 메서드를 호출하여 거리정보 업데이트
        WifiService wifiService = new WifiService();
        List<WifiInfo> wifiList = wifiService.getWifiList(locationData);
        HistoryInfo hi = wifiService.historyUpdate(locationData);
        
        
        hi.setId(historyList.size() + 1);
        historyList.add(hi);        	

        
        Gson gson = new Gson();
//        String jsonResponse = gson.toJson(wifiList);

        JsonArray jsonArray = new JsonArray();
        for (WifiInfo wifiInfo : wifiList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("distance", wifiInfo.getDistance());
            jsonObject.addProperty("manageNo", wifiInfo.getManageNo());
            jsonObject.addProperty("region", wifiInfo.getRegion());
            jsonObject.addProperty("wifiName", wifiInfo.getWifiName());
            jsonObject.addProperty("adress1", wifiInfo.getAdress1());
            jsonObject.addProperty("adress2", wifiInfo.getAdress2());
            jsonObject.addProperty("instlFloor", wifiInfo.getInstlFloor());
            jsonObject.addProperty("instlType", wifiInfo.getInstlType());
            jsonObject.addProperty("instlBy", wifiInfo.getInstlBy());
            jsonObject.addProperty("serviceSe", wifiInfo.getServiceSe());
            jsonObject.addProperty("wifiCMCWR", wifiInfo.getWifiCMCWR());
            jsonObject.addProperty("instlYear", wifiInfo.getInstlYear());
            jsonObject.addProperty("inoutDoor", wifiInfo.getInoutDoor());
            jsonObject.addProperty("wifiEnviron", wifiInfo.getWifiEnviron());
            jsonObject.addProperty("wifiLnt", wifiInfo.getWifiLnt());
            jsonObject.addProperty("wifiLat", wifiInfo.getWifiLat());
            jsonObject.addProperty("workDate", wifiInfo.getWorkDate());
            
            jsonArray.add(jsonObject);
        }
        

        // JSON 응답을 클라이언트에게 보냅니다.
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonArray);
        out.flush();
    }
}