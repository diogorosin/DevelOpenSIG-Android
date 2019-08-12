package br.com.developen.sig.database;


import java.util.Objects;

public class TypeModel {

    private Integer identifier;

    private String denomination;

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

    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeModel that = (TypeModel) o;
        return Objects.equals(identifier, that.identifier);
    }

    public int hashCode() {

        return Objects.hash(identifier);

    }

    public String toString() {

        return denomination;

    }

}