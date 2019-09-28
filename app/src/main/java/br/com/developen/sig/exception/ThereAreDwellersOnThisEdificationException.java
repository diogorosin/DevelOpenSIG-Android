package br.com.developen.sig.exception;

public class ThereAreDwellersOnThisEdificationException extends Exception {

    public ThereAreDwellersOnThisEdificationException(){

        super("A edificação não pode ser demolida pois existem moradores nela.");

    }

}