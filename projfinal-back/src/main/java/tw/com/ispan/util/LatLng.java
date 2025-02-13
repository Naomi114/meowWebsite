package tw.com.ispan.util;

//此類別用來封裝googleapi取下來的經緯度
public class LatLng {
    private double lat;
    private double lng;

    public LatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    @Override
    public String toString() {
        return "Latitude: " + lat + ", Longitude: " + lng;
    }
}