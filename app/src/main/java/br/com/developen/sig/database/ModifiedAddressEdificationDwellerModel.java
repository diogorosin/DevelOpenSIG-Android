package br.com.developen.sig.database;

import androidx.room.Embedded;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.Objects;


public class ModifiedAddressEdificationDwellerModel {

    @Embedded(prefix = "modifiedAddressEdification_")
    private ModifiedAddressEdificationModel modifiedAddressEdification;

    private Integer dweller;

    private Integer subject;

    private String name;

    private String motherName;

    private String fatherName;

    private Long cpf;

    private Long rgNumber;

    @Embedded(prefix = "rgAgency_")
    private AgencyModel rgAgency;

    @Embedded(prefix = "rgState_")
    private StateModel rgState;

    @Embedded(prefix = "birthPlace_")
    private CityModel birthPlace;

    @TypeConverters({DateConverter.class})
    private Date birthDate;

    @Embedded(prefix = "gender_")
    private GenderModel gender;

    @TypeConverters({DateConverter.class})
    private Date from;

    @TypeConverters({DateConverter.class})
    private Date to;

    private Boolean active;


    public ModifiedAddressEdificationModel getModifiedAddressEdification() {

        return modifiedAddressEdification;

    }


    public void setModifiedAddressEdification(ModifiedAddressEdificationModel modifiedAddressEdification) {

        this.modifiedAddressEdification = modifiedAddressEdification;

    }


    public Integer getDweller() {

        return dweller;

    }


    public void setDweller(Integer dweller) {

        this.dweller = dweller;

    }


    public Integer getSubject() {

        return subject;

    }


    public void setSubject(Integer subject) {

        this.subject = subject;

    }


    public String getName() {

        return name;

    }


    public void setName(String name) {

        this.name = name;

    }


    public String getMotherName() {

        return motherName;

    }


    public void setMotherName(String motherName) {

        this.motherName = motherName;

    }


    public String getFatherName() {

        return fatherName;

    }


    public void setFatherName(String fatherName) {

        this.fatherName = fatherName;

    }


    public Long getCpf() {

        return cpf;

    }


    public void setCpf(Long cpf) {

        this.cpf = cpf;

    }


    public Long getRgNumber() {

        return rgNumber;

    }


    public void setRgNumber(Long rgNumber) {

        this.rgNumber = rgNumber;

    }


    public AgencyModel getRgAgency() {

        return rgAgency;

    }


    public void setRgAgency(AgencyModel rgAgency) {

        this.rgAgency = rgAgency;

    }


    public StateModel getRgState() {

        return rgState;

    }


    public void setRgState(StateModel rgState) {

        this.rgState = rgState;

    }


    public CityModel getBirthPlace() {

        return birthPlace;

    }


    public void setBirthPlace(CityModel birthPlace) {

        this.birthPlace = birthPlace;

    }


    public Date getBirthDate() {

        return birthDate;

    }


    public void setBirthDate(Date birthDate) {

        this.birthDate = birthDate;

    }


    public GenderModel getGender() {

        return gender;

    }


    public void setGender(GenderModel gender) {

        this.gender = gender;

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


    public Boolean getActive() {

        return active;

    }


    public void setActive(Boolean active) {

        this.active = active;

    }


    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModifiedAddressEdificationDwellerModel that = (ModifiedAddressEdificationDwellerModel) o;
        return Objects.equals(modifiedAddressEdification, that.modifiedAddressEdification) &&
                Objects.equals(dweller, that.dweller);

    }


    public int hashCode() {

        return Objects.hash(modifiedAddressEdification, dweller);

    }


}