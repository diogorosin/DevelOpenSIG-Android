package br.com.developen.sig.repository;

import java.util.Date;
import java.util.Map;

import br.com.developen.sig.database.AgencyModel;
import br.com.developen.sig.database.CityModel;
import br.com.developen.sig.database.GenderModel;
import br.com.developen.sig.database.IndividualVO;
import br.com.developen.sig.database.ModifiedAddressEdificationDwellerModel;
import br.com.developen.sig.database.ModifiedAddressEdificationDwellerVO;
import br.com.developen.sig.database.StateModel;
import br.com.developen.sig.exception.CityNotFoundException;
import br.com.developen.sig.exception.DocumentNotFoundException;
import br.com.developen.sig.util.DB;

public class ModifiedAddressEdificationDwellerRepository {


    public static final int MODIFIED_ADDRESS_PROPERTY = 0;

    public static final int EDIFICATION_PROPERTY = 1;

    public static final int DWELLER_PROPERTY = 2;

    public static final int INDIVIDUAL_PROPERTY = 4;

    public static final int NAME_PROPERTY = 5;

    public static final int MOTHER_NAME_PROPERTY = 6;

    public static final int FATHER_NAME_PROPERTY = 7;

    public static final int CPF_PROPERTY = 8;

    public static final int RG_NUMBER_PROPERTY = 9;

    public static final int BIRTH_DATE_PROPERTY = 10;

    public static final int BIRTH_PLACE_PROPERTY = 11;

    public static final int RG_AGENCY_PROPERTY = 12;

    public static final int RG_STATE_PROPERTY = 13;

    public static final int GENDER_PROPERTY = 14;

    public static final int TO_PROPERTY = 15;


    private static ModifiedAddressEdificationDwellerRepository instance;

    private final DB database;


    private ModifiedAddressEdificationDwellerRepository(DB database) {

        this.database = database;

    }


    public static ModifiedAddressEdificationDwellerRepository getInstance(final DB database) {

        if (instance == null) {

            synchronized (ModifiedAddressEdificationDwellerRepository.class) {

                if (instance == null) {

                    instance = new ModifiedAddressEdificationDwellerRepository(database);

                }

            }

        }

        return instance;

    }


    public ModifiedAddressEdificationDwellerModel getModifiedAddressEdificationDweller(int modifiedAddress, int edification, int dweller) {

        return database.modifiedAddressEdificationDwellerDAO().getModifiedAddressEdificationDweller(modifiedAddress, edification, dweller);

    }


    public void create(ModifiedAddressEdificationDwellerModel modifiedAddressEdificationDwellerModel){

        ModifiedAddressEdificationDwellerVO modifiedAddressEdificationDwellerVO = new ModifiedAddressEdificationDwellerVO();

        modifiedAddressEdificationDwellerVO.setModifiedAddress(modifiedAddressEdificationDwellerModel.
                getModifiedAddressEdification().
                getModifiedAddress().
                getIdentifier());

        modifiedAddressEdificationDwellerVO.setEdification(modifiedAddressEdificationDwellerModel.
                getModifiedAddressEdification().
                getEdification());

        modifiedAddressEdificationDwellerVO.setDweller(modifiedAddressEdificationDwellerModel.
                getDweller());

        modifiedAddressEdificationDwellerVO.setIndividual(modifiedAddressEdificationDwellerModel.
                getIndividual().
                getIdentifier());

        modifiedAddressEdificationDwellerVO.setName(modifiedAddressEdificationDwellerModel.getName());

        modifiedAddressEdificationDwellerVO.setMotherName(modifiedAddressEdificationDwellerModel.getMotherName());

        modifiedAddressEdificationDwellerVO.setFatherName(modifiedAddressEdificationDwellerModel.getFatherName());

        modifiedAddressEdificationDwellerVO.setCpf(modifiedAddressEdificationDwellerModel.getCpf());

        modifiedAddressEdificationDwellerVO.setRgNumber(modifiedAddressEdificationDwellerModel.getRgNumber());

        modifiedAddressEdificationDwellerVO.setRgAgency(modifiedAddressEdificationDwellerModel.
                getRgAgency().
                getIdentifier());

        modifiedAddressEdificationDwellerVO.setRgState(modifiedAddressEdificationDwellerModel.
                getRgState().
                getIdentifier());

        modifiedAddressEdificationDwellerVO.setBirthPlace(modifiedAddressEdificationDwellerModel.
                getBirthPlace().
                getIdentifier());

        modifiedAddressEdificationDwellerVO.setBirthDate(modifiedAddressEdificationDwellerModel.
                getBirthDate());

        modifiedAddressEdificationDwellerVO.setActive(modifiedAddressEdificationDwellerModel.getActive());

        modifiedAddressEdificationDwellerVO.setGender(modifiedAddressEdificationDwellerModel.
                getGender().
                getIdentifier());

        modifiedAddressEdificationDwellerVO.setFrom(modifiedAddressEdificationDwellerModel.getFrom());

        modifiedAddressEdificationDwellerVO.setTo(modifiedAddressEdificationDwellerModel.getTo());

        database.modifiedAddressEdificationDwellerDAO().create(modifiedAddressEdificationDwellerVO);

    }


