package br.com.developen.sig.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = "ModifiedAddressEdificationDweller",
        primaryKeys = {"modifiedAddress", "edification", "dweller"},
        foreignKeys = {
                @ForeignKey(entity = ModifiedAddressEdificationVO.class,
                        parentColumns = {"modifiedAddress", "edification"},
                        childColumns = {"modifiedAddress", "edification"},
                        onDelete = ForeignKey.RESTRICT,
                        onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = IndividualVO.class,
                        parentColumns = "identifier",
                        childColumns = "individual",
                        onDelete = ForeignKey.RESTRICT,
                        onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value={"modifiedAddress", "edification", "dweller"}), @Index({"individual"})})
public class ModifiedAddressEdificationDwellerVO {

    @NonNull
    @ColumnInfo(name="modifiedAddress")
    private Integer modifiedAddress;

    @NonNull
    @ColumnInfo(name="edification")
    private Integer edification;

    @NonNull
    @ColumnInfo(name="dweller")
    private Integer dweller;

    @ColumnInfo(name="individual")
    private Integer individual;

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name="motherName")
    private String motherName;

    @ColumnInfo(name="fatherName")
    private String fatherName;

    @ColumnInfo(name="cpf")
    private Long cpf;

    @ColumnInfo(name="rgNumber")
    private Long rgNumber;

    @ColumnInfo(name="rgAgency")
    private Integer rgAgency;

    @ColumnInfo(name="rgState")
    private Integer rgState;

    @ColumnInfo(name="birthPlace")
    private Integer birthPlace;

    @TypeConverters({DateConverter.class})
    @ColumnInfo(name="birthDate")
    private Date birthDate;

    @ColumnInfo(name="gender")
    private String gender;

    @NonNull
    @TypeConverters({DateConverter.class})
    @ColumnInfo(name="from")
    private Date from;

    @ColumnInfo(name="to")
    @TypeConverters({DateConverter.class})
    private Date to;

    @NonNull
    @ColumnInfo(name="active")
    private Boolean active;


    public Integer getModifiedAddress() {

        return modifiedAddress;

    }


    public void setModifiedAddress(Integer modifiedAddress) {

        this.modifiedAddress = modifiedAddress;

    }


    public Integer getEdification() {

        return edification;

    }


    public void setEdification(Integer edification) {

        this.edification = edification;

    }


    public Integer getDweller() {

        return dweller;

    }


    public void setDweller(Integer dweller) {

        this.dweller = dweller;

    }


    public Integer getIndividual() {

        return individual;

    }


    public void setIndividual(Integer individual) {

        this.individual = individual;

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


    public Integer getRgAgency() {

        return rgAgency;

    }


    public void setRgAgency(Integer rgAgency) {

        this.rgAgency = rgAgency;

    }


    public Integer getRgState() {

        return rgState;

    }


    public void setRgState(Integer rgState) {

        this.rgState = rgState;

    }


    public Integer getBirthPlace() {

        return birthPlace;

    }


    public void setBirthPlace(Integer birthPlace) {

        this.birthPlace = birthPlace;

    }


    public Date getBirthDate() {

        return birthDate;

    }


    public void setBirthDate(Date birthDate) {

        this.birthDate = birthDate;

    }


    public String getGender() {

        return gender;

    }


    public void setGender(String gender) {

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
        ModifiedAddressEdificationDwellerVO that = (ModifiedAddressEdificationDwellerVO) o;
        return Objects.equals(modifiedAddress, that.modifiedAddress) &&
                Objects.equals(edification, that.edification) &&
                Objects.equals(dweller, that.dweller);

    }


    public int hashCode() {

        return Objects.hash(modifiedAddress, edification, dweller);

    }


}