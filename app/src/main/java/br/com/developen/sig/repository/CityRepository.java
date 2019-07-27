package br.com.developen.sig.repository;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import br.com.developen.sig.database.CityDAO;
import br.com.developen.sig.database.CityModel;
import br.com.developen.sig.util.DB;

public class CityRepository extends AndroidViewModel {

    private CityDAO dao;

    private LiveData<List<CityModel>> cities;

    public CityRepository(Application application){

        super(application);

    }

    public CityDAO getDao() {

        if (dao==null)

            dao = DB.getInstance(getApplication()).cityDAO();

        return dao;

    }

    public void setDao(CityDAO dao) {

        this.dao = dao;

    }

    public LiveData<List<CityModel>> getCities(){

        if (cities==null)

            cities = getDao().getCities();

        return cities;

    }

}