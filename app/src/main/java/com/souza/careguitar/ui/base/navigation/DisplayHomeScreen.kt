package com.souza.careguitar.ui.base.navigation

import com.souza.careguitar.R
import com.souza.careguitar.ui.home.HomeFragment
import com.souza.careguitar.utils.Navigator
import com.souza.careguitar.utils.NoReturnCommand
import com.souza.careguitar.utils.VoidCommand

interface DisplayHomeScreen: NoReturnCommand<DisplayHomeScreen.Args> {
    data class Args(
        val shouldDisplayTopSection: Boolean
    )
}

class DisplayHomeScreenImpl(
    private val navigator: Navigator
): DisplayHomeScreen {
    override fun execute(args: DisplayHomeScreen.Args) {
        navigator.single(R.id.main_container, HomeFragment.newInstance(args.shouldDisplayTopSection))
    }
}