package br.com.developen.sig.repository;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.Map;

import br.com.developen.sig.database.CityModel;
import br.com.developen.sig.database.CityVO;
import br.com.developen.sig.database.LatLngModel;
import br.com.developen.sig.database.ModifiedAddressDAO;
import br.com.developen.sig.database.ModifiedAddressModel;
import br.com.developen.sig.database.ModifiedAddressVO;
import br.com.developen.sig.exception.CityNotFoundException;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.DB;

public class ModifiedAddressRepository extends AndroidViewModel {


    public static final int IDENTIFIER_PROPERTY = 0;

    public static final int DENOMINATION_PROPERTY = 1;

    public static final int NUMBER_PROPERTY = 2;

    public static final int REFERENCE_PROPERTY = 3;

    public static final int DISTRICT_PROPERTY = 4;

    public static final int POSTAL_CODE_PROPERTY = 5;

    public static final int CITY_PROPERTY = 6;


    private ModifiedAddressDAO dao;


    private LiveData<PagedList<ModifiedAddressModel>> modifiedAddressesThatWasNotSynced;

    private LiveData<String> denomination;

    private LiveData<String> number;

    private LiveData<String> reference;

    private LiveData<String> district;

    private LiveData<Integer> postalCode;

    private LiveData<CityModel> city;

    private LiveData<LatLngModel> latLng;

    private LiveData<Boolean> active;


    public ModifiedAddressRepository(Application application){

        super(application);

    }

    private ModifiedAddressDAO getDao() {

        if (dao==null)

            dao = DB.getInstance(getApplication()).modifiedAddressDAO();

        return dao;

    }

    public LiveData<PagedList<ModifiedAddressModel>> getModifiedAddressesThatWasNotSynced(){

        if (modifiedAddressesThatWasNotSynced==null){

            DataSource.Factory<Integer, ModifiedAddressModel> factory = DB.getInstance(
                    getApplication()).
                    modifiedAddressDAO().
                    getModifiedAddressesThatWasNotSynced();

            LivePagedListBuilder<Integer, ModifiedAddressModel> listBuilder =
                    new LivePagedListBuilder<>(factory, 50);

            modifiedAddressesThatWasNotSynced = listBuilder.build();

        }

        return modifiedAddressesThatWasNotSynced;

    }

    public LiveData<String> getDenomination(Integer identifier){

        if (denomination==null)

            denomination = getDao().getDenominationOfModifiedAddress(identifier);

        return denomination;

    }

    public LiveData<String> getNumber(Integer identifier){

        if (number==null)

            number = getDao().getNumberOfModifiedAddress(identifier);

        return number;

    }

    public LiveData<String> getReference(Integer identifier){

        if (reference==null)

            reference = getDao().getReferenceOfModifiedAddress(identifier);

        return reference;

    }

    public LiveData<String> getDistrict(Integer identifier){

        if (district==null)

            district = getDao().getDistrictOfModifiedAddress(identifier);

        return district;

    }

    public LiveData<Integer> getPostalCode(Integer identifier){

        if (postalCode==null)

            postalCode = getDao().getPostalCodeOfModifiedAddress(identifier);

        return postalCode;

    }

    public LiveData<CityModel> getCity(Integer identifier){

        if (city==null)

            city = getDao().getCityOfModifiedAddress(identifier);

        return city;

    }

    public LiveData<LatLngModel> getLatLng(Integer identifier){

        if (latLng==null)

            latLng = getDao().getLatLngOfModifiedAddress(identifier);

        return latLng;

    }

    public LiveData<Boolean> getActive(Integer identifier){

        if (active==null)

            active = getDao().getActiveOfModifiedAddress(identifier);

        return active;

    }


    public void save(Map<Integer, Object> values) throws CityNotFoundException {

        ModifiedAddressVO modifiedAddressVO = getDao().retrieve((Integer) values.get(IDENTIFIER_PROPERTY));

        modifiedAddressVO.setDenomination((String) values.get(DENOMINATION_PROPERTY));

        modifiedAddressVO.setNumber((String) values.get(NUMBER_PROPERTY));

        modifiedAddressVO.setDistrict((String) values.get(DISTRICT_PROPERTY));

        modifiedAddressVO.setReference((String) values.get(REFERENCE_PROPERTY));

        modifiedAddressVO.setPostalCode((Integer) values.get(POSTAL_CODE_PROPERTY));

        modifiedAddressVO.setActive(true);

        if (values.get(CITY_PROPERTY) instanceof String){

            modifiedAddressVO.setCity(null);

            String cityValue = (String) values.get(CITY_PROPERTY);

            String[] parts = cityValue.split("-");

            if (parts.length == 2){

                String city = parts[0].trim();

                String stateAcronym = parts[1].trim();

                CityVO cityVO = DB.getInstance(App.getInstance()).cityDAO().findByCityStateAcronym(city, stateAcronym);

                if (cityVO != null)

                    modifiedAddressVO.setCity(cityVO.getIdentifier());

            }

        }

        if (modifiedAddressVO.getCity() == null)

            throw new CityNotFoundException();

        if (getDao().exists(modifiedAddressVO.getIdentifier()))

            getDao().update(modifiedAddressVO);

    }


}