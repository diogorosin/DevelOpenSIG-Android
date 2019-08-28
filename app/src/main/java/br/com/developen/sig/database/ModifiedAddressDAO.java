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
            " MA.latitude AS 'latitude', " +
            " MA.longitude AS 'longitude', " +
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
            " A.identifier AS 'identifier', " +
            " A.denomination AS 'denomination', " +
            " A.number AS 'number', " +
            " A.reference AS 'reference', " +
            " A.district AS 'district', " +
            " A.postalCode AS 'postalCode', " +
            " A.latitude AS 'latitude', " +
            " A.longitude AS 'longitude', " +
            " City.identifier AS 'city_identifier', " +
            " City.denomination AS 'city_denomination', " +
            " CityState.identifier AS 'city_state_identifier', " +
            " CityState.denomination AS 'city_state_denomination', " +
            " CityState.acronym AS 'city_state_acronym', " +
            " CityStateCountry.identifier AS 'city_state_country_identifier', " +
            " CityStateCountry.denomination AS 'city_state_country_denomination', " +
            " CityStateCountry.acronym AS 'city_state_country_acronym', " +
            " A.verifiedBy AS 'verifiedBy', " +
            " A.verifiedAt AS 'verifiedAt' " +
            "FROM ModifiedAddress MA " +
            "INNER JOIN Address A ON A.identifier = MA.address " +
            "INNER JOIN City City ON City.identifier = A.city " +
            "INNER JOIN State CityState ON CityState.identifier = City.state " +
            "INNER JOIN Country CityStateCountry ON CityStateCountry.identifier = CityState.country " +
            "WHERE MA.identifier = :identifier")
    LiveData<AddressModel> getAddressOfModifiedAddress(Integer identifier);

    @Query("SELECT " +
            "MA.denomination AS 'denomination' " +
            "FROM ModifiedAddress MA " +
            "WHERE MA.identifier = :identifier")
    LiveData<String> getDenominationOfModifiedAddress(Integer identifier);

    @Query("SELECT " +
            "MA.number AS 'number' " +
            "FROM ModifiedAddress MA " +
            "WHERE MA.identifier = :identifier")
    LiveData<String> getNumberOfModifiedAddress(Integer identifier);

    @Query("SELECT " +
            "MA.reference AS 'reference' " +
            "FROM ModifiedAddress MA " +
            "WHERE MA.identifier = :identifier")
    LiveData<String> getReferenceOfModifiedAddress(Integer identifier);

    @Query("SELECT " +
            "MA.district AS 'district' " +
            "FROM ModifiedAddress MA " +
            "WHERE MA.identifier = :identifier")
    LiveData<String> getDistrictOfModifiedAddress(Integer identifier);

    @Query("SELECT " +
            "MA.postalCode AS 'postalCode' " +
            "FROM ModifiedAddress MA " +
            "WHERE MA.identifier = :identifier")
    LiveData<Integer> getPostalCodeOfModifiedAddress(Integer identifier);

    @Query("SELECT " +
            " C.identifier AS 'identifier', " +
            " C.denomination AS 'denomination', " +
            " S.identifier AS 'state_identifier', " +
            " S.denomination AS 'state_denomination', " +
            " S.acronym AS 'state_acronym', " +
            " Co.identifier AS 'state_country_identifier', " +
            " Co.denomination AS 'state_country_denomination', " +
            " Co.acronym AS 'state_country_acronym' " +
            "FROM ModifiedAddress MA " +
            "INNER JOIN City C ON C.identifier = MA.city " +
            "INNER JOIN State S ON S.identifier = C.state " +
            "INNER JOIN Country Co ON Co.identifier = S.country " +
            "WHERE MA.identifier = :identifier")
    LiveData<CityModel> getCityOfModifiedAddress(Integer identifier);

    @Query("SELECT " +
            "MA.latitude AS 'latitude', " +
            "MA.longitude AS 'longitude' " +
            "FROM ModifiedAddress MA " +
            "WHERE MA.identifier = :identifier")
    LiveData<LatLngModel> getLatLngOfModifiedAddress(Integer identifier);

    @Query("SELECT " +
            "MA.active AS 'active' " +
            "FROM ModifiedAddress MA " +
            "WHERE MA.identifier = :identifier")
    LiveData<Boolean> getActiveOfModifiedAddress(Integer identifier);

}