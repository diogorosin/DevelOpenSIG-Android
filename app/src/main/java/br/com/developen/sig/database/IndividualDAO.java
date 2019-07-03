package br.com.developen.sig.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface IndividualDAO {

    @Insert
    Long create(IndividualVO individualVO);

    @Query("SELECT I.* FROM Individual I WHERE I.identifier = :identifier")
    IndividualVO retrieve(int identifier);

    @Query("SELECT COUNT(*) > 0 FROM Individual I WHERE I.identifier = :identifier")
    Boolean exists(int identifier);

    @Update
    void update(IndividualVO individualVO);

    @Delete
    void delete(IndividualVO individualVO);

}