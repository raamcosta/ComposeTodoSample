package racosta.samples.composetodo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mockk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import racosta.samples.composetodo.commons.launchInScope
import kotlin.coroutines.CoroutineContext

fun clearRecordedCalls(
    firstMock: Any,
    vararg mocks: Any
) {
    clearMocks(
        firstMock,
        *mocks,
        answers = false,
        recordedCalls = true,
        childMocks = false,
        verificationMarks = false,
        exclusionRules = false
    )
}

fun mockViewModelLaunch(testDispatcher: CoroutineContext) {
    mockkStatic("racosta.samples.composetodo.commons.Utils")
    @Suppress("UNCHECKED_CAST")
    every { any<ViewModel>().launchInScope(any(), any(), any()) } answers {
        (this.invocation.args[0] as ViewModel).viewModelScope.launch(
            testDispatcher,
            args[2] as CoroutineStart,
            args[3] as suspend CoroutineScope.() -> Unit
        )
    }
}

fun mockLogger() {
    mockkStatic(Log::class)
    every { Log.d(any(), any()) } logsForLevel "d/"

    every { Log.i(any(), any()) } logsForLevel "i/"

    every { Log.v(any(), any()) } logsForLevel "v/"

    every { Log.w(any(), any<String>()) } logsForLevel "w/ "

    every { Log.e(any(), any()) } logsForLevel "e/ "
}

infix fun MockKStubScope<Int, Int>.logsForLevel(logLevel: String) = this answers {
    println(logLevel + args[0].toString() + "| " + args[1].toString())
    0
}

fun <T> isEqualTo(expected: T) = CoreMatchers.`is`(expected)

fun <T> isEqualTo(expected: Matcher<T>?) = CoreMatchers.`is`(expected)