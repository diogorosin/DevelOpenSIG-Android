package br.com.developen.sig.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MarkerDAO {

    @Query("SELECT * FROM Marker")
    LiveData<List<MarkerModel>> getMarkers();

}