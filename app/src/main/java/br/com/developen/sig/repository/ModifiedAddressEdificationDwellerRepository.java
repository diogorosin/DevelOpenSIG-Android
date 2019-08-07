package br.com.developen.sig.repository;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import br.com.developen.sig.database.CityVO;
import br.com.developen.sig.database.ModifiedAddressEdificationDwellerDAO;
import br.com.developen.sig.database.ModifiedAddressEdificationDwellerModel;
import br.com.developen.sig.database.ModifiedAddressEdificationDwellerVO;
import br.com.developen.sig.exception.CityNotFoundException;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.DB;

public class ModifiedAddressEdificationDwellerRepository extends AndroidViewModel {


    private ModifiedAddressEdificationDwellerDAO dao;

    private LiveData<List<ModifiedAddressEdificationDwellerModel>> modifiedAddressEdificationDwellers;

    private LiveData<ModifiedAddressEdificationDwellerModel> modifiedAddressEdificationDweller;


    public ModifiedAddressEdificationDwellerRepository(Application application){

        super(application);

    }


    private ModifiedAddressEdificationDwellerDAO getDao() {

        if (dao==null)

            dao = DB.getInstance(getApplication()).modifiedAddressEdificationDwellerDAO();

        return dao;

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


    public void saveAsIndividual(String name,
                     String motherName,
                     String fatherName,
                     Long cpf,
                     Long rgNumber,
                     Integer rgAgency,
                     Integer rgState,
                     String birthPlace,
                     Date birthDate,
                     String gender) throws CityNotFoundException {


        ModifiedAddressEdificationDwellerModel model = modifiedAddressEdificationDweller.getValue();

        ModifiedAddressEdificationDwellerVO vo = new ModifiedAddressEdificationDwellerVO();


        vo.setModifiedAddress(model.getModifiedAddressEdification().getModifiedAddress().getIdentifier());

        vo.setEdification(model.getModifiedAddressEdification().getEdification());

        vo.setDweller(model.getDweller());

        vo.setSubject(model.getSubject());

        vo.setFrom(model.getFrom());

        vo.setActive(true);


        vo.setType("F");

        vo.setNameOrDenomination(name);

        vo.setMotherName(motherName);

        vo.setFatherName(fatherName);

        vo.setCpf(cpf);

        vo.setRgNumber(rgNumber);

        vo.setRgAgency(rgAgency);

        vo.setRgState(rgState);


        String[] parts = birthPlace.split("-");

        if (parts.length == 2){

            String city = parts[0].trim();

            String stateAcronym = parts[1].trim();

            CityVO cityVO = DB.getInstance(App.getInstance()).cityDAO().findByCityStateAcronym(city, stateAcronym);

            if (cityVO != null)

                vo.setBirthPlace(cityVO.getIdentifier());

        }

        if (vo.getBirthPlace() == null)

            throw new CityNotFoundException();


        vo.setBirthDate(birthDate);

        vo.setGender(gender);


        if (getDao().exists(vo.getModifiedAddress(), vo.getEdification(), vo.getDweller()))

            getDao().update(vo);


    }


}