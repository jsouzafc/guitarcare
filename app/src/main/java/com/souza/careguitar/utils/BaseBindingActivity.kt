package com.souza.careguitar.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import org.koin.androidx.scope.ScopeActivity
import org.koin.androidx.scope.ScopeFragment


typealias ActivityBindingInflater<T> = (LayoutInflater) -> T

abstract class BaseBindingActivity<VB: ViewBinding>(
    private val inflate: ActivityBindingInflater<VB>
) : AppCompatActivity() {

    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        onBindingCreated()
    }

    protected abstract fun onBindingCreated()
}
