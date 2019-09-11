package br.com.developen.sig.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AgencyDAO {

    @Insert
    Long create(AgencyVO agencyVO);

    @Query("SELECT A.* FROM Agency A WHERE A.identifier = :identifier")
    AgencyVO retrieve(int identifier);

    @Query("SELECT COUNT(*) > 0 FROM Agency A WHERE A.identifier = :identifier")
    Boolean exists(int identifier);

    @Update
    void update(AgencyVO agencyVO);

    @Delete
    void delete(AgencyVO agencyVO);

    @Query("SELECT A.* from Agency A ORDER BY A.acronym")
    List<AgencyModel> getAgencies();

}