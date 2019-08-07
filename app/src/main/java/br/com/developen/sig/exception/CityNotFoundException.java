package br.com.developen.sig.exception;

public class CityNotFoundException extends Exception {

    public CityNotFoundException(){

        super("Cidade informada não encontrada");

    }

}