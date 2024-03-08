package com.example.weatherforecast.model.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    var roomId: Long = 0,
    val favLatitude: Double?,
    val favLongitude: Double?,
    val favMain: String?,
    val favTemp: Double?,
    val favMin: Double? ,
    val favMax: Double?
)
