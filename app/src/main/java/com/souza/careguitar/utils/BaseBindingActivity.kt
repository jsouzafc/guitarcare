package com.souza.careguitar.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import org.koin.androidx.scope.ScopeFragment

abstract class BaseBindingFragment<V: ViewBinding> : ScopeFragment() {

    private var _binding: V? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflateBinding(container, savedInstanceState)?.let {
        _binding = it
        onBindingCreated(binding)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected abstract fun inflateBinding(container: ViewGroup?, savedInstanceState: Bundle?): V?

    protected abstract fun onBindingCreated(binding: V)
}
