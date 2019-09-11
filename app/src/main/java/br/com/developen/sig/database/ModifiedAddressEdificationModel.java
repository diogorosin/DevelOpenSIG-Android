package br.com.developen.sig.database;


import androidx.room.Embedded;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.Objects;

public class ModifiedAddressEdificationModel {


    @Embedded(prefix = "modifiedAddress_")
    private ModifiedAddressModel modifiedAddress;

    private Integer edification;

    @Embedded(prefix = "type_")
    private TypeModel type;

    private String reference;

    @TypeConverters({DateConverter.class})
    private Date from;

    @TypeConverters({DateConverter.class})
    private Date to;

    private Boolean active;

    private String firstDwellerName;

    private Integer dwellersCount;


    public ModifiedAddressModel getModifiedAddress() {

        return modifiedAddress;

    }

    public void setModifiedAddress(ModifiedAddressModel modifiedAddress) {

        this.modifiedAddress = modifiedAddress;

    }

    public Integer getEdification() {

        return edification;

    }

    public void setEdification(Integer edification) {

        this.edification = edification;

    }

    public TypeModel getType() {

        return type;

    }

    public void setType(TypeModel type) {

        this.type = type;

    }

    public String getReference() {

        return reference;

    }

    public void setReference(String reference) {

        this.reference = reference;

    }

    public Boolean getActive() {

        return active;

    }

    public void setActive(Boolean active) {

        this.active = active;

    }

    public Date getFrom() {

        return from;

    }

    public void setFrom(Date from) {

        this.from = from;

    }

    public Date getTo() {

        return to;

    }

    public void setTo(Date to) {

        this.to = to;

    }

    public String getFirstDwellerName() {

        return firstDwellerName;

    }

    public void setFirstDwellerName(String firstDwellerName) {

        this.firstDwellerName = firstDwellerName;

    }

    public Integer getDwellersCount() {

        return dwellersCount;

    }

    public void setDwellersCount(Integer dwellersCount) {

        this.dwellersCount = dwellersCount;

    }

    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModifiedAddressEdificationModel that = (ModifiedAddressEdificationModel) o;
        return Objects.equals(modifiedAddress, that.modifiedAddress) &&
                Objects.equals(edification, that.edification);

    }

    public int hashCode() {

        return Objects.hash(modifiedAddress, edification);

    }

}