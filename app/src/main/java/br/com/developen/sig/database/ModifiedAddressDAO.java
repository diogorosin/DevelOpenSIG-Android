package br.com.developen.sig.database;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ModifiedAddressDAO {

    @Insert
    Long create(ModifiedAddressVO modifiedlAddressVO);

    @Query("SELECT MA.* FROM ModifiedAddress MA WHERE MA.identifier = :identifier")
    ModifiedAddressVO retrieve(int identifier);

    @Query("SELECT COUNT(*) > 0 FROM ModifiedAddress MA WHERE MA.identifier = :identifier")
    Boolean exists(int identifier);

    @Query("SELECT COUNT(*) FROM ModifiedAddress MA")
    Integer count();

    @Update
    void update(ModifiedAddressVO modifiedAddressVO);

    @Delete
    void delete(ModifiedAddressVO modifiedAddressVO);

    @Query("SELECT " +
            " MA.identifier AS 'identifier', " +
            " MA.syncedAt AS 'syncedAt', " +
            " MA.modifiedAt AS 'modifiedAt', " +
            " MA.modifiedBy AS 'modifiedBy', " +
            " MA.address AS 'address', " +
            " MA.denomination AS 'denomination', " +
            " MA.number AS 'number', " +
            " MA.reference AS 'reference', " +
            " MA.district AS 'district', " +
            " MA.postalCode AS 'postalCode', " +
            " MA.latitude AS 'latLng_latitude', " +
            " MA.longitude AS 'latLng_longitude', " +
            " MA.active AS 'active', " +
            " C.identifier AS 'city_identifier', " +
            " C.denomination AS 'city_denomination', " +
            " S.identifier AS 'city_state_identifier', " +
            " S.denomination AS 'city_state_denomination', " +
            " S.acronym AS 'city_state_acronym', " +
            " Co.identifier AS 'city_state_country_identifier', " +
            " Co.denomination AS 'city_state_country_denomination', " +
            " Co.acronym AS 'city_state_country_acronym' " +
            "FROM ModifiedAddress MA " +
            "LEFT OUTER JOIN City C ON C.identifier = MA.city " +
            "LEFT OUTER JOIN State S ON S.identifier = C.state " +
            "LEFT OUTER JOIN Country Co ON Co.identifier = S.country " +
            "WHERE MA.syncedAt IS NULL AND MA.active = 1 " +
            "ORDER BY MA.identifier DESC")
    DataSource.Factory<Integer, ModifiedAddressModel> getModifiedAddressesThatWasNotSynced();

    @Query("SELECT " +
            " MA.identifier AS 'identifier', " +
            " MA.syncedAt AS 'syncedAt', " +
            " MA.modifiedAt AS 'modifiedAt', " +
            " MA.modifiedBy AS 'modifiedBy', " +
            " MA.address AS 'address', " +
            " MA.denomination AS 'denomination', " +
            " MA.number AS 'number', " +
            " MA.reference AS 'reference', " +
            " MA.district AS 'district', " +
            " MA.postalCode AS 'postalCode', " +
            " MA.latitude AS 'latLng_latitude', " +
            " MA.longitude AS 'latLng_longitude', " +
            " MA.active AS 'active', " +
            " C.identifier AS 'city_identifier', " +
            " C.denomination AS 'city_denomination', " +
            " S.identifier AS 'city_state_identifier', " +
            " S.denomination AS 'city_state_denomination', " +
            " S.acronym AS 'city_state_acronym', " +
            " Co.identifier AS 'city_state_country_identifier', " +
            " Co.denomination AS 'city_state_country_denomination', " +
            " Co.acronym AS 'city_state_country_acronym' " +
            "FROM ModifiedAddress MA " +
            "LEFT OUTER JOIN City C ON C.identifier = MA.city " +
            "LEFT OUTER JOIN State S ON S.identifier = C.state " +
            "LEFT OUTER JOIN Country Co ON Co.identifier = S.country " +
            "WHERE MA.identifier = :identifier")
    ModifiedAddressModel getModifiedAddress(int identifier);

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
            " MA.latitude AS 'modifiedAddress_latLng_latitude', " +
            " MA.longitude AS 'modifiedAddress_latLng_longitude', " +
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
            "(SELECT MAED2.name FROM ModifiedAddressEdificationDweller MAED2 WHERE MAED2.modifiedAddress = MA.identifier AND MAED2.edification = MAE.edification AND MAED2.active = 1 LIMIT 1) AS 'firstDwellerName', " +
            "(SELECT COUNT(*) FROM ModifiedAddressEdificationDweller MAED3 WHERE MAED3.modifiedAddress = MA.identifier AND MAED3.edification = MAE.edification AND MAED3.active = 1) AS 'dwellersCount' " +
            "FROM ModifiedAddressEdification MAE " +
            "INNER JOIN ModifiedAddress MA ON MA.identifier = MAE.modifiedAddress " +
            "LEFT OUTER JOIN Type ModifiedAddressEdificationType ON MAE.type = ModifiedAddressEdificationType.identifier " +
            "LEFT OUTER JOIN City ModifiedAddressCity ON ModifiedAddressCity.identifier = MA.city " +
            "LEFT OUTER JOIN State ModifiedAddressCityState ON ModifiedAddressCityState.identifier = ModifiedAddressCity.state " +
            "LEFT OUTER JOIN Country ModifiedAddressCityStateCountry ON ModifiedAddressCityStateCountry.identifier = ModifiedAddressCityState.country " +
            "WHERE MA.identifier = :modifiedAddress AND MAE.active = 1 " +
            "ORDER BY MAE.reference, MAE.type")
    LiveData<List<ModifiedAddressEdificationModel>> getEdificationsOfModifiedAddress(Integer modifiedAddress);


}