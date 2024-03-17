package com.example.weatherforecast.homePage.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.databinding.DailyCellBinding
import com.example.weatherforecast.model.models.DailyItem
import com.example.weatherforecast.model.utils.Constants
import com.example.weatherforecast.model.utils.Converter
import com.example.weatherforecast.model.utils.PreferenceManager


class DailyAdapter(private val context: Context) : ListAdapter<DailyItem, DailyAdapter.DailyViewHolder>(DailyItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val binding = DailyCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(context,currentItem)
    }

    class DailyViewHolder(private val binding: DailyCellBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cnx:Context ,item: DailyItem) {
            binding.apply {
                if (PreferenceManager.getLanguage(cnx)== Constants.LANGUAGE_EN){
                    dayCell.text = if (position == 0) "Today" else if (position == 1) "Tomorrow" else item.dt?.let { Converter.convertUnixTimeToDay(it) }
                }else{
                    dayCell.text = if (position == 0) "اليوم" else if (position == 1) "غدا" else item.dt?.let { Converter.convertUnixTimeToArabicDay(it) }
                }
                if (PreferenceManager.getTempUnit(cnx)== Constants.UNITS_CELSIUS){
                    if (PreferenceManager.getLanguage(cnx)==Constants.LANGUAGE_EN){
                        tempHighCell.text = "H:"+ Converter.convertToCelsius(item.temp?.max)
                        tempLowCell.text = "L:"+ Converter.convertToCelsius(item.temp?.min)
                    }else{
                        tempHighCell.text = "ك:" +Converter.convertToCelsiusArabic(item.temp?.max)
                        tempLowCell.text = "ص:" +Converter.convertToCelsiusArabic(item.temp?.min)
                    }

                }else if(PreferenceManager.getTempUnit(cnx)== Constants.UNITS_FAHRENHEIT){
                    if (PreferenceManager.getLanguage(cnx)==Constants.LANGUAGE_EN){
                        tempHighCell.text = "H:"+ Converter.convertToFahrenheit(item.temp?.max)
                        tempLowCell.text = "L:"+ Converter.convertToFahrenheit(item.temp?.min)
                    }else{
                        tempHighCell.text = "ك:" +Converter.convertToFahrenheitArabic(item.temp?.max)
                        tempLowCell.text = "ص:" +Converter.convertToFahrenheitArabic(item.temp?.min)
                    }
                }else{
                    if (PreferenceManager.getLanguage(cnx)==Constants.LANGUAGE_EN){
                        tempHighCell.text = "H:"+ Converter.convertToKelvin(item.temp?.max)
                        tempLowCell.text = "L:"+ Converter.convertToKelvin(item.temp?.min)
                    }else{
                        tempHighCell.text = "ك:" +Converter.convertToKelvinArabic(item.temp?.max)
                        tempLowCell.text = "ص:" +Converter.convertToKelvinArabic(item.temp?.min)
                    }
                }

            }
        }
    }

    class DailyItemDiffCallback : DiffUtil.ItemCallback<DailyItem>() {
        override fun areItemsTheSame(oldItem: DailyItem, newItem: DailyItem): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: DailyItem, newItem: DailyItem): Boolean {
            return oldItem == newItem
        }
    }
}


