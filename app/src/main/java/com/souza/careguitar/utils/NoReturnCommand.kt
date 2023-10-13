package com.souza.careguitar.utils

interface NoReturnCommand<Args> {
    fun execute(args: Args)
}