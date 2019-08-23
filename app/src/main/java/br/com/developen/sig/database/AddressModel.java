package br.com.developen.sig.database;


import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.TypeConverters;

import java.util.Date;

public class AddressModel {

    private Integer identifier;

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
        AddressModel addressVO = (AddressModel) o;
        return identifier.equals(addressVO.identifier);

    }

    public int hashCode() {

        return identifier.hashCode();

    }

}