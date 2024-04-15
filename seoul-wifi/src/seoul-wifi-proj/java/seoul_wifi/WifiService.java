package seoul_wifi;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WifiService {
    public void showWifiInfo() {
        String url = "jdbc:mariadb://13.209.127.192:3306/wifi_db";
        String dbUserId = "wifi_user";
        String dbPassword = "wifi1234";

        try {
            Class.forName("org.mariadb.jdbc.Driver"); //Driver class 로드 (드라이버 로드)
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);

            String sql = "SELECT DISTANCE , " +
                    "\tX_SWIFI_MGR_NO , " +
                    "\tX_SWIFI_WRDOFC , " +
                    "\tX_SWIFI_MAIN_NM , " +
                    "\tX_SWIFI_ADRES1 , " +
                    "\tX_SWIFI_ADRES2 , " +
                    "\tX_SWIFI_INSTL_FLOOR , " +
                    "\tX_SWIFI_INSTL_TY , " +
                    "\tX_SWIFI_INSTL_MBY , " +
                    "\tX_SWIFI_SVC_SE , " +
                    "\tX_SWIFI_CMCWR , " +
                    "\tX_SWIFI_CNSTC_YEAR , " +
                    "\tX_SWIFI_INOUT_DOOR , " +
                    "\tX_SWIFI_REMARS3 , " +
                    "\tLNT , " +
                    "\tLAT , " +
                    "\tWORK_DTTM  " +
                    "FROM SEARCH_WIFI sw " +
                    " ORDER BY DISTANCE " +
                    " LIMIT 20; ";

            preparedStatement = connection.prepareStatement(sql);

            rs = preparedStatement.executeQuery();

            while (rs.next()) {

                String distance = rs.getString("DISTANCE");
                String manageNo = rs.getString("X_SWIFI_MGR_NO");
                String region = rs.getString("X_SWIFI_WRDOFC");
                String wifiName = rs.getString("X_SWIFI_MAIN_NM");
                String address1 = rs.getString("X_SWIFI_ADRES1");
                String address2 = rs.getString("X_SWIFI_ADRES2");
                String floor = rs.getString("X_SWIFI_INSTL_FLOOR");
                String wifiType= rs.getString("X_SWIFI_INSTL_TY");
                String manageBy = rs.getString("X_SWIFI_INSTL_MBY");
                String serviceTy = rs.getString("X_SWIFI_SVC_SE");
                String netType = rs.getString("X_SWIFI_CMCWR");
                String instlYear = rs.getString("X_SWIFI_CNSTC_YEAR");
                String inoutDoor = rs.getString("X_SWIFI_INOUT_DOOR");
                String remars3 = rs.getString("X_SWIFI_REMARS3");
                String lnt = rs.getString("LNT");
                String lat = rs.getString("LAT");
                String workDttm = rs.getString("WORK_DTTM");

                System.out.printf("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, ",
                        distance, manageNo, region, wifiName, address1, address2, floor, wifiType, manageBy, serviceTy,
                        netType, instlYear, inoutDoor, remars3, lnt, lat, workDttm);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<WifiInfo> getWifiList(LocationData locationData) {
    	
    	wifiDistUpdate(locationData);
    	
        String url = "jdbc:mariadb://13.209.127.192:3306/wifi_db";
        String dbUserId = "wifi_user";
        String dbPassword = "wifi1234";
        List<WifiInfo> wifiInfoList = new ArrayList<>();

        try {
            Class.forName("org.mariadb.jdbc.Driver"); //Driver class 로드 (드라이버 로드)
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);

            String sql = "SELECT DISTANCE , " +
                    "\tX_SWIFI_MGR_NO , " +
                    "\tX_SWIFI_WRDOFC , " +
                    "\tX_SWIFI_MAIN_NM , " +
                    "\tX_SWIFI_ADRES1 , " +
                    "\tX_SWIFI_ADRES2 , " +
                    "\tX_SWIFI_INSTL_FLOOR , " +
                    "\tX_SWIFI_INSTL_TY , " +
                    "\tX_SWIFI_INSTL_MBY , " +
                    "\tX_SWIFI_SVC_SE , " +
                    "\tX_SWIFI_CMCWR , " +
                    "\tX_SWIFI_CNSTC_YEAR , " +
                    "\tX_SWIFI_INOUT_DOOR , " +
                    "\tX_SWIFI_REMARS3 , " +
                    "\tLNT , " +
                    "\tLAT , " +
                    "\tWORK_DTTM  " +
                    "FROM SEARCH_WIFI sw " +
                    " ORDER BY DISTANCE " +
                    " LIMIT 20; ";

            preparedStatement = connection.prepareStatement(sql);

            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                WifiInfo wifi = new WifiInfo();
                wifi.setDistance(rs.getString("DISTANCE"));
                wifi.setManageNo(rs.getString("X_SWIFI_MGR_NO"));
                wifi.setRegion(rs.getString("X_SWIFI_WRDOFC"));
                wifi.setWifiName(rs.getString("X_SWIFI_MAIN_NM"));
                wifi.setAdress1(rs.getString("X_SWIFI_ADRES1"));
                wifi.setAdress2(rs.getString("X_SWIFI_ADRES2"));
                wifi.setInstlFloor(rs.getString("X_SWIFI_INSTL_FLOOR"));
                wifi.setInstlType(rs.getString("X_SWIFI_INSTL_TY"));
                wifi.setInstlBy(rs.getString("X_SWIFI_INSTL_MBY"));
                wifi.setServiceSe(rs.getString("X_SWIFI_SVC_SE"));
                wifi.setWifiCMCWR(rs.getString("X_SWIFI_CMCWR"));
                wifi.setInstlYear(rs.getString("X_SWIFI_CNSTC_YEAR"));
                wifi.setInoutDoor(rs.getString("X_SWIFI_INOUT_DOOR"));
                wifi.setWifiEnviron(rs.getString("X_SWIFI_REMARS3"));
                wifi.setWifiLnt(rs.getString("LNT"));
                wifi.setWifiLat(rs.getString("LAT"));
                wifi.setWorkDate(rs.getString("WORK_DTTM"));

                wifiInfoList.add(wifi);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return wifiInfoList;
    }
    
    public int countData() {
        String url = "jdbc:mariadb://13.209.127.192:3306/wifi_db";
        String dbUserId = "wifi_user";
        String dbPassword = "wifi1234";
        List<WifiInfo> wifiInfoList = new ArrayList<>();

        try {
            Class.forName("org.mariadb.jdbc.Driver"); //Driver class 로드 (드라이버 로드)
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        int answer = 0;
        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);

            String sql = " SELECT COUNT(*) as count " + " FROM SEARCH_WIFI sw; ";

            preparedStatement = connection.prepareStatement(sql);

            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                answer = Integer.parseInt(rs.getString("count"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return answer;
    }
    
    public void wifiDistUpdate(LocationData locationData) {
        String url = "jdbc:mariadb://13.209.127.192:3306/wifi_db";
        String dbUserId = "wifi_user";
        String dbPassword = "wifi1234";

        try {
            Class.forName("org.mariadb.jdbc.Driver"); //Driver class 로드 (드라이버 로드)
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);

            String sql = " UPDATE SEARCH_WIFI sw " +
                    " SET DISTANCE = ST_Distance_Sphere(POINT(sw.LNT , sw.LAT), POINT(?, ?)) / 1000; ";

            preparedStatement = connection.prepareStatement(sql);

            double lat = locationData.getLatitude();
            double lnt = locationData.getLongitude();
            preparedStatement.setDouble(1, lnt);
            preparedStatement.setDouble(2, lat);

            int affected = preparedStatement.executeUpdate();

            if (affected > 0) {
                System.out.println(" 저장 성공 ");
            } else {
                System.out.println(" 저장 실패 ");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public HistoryInfo historyUpdate(LocationData locationData) {
    	HistoryInfo hi = new HistoryInfo();
        hi.setMyLat(locationData.getLatitude());
        hi.setMyLnt(locationData.getLongitude());
        hi.setTimeStamp();

        return hi;
    }

    public List<String> getHistory() {

        String url = "jdbc:mariadb://13.209.127.192:3306/wifi_db";
        String dbUserId = "wifi_user";
        String dbPassword = "wifi1234";

        try {
            Class.forName("org.mariadb.jdbc.Driver"); //Driver class 로드 (드라이버 로드)
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        ArrayList<String> historyList = null;
        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);

            String sql = " SELECT * " +
                    " FROM SEARCH_HISTORY; ";

            preparedStatement = connection.prepareStatement(sql);

            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                historyList.add(rs.getString("DISTANCE"));
                historyList.add(rs.getString("VIEW_DATE"));
                historyList.add(rs.getString("MY_LNT"));
                historyList.add(rs.getString("MY_LAT"));
                historyList.add(rs.getString("NOTE"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return historyList;
    }
}
