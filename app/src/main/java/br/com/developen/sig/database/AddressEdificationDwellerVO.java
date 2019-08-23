package br.com.developen.sig.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = "AddressEdificationDweller",
        primaryKeys = {"address", "edification", "dweller"},
        foreignKeys = {
                @ForeignKey(entity = AddressEdificationVO.class,
                        parentColumns = {"address", "edification"},
                        childColumns = {"address", "edification"},
                        onDelete = ForeignKey.RESTRICT,
                        onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = SubjectVO.class,
                        parentColumns = "identifier",
                        childColumns = "subject",
                        onDelete = ForeignKey.RESTRICT,
                        onUpdate = ForeignKey.CASCADE)},
        indices = {@Index({"address", "edification", "dweller"}), @Index({"subject"})})
public class AddressEdificationDwellerVO {

    @NonNull
    @ColumnInfo(name="address")
    private Integer address;

    @NonNull
    @ColumnInfo(name="edification")
    private Integer edification;

    @NonNull
    @ColumnInfo(name="dweller")
    private Integer dweller;

    @NonNull
    @ColumnInfo(name="subject")
    private Integer subject;

    @NonNull
    @TypeConverters({TimestampConverter.class})
    @ColumnInfo(name="from")
    private Date from;

    @ColumnInfo(name="to")
    @TypeConverters({TimestampConverter.class})
    private Date to;

    public Integer getAddress() {

        return address;

    }

    public void setAddress(Integer address) {

        this.address = address;

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

    public Integer getSubject() {

        return subject;

    }

    public void setSubject(Integer subject) {

        this.subject = subject;

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
        AddressEdificationDwellerVO that = (AddressEdificationDwellerVO) o;
        return Objects.equals(address, that.address) &&
                Objects.equals(edification, that.edification) &&
                Objects.equals(dweller, that.dweller);

    }

    public int hashCode() {

        return Objects.hash(address, edification, dweller);

    }

}