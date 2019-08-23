package br.com.developen.sig.database;


import androidx.room.Embedded;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.Objects;

public class ModifiedAddressModel {


    private Integer identifier;

    @TypeConverters({TimestampConverter.class})
    private Date syncedAt;

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

    private Boolean active;

    @TypeConverters({TimestampConverter.class})
    private Date modifiedAt;

    private Integer modifiedBy;


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

    public Boolean getActive() {

        return active;

    }

    public void setActive(Boolean active) {

        this.active = active;

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

    public boolean hasSameContents(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModifiedAddressModel that = (ModifiedAddressModel) o;
        if (identifier != null ? !identifier.equals(that.identifier) : that.identifier != null)
            return false;
        if (syncedAt != null ? !syncedAt.equals(that.syncedAt) : that.syncedAt != null)
            return false;
        if (modifiedAt != null ? !modifiedAt.equals(that.modifiedAt) : that.modifiedAt != null)
            return false;
        if (modifiedBy != null ? !modifiedBy.equals(that.modifiedBy) : that.modifiedBy != null)
            return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (denomination != null ? !denomination.equals(that.denomination) : that.denomination != null)
            return false;
        if (number != null ? !number.equals(that.number) : that.number != null) return false;
        if (reference != null ? !reference.equals(that.reference) : that.reference != null)
            return false;
        if (district != null ? !district.equals(that.district) : that.district != null)
            return false;
        if (postalCode != null ? !postalCode.equals(that.postalCode) : that.postalCode != null)
            return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (latitude != null ? !latitude.equals(that.latitude) : that.latitude != null)
            return false;
        if (longitude != null ? !longitude.equals(that.longitude) : that.longitude != null)
            return false;
        return active != null ? active.equals(that.active) : that.active == null;

    }

}