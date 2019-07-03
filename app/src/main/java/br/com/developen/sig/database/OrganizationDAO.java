package br.com.developen.sig.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface OrganizationDAO {

    @Insert
    Long create(OrganizationVO organizationVO);

    @Query("SELECT O.* FROM Organization O WHERE O.identifier = :identifier")
    OrganizationVO retrieve(int identifier);

    @Query("SELECT COUNT(*) > 0 FROM Organization O WHERE O.identifier = :identifier")
    Boolean exists(int identifier);

    @Update
    void update(OrganizationVO organizationVO);

    @Delete
    void delete(OrganizationVO organizationVO);

}