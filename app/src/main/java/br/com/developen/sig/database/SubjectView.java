package br.com.developen.sig.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SubjectView")
public class SubjectView {

    @PrimaryKey
    @ColumnInfo(name="identifier")
    private Integer identifier;

    @ColumnInfo(name="nameOrDenomination")
    private String nameOrDenomination;

    @ColumnInfo(name="type")
    private String type;

    public Integer getIdentifier() {

        return identifier;

    }

    public void setIdentifier(Integer identifier) {

        this.identifier = identifier;

    }

    public String getNameOrDenomination() {

        return nameOrDenomination;

    }

    public void setNameOrDenomination(String nameOrDenomination) {

        this.nameOrDenomination = nameOrDenomination;

    }

    public String getType() {

        return type;

    }

    public void setType(String type) {

        this.type = type;

    }

    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubjectView countryVO = (SubjectView) o;
        return identifier.equals(countryVO.identifier);

    }

    public int hashCode() {

        return identifier.hashCode();

    }

}