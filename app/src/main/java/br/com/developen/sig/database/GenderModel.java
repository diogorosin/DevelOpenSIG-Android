package br.com.developen.sig.database;

public class GenderModel {

    private String identifier;

    private String denomination;

    public GenderModel(){}

    public GenderModel(String identifier, String denomination){

        this.identifier = identifier;

        this.denomination = denomination;

    }

    public String getIdentifier() {

        return identifier;

    }

    public void setIdentifier(String identifier) {

        this.identifier = identifier;

    }

    public String getDenomination() {

        return denomination;

    }

    public void setDenomination(String denomination) {

        this.denomination = denomination;

    }

    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenderModel that = (GenderModel) o;
        return identifier.equals(that.identifier);

    }

    public int hashCode() {

        return identifier.hashCode();

    }

    public String toString() {

        return denomination;

    }

}
