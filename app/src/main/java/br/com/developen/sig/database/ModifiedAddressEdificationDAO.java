package br.com.developen.sig.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.developen.sig.util.Constants;

@Dao
public interface ModifiedAddressEdificationDAO {

    String DWELLERS_OF_MODIFIED_ADDRESS_EDIFICATION = "SELECT " +
            " MA.identifier AS 'modifiedAddressEdification_modifiedAddress_identifier', " +
            " MA.syncedAt AS 'modifiedAddressEdification_modifiedAddress_syncedAt', " +
            " MA.address AS 'modifiedAddressEdification_modifiedAddress_address', " +
            " MA.denomination AS 'modifiedAddressEdification_modifiedAddress_denomination', " +
            " MA.number AS 'modifiedAddressEdification_modifiedAddress_number', " +
            " MA.reference AS 'modifiedAddressEdification_modifiedAddress_reference', " +
            " MA.district AS 'modifiedAddressEdification_modifiedAddress_district', " +
            " MA.postalCode AS 'modifiedAddressEdification_modifiedAddress_postalCode', " +
            " MA.latitude AS 'modifiedAddressEdification_modifiedAddress_latitude', " +
            " MA.longitude AS 'modifiedAddressEdification_modifiedAddress_longitude', " +
            " MA.modifiedAt AS 'modifiedAddressEdification_modifiedAddress_modifiedAt', " +
            " MA.modifiedBy AS 'modifiedAddressEdification_modifiedAddress_modifiedBy', " +
            " MA.active AS 'modifiedAddressEdification_modifiedAddress_active', " +
            " ModifiedAddressCity.identifier AS 'modifiedAddressEdification_modifiedAddress_city_identifier', " +
            " ModifiedAddressCity.denomination AS 'modifiedAddressEdification_modifiedAddress_city_denomination', " +
            " ModifiedAddressCityState.identifier AS 'modifiedAddressEdification_modifiedAddress_city_state_identifier', " +
            " ModifiedAddressCityState.denomination AS 'modifiedAddressEdification_modifiedAddress_city_state_denomination', " +
            " ModifiedAddressCityState.acronym AS 'modifiedAddressEdification_modifiedAddress_city_state_acronym', " +
            " ModifiedAddressCityStateCountry.identifier AS 'modifiedAddressEdification_modifiedAddress_city_state_country_identifier', " +
            " ModifiedAddressCityStateCountry.denomination AS 'modifiedAddressEdification_modifiedAddress_city_state_country_denomination', " +
            " ModifiedAddressCityStateCountry.acronym AS 'modifiedAddressEdification_modifiedAddress_city_state_country_acronym', " +
            " MAE.edification AS 'modifiedAddressEdification_edification', " +
            " ModifiedAddressEdificationType.identifier AS 'modifiedAddressEdification_type_identifier', " +
            " ModifiedAddressEdificationType.denomination AS 'modifiedAddressEdification_type_denomination', " +
            " MAE.reference AS 'modifiedAddressEdification_reference', " +
            " MAE.'from' AS 'modifiedAddressEdification_from', " +
            " MAE.'to' AS 'modifiedAddressEdification_to', " +
            " MAE.active AS 'modifiedAddressEdification_active', " +
            " MAED.dweller AS 'dweller', " +
            " I.identifier AS 'individual_identifier', " +
            " MAED.name AS 'name', " +
            " MAED.motherName AS 'motherName', " +
            " MAED.fatherName AS 'fatherName', " +
            " MAED.cpf AS 'cpf', " +
            " MAED.rgNumber AS 'rgNumber', " +
            " RgAgency.identifier AS 'rgAgency_identifier', " +
            " RgAgency.denomination AS 'rgAgency_denomination', " +
            " RgAgency.acronym AS 'rgAgency_acronym', " +
            " RgState.identifier AS 'rgState_identifier', " +
            " RgState.denomination AS 'rgState_denomination', " +
            " RgState.acronym AS 'rgState_acronym', " +
            " RgStateCountry.identifier AS 'rgState_country_identifier', " +
            " RgStateCountry.denomination AS 'rgState_country_denomination', " +
            " RgStateCountry.acronym AS 'rgState_country_acronym', " +
            " BirthPlace.identifier AS 'birthPlace_identifier', " +
            " BirthPlace.denomination AS 'birthPlace_denomination', " +
            " BirthPlaceState.identifier AS 'birthPlace_state_identifier', " +
            " BirthPlaceState.denomination AS 'birthPlace_state_denomination', " +
            " BirthPlaceState.acronym AS 'birthPlace_state_acronym', " +
            " BirthPlaceStateCountry.identifier AS 'birthPlace_state_country_identifier', " +
            " BirthPlaceStateCountry.denomination AS 'birthPlace_state_country_denomination', " +
            " BirthPlaceStateCountry.acronym AS 'birthPlace_state_country_acronym', " +
            " MAED.birthDate AS 'birthDate', " +
            " MAED.gender AS 'gender_identifier', " +
            " CASE WHEN MAED.gender = 'M' THEN '" + Constants.MALE_GENDER_DENOMINATION + "' ELSE '" + Constants.FEMALE_GENDER_DENOMINATION + "' END AS 'gender_denomination', " +
            " MAED.gender AS 'gender', " +
            " MAED.'from' AS 'from', " +
            " MAED.'to' AS 'to', " +
            " MAED.active AS 'active' " +
            "FROM ModifiedAddressEdificationDweller MAED " +
            "INNER JOIN ModifiedAddressEdification MAE ON MAE.modifiedAddress = MAED.modifiedAddress AND MAE.edification = MAED.edification " +
            "INNER JOIN ModifiedAddress MA ON MA.identifier = MAE.modifiedAddress " +
            "LEFT OUTER JOIN Individual I ON I.identifier = MAED.individual " +
            "LEFT OUTER JOIN Type ModifiedAddressEdificationType ON MAE.type = ModifiedAddressEdificationType.identifier " +
            "LEFT OUTER JOIN City ModifiedAddressCity ON ModifiedAddressCity.identifier = MA.city " +
            "LEFT OUTER JOIN State ModifiedAddressCityState ON ModifiedAddressCityState.identifier = ModifiedAddressCity.state " +
            "LEFT OUTER JOIN Country ModifiedAddressCityStateCountry ON ModifiedAddressCityStateCountry.identifier = ModifiedAddressCityState.country " +
            "LEFT OUTER JOIN Agency RgAgency ON RgAgency.identifier = MAED.rgAgency " +
            "LEFT OUTER JOIN State RgState ON RgState.identifier = MAED.rgState " +
            "LEFT OUTER JOIN Country RgStateCountry ON RgStateCountry.identifier = RgState.country " +
            "LEFT OUTER JOIN City BirthPlace ON BirthPlace.identifier = MAED.birthPlace " +
            "LEFT OUTER JOIN State BirthPlaceState ON BirthPlaceState.identifier = BirthPlace.state " +
            "LEFT OUTER JOIN Country BirthPlaceStateCountry ON BirthPlaceStateCountry.identifier = BirthPlaceState.country " +
            "WHERE MAE.modifiedAddress = :modifiedAddress AND MAE.edification = :edification AND MAED.active = 1";

