package com.souza.careguitar.di

import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.souza.careguitar.ui.base.BaseViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        FirebaseApp.initializeApp(get())
    }

    single {
        FirebaseFirestore.getInstance()
    }

    viewModel {
        BaseViewModel(get(), get())
    }
}