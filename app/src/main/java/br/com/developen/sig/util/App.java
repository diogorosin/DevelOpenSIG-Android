package br.com.developen.sig.util;

import android.app.Application;
import android.content.Context;

import com.mobsandgeeks.saripaar.Validator;

import java.util.TimeZone;

import br.com.developen.sig.widget.validator.Birthdate;
import br.com.developen.sig.widget.validator.CPF;
import br.com.developen.sig.widget.validator.IndividualName;

public class App extends Application {


    private static App instance;


    public void onCreate() {

        super.onCreate();

        instance = this;

    }


    public void initialize(){

        //AJUSTA O TIMEZONE PARA CALCULOS DE DATA E HORA
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));

        //ADICIONA OS VALIDADORES
        Validator.registerAnnotation(IndividualName.class);

        Validator.registerAnnotation(CPF.class);

        Validator.registerAnnotation(Birthdate.class);

    }


    public static App getInstance() {

        return instance;

    }


    public static Context getContext(){

        return instance;

    }


}