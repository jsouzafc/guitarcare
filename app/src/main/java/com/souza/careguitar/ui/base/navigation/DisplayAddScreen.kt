package com.souza.careguitar.ui.base.navigation

import com.souza.careguitar.R
import com.souza.careguitar.ui.home.Instrument
import com.souza.careguitar.ui.maintenance.AddFragment
import com.souza.careguitar.utils.Navigator
import com.souza.careguitar.utils.NoReturnCommand

interface DisplayAddScreen: NoReturnCommand<DisplayAddScreen.Args> {
    data class Args(
        val isAddInstrument: Boolean,
        val instrument: Instrument? = null
    )
}

class DisplayAddScreenImpl(
    private val navigator: Navigator
): DisplayAddScreen {
    override fun execute(args: DisplayAddScreen.Args) {
        navigator.single(R.id.main_container, AddFragment.newInstance(args.isAddInstrument, args.instrument))
    }
}