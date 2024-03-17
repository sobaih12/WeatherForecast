package com.example.weatherforecast.homePage.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.databinding.HourlyCellBinding
import com.example.weatherforecast.model.models.HourlyItem
import com.example.weatherforecast.model.utils.Constants
import com.example.weatherforecast.model.utils.Converter
import com.example.weatherforecast.model.utils.PreferenceManager


class HourlyAdapter(private val context: Context) : ListAdapter<HourlyItem, HourlyAdapter.HourlyViewHolder>(HourlyItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val binding = HourlyCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(context,currentItem)
    }

    class HourlyViewHolder(private val binding: HourlyCellBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cnx:Context,item: HourlyItem) {
            binding.apply {
                if(PreferenceManager.getLanguage(cnx)==Constants.LANGUAGE_EN){
                    time.text = if (position == 0) "Now" else Converter.convertUnixTimeToHour(item.dt?.toLong())
                }else{
                    time.text = if (position == 0) "الان" else Converter.convertUnixTimeToHourArabic(item.dt?.toLong())
                }
                if (PreferenceManager.getTempUnit(cnx)==Constants.UNITS_CELSIUS){
                    if (PreferenceManager.getLanguage(cnx)==Constants.LANGUAGE_EN){
                        tempCell.text = Converter.convertToCelsius(item.temp)
                    }else{
                        tempCell.text = Converter.convertToCelsiusArabic(item.temp)
                    }
                }else if (PreferenceManager.getTempUnit(cnx)==Constants.UNITS_FAHRENHEIT){
                    if (PreferenceManager.getLanguage(cnx)==Constants.LANGUAGE_EN){
                        tempCell.text = Converter.convertToFahrenheit(item.temp)
                    }else{
                        tempCell.text = Converter.convertToFahrenheitArabic(item.temp)
                    }
                }else{
                    if (PreferenceManager.getLanguage(cnx)==Constants.LANGUAGE_EN){
                        tempCell.text = Converter.convertToKelvin(item.temp)
                    }else{
                        tempCell.text = Converter.convertToKelvinArabic(item.temp)
                    }
                }


            }
        }
    }

    class HourlyItemDiffCallback : DiffUtil.ItemCallback<HourlyItem>() {
        override fun areItemsTheSame(oldItem: HourlyItem, newItem: HourlyItem): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: HourlyItem, newItem: HourlyItem): Boolean {
            return oldItem == newItem
        }
    }
}


