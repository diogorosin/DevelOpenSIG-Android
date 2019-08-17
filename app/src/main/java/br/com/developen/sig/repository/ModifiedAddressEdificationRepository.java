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

    private LiveData<Boolean> activeOfModifiedAddressEdification;

    private LiveData<Integer> typeOfModifiedAddressEdification;

    private LiveData<String> referenceOfModifiedAddressEdification;


    public ModifiedAddressEdificationRepository(Application application){

        super(application);

    }


    private ModifiedAddressEdificationDAO getDao() {

        if (dao==null)

            dao = DB.getInstance(getApplication()).modifiedAddressEdificationDAO();

        return dao;

    }


    public LiveData<List<ModifiedAddressEdificationModel>> getEdificationsOfModifiedAddress(Integer modifiedAddress){

        if (modifiedAddressEdifications==null)

            modifiedAddressEdifications = getDao().getEdificationsOfModifiedAddress(modifiedAddress);

        return modifiedAddressEdifications;

    }

    public LiveData<Boolean> getActiveOfModifiedAddressEdification(Integer modifiedAddress, Integer edification){

        if (activeOfModifiedAddressEdification == null)

            activeOfModifiedAddressEdification = getDao().getActiveOfModifiedAddressEdification(modifiedAddress, edification);

        return activeOfModifiedAddressEdification;

    }


    public LiveData<Integer> getTypeOfModifiedAddressEdification(Integer modifiedAddress, Integer edification){

        if (typeOfModifiedAddressEdification == null)

            typeOfModifiedAddressEdification = getDao().getTypeOfModifiedAddressEdification(modifiedAddress, edification);

        return typeOfModifiedAddressEdification;

    }


    public LiveData<String> getReferenceOfModifiedAddressEdification(Integer modifiedAddress, Integer edification){

        if (referenceOfModifiedAddressEdification == null)

            referenceOfModifiedAddressEdification = getDao().getReferenceOfModifiedAddressEdification(modifiedAddress, edification);

        return referenceOfModifiedAddressEdification;

    }


/*    public LiveData<ModifiedAddressEdificationModel> getModifiedAddressEdification(Integer modifiedAddress, Integer edification){

        if (modifiedAddressEdification==null)

            modifiedAddressEdification = getDao().getModifiedAddressEdification(modifiedAddress, edification);

        return modifiedAddressEdification;

    }


    public void validate(Integer typeOfModifiedAddressEdification, String reference){


        ModifiedAddressEdificationModel model = modifiedAddressEdification.getValue();

        ModifiedAddressEdificationVO vo = new ModifiedAddressEdificationVO();

        vo.setModifiedAddress(model.getModifiedAddress().getIdentifier());

        vo.setEdification(model.getEdification());

        vo.setType(typeOfModifiedAddressEdification);

        vo.setReference(reference);

        vo.setActive(true);

        if (getDao().exists(vo.getModifiedAddress(), vo.getEdification()))

            getDao().update(vo);


    }

    public Integer getType() {

        return typeOfModifiedAddressEdification;

    }

    public void setType(Integer typeOfModifiedAddressEdification) {

        this.typeOfModifiedAddressEdification = typeOfModifiedAddressEdification;

    }

    public String getReference() {

        return reference;

    }

    public void setReference(String reference) {

        this.reference = reference;

    } */

}