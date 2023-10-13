package com.souza.careguitar.ui.instrumentDetail

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.souza.careguitar.databinding.FragmentDetailBinding
import com.souza.careguitar.ui.base.BaseViewModel
import com.souza.careguitar.ui.base.navigation.DisplayAddScreen
import com.souza.careguitar.ui.base.navigation.DisplayHomeScreen
import com.souza.careguitar.ui.home.Instrument
import com.souza.careguitar.ui.home.Maintenance
import com.souza.careguitar.ui.home.MaintenanceAdapter
import com.souza.careguitar.utils.BaseBindingFragment
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class InstrumentDetailActivity: BaseBindingFragment<FragmentDetailBinding>() {

    private val viewModel: BaseViewModel by viewModel()
    private val displayAddScreen: DisplayAddScreen by inject()
    private val displayHomeScreen: DisplayHomeScreen by inject()
    private var instrument: Instrument? = null
    private val maintenanceAdapter = MaintenanceAdapter()

    override fun inflateBinding(
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentDetailBinding = FragmentDetailBinding.inflate(
        layoutInflater,
        container,
        false
    )
    override fun onBindingCreated() {
        arguments?.let {
            instrument = it.getParcelable<Instrument>("instrument")
            Picasso.get().load(instrument?.image).into(binding.instrumentDetailIv)
            binding.instrumentTitleTv.text = instrument?.name
        }

        binding.deleteIv.setOnClickListener {
            instrument?.let { it1 -> viewModel.deleteInstrument(it1) }
        }

        binding.photoIv.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, 1)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.historyTv.setOnClickListener {
            displayAddScreen.execute(DisplayAddScreen.Args(false, instrument?.id.orEmpty()))
        }

        viewModel.getMaintenance(instrument?.id.orEmpty())

        binding.recyclerViewBottom.adapter = maintenanceAdapter

        viewModel.instrumentImage.observe(this) {
            Picasso.get().load(it).into(binding.instrumentDetailIv)
        }

        viewModel.onDeleteInstrument.observe(this) {
            displayHomeScreen.execute(DisplayHomeScreen.Args(true))
        }

        viewModel.maintenance.observe(this) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            val sortedList = it.sortedByDescending {
                val date = dateFormat.parse(it.date)
                date
            }

            maintenanceAdapter.submitList(sortedList)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                viewModel.uploadInstrumentImage(instrument?.id.orEmpty(), uri)
            }
        }
    }

    companion object {
        fun newInstance(instrument: Instrument): Fragment {
            val fragment = InstrumentDetailActivity()
            val args = Bundle()
            args.putParcelable("instrument", instrument)
            fragment.arguments = args
            return fragment
        }
    }
}