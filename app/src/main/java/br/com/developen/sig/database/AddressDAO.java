package br.com.developen.sig.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface AddressDAO {

    String GET_ADDRESS = "SELECT " +
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
            "FROM Address A " +
            "INNER JOIN City City ON City.identifier = A.city " +
            "INNER JOIN State CityState ON CityState.identifier = City.state " +
            "INNER JOIN Country CityStateCountry ON CityStateCountry.identifier = CityState.country " +
            "WHERE A.identifier = :identifier";

    String GET_ADDRESSES = "SELECT " +
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
            "FROM Address A " +
            "INNER JOIN City City ON City.identifier = A.city " +
            "INNER JOIN State CityState ON CityState.identifier = City.state " +
            "INNER JOIN Country CityStateCountry ON CityStateCountry.identifier = CityState.country";

    String GET_EDIFICATIONS_OF_ADDRESS = "SELECT " +
            " A.identifier AS 'address_identifier', " +
            " A.denomination AS 'address_denomination', " +
            " A.number AS 'address_number', " +
            " A.reference AS 'address_reference', " +
            " A.district AS 'address_district', " +
            " A.postalCode AS 'address_postalCode', " +
            " AddressCity.identifier AS 'address_city_identifier', " +
            " AddressCity.denomination AS 'address_city_denomination', " +
            " AddressCityState.identifier AS 'address_city_state_identifier', " +
            " AddressCityState.denomination AS 'address_city_state_denomination', " +
            " AddressCityState.acronym AS 'address_city_state_acronym', " +
            " AddressCityStateCountry.identifier AS 'address_city_state_country_identifier', " +
            " AddressCityStateCountry.denomination AS 'address_city_state_country_denomination', " +
            " AddressCityStateCountry.acronym AS 'address_city_state_country_acronym', " +
            " A.latitude AS 'address_latLng_latitude', " +
            " A.longitude AS 'address_latLng_longitude', " +
            " AE.edification AS 'edification', " +
            " AddressEdificationType.identifier AS 'type_identifier', " +
            " AddressEdificationType.denomination AS 'type_denomination', " +
            " AE.reference AS 'reference', " +
            " AE.'from' AS 'from', " +
            " AE.'to' AS 'to' " +
            "FROM AddressEdification AE " +
            "INNER JOIN Address A ON A.identifier = AE.address " +
            "LEFT OUTER JOIN Type AddressEdificationType ON AE.type = AddressEdificationType.identifier " +
            "LEFT OUTER JOIN City AddressCity ON AddressCity.identifier = A.city " +
            "LEFT OUTER JOIN State AddressCityState ON AddressCityState.identifier = AddressCity.state " +
            "LEFT OUTER JOIN Country AddressCityStateCountry ON AddressCityStateCountry.identifier = AddressCityState.country " +
            "WHERE A.identifier = :address AND AE.'to' IS NULL " +
            "ORDER BY AE.type, AE.reference";

    @Insert
    Long create(AddressVO addressVO);

    @Query("SELECT A.* FROM Address A WHERE A.identifier = :identifier")
    AddressVO retrieve(int identifier);

    @Query("SELECT COUNT(*) > 0 FROM Address A WHERE A.identifier = :identifier")
    Boolean exists(int identifier);

    @Query("SELECT COUNT(*) FROM Address A")
    Integer count();

    @Update
    void update(AddressVO addressVO);

    @Delete
    void delete(AddressVO addressVO);

    @Query(GET_ADDRESSES)
    LiveData<List<AddressModel>> getAddresses();

    @Query(GET_ADDRESS)
    AddressModel getAddress(Integer identifier);

    @Query(GET_EDIFICATIONS_OF_ADDRESS)
    List<AddressEdificationModel> getEdificationsOfAddress(int address);

}