package br.com.developen.sig.util;

public interface DatabaseValidatorListener {

    void onValidationSuccess(int field);

    void onValidationError(int field, Exception e);

}