    public void save(Map<Integer, Object> values) throws CityNotFoundException, DocumentNotFoundException {

        ModifiedAddressEdificationDwellerVO vo = database.modifiedAddressEdificationDwellerDAO().
                retrieve(((Integer) values.get(MODIFIED_ADDRESS_PROPERTY)),
                        (Integer) values.get(EDIFICATION_PROPERTY),
                        (Integer) values.get(DWELLER_PROPERTY));

        vo.setName((String) values.get(NAME_PROPERTY));

        vo.setMotherName((String) values.get(MOTHER_NAME_PROPERTY));

        vo.setFatherName((String) values.get(FATHER_NAME_PROPERTY));

        if (values.get(INDIVIDUAL_PROPERTY) instanceof Integer) {

            vo.setIndividual(null);

            Integer individualValue = (Integer) values.get(INDIVIDUAL_PROPERTY);

            if (individualValue != null){

                IndividualVO individualVO = database.individualDAO().retrieve(individualValue);

                if (individualVO != null)

                    vo.setIndividual(individualVO.getIdentifier());

            }

        }

        if (values.get(BIRTH_PLACE_PROPERTY) instanceof String){

            vo.setBirthPlace(null);

            String cityValue = (String) values.get(BIRTH_PLACE_PROPERTY);

            String[] parts = cityValue.split("-");

            if (parts.length == 2){

                String city = parts[0].trim();

                String stateAcronym = parts[1].trim();

                CityModel cityModel = database.cityDAO().findByCityStateAcronym(city, stateAcronym);

                if (cityModel != null)

                    vo.setBirthPlace(cityModel.getIdentifier());

            }

        }

        if (vo.getBirthPlace() == null)

            throw new CityNotFoundException();

        vo.setCpf((Long) values.get(CPF_PROPERTY));

        vo.setBirthDate((Date) values.get(BIRTH_DATE_PROPERTY));

        vo.setRgNumber((Long) values.get(RG_NUMBER_PROPERTY));

        vo.setRgAgency(((AgencyModel) values.get(RG_AGENCY_PROPERTY)).getIdentifier());

        vo.setRgState(((StateModel) values.get(RG_STATE_PROPERTY)).getIdentifier());

        vo.setGender(((GenderModel) values.get(GENDER_PROPERTY)).getIdentifier());

        vo.setTo((Date) values.get(TO_PROPERTY));

        vo.setActive(true);

        if (vo.getCpf() == null && vo.getRgNumber() == null)

            throw new DocumentNotFoundException();

        database.getTransactionExecutor().execute(() -> {

            if (database.modifiedAddressEdificationDwellerDAO().exists(
                    (Integer) values.get(MODIFIED_ADDRESS_PROPERTY),
                    (Integer) values.get(EDIFICATION_PROPERTY),
                    (Integer) values.get(DWELLER_PROPERTY)))

                database.modifiedAddressEdificationDwellerDAO().update(vo);

        });

    }


    public void delete(Map<Integer, Object> values){

        ModifiedAddressEdificationDwellerVO vo = database.modifiedAddressEdificationDwellerDAO().
                retrieve(((Integer) values.get(MODIFIED_ADDRESS_PROPERTY)),
                        (Integer) values.get(EDIFICATION_PROPERTY),
                        (Integer) values.get(DWELLER_PROPERTY));

        vo.setActive(false);

        database.getTransactionExecutor().execute(() -> {

            if (database.modifiedAddressEdificationDwellerDAO().exists(
                    (Integer) values.get(MODIFIED_ADDRESS_PROPERTY),
                    (Integer) values.get(EDIFICATION_PROPERTY),
                    (Integer) values.get(DWELLER_PROPERTY)))

                database.modifiedAddressEdificationDwellerDAO().update(vo);

        });

    }


    public void move(Map<Integer, Object> values){

        defineTo(((Integer) values.get(MODIFIED_ADDRESS_PROPERTY)),
                (Integer) values.get(EDIFICATION_PROPERTY),
                (Integer) values.get(DWELLER_PROPERTY), new Date());

    }


    public void undoMove(Map<Integer, Object> values){

        defineTo(((Integer) values.get(MODIFIED_ADDRESS_PROPERTY)),
                (Integer) values.get(EDIFICATION_PROPERTY),
                (Integer) values.get(DWELLER_PROPERTY),
                null);

    }


    private void defineTo(Integer modifiedAddress, Integer edification, Integer dweller, Date to){

        ModifiedAddressEdificationDwellerVO vo = database.
                modifiedAddressEdificationDwellerDAO().
                retrieve(modifiedAddress, edification, dweller);

        vo.setTo(to);

        database.getTransactionExecutor().execute(() -> {

            if (database.modifiedAddressEdificationDwellerDAO().exists(
                    vo.getModifiedAddress(),
                    vo.getEdification(),
                    vo.getDweller()))

                database.modifiedAddressEdificationDwellerDAO().update(vo);

        });

    }


}