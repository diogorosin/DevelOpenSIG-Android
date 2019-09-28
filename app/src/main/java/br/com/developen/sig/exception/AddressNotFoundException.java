package br.com.developen.sig.exception;

import br.com.developen.sig.util.Messaging;

public class AddressNotFoundException extends Exception implements Messaging {

    public AddressNotFoundException(){

        super("Endereço não encontrado");

    }

    public String[] getMessages(){

        return new String[]{ getMessage() };

    }

}