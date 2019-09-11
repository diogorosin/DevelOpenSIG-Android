package br.com.developen.sig.repository;

import java.util.ArrayList;
import java.util.List;

import br.com.developen.sig.database.GenderModel;
import br.com.developen.sig.util.Constants;

public class GenderRepository {


    private static GenderRepository instance;


    public static GenderRepository getInstance() {

        if (instance == null) {

            synchronized (GenderRepository.class) {

                if (instance == null) {

                    instance = new GenderRepository();

                }

            }

        }

        return instance;

    }


    public List<GenderModel> getGenders(){

        List<GenderModel> list = new ArrayList<>();

        list.add(new GenderModel("M", Constants.MALE_GENDER_DENOMINATION));

        list.add(new GenderModel("F", Constants.FEMALE_GENDER_DENOMINATION));

        return list;

    }


}
