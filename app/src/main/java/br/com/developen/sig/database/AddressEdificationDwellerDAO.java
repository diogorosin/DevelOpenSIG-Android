package br.com.developen.sig.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.developen.sig.util.Constants;

@Dao
public interface AddressEdificationDwellerDAO {

    @Insert
    void create(AddressEdificationDwellerVO addressEdificationDwellerVO);

    @Query("SELECT AED.* " +
            "FROM AddressEdificationDweller AED " +
            "WHERE AED.address = :address AND AED.edification = :edification AND AED.dweller = :dweller")
    AddressEdificationDwellerVO retrieve(int address, Integer edification, Integer dweller);

    @Query("SELECT COUNT(*) > 0 " +
            "FROM AddressEdificationDweller AED " +
            "WHERE AED.address = :address AND AED.edification = :edification AND AED.dweller = :dweller")
    Boolean exists(int address, Integer edification, Integer dweller);

    @Update
    void update(AddressEdificationDwellerVO addressEdificationDwellerVO);

    @Delete
    void delete(AddressEdificationDwellerVO addressEdificationDwellerVO);

    @Query("SELECT " +
            " A.identifier AS 'addressEdification_address_identifier', " +
            " A.denomination AS 'addressEdification_address_denomination', " +
            " A.number AS 'addressEdification_address_number', " +
            " A.reference AS 'addressEdification_address_reference', " +
            " A.district AS 'addressEdification_address_district', " +
            " A.postalCode AS 'addressEdification_address_postalCode', " +
            " A.latitude AS 'addressEdification_address_latitude', " +
            " A.longitude AS 'addressEdification_address_longitude', " +
            " A.verifiedBy AS 'addressEdification_address_verifiedBy', " +
            " A.verifiedAt AS 'addressEdification_address_verifiedAt', " +
            " AddressCity.identifier AS 'addressEdification_address_city_identifier', " +
            " AddressCity.denomination AS 'addressEdification_address_city_denomination', " +
            " AddressCityState.identifier AS 'addressEdification_address_city_state_identifier', " +
            " AddressCityState.denomination AS 'addressEdification_address_city_state_denomination', " +
            " AddressCityState.acronym AS 'addressEdification_address_city_state_acronym', " +
            " AddressCityStateCountry.identifier AS 'addressEdification_address_city_state_country_identifier', " +
            " AddressCityStateCountry.denomination AS 'addressEdification_address_city_state_country_denomination', " +
            " AddressCityStateCountry.acronym AS 'addressEdification_address_city_state_country_acronym', " +
            " AE.edification AS 'addressEdification_edification', " +
            " AddressEdificationType.identifier AS 'addressEdification_type_identifier', " +
            " AddressEdificationType.denomination AS 'addressEdification_type_denomination', " +
            " AE.reference AS 'addressEdification_reference', " +
            " AE.'from' AS 'addressEdification_from', " +
            " AE.'to' AS 'addressEdification_to', " +
            " AED.dweller AS 'dweller', " +
            " I.identifier AS 'individual_identifier', " +
            " I.active AS 'individual_active', " +
            " I.name AS 'individual_name', " +
            " I.motherName AS 'individual_motherName', " +
            " I.fatherName AS 'individual_fatherName', " +
            " I.cpf AS 'individual_cpf', " +
            " I.birthDate AS 'individual_birthDate', " +
            " I.gender AS 'individual_gender_identifier', " +
            " CASE WHEN I.gender = 'M' THEN '" + Constants.MALE_GENDER_DENOMINATION + "' ELSE '" + Constants.FEMALE_GENDER_DENOMINATION + "' END AS 'individual_gender_denomination', " +
            " AED.'from' AS 'from', " +
            " AED.'to' AS 'to' " +
            "FROM AddressEdificationDweller AED " +
            "INNER JOIN AddressEdification AE ON AE.address = AED.address AND AE.edification = AED.edification " +
            "INNER JOIN Address A ON A.identifier = AE.address " +
            "INNER JOIN Type AddressEdificationType ON AddressEdificationType.identifier = AE.type " +
            "INNER JOIN City AddressCity ON AddressCity.identifier = A.city " +
            "INNER JOIN State AddressCityState ON AddressCityState.identifier = AddressCity.state " +
            "INNER JOIN Country AddressCityStateCountry ON AddressCityStateCountry.identifier = AddressCityState.country " +
            "INNER JOIN Individual I ON I.identifier = AED.individual " +
            "WHERE I.name LIKE :name AND AED.'to' IS NULL")
    List<AddressEdificationDwellerModel> findByIndividualName(String name);


}