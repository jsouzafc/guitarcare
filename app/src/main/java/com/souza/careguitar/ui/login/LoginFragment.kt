package com.souza.careguitar.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.souza.careguitar.R
import com.souza.careguitar.databinding.ActivityLoginBinding
import com.souza.careguitar.ui.base.BaseActivity
import com.souza.careguitar.utils.BaseBindingActivity
import com.souza.careguitar.utils.BaseBindingFragment
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity: BaseBindingFragment<ActivityLoginBinding>() {

    private val viewModel: LoginViewModel by viewModel()
    private lateinit var activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>

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
        setupObservers()
    }

    private fun setupObservers() = with(viewModel) {
        onLoginSuccess.observe(this@LoginActivity) {
            val intentSenderRequest = IntentSenderRequest.Builder(it).build()
            activityResultLauncher.launch(intentSenderRequest)
        }

        onLoginFailure.observe(this@LoginActivity) {
            Toast.makeText(requireContext(), "Falha no login", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupListeners() {
        activityResultLauncher  = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                try {
                    val credential: SignInCredential = viewModel.getSignInCredentialFromIntent(result.data)
                    val email = credential.id
                    Toast.makeText(this,"$email", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, BaseActivity()::class.java)
                    startActivity(intent)
                } catch (e: ApiException) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }


        binding.Signin.setOnClickListener{
            viewModel.login(getString(R.string.default_web_client_id))
        }
    }

    override fun onStart() {
        super.onStart()
        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
            startActivity(Intent(this, BaseActivity::class.java))
            finish()
        }
    }
}

