package br.com.developen.sig.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CityDAO {

    @Insert
    Long create(CityVO cityVO);

    @Query("SELECT C.* FROM City C WHERE C.identifier = :identifier")
    CityVO retrieve(int identifier);

    @Query("SELECT COUNT(*) > 0 FROM City C WHERE C.identifier = :identifier")
    Boolean exists(int identifier);

    @Query("SELECT Ci.* " +
            "FROM City Ci " +
            "INNER JOIN State St ON St.identifier = Ci.state " +
            "INNER JOIN Country Co ON Co.identifier = St.country " +
            "WHERE Ci.denomination LIKE :city " +
            "AND St.acronym LIKE :stateAcronym")
    CityVO findByCityStateAcronym(String city, String stateAcronym);

    @Query("SELECT Ci.* " +
            "FROM City Ci " +
            "INNER JOIN State St ON St.identifier = Ci.state " +
            "INNER JOIN Country Co ON Co.identifier = St.country " +
            "WHERE Ci.denomination LIKE :city " +
            "AND St.denomination LIKE :state " +
            "AND Co.denomination LIKE :country")
    CityVO findByCityStateCountry(String city, String state, String country);

    @Query("SELECT COUNT(*) FROM City C")
    Integer count();

    @Update
    void update(CityVO cityVO);

    @Delete
    void delete(CityVO cityVO);

    @Query("SELECT Ci.identifier AS 'identifier', " +
            " Ci.denomination AS 'denomination', " +
            " St.identifier AS 'state_identifier', " +
            " St.denomination AS 'state_denomination', " +
            " St.acronym AS 'state_acronym', " +
            " Co.identifier AS 'state_country_identifier', " +
            " Co.denomination AS 'state_country_denomination', " +
            " Co.acronym AS 'state_country_acronym' " +
            "FROM City Ci " +
            "INNER JOIN State St ON St.identifier = Ci.state " +
            "INNER JOIN Country Co ON Co.identifier = St.country " +
            "ORDER BY St.denomination, Ci.denomination")
    LiveData<List<CityModel>> getCities();

}