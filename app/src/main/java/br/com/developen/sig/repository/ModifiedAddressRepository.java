package br.com.developen.sig.repository;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.List;
import java.util.Map;

import br.com.developen.sig.database.CityModel;
import br.com.developen.sig.database.LatLngModel;
import br.com.developen.sig.database.ModifiedAddressEdificationModel;
import br.com.developen.sig.database.ModifiedAddressModel;
import br.com.developen.sig.database.ModifiedAddressVO;
import br.com.developen.sig.exception.CityNotFoundException;
import br.com.developen.sig.util.DB;
import br.com.developen.sig.util.StringUtils;

public class ModifiedAddressRepository {


    public static final int IDENTIFIER_PROPERTY = 0;

    public static final int DENOMINATION_PROPERTY = 1;

    public static final int NUMBER_PROPERTY = 2;

    public static final int REFERENCE_PROPERTY = 3;

    public static final int DISTRICT_PROPERTY = 4;

    public static final int POSTAL_CODE_PROPERTY = 5;

    public static final int CITY_PROPERTY = 6;

    public static final int LATLON_PROPERTY = 7;


    private static ModifiedAddressRepository instance;

    private final DB database;


    private ModifiedAddressRepository(DB database) {

        this.database = database;

    }


    public static ModifiedAddressRepository getInstance(final DB database) {

        if (instance == null) {

            synchronized (ModifiedAddressRepository.class) {

                if (instance == null) {

                    instance = new ModifiedAddressRepository(database);

                }

            }

        }

        return instance;

    }


    public void save(Map<Integer, Object> values) throws CityNotFoundException {

        ModifiedAddressVO vo = database.modifiedAddressDAO().retrieve(((Integer) values.get(IDENTIFIER_PROPERTY)));

        vo.setDenomination((String) values.get(DENOMINATION_PROPERTY));

        vo.setNumber((String) values.get(NUMBER_PROPERTY));

        vo.setReference((String) values.get(REFERENCE_PROPERTY));

        vo.setDistrict((String) values.get(DISTRICT_PROPERTY));

        vo.setPostalCode(StringUtils.parsePostalCode((String) values.get(POSTAL_CODE_PROPERTY)));

        if (values.get(CITY_PROPERTY) instanceof String){

            vo.setCity(null);

            String cityValue = (String) values.get(CITY_PROPERTY);

            String[] parts = cityValue.split("-");

            if (parts.length == 2){

                String city = parts[0].trim();

                String stateAcronym = parts[1].trim();

                CityModel cityModel = database.cityDAO().findByCityStateAcronym(city, stateAcronym);

                if (cityModel != null)

                    vo.setCity(cityModel.getIdentifier());

            }

        }

        if (vo.getCity() == null)

            throw new CityNotFoundException();

        vo.setLatitude(((LatLngModel) values.get(LATLON_PROPERTY)).getLatitude());

        vo.setLongitude(((LatLngModel) values.get(LATLON_PROPERTY)).getLongitude());

        vo.setActive(true);

        database.getTransactionExecutor().execute(() -> {

            if (database.modifiedAddressDAO().exists((Integer) values.get(IDENTIFIER_PROPERTY)))

                database.modifiedAddressDAO().update(vo);

        });

    }


    public void delete(Map<Integer, Object> values){

        ModifiedAddressVO vo = database.modifiedAddressDAO().retrieve(((Integer) values.get(IDENTIFIER_PROPERTY)));

        vo.setActive(false);

        database.getTransactionExecutor().execute(() -> {

            if (database.modifiedAddressDAO().exists((Integer) values.get(IDENTIFIER_PROPERTY)))

                database.modifiedAddressDAO().update(vo);

        });

    }


    public ModifiedAddressModel getModifiedAddress(int modifiedAddress) {

        return database.modifiedAddressDAO().getModifiedAddress(modifiedAddress);

    }


    public LiveData<List<ModifiedAddressEdificationModel>> getEdificationsOfModifiedAddress(int modifiedAddress) {

        return database.modifiedAddressDAO().getEdificationsOfModifiedAddress(modifiedAddress);

    }


    public LiveData<PagedList<ModifiedAddressModel>> getModifiedAddressesThatWasNotSynced(){

        DataSource.Factory<Integer, ModifiedAddressModel> factory = database.modifiedAddressDAO().getModifiedAddressesThatWasNotSynced();

        LivePagedListBuilder<Integer, ModifiedAddressModel> listBuilder = new LivePagedListBuilder<>(factory, 50);

        return listBuilder.build();

    }


}