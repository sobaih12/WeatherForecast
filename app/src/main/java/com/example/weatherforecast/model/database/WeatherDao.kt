package com.example.weatherforecast.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weatherforecast.model.models.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM favorite")
    fun getAllFavorite(): Flow<List<Favorite>>
    @Insert
    fun insert(favorite: Favorite)
    @Delete
    fun delete(favorite: Favorite)
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(favorites: Favorite)
}