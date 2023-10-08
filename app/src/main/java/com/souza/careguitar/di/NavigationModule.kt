package com.souza.careguitar.di

import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.souza.careguitar.ui.base.BaseActivity
import com.souza.careguitar.ui.base.navigation.DisplayHomeScreen
import com.souza.careguitar.ui.base.navigation.DisplayHomeScreenImpl
import com.souza.careguitar.ui.base.navigation.DisplayLoginScreen
import com.souza.careguitar.ui.base.navigation.DisplayLoginScreenImpl
import com.souza.careguitar.ui.login.LoginViewModel
import com.souza.careguitar.utils.Navigator
import com.souza.careguitar.utils.NavigatorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val navigationModule = module {
        scope<BaseActivity> {
                factory<DisplayLoginScreen> { DisplayLoginScreenImpl(get()) }
                factory<DisplayHomeScreen> { DisplayHomeScreenImpl(get()) }
                scoped { get<BaseActivity>().supportFragmentManager }
                scoped<Navigator> { NavigatorImpl(get()) }
        }

        single { FirebaseAuth.getInstance() }
        viewModel { LoginViewModel(get()) }
}

