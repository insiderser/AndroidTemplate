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
package com.insiderser.android.template.prefs.data.domain.theme

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.insiderser.android.template.core.domain.execute
import com.insiderser.android.template.model.Theme
import com.insiderser.android.template.prefs.data.fake.FakeAppPreferencesStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@ObsoleteCoroutinesApi
class ObservableThemeUseCaseTest {

    @Rule
    @JvmField
    val executorRule = InstantTaskExecutorRule()

    private val storage = FakeAppPreferencesStorage()

    private val useCase = ObservableThemeUseCase(storage)

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun test() {
        val observable = useCase.observe()

        var latch = CountDownLatch(1)
        var postedTheme: Theme? = null
        observable.observeForever {
            postedTheme = it
            latch.countDown()
        }

        runBlocking { useCase.execute() }

        // Check default value
        latch.await(500, TimeUnit.MILLISECONDS)
        assertThat(postedTheme).isAnyOf(Theme.AUTO_BATTERY, Theme.FOLLOW_SYSTEM)

        Theme.values().forEach { theme ->
            latch = CountDownLatch(1)
            storage.selectedTheme = theme.storageKey

            latch.await(500, TimeUnit.MILLISECONDS)
            assertThat(postedTheme).isEqualTo(theme)
            assertThat(storage.selectedTheme).isEqualTo(theme.storageKey)
        }

        // Check no more posted values
        postedTheme = null
        latch = CountDownLatch(1)
        latch.await(500, TimeUnit.MILLISECONDS)
        assertThat(postedTheme as Any?).isNull()
    }
}
