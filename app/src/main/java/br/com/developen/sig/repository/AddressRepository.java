package br.com.developen.sig.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import br.com.developen.sig.database.AddressModel;
import br.com.developen.sig.database.SubjectModel;
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


    public List<SubjectModel> getSubjectsOfAddress(Integer address){

        return database.addressDAO().getSubjectsOfAddress(address);

    }


}
