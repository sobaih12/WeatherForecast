package com.example.weatherForecast.homePage.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherForecast.model.models.DailyItem
import com.example.weatherForecast.model.utils.Converter
import com.example.weatherforecast.databinding.DailyCellBinding


class DailyAdapter : ListAdapter<DailyItem, DailyAdapter.DailyViewHolder>(DailyItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val binding = DailyCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class DailyViewHolder(private val binding: DailyCellBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DailyItem) {
            binding.apply {
                dayCell.text = if (position == 0) "Today" else if (position == 1) "Tomorrow" else item.dt?.let { Converter.convertUnixTimeToDay(it) }
                tempHighCell.text = "H:"+ Converter.convertTemperatureToString(item.temp?.max)
                tempLowCell.text = "L:"+Converter.convertTemperatureToString(item.temp?.min)
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


