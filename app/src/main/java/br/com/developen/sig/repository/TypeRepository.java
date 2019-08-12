package br.com.developen.sig.repository;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import br.com.developen.sig.database.TypeDAO;
import br.com.developen.sig.database.TypeModel;
import br.com.developen.sig.util.DB;

public class TypeRepository extends AndroidViewModel {

    private TypeDAO dao;

    private LiveData<List<TypeModel>> types;

    public TypeRepository(Application application){

        super(application);

    }

    public TypeDAO getDao() {

        if (dao==null)

            dao = DB.getInstance(getApplication()).typeDAO();

        return dao;

    }

    public void setDao(TypeDAO dao) {

        this.dao = dao;

    }

    public LiveData<List<TypeModel>> getTypes(){

        if (types==null)

            types = getDao().getList();

        return types;

    }

}