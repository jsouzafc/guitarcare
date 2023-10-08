package com.souza.careguitar.ui.home

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import com.souza.careguitar.databinding.FragmentHomeBinding
import com.souza.careguitar.utils.BaseBindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: BaseBindingFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModel()
    private val adapter = InstrumentAdapter()

    override fun inflateBinding(
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding? = FragmentHomeBinding.inflate(
        layoutInflater,
        container,
        false
    )

    override fun onBindingCreated() {
        initObservers()
        val instrument = Instrument(id = null, image = "https://http2.mlstatic.com/D_NQ_NP_698537-MLB53861051229_022023-O.webp", name = "Nome do Instrumento")
        viewModel.addInstrument(instrument)
        viewModel.addInstrument(instrument)
        viewModel.addInstrument(instrument)
        viewModel.addInstrument(instrument)
        viewModel.addInstrument(instrument)
        viewModel.addInstrument(instrument)
        viewModel.addInstrument(instrument)
        viewModel.addInstrument(instrument)
        viewModel.addInstrument(instrument)
        viewModel.addInstrument(instrument)
        viewModel.addInstrument(instrument)
        viewModel.addInstrument(instrument)
        viewModel.addInstrument(instrument)
        viewModel.addInstrument(instrument)
        viewModel.addInstrument(instrument)
        viewModel.addInstrument(instrument)
        viewModel.addInstrument(instrument)
        viewModel.addInstrument(instrument)
        viewModel.addInstrument(instrument)
        viewModel.addInstrument(instrument)
        binding.recyclerViewBottom.adapter = adapter
        viewModel.getInstruments()
    }

    private fun initObservers() {
        viewModel.instruments.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}