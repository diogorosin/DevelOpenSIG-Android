package br.com.developen.sig.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AddressEdificationDAO {

    @Insert
    void create(AddressEdificationVO addressEdificationVO);

    @Query("SELECT AE.* FROM AddressEdification AE WHERE AE.address = :address AND AE.edification = :edification")
    AddressEdificationVO retrieve(int address, Integer edification);

    @Query("SELECT COUNT(*) > 0 FROM AddressEdification AE WHERE AE.address = :address AND AE.edification = :edification")
    Boolean exists(int address, Integer edification);

    @Update
    void update(AddressEdificationVO addressEdificationVO);

    @Delete
    void delete(AddressEdificationVO addressEdificationVO);

}