/*
 * Copyright 2020 Oleksandr Bezushko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.insiderser.android.template.test.listeners

import android.app.Instrumentation
import android.os.Bundle
import android.util.Log
import androidx.test.internal.runner.listener.InstrumentationResultPrinter
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.runner.Description
import org.junit.runner.notification.RunListener

/**
 * A JUnit [RunListener] that installs a default [Thread.UncaughtExceptionHandler] to detect crashes
 * in instrumentation tests and report a failure status to [Instrumentation], before letting the
 * default crash handling continue (which will terminate the process).
 *
 * All you need to do is add the following to the defaultConfig of build.gradle:
 *
 * ```
 * testInstrumentationRunnerArgument "listener",
 *   "com.insiderser.android.template.test.listeners.CrashingRunListener"
 * ```
 *
 * Then you can run instrumentation tests via Gradle as usual.
 *
 * When running UI tests via adb, add a *listener* execution argument to the command line for running
 * the UI tests:
 * `-e listener com.insiderser.android.template.test.shared.util.CrashingRunListener`. The full command
 * line should look something like this:
 *
 * ```
 * adb shell am instrument \\
 * -w com.android.foo/android.support.test.runner.AndroidJUnitRunner \\
 * -e listener com.insiderser.android.template.test.shared.util.CrashingRunListener
 * ```
 */
// Credits to pyricau https://gist.github.com/pyricau/970b95a4757a99b26562fd95e146f38f
@Suppress("unused")
class CrashingRunListener : RunListener() {

    @Volatile
    private lateinit var bundle: Bundle

    @Volatile
    private var isTestRunning = false

    override fun testRunStarted(description: Description) {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()!!
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            if (isTestRunning) {
                isTestRunning = false
                reportTestFailure(
                    "Instrumentation test failed due to uncaught exception in thread [${thread.name}]:\n" +
                        Log.getStackTraceString(throwable)
                )
            }
            defaultHandler.uncaughtException(thread, throwable)
        }
    }

    override fun testStarted(description: Description) {
        val testClass = description.className
        val testName = description.methodName
        bundle = Bundle()
        bundle.putString(
            Instrumentation.REPORT_KEY_IDENTIFIER,
            CrashingRunListener::class.java.name
        )
        bundle.putString(InstrumentationResultPrinter.REPORT_KEY_NAME_CLASS, testClass)
        bundle.putString(InstrumentationResultPrinter.REPORT_KEY_NAME_TEST, testName)
        isTestRunning = true
    }

    override fun testFinished(description: Description?) {
        isTestRunning = false
    }

    /**
     * Reports that the test has failed, with the provided [message].
     */
    private fun reportTestFailure(message: String) {
        bundle.putString(InstrumentationResultPrinter.REPORT_KEY_STACK, message)
        InstrumentationRegistry.getInstrumentation()
            .sendStatus(InstrumentationResultPrinter.REPORT_VALUE_RESULT_FAILURE, bundle)
    }
}
