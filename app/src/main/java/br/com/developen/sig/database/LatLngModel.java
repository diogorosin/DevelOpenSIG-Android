package br.com.developen.sig.database;


public class LatLngModel {

    private Double latitude;

    private Double longitude;

    public Double getLatitude() {

        return latitude;

    }

    public void setLatitude(Double latitude) {

        this.latitude = latitude;

    }

    public Double getLongitude() {

        return longitude;

    }

    public void setLongitude(Double longitude) {

        this.longitude = longitude;

    }

    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LatLngModel that = (LatLngModel) o;
        if (!latitude.equals(that.latitude)) return false;
        return longitude.equals(that.longitude);

    }

    public int hashCode() {

        int result = latitude.hashCode();
        result = 31 * result + longitude.hashCode();
        return result;

    }

}