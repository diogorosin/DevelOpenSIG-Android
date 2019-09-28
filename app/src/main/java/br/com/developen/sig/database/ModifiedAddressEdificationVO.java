package br.com.developen.sig.database;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = "ModifiedAddressEdification",
        primaryKeys = {"modifiedAddress", "edification"},
        foreignKeys = {
                @ForeignKey(entity = ModifiedAddressVO.class,
                        parentColumns = "identifier",
                        childColumns = "modifiedAddress",
                        onDelete = ForeignKey.RESTRICT,
                        onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = TypeVO.class,
                        parentColumns = "identifier",
                        childColumns = "type",
                        onDelete = ForeignKey.RESTRICT,
                        onUpdate = ForeignKey.CASCADE)
                },
        indices = {@Index(value={"modifiedAddress", "edification"}),
                @Index(value={"type"})})
public class ModifiedAddressEdificationVO {

    @NonNull
    @ColumnInfo(name="modifiedAddress")
    private Integer modifiedAddress;

    @NonNull
    @ColumnInfo(name="edification")
    private Integer edification;

    @NonNull
    @ColumnInfo(name="type")
    private Integer type;

    @ColumnInfo(name="reference")
    private String reference;

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

    public Integer getType() {

        return type;

    }

    public void setType(Integer type) {

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

    public Boolean getActive() {

        return active;

    }

    public void setActive(Boolean active) {

        this.active = active;

    }

    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModifiedAddressEdificationVO that = (ModifiedAddressEdificationVO) o;
        return Objects.equals(modifiedAddress, that.modifiedAddress) &&
                Objects.equals(edification, that.edification);

    }

    public int hashCode() {

        return Objects.hash(modifiedAddress, edification);

    }

}