    @Insert
    Long create(ModifiedAddressEdificationVO modifiedAddressEdificationVO);

    @Query("SELECT MAE.* FROM ModifiedAddressEdification MAE WHERE MAE.modifiedAddress = :modifiedAddress AND MAE.edification = :edification")
    ModifiedAddressEdificationVO retrieve(Integer modifiedAddress, Integer edification);

    @Query("SELECT COUNT(*) > 0 FROM ModifiedAddressEdification MAE WHERE MAE.modifiedAddress = :modifiedAddress AND MAE.edification = :edification")
    Boolean exists(Integer modifiedAddress, Integer edification);

    @Query("SELECT COUNT(*) FROM ModifiedAddressEdification MAE")
    Integer count();

    @Update
    void update(ModifiedAddressEdificationVO modifiedAddressEdificationVO);

    @Delete
    void delete(ModifiedAddressEdificationVO modifiedAddressEdificationVO);

    @Query("SELECT IFNULL(MAX(MAE.edification), 0) " +
            "FROM ModifiedAddressEdification MAE " +
            "WHERE MAE.modifiedAddress = :modifiedAddress")
    Integer retrieveLastEdificationIdOfModifiedAddress(Integer modifiedAddress);

    @Query(DWELLERS_OF_MODIFIED_ADDRESS_EDIFICATION)
    LiveData<List<ModifiedAddressEdificationDwellerModel>> getDwellersOfModifiedAddressEdification(Integer modifiedAddress, Integer edification);

