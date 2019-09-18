package br.com.developen.sig.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import br.com.developen.sig.database.AddressEdificationModel;
import br.com.developen.sig.database.AddressModel;
import br.com.developen.sig.util.DB;

public class AddressRepository {


    private static AddressRepository instance;

    private final DB database;


    private AddressRepository(DB database) {

        this.database = database;

    }


    public static AddressRepository getInstance(final DB database) {

        if (instance == null) {

            synchronized (AddressRepository.class) {

                if (instance == null) {

                    instance = new AddressRepository(database);

                }

            }

        }

        return instance;

    }


    public LiveData<List<AddressModel>> getAddresses(){

        return database.addressDAO().getAddresses();

    }


    public AddressModel getAddress(Integer identifier){

        return database.addressDAO().getAddress(identifier);

    }


    public List<AddressEdificationModel> getEdificationsOfAddress(Integer address){

        return database.addressDAO().getEdificationsOfAddress(address);

    }


}
