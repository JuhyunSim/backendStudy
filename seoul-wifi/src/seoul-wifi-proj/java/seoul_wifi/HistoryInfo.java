package seoul_wifi;

import java.time.LocalDateTime;


public class HistoryInfo {

    private int id;
    private double myLnt;
    private double myLat;
    private String timeStamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMyLnt() {
        return myLnt;
    }

    public void setMyLnt(double myLnt) {
        this.myLnt = myLnt;
    }

    public double getMyLat() {
        return myLat;
    }

    public void setMyLat(double myLat) {
        this.myLat = myLat;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp() {
        this.timeStamp = LocalDateTime.now().toString();
    }
}
