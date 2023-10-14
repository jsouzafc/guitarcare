package com.souza.careguitar.ui.help

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.souza.careguitar.databinding.ImageItemViewBinding
import com.squareup.picasso.Picasso

class HelpAdapter(
    private val onItemClick: (String) -> Unit
): ListAdapter<String, InstrumentViewHolder>(StringListDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InstrumentViewHolder {
        val binding = ImageItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InstrumentViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: InstrumentViewHolder,
        position: Int
    ) {
        holder.bind(currentList[position], onItemClick)
    }
}

class InstrumentViewHolder(
    private val binding: ImageItemViewBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind (
        item: String,
        onItemClick: (String) -> Unit
    ) {
        Picasso.get().load(item).into(binding.photoIv)

        binding.photoIv.setOnClickListener {
            onItemClick(item)
        }
    }
}

class StringListDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(
        oldItem: String,
        newItem: String
    ) = oldItem.length == newItem.length

    override fun areContentsTheSame(
        oldItem: String,
        newItem: String
    ) = oldItem == newItem
}