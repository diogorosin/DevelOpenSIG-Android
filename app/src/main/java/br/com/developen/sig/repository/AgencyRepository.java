package br.com.developen.sig.repository;

import java.util.List;

import br.com.developen.sig.database.AgencyModel;
import br.com.developen.sig.util.DB;

public class AgencyRepository {


    private static AgencyRepository instance;

    private final DB database;


    private AgencyRepository(DB database) {

        this.database = database;

    }


    public static AgencyRepository getInstance(final DB database) {

        if (instance == null) {

            synchronized (AgencyRepository.class) {

                if (instance == null) {

                    instance = new AgencyRepository(database);

                }

            }

        }

        return instance;

    }


    public List<AgencyModel> getAgencies(){

        return database.agencyDAO().getAgencies();

    }


}
