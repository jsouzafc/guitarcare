package com.souza.careguitar.ui.home

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.souza.careguitar.databinding.ImageItemViewBinding
import com.squareup.picasso.Picasso

data class Instrument(
    val id: String? = null,
    var image: String? = null,
    val name: String? = null,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(image)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Instrument> {
        override fun createFromParcel(parcel: Parcel): Instrument {
            return Instrument(parcel)
        }

        override fun newArray(size: Int): Array<Instrument?> {
            return arrayOfNulls(size)
        }
    }
}

class InstrumentAdapter(
    private val onInstrumentClick: (Instrument) -> Unit
) : ListAdapter<Instrument, InstrumentViewHolder>(InstrumentDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InstrumentViewHolder {
        val binding = ImageItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InstrumentViewHolder(binding, onInstrumentClick)
    }

    override fun onBindViewHolder(
        holder: InstrumentViewHolder,
        position: Int
    ) {
        holder.bind(currentList[position])
    }
}

class InstrumentViewHolder(
    private val binding: ImageItemViewBinding,
    private val onInstrumentClick: (Instrument) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind (instrument: Instrument) {
        binding.root.setOnClickListener {
            onInstrumentClick(instrument)
        }

        Picasso.get().load(instrument.image).into(binding.photoIv)
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

