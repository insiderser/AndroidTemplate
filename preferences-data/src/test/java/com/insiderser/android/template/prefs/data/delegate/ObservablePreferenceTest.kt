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
package com.insiderser.android.template.prefs.data.delegate

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ObservablePreferenceTest {

    companion object {
        private const val preferencesName = "prefs"
        private const val preferenceKey = "fake-pref"
        private const val default = "default-value"
    }

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var observablePreference: ObservablePreference<String?>

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        sharedPreferences = context.getSharedPreferences(preferencesName, MODE_PRIVATE)
        observablePreference =
            ObservableStringPreference(lazyOf(sharedPreferences), preferenceKey, default)
    }

    @After
    fun tearDown() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.deleteSharedPreferences(preferencesName)
    }

    @Test
    fun assert_noValue_sendsDefault() = runBlockingTest {
        withTimeout(2000L) {
            val flow = observablePreference.getValue(mockk(), mockk())
            checkValue(flow, default)
        }
    }

    @Test
    fun assert_valueSet_sendsValue() = runBlockingTest {
        withTimeout(2000L) {
            val value = "other value"
            setPreference(value)
            val flow = observablePreference.getValue(mockk(), mockk())
            checkValue(flow, value)

            val nextValue = "yet another value"
            setPreference(nextValue)
            checkValue(flow, nextValue)
        }
    }

    private suspend fun <T> checkValue(flow: Flow<T>, expected: T) {
        assertThat(flow.first()).isEqualTo(expected)
    }

    private fun setPreference(newValue: String?) {
        sharedPreferences.edit(commit = true) {
            putString(preferenceKey, newValue)
        }
    }
}
