package com.souza.careguitar.ui.base.navigation

import com.souza.careguitar.R
import com.souza.careguitar.ui.login.LoginFragment
import com.souza.careguitar.utils.Navigator
import com.souza.careguitar.utils.VoidCommand

interface DisplayLoginScreen: VoidCommand

class DisplayLoginScreenImpl(
    private val navigator: Navigator
): DisplayLoginScreen {

    override fun execute() {
        navigator.single(R.id.main_container, LoginFragment())
    }
}