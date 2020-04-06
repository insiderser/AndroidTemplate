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

package com.insiderser.android.template.core.domain.prefs.theme

import com.google.common.truth.Truth.assertThat
import com.insiderser.android.template.core.fakes.FakeAppPreferencesStorage
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class ObservableThemeUseCaseTest {

    private val storage = FakeAppPreferencesStorage()

    private val useCase = ObservableThemeUseCase(storage)

    @Test
    fun whenPreferenceIsUpdated_observable_isUpdated() = runBlockingTest {
        val receivedThemes = mutableListOf<Theme>()
        val receiverJob = launch {
            useCase().collect { receivedThemes.add(it) }
        }

        assertThat(receivedThemes).containsExactly(DEFAULT_THEME)

        Theme.values().forEachIndexed { index, theme ->
            storage.selectedTheme = theme.storageKey

            assertThat(receivedThemes).hasSize(index + 2)
            assertThat(receivedThemes.last()).isSameInstanceAs(theme)
            assertThat(storage.selectedTheme).isEqualTo(theme.storageKey)
        }

        receiverJob.cancelAndJoin()
    }
}
