package com.souza.careguitar.di

import com.google.firebase.auth.FirebaseAuth
import com.souza.careguitar.ui.base.BaseActivity
import com.souza.careguitar.ui.base.navigation.DisplayAddScreen
import com.souza.careguitar.ui.base.navigation.DisplayAddScreenImpl
import com.souza.careguitar.ui.base.navigation.DisplayHelpScreen
import com.souza.careguitar.ui.base.navigation.DisplayHelpScreenImpl
import com.souza.careguitar.ui.base.navigation.DisplayHomeScreen
import com.souza.careguitar.ui.base.navigation.DisplayHomeScreenImpl
import com.souza.careguitar.ui.base.navigation.DisplayInstrumentDetailScreen
import com.souza.careguitar.ui.base.navigation.DisplayInstrumentDetailScreenImpl
import com.souza.careguitar.ui.base.navigation.DisplayLoginScreen
import com.souza.careguitar.ui.base.navigation.DisplayLoginScreenImpl
import com.souza.careguitar.ui.base.navigation.PopFragment
import com.souza.careguitar.ui.login.LoginViewModel
import com.souza.careguitar.utils.Navigator
import com.souza.careguitar.utils.NavigatorImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val navigationModule = module {
        scope<BaseActivity> {
                factory<DisplayLoginScreen> { DisplayLoginScreenImpl(get()) }
                factory<DisplayHomeScreen> { DisplayHomeScreenImpl(get()) }
                factory<DisplayHelpScreen> { DisplayHelpScreenImpl(get()) }
                factory { PopFragment(get()) }
                factory<DisplayInstrumentDetailScreen> { DisplayInstrumentDetailScreenImpl(get()) }
                factory<DisplayAddScreen> { DisplayAddScreenImpl(get()) }
                scoped { get<BaseActivity>().supportFragmentManager }
                scoped<Navigator> { NavigatorImpl(get()) }
        }

        single { FirebaseAuth.getInstance() }
        viewModel { LoginViewModel(get()) }
}

