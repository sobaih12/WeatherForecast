package com.example.weatherforecast.favoritePage.view

import com.example.weatherforecast.model.models.Favorite

interface OnDeleteClickListener {
    fun onDeleteFromFavorite(favorite: Favorite)
}