    @Query(DWELLERS_OF_MODIFIED_ADDRESS_EDIFICATION)
    List<ModifiedAddressEdificationDwellerModel> getDwellersOfModifiedAddressEdificationAsList(Integer modifiedAddress, Integer edification);

    @Query("SELECT " +
            " MA.identifier AS 'modifiedAddress_identifier', " +
            " MA.syncedAt AS 'modifiedAddress_syncedAt', " +
            " MA.modifiedAt AS 'modifiedAddress_modifiedAt', " +
            " MA.modifiedBy AS 'modifiedAddress_modifiedBy', " +
            " MA.address AS 'modifiedAddress_address', " +
            " MA.denomination AS 'modifiedAddress_denomination', " +
            " MA.number AS 'modifiedAddress_number', " +
            " MA.reference AS 'modifiedAddress_reference', " +
            " MA.district AS 'modifiedAddress_district', " +
            " MA.postalCode AS 'modifiedAddress_postalCode', " +
            " MA.latitude AS 'modifiedAddress_latitude', " +
            " MA.longitude AS 'modifiedAddress_longitude', " +
            " MA.active AS 'modifiedAddress_active', " +
            " ModifiedAddressCity.identifier AS 'modifiedAddress_city_identifier', " +
            " ModifiedAddressCity.denomination AS 'modifiedAddress_city_denomination', " +
            " ModifiedAddressCityState.identifier AS 'modifiedAddress_city_state_identifier', " +
            " ModifiedAddressCityState.denomination AS 'modifiedAddress_city_state_denomination', " +
            " ModifiedAddressCityState.acronym AS 'modifiedAddress_city_state_acronym', " +
            " ModifiedAddressCityStateCountry.identifier AS 'modifiedAddress_city_state_country_identifier', " +
            " ModifiedAddressCityStateCountry.denomination AS 'modifiedAddress_city_state_country_denomination', " +
            " ModifiedAddressCityStateCountry.acronym AS 'modifiedAddress_city_state_country_acronym', " +
            " MAE.edification AS 'edification', " +
            " ModifiedAddressEdificationType.identifier AS 'type_identifier', " +
            " ModifiedAddressEdificationType.denomination AS 'type_denomination', " +
            " MAE.reference AS 'reference', " +
            " MAE.'from' AS 'from', " +
            " MAE.'to' AS 'to', " +
            " MAE.active AS 'active', " +
            "(SELECT COUNT(*) FROM ModifiedAddressEdificationDweller MAED2 WHERE MAED2.modifiedAddress = MA.identifier AND MAED2.edification = MAE.edification AND MAED2.active = 1) AS 'dwellersCount' " +
            "FROM ModifiedAddressEdification MAE " +
            "INNER JOIN ModifiedAddress MA ON MA.identifier = MAE.modifiedAddress " +
            "LEFT OUTER JOIN Type ModifiedAddressEdificationType ON MAE.type = ModifiedAddressEdificationType.identifier " +
            "LEFT OUTER JOIN City ModifiedAddressCity ON ModifiedAddressCity.identifier = MA.city " +
            "LEFT OUTER JOIN State ModifiedAddressCityState ON ModifiedAddressCityState.identifier = ModifiedAddressCity.state " +
            "LEFT OUTER JOIN Country ModifiedAddressCityStateCountry ON ModifiedAddressCityStateCountry.identifier = ModifiedAddressCityState.country " +
            "WHERE MAE.modifiedAddress = :modifiedAddress AND MAE.edification = :edification")
    ModifiedAddressEdificationModel getModifiedAddressEdification(Integer modifiedAddress, Integer edification);

    @Query("SELECT COUNT(*) = 0 " +
            "FROM ModifiedAddressEdificationDweller MAED " +
            "INNER JOIN ModifiedAddressEdification MAE ON MAE.modifiedAddress = MAED.modifiedAddress AND MAE.edification = MAED.edification " +
            "WHERE " +
            "MAE.modifiedAddress = :modifiedAddress AND " +
            "MAE.edification = :edification AND " +
            "MAED.active = 1 AND " +
            "MAED.`to` IS NULL")
    Boolean canBeDemolished(Integer modifiedAddress, Integer edification);


}