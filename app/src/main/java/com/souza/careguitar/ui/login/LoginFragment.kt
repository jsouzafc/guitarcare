package com.souza.careguitar.ui.login

import android.R.attr.password
import android.os.Bundle
import android.view.ViewGroup
import com.souza.careguitar.databinding.ActivityLoginBinding
import com.souza.careguitar.ui.base.BaseActivity
import com.souza.careguitar.utils.BaseBindingFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class LoginFragment: BaseBindingFragment<ActivityLoginBinding>() {

    private val viewModel: LoginViewModel by activityViewModel()

    override fun inflateBinding(
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ActivityLoginBinding = ActivityLoginBinding.inflate(
        layoutInflater,
        container,
        false
    )

    override fun onBindingCreated() {
        setupListeners()
        (requireActivity() as BaseActivity).hideBottomBar()
    }

    private fun setupListeners() {
        binding.Signin.setOnClickListener{
            viewModel.login(binding.emailEt.text.toString(), binding.passwordEt.text.toString())
        }

        binding.signOutCv.setOnClickListener{
            viewModel.signUp(binding.emailEt.text.toString(), binding.passwordEt.text.toString())
        }
    }
}

