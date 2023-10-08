package com.souza.careguitar.ui.base

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.souza.careguitar.R
import com.souza.careguitar.databinding.ActivityBaseBinding
import com.souza.careguitar.ui.base.navigation.DisplayHomeScreen
import com.souza.careguitar.ui.base.navigation.DisplayLoginScreen
import com.souza.careguitar.ui.login.LoginViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ScopeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class BaseActivity : ScopeActivity() {

    private lateinit var binding: ActivityBaseBinding
    private val displayHomeScreen: DisplayHomeScreen by inject()
    private val displayLoginScreen: DisplayLoginScreen by inject()
    private val viewModel: LoginViewModel by viewModel()
    lateinit var activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getIsLoggedIn()
    }

    private fun setupListeners() {

    }

    private fun setupObservers() = with(viewModel) {
        isUserLoggedIn.observe(this@BaseActivity) {
            if (it) displayHomeScreen.execute() else displayLoginScreen.execute()
        }

        onLoginSuccess.observe(this@BaseActivity) {
            displayHomeScreen.execute()
        }

        onLoginFailure.observe(this@BaseActivity) {
            Toast.makeText(this@BaseActivity, "Falha no login", Toast.LENGTH_SHORT).show()
        }

        onSignupFailure.observe(this@BaseActivity) {
            Toast.makeText(this@BaseActivity, "Falha no cadastro", Toast.LENGTH_SHORT).show()
        }

        onSignUpSuccess.observe(this@BaseActivity) {
            displayHomeScreen.execute()
        }
    }

    override fun onStart() {
        super.onStart()
    }
}

