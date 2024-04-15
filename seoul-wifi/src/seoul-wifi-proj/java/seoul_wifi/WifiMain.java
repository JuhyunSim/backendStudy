package seoul_wifi;

import java.util.List;

public class WifiMain {

    public static void main(String[] args) {
    	WifiService wfs = new WifiService();
        LocationData ld = new LocationData();


        ld.setLatitude(31.1111);
        ld.setLongitude(125.2222);
        wfs.wifiDistUpdate(ld);
        List<WifiInfo> wifiList = wfs.getWifiList(ld);

        for (int i = 0; i < wifiList.size(); i++) {
            System.out.println(i + 1 + ". " + wifiList.get(i).getDistance() + " / " + wifiList.get(i).getRegion() + " / " +
                    wifiList.get(i).getAdress1());
        }
    }
}
