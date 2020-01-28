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
import com.insiderser.android.template.model.Theme
import com.insiderser.android.template.prefs.data.AppPreferencesStorage
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class GetThemeUseCaseTest {

    @MockK
    private lateinit var preferencesStorage: AppPreferencesStorage

    private lateinit var getThemeUseCase: GetThemeUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getThemeUseCase = GetThemeUseCase(preferencesStorage)
    }

    @Test
    fun assert_whenThemeKeyIsSet_execute_returnsTheme() = runBlockingTest {
        Theme.values().forEach { checkForTheme(it) }
    }

    @Test
    fun assert_whenThemeKeyIsNull_execute_returnsDefaultTheme() = runBlockingTest {
        checkForTheme(null)
    }

    private suspend fun checkForTheme(theme: Theme?) {
        every { preferencesStorage.selectedTheme } returns theme?.storageKey
        val returnedTheme = getThemeUseCase.execute(Unit)

        if (theme != null) {
            assertThat(returnedTheme).isSameInstanceAs(theme)
        } else {
            assertThat(returnedTheme).isSameInstanceAs(DEFAULT_THEME)
        }
    }
}
