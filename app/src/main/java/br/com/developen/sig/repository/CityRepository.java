package br.com.developen.sig.repository;

import java.util.List;

import br.com.developen.sig.database.CityModel;
import br.com.developen.sig.util.DB;

public class CityRepository {


    private static CityRepository instance;

    private final DB database;


    private CityRepository(DB database) {

        this.database = database;

    }


    public static CityRepository getInstance(final DB database) {

        if (instance == null) {

            synchronized (CityRepository.class) {

                if (instance == null) {

                    instance = new CityRepository(database);

                }

            }

        }

        return instance;

    }


    public List<CityModel> getCities(){

        return database.cityDAO().getCities();

    }


    public CityModel findByCityStateAcronym(String city, String acronym){

        return database.cityDAO().findByCityStateAcronym(city, acronym);

    }

    public CityModel findByCityStateCountry(String city, String state, String country){

        return database.cityDAO().findByCityStateCountry(city, state, country);

    }

}
