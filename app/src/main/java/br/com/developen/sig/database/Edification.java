package br.com.developen.sig.database;

import java.util.Date;

public interface Edification {

    Integer getParentIdentifier();

    String getParentType();

    Integer getEdification();

    TypeModel getType();

    String getReference();

    Date getFrom();

    Date getTo();

}