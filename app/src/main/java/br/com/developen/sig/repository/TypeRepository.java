package br.com.developen.sig.repository;

import java.util.List;

import br.com.developen.sig.database.StateModel;
import br.com.developen.sig.database.TypeModel;
import br.com.developen.sig.util.DB;

public class TypeRepository {


    private static TypeRepository instance;

    private final DB database;


    private TypeRepository(DB database) {

        this.database = database;

    }


    public static TypeRepository getInstance(final DB database) {

        if (instance == null) {

            synchronized (TypeRepository.class) {

                if (instance == null) {

                    instance = new TypeRepository(database);

                }

            }

        }

        return instance;

    }


    public List<TypeModel> getList(){

        return database.typeDAO().getList();

    }


}
