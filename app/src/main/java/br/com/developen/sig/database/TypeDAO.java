package br.com.developen.sig.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TypeDAO {

    @Insert
    Long create(TypeVO typeVO);

    @Query("SELECT T.* FROM Type T WHERE T.identifier = :identifier")
    TypeVO retrieve(int identifier);

    @Query("SELECT COUNT(*) > 0 FROM Type T WHERE T.identifier = :identifier")
    Boolean exists(int identifier);

    @Query("SELECT COUNT(*) FROM Type T")
    Integer count();

    @Query("SELECT * FROM Type T ORDER BY identifier")
    LiveData<List<TypeModel>> getList();

    @Update
    void update(TypeVO typeVO);

    @Delete
    void delete(TypeVO typeVO);

}