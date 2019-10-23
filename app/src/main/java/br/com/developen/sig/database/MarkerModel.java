package br.com.developen.sig.database;


import androidx.room.DatabaseView;
import androidx.room.Embedded;
import androidx.room.TypeConverters;

import java.util.Date;

import br.com.developen.sig.util.Constants;

@DatabaseView(viewName = "Marker",
        value = "SELECT " +
                " A.identifier AS 'identifier', " +
                " '" + Constants.MARKER_TYPE_ADDRESS + "' AS 'type', " +
                " A.identifier AS 'address', " +
                " A.denomination AS 'denomination', " +
                " A.number AS 'number', " +
                " A.reference AS 'reference', " +
                " A.district AS 'district', " +
                " A.postalCode AS 'postalCode', " +
                " A.latitude AS 'latitude', " +
                " A.longitude AS 'longitude', " +
                " City.identifier AS 'city_identifier', " +
                " City.denomination AS 'city_denomination', " +
                " CityState.identifier AS 'city_state_identifier', " +
                " CityState.denomination AS 'city_state_denomination', " +
                " CityState.acronym AS 'city_state_acronym', " +
                " CityStateCountry.identifier AS 'city_state_country_identifier', " +
                " CityStateCountry.denomination AS 'city_state_country_denomination', " +
                " CityStateCountry.acronym AS 'city_state_country_acronym', " +
                " A.verifiedBy AS 'verifiedBy', " +
                " A.verifiedAt AS 'verifiedAt' " +
                "FROM Address A " +
                "INNER JOIN City City ON City.identifier = A.city " +
                "INNER JOIN State CityState ON CityState.identifier = City.state " +
                "INNER JOIN Country CityStateCountry ON CityStateCountry.identifier = CityState.country " +
                "WHERE A.identifier NOT IN (SELECT MA.address FROM ModifiedAddress MA WHERE MA.address IS NOT NULL AND MA.active = 1 AND MA.syncedAt IS NULL) " +
                "UNION ALL " +
                "SELECT " +
                " MA.identifier AS 'identifier', " +
                " '" + Constants.MARKER_TYPE_MODIFIED + "' AS 'type', " +
                " MA.address AS 'address', " +
                " MA.denomination AS 'denomination', " +
                " MA.number AS 'number', " +
                " MA.reference AS 'reference', " +
                " MA.district AS 'district', " +
                " MA.postalCode AS 'postalCode', " +
                " MA.latitude AS 'latitude', " +
                " MA.longitude AS 'longitude', " +
                " City.identifier AS 'city_identifier', " +
                " City.denomination AS 'city_denomination', " +
                " CityState.identifier AS 'city_state_identifier', " +
                " CityState.denomination AS 'city_state_denomination', " +
                " CityState.acronym AS 'city_state_acronym', " +
                " CityStateCountry.identifier AS 'city_state_country_identifier', " +
                " CityStateCountry.denomination AS 'city_state_country_denomination', " +
                " CityStateCountry.acronym AS 'city_state_country_acronym', " +
                " MA.modifiedBy AS 'verifiedBy', " +
                " MA.modifiedAt AS 'verifiedAt' " +
                "FROM ModifiedAddress MA " +
                "INNER JOIN City City ON City.identifier = MA.city " +
                "INNER JOIN State CityState ON CityState.identifier = City.state " +
                "INNER JOIN Country CityStateCountry ON CityStateCountry.identifier = CityState.country " +
                "WHERE MA.syncedAt IS NULL AND Ma.active = 1 "
)
public class MarkerModel {

    private Integer identifier;

    private String type;

    private Integer address;

    private String denomination;

    private String number;

    private String reference;

    private String district;

    private Integer postalCode;

    @Embedded(prefix = "city_")
    private CityModel city;

    private Double latitude;

    private Double longitude;

    private Integer verifiedBy;

    @TypeConverters({TimestampConverter.class})
    private Date verifiedAt;

    public Integer getIdentifier() {

        return identifier;

    }

    public void setIdentifier(Integer identifier) {

        this.identifier = identifier;

    }

    public String getType() {

        return type;

    }

    public void setType(String type) {

        this.type = type;

    }

    public Integer getAddress() {

        return address;

    }

    public void setAddress(Integer address) {

        this.address = address;

    }

    public String getDenomination() {

        return denomination;

    }

    public void setDenomination(String denomination) {

        this.denomination = denomination;

    }

    public String getNumber() {

        return number;

    }

    public void setNumber(String number) {

        this.number = number;

    }

    public String getReference() {

        return reference;

    }

    public void setReference(String reference) {

        this.reference = reference;

    }

    public String getDistrict() {

        return district;

    }

    public void setDistrict(String district) {

        this.district = district;

    }

    public Integer getPostalCode() {

        return postalCode;

    }

    public void setPostalCode(Integer postalCode) {

        this.postalCode = postalCode;

    }

    public CityModel getCity() {

        return city;

    }

    public void setCity(CityModel city) {

        this.city = city;

    }

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

    public Integer getVerifiedBy() {

        return verifiedBy;

    }

    public void setVerifiedBy(Integer verifiedBy) {

        this.verifiedBy = verifiedBy;

    }

    public Date getVerifiedAt() {

        return verifiedAt;

    }

    public void setVerifiedAt(Date verifiedAt) {

        this.verifiedAt = verifiedAt;

    }

    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarkerModel that = (MarkerModel) o;
        if (!identifier.equals(that.identifier)) return false;
        return type.equals(that.type);

    }

    public int hashCode() {

        int result = identifier.hashCode();
        result = 31 * result + type.hashCode();
        return result;

    }

}