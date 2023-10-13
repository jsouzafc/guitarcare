package com.souza.careguitar.ui.base.navigation

import com.souza.careguitar.utils.Navigator
import com.souza.careguitar.utils.VoidCommand

class PopFragment(
    private val navigator: Navigator
): VoidCommand {
    override fun execute() = navigator.pop()
}
