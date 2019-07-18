package br.com.developen.sig.repository;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import br.com.developen.sig.database.StateDAO;
import br.com.developen.sig.database.StateModel;
import br.com.developen.sig.util.DB;

public class StateRepository extends AndroidViewModel {

    private StateDAO dao;

    private LiveData<List<StateModel>> states;

    public StateRepository(Application application){

        super(application);

    }

    public StateDAO getDao() {

        if (dao==null)

            dao = DB.getInstance(getApplication()).stateDAO();

        return dao;

    }

    public void setDao(StateDAO dao) {

        this.dao = dao;

    }

    public LiveData<List<StateModel>> getStates(){

        if (states==null)

            states = getDao().getStates();

        return states;

    }

}