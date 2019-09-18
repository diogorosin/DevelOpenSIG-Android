package br.com.developen.sig.repository;

import java.util.List;

import br.com.developen.sig.database.AddressEdificationDwellerModel;
import br.com.developen.sig.util.DB;

public class AddressEdificationRepository {


    private static AddressEdificationRepository instance;

    private final DB database;


    private AddressEdificationRepository(DB database) {

        this.database = database;

    }


    public static AddressEdificationRepository getInstance(final DB database) {

        if (instance == null) {

            synchronized (AddressEdificationRepository.class) {

                if (instance == null) {

                    instance = new AddressEdificationRepository(database);

                }

            }

        }

        return instance;

    }


    public List<AddressEdificationDwellerModel> getDwellersOfAddressEdifications(Integer address, Integer edification){

        return database.addressEdificationDAO().getDwellersOfAddressEdification(address, edification);

    }


}