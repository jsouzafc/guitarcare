package com.souza.careguitar.ui.help

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.souza.careguitar.databinding.FragmentHelpBinding
import com.souza.careguitar.ui.base.BaseViewModel
import com.souza.careguitar.utils.BaseBindingFragment
import com.souza.careguitar.utils.WebViewActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HelpFragment: BaseBindingFragment<FragmentHelpBinding>() {

    private val viewModel: BaseViewModel by viewModel()
    private val adapter = HelpAdapter {
        openWebView(it)
    }

    private fun openWebView(url: String) {
        val intent = Intent(requireActivity(), WebViewActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }

    override fun inflateBinding(
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHelpBinding = FragmentHelpBinding.inflate(
        layoutInflater,
        container,
        false
    )

    override fun onBindingCreated() {
        binding.recyclerViewBottom.adapter = adapter
        viewModel.help.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }
}