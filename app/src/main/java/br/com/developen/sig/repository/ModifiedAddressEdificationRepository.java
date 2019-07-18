package br.com.developen.sig.repository;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import br.com.developen.sig.database.ModifiedAddressEdificationDAO;
import br.com.developen.sig.database.ModifiedAddressEdificationModel;
import br.com.developen.sig.util.DB;

public class ModifiedAddressEdificationRepository extends AndroidViewModel {

    private ModifiedAddressEdificationDAO dao;

    private LiveData<List<ModifiedAddressEdificationModel>> modifiedAddressEdifications;

    private LiveData<ModifiedAddressEdificationModel> modifiedAddressEdification;

    public ModifiedAddressEdificationRepository(Application application){

        super(application);

    }

    public ModifiedAddressEdificationDAO getDao() {

        if (dao==null)

            dao = DB.getInstance(getApplication()).modifiedAddressEdificationDAO();

        return dao;

    }

    public void setDao(ModifiedAddressEdificationDAO dao) {

        this.dao = dao;

    }

    public LiveData<List<ModifiedAddressEdificationModel>> getEdificationsOfModifiedAddress(Integer modifiedAddress){

        if (modifiedAddressEdifications==null)

            modifiedAddressEdifications = getDao().getEdificationsOfModifiedAddress(modifiedAddress);

        return modifiedAddressEdifications;

    }

    public LiveData<ModifiedAddressEdificationModel> getModifiedAddressEdification(Integer modifiedAddress, Integer edification){

        if (modifiedAddressEdification==null)

            modifiedAddressEdification = getDao().getModifiedAddressEdification(modifiedAddress, edification);

        return modifiedAddressEdification;

    }

}