package com.example.weatherforecast.favoritePage.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.databinding.FavoriteCellBinding
import com.example.weatherforecast.model.models.Favorite
import com.example.weatherforecast.model.utils.Converter

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
                countryName.text = Converter.getAddressEnglish(cnx,item.favLatitude,item.favLongitude)
                highTemp.text = "H:"+ Converter.convertTemperatureToString(item.favMax)
                lowTemp.text = "L:"+ Converter.convertTemperatureToString(item.favMin)
                temp.text = Converter.convertTemperatureToString(item.favTemp)
                weatherDesc.text = item.favMain
                weatherIcon.setOnClickListener {
                    onDeleteClickListener.onDeleteFromFavorite(item)
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