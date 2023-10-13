package com.souza.careguitar.ui.home

import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isGone
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Assertions.checkMainThread
import com.souza.careguitar.databinding.FragmentHomeBinding
import com.souza.careguitar.ui.base.BaseActivity
import com.souza.careguitar.ui.base.BaseViewModel
import com.souza.careguitar.ui.base.navigation.DisplayAddScreen
import com.souza.careguitar.ui.base.navigation.DisplayInstrumentDetailScreen
import com.souza.careguitar.utils.BaseBindingFragment
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: BaseBindingFragment<FragmentHomeBinding>() {

    private val viewModel: BaseViewModel by viewModel()
    private val displayAddScreen: DisplayAddScreen by inject()
    private val displayInstrumentDetailScreen: DisplayInstrumentDetailScreen by inject()
    private val adapter = InstrumentAdapter {
        displayInstrumentDetailScreen.execute(DisplayInstrumentDetailScreen.Args(it, requireContext()))
    }
    private lateinit var exoPlayer: SimpleExoPlayer

    override fun inflateBinding(
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(
        layoutInflater,
        container,
        false
    )

    override fun onBindingCreated() {
        initObservers()

        arguments?.let {
            val shouldDisplayTopSection = it.getBoolean("shouldDisplayTopSection")
            if (!shouldDisplayTopSection) {
                binding.playerView.isGone = true
                binding.titleTv.isGone = true
            }
        }

        binding.searchText.apply {
            textChanges().debounce(300L).onEach {
                viewModel.getInstruments(it.toString())
            }.launchIn(lifecycleScope)
        }

        binding.instrumentsTv.setOnClickListener {
            displayAddScreen.execute(DisplayAddScreen.Args(true))
        }

        binding.recyclerViewBottom.adapter = adapter
        (requireActivity() as BaseActivity).showBottomBar()
        viewModel.getInstruments()
        setupVideoPlayer()
    }

    private fun EditText.textChanges(): Flow<CharSequence?> {
        return callbackFlow {
            checkMainThread()

            val listener = doOnTextChanged { text, _, _, _ -> trySend(text) }
            awaitClose { removeTextChangedListener(listener) }
        }.onStart { emit(text) }
    }

    private fun setupVideoPlayer() {
        val videoUrl = "https://dl.dropbox.com/scl/fi/xfael6oknu3k6h7mxoypc/videoplayback.mp4?rlkey=eursx6l22ay8grm86b24catvw&dl=0"
        exoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = exoPlayer
        val dataSource = DefaultDataSourceFactory(requireContext(), "sample")
        val mediaSource = ProgressiveMediaSource.Factory(dataSource).createMediaSource(MediaItem.fromUri(videoUrl))
        exoPlayer.setMediaSource(mediaSource)
        exoPlayer?.prepare()
        exoPlayer?.playWhenReady = true
    }

    private fun initObservers() {
        viewModel.instruments.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        exoPlayer?.release()
        viewModel.instruments.removeObservers(viewLifecycleOwner)
    }

    companion object {
        fun newInstance(shouldDisplayTopSection: Boolean): Fragment {
            val args = Bundle()
            args.putBoolean("shouldDisplayTopSection", shouldDisplayTopSection)
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}