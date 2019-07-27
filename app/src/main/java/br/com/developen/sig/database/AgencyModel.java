package br.com.developen.sig.database;


public class AgencyModel {


    private Integer identifier;

    private String denomination;

    private String acronym;


    public Integer getIdentifier() {

        return identifier;

    }


    public void setIdentifier(Integer identifier) {

        this.identifier = identifier;

    }


    public String getDenomination() {

        return denomination;

    }


    public void setDenomination(String denomination) {

        this.denomination = denomination;

    }


    public String getAcronym() {

        return acronym;

    }


    public void setAcronym(String acronym) {

        this.acronym = acronym;

    }


    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgencyModel agencyModel = (AgencyModel) o;
        return identifier.equals(agencyModel.identifier);

    }


    public int hashCode() {

        return identifier.hashCode();

    }


    public String toString(){

        return getAcronym();

    }


}