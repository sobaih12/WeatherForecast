package com.example.weatherforecast.favoritePage.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.databinding.FavoriteCellBinding
import com.example.weatherforecast.model.models.Favorite
import com.example.weatherforecast.model.utils.Constants
import com.example.weatherforecast.model.utils.Converter
import com.example.weatherforecast.model.utils.PreferenceManager

class FavoriteAdapter(private val context:Context,private val onDeleteClickListener: OnDeleteClickListener) : ListAdapter<Favorite, FavoriteAdapter.FavoriteViewHolder>(FavoriteItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = FavoriteCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(context,onDeleteClickListener,currentItem)
    }

    class FavoriteViewHolder(private val binding: FavoriteCellBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cnx: Context,onDeleteClickListener: OnDeleteClickListener, item: Favorite,) {

            binding.apply {
                if (PreferenceManager.getLanguage(cnx)==Constants.LANGUAGE_EN){
                    countryName.text = Converter.getAddressEnglish(cnx,item.favLatitude,item.favLongitude!!)
                }else{
                    countryName.text = Converter.getAddressArabic(cnx,item.favLatitude,item.favLongitude)
                }
                if (PreferenceManager.getTempUnit(cnx)== Constants.UNITS_CELSIUS){
                    if (PreferenceManager.getLanguage(cnx)==Constants.LANGUAGE_EN){
                        highTemp.text = "H:"+ Converter.convertToCelsius(item.favMax)
                        lowTemp.text = "L:"+ Converter.convertToCelsius(item.favMin)
                        temp.text = Converter.convertToCelsius(item.favTemp)
                    }else{
                        highTemp.text = "ك:" +Converter.convertToCelsiusArabic(item.favMax)
                        lowTemp.text = "ص:" +Converter.convertToCelsiusArabic(item.favMin)
                        temp.text = Converter.convertToCelsiusArabic(item.favTemp)
                    }
                }else if(PreferenceManager.getTempUnit(cnx)== Constants.UNITS_FAHRENHEIT){
                    if (PreferenceManager.getLanguage(cnx)==Constants.LANGUAGE_EN){
                        highTemp.text = "H:"+ Converter.convertToFahrenheit(item.favMax)
                        lowTemp.text = "L:"+ Converter.convertToFahrenheit(item.favMin)
                        temp.text = Converter.convertToFahrenheit(item.favTemp)
                    }else{
                        highTemp.text = "ك:" +Converter.convertToFahrenheitArabic(item.favMax)
                        lowTemp.text = "ص:" +Converter.convertToFahrenheitArabic(item.favMin)
                        temp.text = Converter.convertToFahrenheitArabic(item.favTemp)
                    }
                }else{
                    if (PreferenceManager.getLanguage(cnx)==Constants.LANGUAGE_EN){
                        highTemp.text = "H:"+ Converter.convertToKelvin(item.favMax)
                        lowTemp.text = "L:"+ Converter.convertToKelvin(item.favMin)
                        temp.text = Converter.convertToKelvin(item.favTemp)
                    }else{
                        highTemp.text = "ك:" +Converter.convertToKelvinArabic(item.favMax)
                        lowTemp.text = "ص:" +Converter.convertToKelvinArabic(item.favMin)
                        temp.text = Converter.convertToKelvinArabic(item.favTemp)
                    }
                }
//                if (PreferenceManager.getTempUnit(cnx)==Constants.UNITS_CELSIUS){
//                    highTemp.text = "H:"+ Converter.convertToCelsius(item.favMax)
//                    lowTemp.text = "L:"+ Converter.convertToCelsius(item.favMin)
//                    temp.text = Converter.convertToCelsius(item.favTemp)
//                }else if(PreferenceManager.getTempUnit(cnx)==Constants.UNITS_FAHRENHEIT){
//                    highTemp.text = "H:"+ Converter.convertToFahrenheit(item.favMax)
//                    lowTemp.text = "L:"+ Converter.convertToFahrenheit(item.favMin)
//                    temp.text = Converter.convertToFahrenheit(item.favTemp)
//                }else{
//                    highTemp.text = "H:"+ Converter.convertToKelvin(item.favMax)
//                    lowTemp.text = "L:"+ Converter.convertToKelvin(item.favMin)
//                    temp.text = Converter.convertToKelvin(item.favTemp)
//                }
                weatherDesc.text = item.favMain
                weatherIcon.setOnClickListener {
                    onDeleteClickListener.onDeleteFromFavorite(item)
                }
                favoriteCell.setOnClickListener {
                    onDeleteClickListener.onNavigateToDetails(item)
                }
            }
        }
    }

    class FavoriteItemDiffCallback : DiffUtil.ItemCallback<Favorite>() {
        override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem.favLatitude == newItem.favLatitude
        }

        override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem == newItem
        }
    }
}