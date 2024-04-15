package seoul_wifi;

import java.time.LocalDate;

public class LocationData {

    private double latitude;
    private double longitude;

    private final LocalDate timeStamp = LocalDate.now();

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LocalDate getTimeStamp() {
        return timeStamp;
    }
}
