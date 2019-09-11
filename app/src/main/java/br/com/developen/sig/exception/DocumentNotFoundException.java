package br.com.developen.sig.exception;

public class DocumentNotFoundException extends Exception {

    public DocumentNotFoundException(){

        super("Número do RG ou CPF deve ser informado.");

    }

}