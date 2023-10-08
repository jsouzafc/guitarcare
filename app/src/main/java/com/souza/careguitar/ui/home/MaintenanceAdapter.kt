package com.souza.careguitar.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.souza.careguitar.databinding.MaintenanceItemViewBinding

data class Maintenance (
    val id: String? = null,
    val description: String? = null,
    val date: String? = null
)

class MaintenanceAdapter(
    private val maintenances: List<Maintenance>
) : ListAdapter<Maintenance, MaintenanceViewHolder>(MaintenanceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaintenanceViewHolder {
        val binding = MaintenanceItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MaintenanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MaintenanceViewHolder, position: Int) {
        val maintenanceItem = maintenances[position]
        holder.binding.textViewTitle.text = maintenanceItem.description
        holder.binding.textViewDate.text = maintenanceItem.date
    }
}

class MaintenanceViewHolder(val binding: MaintenanceItemViewBinding) : RecyclerView.ViewHolder(binding.root)

class MaintenanceDiffCallback : DiffUtil.ItemCallback<Maintenance>() {
    override fun areItemsTheSame(oldItem: Maintenance, newItem: Maintenance) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Maintenance, newItem: Maintenance) = oldItem == newItem
}