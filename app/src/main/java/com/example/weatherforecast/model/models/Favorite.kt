package com.example.weatherforecast.model.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "favorite")
data class Favorite(
    @PrimaryKey()
    @NotNull
    val favLatitude: Double,
    val favLongitude: Double?,
    val favMain: String?,
    val favTemp: Double?,
    val favMin: Double? ,
    val favMax: Double?,
    val time :Int?
)
