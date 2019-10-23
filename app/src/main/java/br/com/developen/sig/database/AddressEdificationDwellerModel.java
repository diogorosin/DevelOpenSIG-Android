package br.com.developen.sig.database;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.Objects;

public class AddressEdificationDwellerModel implements Dweller {


    @Embedded(prefix = "addressEdification_")
    private AddressEdificationModel addressEdification;

    private Integer dweller;

    @Embedded(prefix = "individual_")
    private IndividualModel individual;

    @NonNull
    @TypeConverters({TimestampConverter.class})
    private Date from;

    @TypeConverters({TimestampConverter.class})
    private Date to;


    public AddressEdificationModel getAddressEdification() {

        return addressEdification;

    }


    public void setAddressEdification(AddressEdificationModel addressEdification) {

        this.addressEdification = addressEdification;

    }


    public Integer getDweller() {

        return dweller;

    }


    public void setDweller(Integer dweller) {

        this.dweller = dweller;

    }


    public String getName() {

        return this.individual.getName();

    }


    public GenderModel getGender() {

        return this.individual.getGender();

    }


    public IndividualModel getIndividual() {

        return individual;

    }


    public void setIndividual(IndividualModel individual) {

        this.individual = individual;

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
        AddressEdificationDwellerModel that = (AddressEdificationDwellerModel) o;
        return Objects.equals(addressEdification, that.addressEdification) &&
                Objects.equals(dweller, that.dweller);

    }


    public int hashCode() {

        return Objects.hash(addressEdification, dweller);

    }


}