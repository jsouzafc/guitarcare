package com.souza.careguitar.utils

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        onBindingCreated()
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        val fragment: Fragment? = childFragmentManager.findFragmentByTag(tag) as Fragment?
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, intent)
        }
    }

    protected abstract fun inflateBinding(container: ViewGroup?, savedInstanceState: Bundle?): V?

    protected abstract fun onBindingCreated()
}
