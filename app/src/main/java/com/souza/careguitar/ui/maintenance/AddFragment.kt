package com.souza.careguitar.ui.maintenance

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.souza.careguitar.databinding.FragmentAddBinding
import com.souza.careguitar.ui.base.BaseViewModel
import com.souza.careguitar.ui.base.navigation.DisplayHomeScreen
import com.souza.careguitar.ui.base.navigation.DisplayInstrumentDetailScreen
import com.souza.careguitar.ui.base.navigation.PopFragment
import com.souza.careguitar.ui.home.Instrument
import com.souza.careguitar.ui.home.Maintenance
import com.souza.careguitar.utils.BaseBindingFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class AddFragment: BaseBindingFragment<FragmentAddBinding>() {

    private val displayInstrumentDetailScreen: DisplayInstrumentDetailScreen by inject()
    private val displayHomeScreen: DisplayHomeScreen by inject()
    private val viewModel: BaseViewModel by viewModel()
    private var imageUri: String? = null
    private var isAddInstrument = false
    private var instrument: Instrument? = null

    override fun inflateBinding(
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentAddBinding {
        return FragmentAddBinding.inflate(layoutInflater, container, false)
    }

    override fun onBindingCreated() {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val formattedDate = dateFormat.format(currentDate)
        binding.dateEt.setText(formattedDate)
        binding.dateEt.isFocusable = false

        arguments?.let {
            isAddInstrument = it.getBoolean("isAddInstrument")
            instrument = it.getParcelable<Instrument>("instrument")
        }

        initObservers()
        setListeners()
    }

    private fun setListeners() {
        binding.photoIv.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, 1)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (isAddInstrument) {
            binding.descriptionEt.isGone = true
            binding.dateEt.isGone = true
        } else {
            binding.photoIv.isGone = true
            binding.nameEt.isGone = true
        }

        binding.saveBtn.setOnClickListener {
            if (isAddInstrument) {
                viewModel.addInstrument(Instrument(
                    id = UUID.randomUUID().toString(),
                    image = imageUri,
                    name = binding.nameEt.text.toString()
                ))
            } else {
                viewModel.addMaintenance(
                    maintenance = Maintenance(
                        instrumentId = instrument?.id,
                        description = binding.descriptionEt.text.toString(),
                        date = binding.dateEt.text.toString()
                    )
                )
            }
        }
    }

    private fun initObservers() {
        viewModel.onInstrumentAdded.observe(viewLifecycleOwner) {
            displayHomeScreen.execute(DisplayHomeScreen.Args(true))
        }

        viewModel.onMaintenanceAdded.observe(viewLifecycleOwner) {
            instrument?.let {
                displayInstrumentDetailScreen.execute(DisplayInstrumentDetailScreen.Args(it, requireContext()))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                imageUri = uri.toString()
            }
        }
    }

    companion object {
        fun newInstance(
            isAddInstrument: Boolean,
            instrument: Instrument? = null
        ): Fragment {
            val fragment = AddFragment()
            val args = Bundle()
            args.putBoolean("isAddInstrument", isAddInstrument)
            args.putParcelable("instrument", instrument)
            fragment.arguments = args
            return fragment
        }
    }
}