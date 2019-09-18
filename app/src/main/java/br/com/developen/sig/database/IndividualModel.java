package br.com.developen.sig.database;


import androidx.room.Embedded;
import androidx.room.TypeConverters;

import java.util.Date;


public class IndividualModel {


    private Integer identifier;

    private Boolean active;

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


    public Integer getIdentifier() {

        return identifier;

    }


    public void setIdentifier(Integer identifier) {

        this.identifier = identifier;

    }


    public Boolean getActive() {

        return active;

    }


    public void setActive(Boolean active) {

        this.active = active;

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


    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndividualModel that = (IndividualModel) o;
        return identifier.equals(that.identifier);

    }


    public int hashCode() {

        return identifier.hashCode();

    }


}