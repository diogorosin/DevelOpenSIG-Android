package br.com.developen.sig.repository;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import br.com.developen.sig.database.ModifiedAddressEdificationDwellerDAO;
import br.com.developen.sig.database.ModifiedAddressEdificationDwellerModel;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.DB;

public class ModifiedAddressEdificationDwellerRepository extends AndroidViewModel {


    private ModifiedAddressEdificationDwellerDAO dao;

    private LiveData<List<ModifiedAddressEdificationDwellerModel>> modifiedAddressEdificationDwellers;

    private LiveData<ModifiedAddressEdificationDwellerModel> modifiedAddressEdificationDweller;


    public ModifiedAddressEdificationDwellerRepository(Application application){

        super(application);

    }


    public ModifiedAddressEdificationDwellerDAO getDao() {

        if (dao==null)

            dao = DB.getInstance(getApplication()).modifiedAddressEdificationDwellerDAO();

        return dao;

    }


    public void setDao(ModifiedAddressEdificationDwellerDAO dao) {

        this.dao = dao;

    }


    public LiveData<List<ModifiedAddressEdificationDwellerModel>> getDwellersOfModifiedAddressEdification(Integer modifiedAddress, Integer edification){

        if (modifiedAddressEdificationDwellers ==null)

            modifiedAddressEdificationDwellers = getDao().getDwellersOfModifiedAddressEdification(modifiedAddress, edification);

        return modifiedAddressEdificationDwellers;

    }


    public LiveData<ModifiedAddressEdificationDwellerModel> getModifiedAddressEdificationDweller(Integer modifiedAddress, Integer edification, Integer dweller){

        if (modifiedAddressEdificationDweller ==null)

            modifiedAddressEdificationDweller = getDao().getModifiedAddressEdificationDweller(modifiedAddress, edification, dweller);

        return modifiedAddressEdificationDweller;

    }


}