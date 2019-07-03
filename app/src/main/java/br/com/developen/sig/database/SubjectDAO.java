package br.com.developen.sig.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SubjectDAO {

    @Insert
    Long create(SubjectVO subjectVO);

    @Query("SELECT S.* FROM Subject S WHERE S.identifier = :identifier")
    SubjectVO retrieve(int identifier);

    @Query("SELECT COUNT(*) > 0 FROM Subject S WHERE S.identifier = :identifier")
    Boolean exists(int identifier);

    @Update
    void update(SubjectVO subjectVO);

    @Delete
    void delete(SubjectVO subjectVO);

}