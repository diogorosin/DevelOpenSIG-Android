package br.com.developen.sig.database;


import androidx.room.Embedded;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.Objects;

public class ModifiedAddressModel {


    private Integer identifier;

    @TypeConverters({DateConverter.class})
    private Date syncedAt;

    @TypeConverters({DateConverter.class})
    private Date modifiedAt;

    private Integer modifiedBy;

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


    public Integer getIdentifier() {

        return identifier;

    }

    public void setIdentifier(Integer identifier) {

        this.identifier = identifier;

    }

    public Date getSyncedAt() {

        return syncedAt;

    }

    public void setSyncedAt(Date syncedAt) {

        this.syncedAt = syncedAt;

    }

    public Date getModifiedAt() {

        return modifiedAt;

    }

    public void setModifiedAt(Date modifiedAt) {

        this.modifiedAt = modifiedAt;

    }

    public Integer getModifiedBy() {

        return modifiedBy;

    }

    public void setModifiedBy(Integer modifiedBy) {

        this.modifiedBy = modifiedBy;

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

    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModifiedAddressModel that = (ModifiedAddressModel) o;
        return Objects.equals(identifier, that.identifier);

    }

    public int hashCode() {

        return Objects.hash(identifier);

    }

}