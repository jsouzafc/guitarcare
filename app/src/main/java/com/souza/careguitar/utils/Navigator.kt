package com.souza.careguitar.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface Navigator {
    fun go(containerId: Int, screen: Fragment)

    fun go(containerId: Int, screen: Fragment, addToBackStack: Boolean)

    fun exit()

    fun pop()

    fun back(): Boolean
}

fun FragmentManager.isNotReadyForTransaction(): Boolean = isDestroyed || isStateSaved

