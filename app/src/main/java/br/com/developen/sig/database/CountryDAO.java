package br.com.developen.sig.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CountryDAO {

    @Insert
    Long create(CountryVO countryVO);

    @Query("SELECT C.* FROM Country C WHERE C.identifier = :identifier")
    CountryVO retrieve(int identifier);

    @Query("SELECT COUNT(*) > 0 FROM Country C WHERE C.identifier = :identifier")
    Boolean exists(int identifier);

    @Query("SELECT COUNT(*) FROM Country C")
    Integer count();

    @Update
    void update(CountryVO countryVO);

    @Delete
    void delete(CountryVO countryVO);

    @Query("SELECT C.* FROM Country C")
    LiveData<List<CountryModel>> list();

    @Query("SELECT C.* FROM Country C WHERE C.identifier = :identifier")
    LiveData<CountryModel> get(int identifier);

}