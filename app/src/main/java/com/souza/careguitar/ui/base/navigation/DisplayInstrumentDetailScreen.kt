package com.souza.careguitar.ui.base.navigation

import android.content.Context
import com.souza.careguitar.R
import com.souza.careguitar.ui.home.HomeFragment
import com.souza.careguitar.ui.home.Instrument
import com.souza.careguitar.ui.instrumentDetail.InstrumentDetailActivity
import com.souza.careguitar.utils.Navigator
import com.souza.careguitar.utils.NoReturnCommand
import com.souza.careguitar.utils.VoidCommand

interface DisplayInstrumentDetailScreen: NoReturnCommand<DisplayInstrumentDetailScreen.Args> {
    data class Args(
        val instrument: Instrument,
        val context: Context
    )
}

class DisplayInstrumentDetailScreenImpl(
    private val navigator: Navigator
): DisplayInstrumentDetailScreen {

    override fun execute(args: DisplayInstrumentDetailScreen.Args) {
        navigator.single(R.id.main_container, InstrumentDetailActivity.newInstance(args.instrument), true)
    }
}