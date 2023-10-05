package com.souza.careguitar.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.souza.careguitar.databinding.InstrumentItemViewBinding
import com.squareup.picasso.Picasso

data class Instrument(
    val id: String? = null,
    val image: String? = null,
    val name: String? = null
)

class InstrumentAdapter : ListAdapter<Instrument, InstrumentViewHolder>(InstrumentDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InstrumentViewHolder {
        val binding = InstrumentItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InstrumentViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: InstrumentViewHolder,
        position: Int
    ) {
        holder.bind(currentList[position])
    }
}

class InstrumentViewHolder(
    private val binding: InstrumentItemViewBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind (instrument: Instrument) {
        Picasso.get().load(instrument.image).into(binding.imageView)
        binding.textViewName.text = instrument.name
    }
}

class InstrumentDiffCallback : DiffUtil.ItemCallback<Instrument>() {
    override fun areItemsTheSame(
        oldItem: Instrument,
        newItem: Instrument
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Instrument,
        newItem: Instrument
    ) = oldItem == newItem
}

