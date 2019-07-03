package br.com.developen.sig.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface StateDAO {

    @Insert
    Long create(StateVO stateVO);

    @Query("SELECT S.* FROM State S WHERE S.identifier = :identifier")
    StateVO retrieve(int identifier);

    @Query("SELECT COUNT(*) > 0 FROM State S WHERE S.identifier = :identifier")
    Boolean exists(int identifier);

    @Query("SELECT COUNT(*) FROM State S")
    Integer count();

    @Update
    void update(StateVO stateVO);

    @Delete
    void delete(StateVO stateVO);

}