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

import com.google.common.truth.Truth.assertThat
import com.insiderser.android.template.core.domain.invoke
import com.insiderser.android.template.model.Theme
import com.insiderser.android.template.prefs.data.test.FakeAppPreferencesStorage
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Test

@ObsoleteCoroutinesApi
class ObservableThemeUseCaseTest {

    private val storage = FakeAppPreferencesStorage()

    private val useCase = ObservableThemeUseCase(storage)

    @Test
    fun assert_whenPreferenceUpdated_flowIsUpdated() = runBlocking {
        val channel = ConflatedBroadcastChannel<Theme>()
        val currentThemeSubscription = channel.openSubscription()
        val collectJob = launch {
            useCase.observe().collect { channel.offer(it) }
        }
        useCase()

        val defaultValue = currentThemeSubscription.receiveWithTimeout(250L)
        assertThat(defaultValue).isAnyOf(Theme.AUTO_BATTERY, Theme.FOLLOW_SYSTEM)

        Theme.values().forEach { theme ->
            storage.selectedTheme = theme.storageKey

            val postedTheme = currentThemeSubscription.receiveWithTimeout(250L)
            assertThat(postedTheme).isEqualTo(theme)
            assertThat(storage.selectedTheme).isEqualTo(theme.storageKey)
        }

        channel.cancel()
        collectJob.cancelAndJoin()
    }

    @Throws(TimeoutCancellationException::class)
    private suspend fun <T> ReceiveChannel<T>.receiveWithTimeout(timeoutMs: Long) =
        withTimeout(timeoutMs) {
            receive()
        }
}
