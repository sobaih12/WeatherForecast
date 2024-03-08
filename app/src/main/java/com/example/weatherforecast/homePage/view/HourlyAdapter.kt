package com.example.weatherForecast.homePage.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherForecast.model.models.HourlyItem
import com.example.weatherForecast.model.utils.Converter
import com.example.weatherforecast.databinding.HourlyCellBinding


class HourlyAdapter : ListAdapter<HourlyItem, HourlyAdapter.HourlyViewHolder>(HourlyItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val binding = HourlyCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class HourlyViewHolder(private val binding: HourlyCellBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HourlyItem) {
            binding.apply {
                time.text = if (position == 0) "Now" else Converter.convertUnixTimeToHour(item.dt?.toLong())
                tempCell.text = Converter.convertTemperatureToString(item.temp)
//                lottieAnimationView.playAnimation()
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


