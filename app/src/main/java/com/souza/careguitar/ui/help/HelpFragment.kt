package com.souza.careguitar.ui.help

import android.os.Bundle
import android.view.ViewGroup
import com.souza.careguitar.databinding.FragmentHelpBinding
import com.souza.careguitar.utils.BaseBindingFragment

class HelpFragment: BaseBindingFragment<FragmentHelpBinding>() {
    override fun inflateBinding(
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHelpBinding = FragmentHelpBinding.inflate(
        layoutInflater,
        container,
        false
    )

    override fun onBindingCreated() {

    }
}