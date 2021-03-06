package br.com.developen.sig.util;

import android.app.Application;
import android.content.Context;

import com.evernote.android.job.JobManager;
import com.mlykotom.valifi.ValiFi;

import java.util.TimeZone;

import br.com.developen.sig.R;
import br.com.developen.sig.job.AppJobCreator;
import br.com.developen.sig.repository.AddressEdificationRepository;
import br.com.developen.sig.repository.AddressRepository;
import br.com.developen.sig.repository.CityRepository;
import br.com.developen.sig.repository.MarkerRepository;
import br.com.developen.sig.repository.ModifiedAddressEdificationDwellerRepository;
import br.com.developen.sig.repository.ModifiedAddressEdificationRepository;
import br.com.developen.sig.repository.ModifiedAddressRepository;


public class App extends Application {


    private static App instance;


    public void onCreate() {

        super.onCreate();

        instance = this;

    }


    public static App getInstance() {

        return instance;

    }


    public static Context getContext(){

        return instance;

    }


    public void initialize(){

        //AJUSTA O TIMEZONE PARA CALCULOS DE DATA E HORA
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));

        //CONFIGURA VALIDADOR
        ValiFi.install(this,
                new ValiFi.Builder().
                        setErrorResource(ValiFi.Builder.ERROR_RES_LENGTH_MIN, R.string.validation_error_min_length).
                        setErrorResource(ValiFi.Builder.ERROR_RES_LENGTH_MAX, R.string.validation_error_max_length).
                        setErrorResource(ValiFi.Builder.ERROR_RES_LENGTH_EXACT, R.string.validation_error_exact_length).
                        setErrorResource(ValiFi.Builder.ERROR_RES_NOT_EMPTY, R.string.validation_error_not_empty).
                        build());

        //AGENDA OS SERVICOS
        JobManager.create(this).addJobCreator(new AppJobCreator());

    }


    public CityRepository getCityRepository() {

        return CityRepository.getInstance(DB.getInstance(this));

    }


    public ModifiedAddressEdificationDwellerRepository getModifiedAddressEdificationDwellerRepository() {

        return ModifiedAddressEdificationDwellerRepository.getInstance(DB.getInstance(this));

    }


    public ModifiedAddressEdificationRepository getModifiedAddressEdificationRepository() {

        return ModifiedAddressEdificationRepository.getInstance(DB.getInstance(this));

    }


    public ModifiedAddressRepository getModifiedAddressRepository() {

        return ModifiedAddressRepository.getInstance(DB.getInstance(this));

    }


    public MarkerRepository getMarkerRepository() {

        return MarkerRepository.getInstance(DB.getInstance(this));

    }


    public AddressRepository getAddressRepository() {

        return AddressRepository.getInstance(DB.getInstance(this));

    }


    public AddressEdificationRepository getAddressEdificationRepository() {

        return AddressEdificationRepository.getInstance(DB.getInstance(this));

    }


}