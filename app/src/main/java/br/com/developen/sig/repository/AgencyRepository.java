package br.com.developen.sig.repository;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import br.com.developen.sig.database.AgencyDAO;
import br.com.developen.sig.database.AgencyModel;
import br.com.developen.sig.util.DB;

public class AgencyRepository extends AndroidViewModel {

    private AgencyDAO dao;

    private LiveData<List<AgencyModel>> agencies;

    public AgencyRepository(Application application){

        super(application);

    }

    public AgencyDAO getDao() {

        if (dao==null)

            dao = DB.getInstance(getApplication()).agencyDAO();

        return dao;

    }

    public void setDao(AgencyDAO dao) {

        this.dao = dao;

    }

    public LiveData<List<AgencyModel>> getAgencies(){

        if (agencies==null)

            agencies = getDao().getAgencies();

        return agencies;

    }

}