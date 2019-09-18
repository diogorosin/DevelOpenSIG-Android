package br.com.developen.sig.repository;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.developen.sig.database.ModifiedAddressEdificationDwellerModel;
import br.com.developen.sig.database.ModifiedAddressEdificationModel;
import br.com.developen.sig.database.ModifiedAddressEdificationVO;
import br.com.developen.sig.database.TypeModel;
import br.com.developen.sig.util.DB;

public class ModifiedAddressEdificationRepository {


    public static final int MODIFIED_ADDRESS_PROPERTY = 0;

    public static final int EDIFICATION_PROPERTY = 1;

    public static final int TYPE_PROPERTY = 2;

    public static final int REFERENCE_PROPERTY = 3;


    private static ModifiedAddressEdificationRepository instance;

    private final DB database;


    private ModifiedAddressEdificationRepository(DB database) {

        this.database = database;

    }


    public static ModifiedAddressEdificationRepository getInstance(final DB database) {

        if (instance == null) {

            synchronized (ModifiedAddressEdificationRepository.class) {

                if (instance == null) {

                    instance = new ModifiedAddressEdificationRepository(database);

                }

            }

        }

        return instance;

    }


    public LiveData<List<ModifiedAddressEdificationDwellerModel>> getDwellersOfModifiedAddressEdification(int modifiedAddress, int edification) {

        return database.modifiedAddressEdificationDAO().getDwellersOfModifiedAddressEdification(modifiedAddress, edification);

    }


    public List<ModifiedAddressEdificationDwellerModel> getDwellersOfModifiedAddressEdificationAsList(int modifiedAddress, int edification) {

        return database.modifiedAddressEdificationDAO().getDwellersOfModifiedAddressEdificationAsList(modifiedAddress, edification);

    }


    public ModifiedAddressEdificationModel getModifiedAddressEdification(int modifiedAddress, int edification) {

        return database.modifiedAddressEdificationDAO().getModifiedAddressEdification(modifiedAddress, edification);

    }


    public void save(Map<Integer, Object> values) {

        ModifiedAddressEdificationVO vo = database.modifiedAddressEdificationDAO().
                retrieve(((Integer) values.get(MODIFIED_ADDRESS_PROPERTY)),
                        (Integer) values.get(EDIFICATION_PROPERTY));

        vo.setType(((TypeModel) values.get(TYPE_PROPERTY)).getIdentifier());

        vo.setReference((String) values.get(REFERENCE_PROPERTY));

        vo.setActive(true);

        database.getTransactionExecutor().execute(() -> {

            if (database.modifiedAddressEdificationDAO().exists(
                    (Integer) values.get(MODIFIED_ADDRESS_PROPERTY),
                    (Integer) values.get(EDIFICATION_PROPERTY)))

                database.modifiedAddressEdificationDAO().update(vo);

        });

    }


    public void delete(Map<Integer, Object> values){

        ModifiedAddressEdificationVO vo = database.modifiedAddressEdificationDAO().
                retrieve(((Integer) values.get(MODIFIED_ADDRESS_PROPERTY)),
                        (Integer) values.get(EDIFICATION_PROPERTY));

        vo.setActive(false);

        database.getTransactionExecutor().execute(() -> {

            if (database.modifiedAddressEdificationDAO().exists(
                    (Integer) values.get(MODIFIED_ADDRESS_PROPERTY),
                    (Integer) values.get(EDIFICATION_PROPERTY)))

                database.modifiedAddressEdificationDAO().update(vo);

        });

    }


    public void demolish(Map<Integer, Object> values){

        ModifiedAddressEdificationVO vo = database.modifiedAddressEdificationDAO().
                retrieve(((Integer) values.get(MODIFIED_ADDRESS_PROPERTY)),
                        (Integer) values.get(EDIFICATION_PROPERTY));

        vo.setTo(new Date());

        database.getTransactionExecutor().execute(() -> {

            if (database.modifiedAddressEdificationDAO().exists(
                    (Integer) values.get(MODIFIED_ADDRESS_PROPERTY),
                    (Integer) values.get(EDIFICATION_PROPERTY)))

                database.modifiedAddressEdificationDAO().update(vo);

        });

    }


}