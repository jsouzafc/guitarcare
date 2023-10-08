package com.souza.careguitar.ui.base.navigation

import com.souza.careguitar.R
import com.souza.careguitar.ui.home.HomeFragment
import com.souza.careguitar.utils.Navigator
import com.souza.careguitar.utils.VoidCommand

interface DisplayHomeScreen: VoidCommand

class DisplayHomeScreenImpl(
    private val navigator: Navigator
): DisplayHomeScreen {

    override fun execute() {
        navigator.single(R.id.main_container, HomeFragment())
    }
}