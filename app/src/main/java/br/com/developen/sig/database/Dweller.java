package br.com.developen.sig.database;

import java.util.Date;

public interface Dweller {

    Integer getDweller() ;

    String getName();

    GenderModel getGender();

    Date getFrom();

    Date getTo();

}