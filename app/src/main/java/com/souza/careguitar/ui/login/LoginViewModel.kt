package com.souza.careguitar.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel(
    private val auth: FirebaseAuth,
): ViewModel() {

    private val _onSignUpSuccess = MutableLiveData<AuthResult>()
    val onSignUpSuccess: LiveData<AuthResult> = _onSignUpSuccess

    private val _onSignUpFailure = MutableLiveData<Unit>()
    val onSignupFailure: LiveData<Unit> = _onSignUpFailure

    private val _onLoginSuccess = MutableLiveData<AuthResult>()
    val onLoginSuccess: LiveData<AuthResult> = _onLoginSuccess

    private val _onLoginFailure = MutableLiveData<Unit>()
    val onLoginFailure: LiveData<Unit> = _onLoginFailure

    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn: LiveData<Boolean> = _isUserLoggedIn

    fun login(
        email: String,
        password: String
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            _onLoginSuccess.postValue(it)
        }.addOnFailureListener {
            _onLoginFailure.postValue(Unit)
        }
    }

    fun signUp(
        email: String,
        password: String
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _onSignUpSuccess.postValue(it)
            }.addOnFailureListener {
                _onSignUpFailure.postValue(Unit)
            }
    }

    fun getIsLoggedIn() {
        _isUserLoggedIn.postValue(auth.currentUser != null)
    }

    fun signOut() {
        auth.signOut()
        getIsLoggedIn()
    }
}
