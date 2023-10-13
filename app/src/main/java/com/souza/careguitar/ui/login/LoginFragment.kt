package com.souza.careguitar.ui.login

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
            if (binding.emailEt.text.toString().isNotBlank() && binding.passwordEt.text.toString().isNotBlank()) {
                viewModel.login(binding.emailEt.text.toString(), binding.passwordEt.text.toString())
            } else {
                binding.emailEt.error = "Campo obrigatório"
                binding.passwordEt.error = "Campo obrigatório"
            }
        }

        binding.signOutCv.setOnClickListener{
            if (binding.emailEt.text.toString().isNotBlank() && binding.passwordEt.text.toString().isNotBlank()) {
                viewModel.signUp(binding.emailEt.text.toString(), binding.passwordEt.text.toString())
            } else {
                binding.emailEt.error = "Campo obrigatório"
                binding.passwordEt.error = "Campo obrigatório"
            }
        }
    }
}

