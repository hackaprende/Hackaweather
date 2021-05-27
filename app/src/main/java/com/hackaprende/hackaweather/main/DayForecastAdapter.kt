package com.hackaprende.hackaweather.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hackaprende.hackaweather.common.DayForecast
import com.hackaprende.hackaweather.databinding.DayForecastItemBinding

class DayForecastAdapter: ListAdapter<DayForecast, DayForecastAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<DayForecast>() {
        override fun areItemsTheSame(oldItem: DayForecast, newItem: DayForecast): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: DayForecast, newItem: DayForecast): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(DayForecastItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: DayForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dayForecast: DayForecast) {
            binding.dayForecast = dayForecast

            val hourForecastAdapter = HourForecastAdapter()
            binding.hourRecycler.layoutManager = LinearLayoutManager(binding.hourRecycler.context,
                LinearLayoutManager.HORIZONTAL, false)
            binding.hourRecycler.adapter = hourForecastAdapter
            hourForecastAdapter.submitList(dayForecast.hourly)
        }
    }
}