package com.souza.careguitar.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlin.system.exitProcess

class NavigatorImpl(
    private val fragmentManager: FragmentManager
) : Navigator {
    override fun go(containerId: Int, screen: Fragment) {
        go(containerId, screen, true)
    }

    override fun go(containerId: Int, screen: Fragment, addToBackStack: Boolean) {
        if (fragmentManager.isNotReadyForTransaction()) return

        val tag = screen.javaClass.canonicalName
        val transaction = fragmentManager.beginTransaction()
            .replace(containerId, screen, tag)
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.setReorderingAllowed(true)
            .commit()
    }

    override fun exit() {
        exitProcess(0)
    }

    override fun pop() {
        if (fragmentManager.isNotReadyForTransaction()) return

        val fragment = top()
        if (fragment is BackHandler && (fragment as BackHandler).onBackPressed()) return
        fragmentManager.popBackStack()
    }

    override fun back(): Boolean {
        if (fragmentManager.isNotReadyForTransaction()) return false

        if (fragmentManager.backStackEntryCount <= 1) return false
        val fragment = top()
        if (fragment is BackHandler && (fragment as BackHandler).onBackPressed()) return true
        fragmentManager.popBackStack()
        return true
    }

    override fun single(containerId: Int, screen: Fragment) {
        if (fragmentManager.isNotReadyForTransaction()) return

        val backStackEntryCount = fragmentManager.backStackEntryCount
        for (i in 0 until backStackEntryCount) {
            fragmentManager.popBackStack()
        }
        go(containerId, screen)
    }

    override fun single(containerId: Int, screen: Fragment, addToBackStack: Boolean) {
        if (fragmentManager.isNotReadyForTransaction()) return

        val backStackEntryCount = fragmentManager.backStackEntryCount
        for (i in 0 until backStackEntryCount) {
            fragmentManager.popBackStack()
        }
        go(containerId, screen, addToBackStack)
    }

    private fun top(): Fragment? {
        return try {
            fragmentManager.executePendingTransactions()
            val tag = fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 1)
                .name
            fragmentManager.findFragmentByTag(tag)
        } catch (e: Exception) {
            null
        }
    }
}