package racosta.samples.composetodo.commons

import android.util.Log

interface Logger {
    /**
     * The logger tag used in extension functions for the [Logger].
     * Note that the tag length should not be more than 23 symbols.
     */
    val tag: String
        get() = this.TAG

    fun verbose(message: Any?) = Log.v(tag, message?.toString() ?: "null")
    fun info(message: Any?) = Log.i(tag, message?.toString() ?: "null")
    fun warn(message: Any?) = Log.w(tag, message?.toString() ?: "null")
    fun debug(message: Any?) = Log.d(tag, message?.toString() ?: "null")
    fun error(message: Any?) = Log.e(tag, message?.toString() ?: "null")
}

val Logger.TAG: String
    get() = this.javaClass.simpleName.let {
        if (it.length <= 23) it
        else it.substring(0, 23)
    }

fun Logger?.verbose(message: Any?) = this?.run { verbose(message?.toString() ?: "null") }
fun Logger?.info(message: Any?) = this?.run { info(message?.toString() ?: "null") }
fun Logger?.warn(message: Any?) = this?.run { warn(message?.toString() ?: "null") }
fun Logger?.debug(message: Any?) = this?.run { debug(message?.toString() ?: "null") }
fun Logger?.error(message: Any?) = this?.run { error(message?.toString() ?: "null") }