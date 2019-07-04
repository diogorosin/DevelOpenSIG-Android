package br.com.developen.sig.database;


import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;

@DatabaseView(viewName = "SubjectView", value =
        "SELECT I.identifier, I.name AS nameOrDenomination, 'I' AS type " +
        "FROM Subject S1 " +
        "INNER JOIN Individual I ON I.identifier = S1.identifier " +
        "UNION ALL " +
        "SELECT O.identifier, O.denomination AS nameOrDenomination, 'O' AS type " +
        "FROM Subject S2 " +
        "INNER JOIN Organization O ON O.identifier = S2.identifier")
public class SubjectModel {

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
        SubjectModel countryVO = (SubjectModel) o;
        return identifier.equals(countryVO.identifier);

    }

    public int hashCode() {

        return identifier.hashCode();

    }

}