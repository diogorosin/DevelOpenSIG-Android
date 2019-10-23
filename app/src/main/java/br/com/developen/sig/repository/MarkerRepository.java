package br.com.developen.sig.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import br.com.developen.sig.database.MarkerModel;
import br.com.developen.sig.util.DB;

public class MarkerRepository {


    private static MarkerRepository instance;

    private final DB database;


    private MarkerRepository(DB database) {

        this.database = database;

    }


    public static MarkerRepository getInstance(final DB database) {

        if (instance == null) {

            synchronized (MarkerRepository.class) {

                if (instance == null) {

                    instance = new MarkerRepository(database);

                }

            }

        }

        return instance;

    }


    public LiveData<List<MarkerModel>> getMarkers(){

        return database.markerDAO().getMarkers();

    }


}
