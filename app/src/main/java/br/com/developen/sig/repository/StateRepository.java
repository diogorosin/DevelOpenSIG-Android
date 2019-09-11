package br.com.developen.sig.repository;

import java.util.List;

import br.com.developen.sig.database.StateModel;
import br.com.developen.sig.util.DB;

public class StateRepository {


    private static StateRepository instance;

    private final DB database;


    private StateRepository(DB database) {

        this.database = database;

    }


    public static StateRepository getInstance(final DB database) {

        if (instance == null) {

            synchronized (StateRepository.class) {

                if (instance == null) {

                    instance = new StateRepository(database);

                }

            }

        }

        return instance;

    }


    public List<StateModel> getStatesOfCountry(Integer country){

        return database.stateDAO().getStatesOfCountry(country);

    }


}
