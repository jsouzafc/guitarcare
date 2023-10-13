package com.souza.careguitar.ui.base.navigation

import com.souza.careguitar.R
import com.souza.careguitar.ui.help.HelpFragment
import com.souza.careguitar.ui.login.LoginFragment
import com.souza.careguitar.utils.Navigator
import com.souza.careguitar.utils.VoidCommand

interface DisplayHelpScreen: VoidCommand

class DisplayHelpScreenImpl(
    private val navigator: Navigator
): DisplayHelpScreen {

    override fun execute() {
        navigator.single(R.id.main_container, HelpFragment())
    }
}