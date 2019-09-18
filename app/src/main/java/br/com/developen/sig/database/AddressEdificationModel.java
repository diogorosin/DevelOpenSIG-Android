package br.com.developen.sig.database;

import androidx.room.Embedded;
import androidx.room.TypeConverters;

import java.util.Date;

public class AddressEdificationModel {

    @Embedded(prefix = "address_")
    private AddressModel address;

    private Integer edification;

    @Embedded(prefix = "type_")
    private TypeModel type;

    private String reference;

    @TypeConverters({DateConverter.class})
    private Date from;

    @TypeConverters({DateConverter.class})
    private Date to;

    public AddressModel getAddress() {

        return address;

    }

    public void setAddress(AddressModel address) {

        this.address = address;

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

    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressEdificationModel that = (AddressEdificationModel) o;
        if (!address.equals(that.address)) return false;
        return edification.equals(that.edification);

    }

    public int hashCode() {

        int result = address.hashCode();
        result = 31 * result + edification.hashCode();
        return result;

    }

}