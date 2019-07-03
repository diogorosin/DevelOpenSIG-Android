package br.com.developen.sig.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
            "AND St.denomination LIKE :state " +
            "AND Co.denomination LIKE :country")
    CityVO findByCityStateCountry(String city, String state, String country);

    @Query("SELECT COUNT(*) FROM City C")
    Integer count();

    @Update
    void update(CityVO cityVO);

    @Delete
    void delete(CityVO cityVO);

}