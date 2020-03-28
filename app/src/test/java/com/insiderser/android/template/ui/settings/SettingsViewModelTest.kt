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

package com.insiderser.android.template.ui.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.insiderser.android.template.core.domain.prefs.theme.GetAvailableThemesUseCase
import com.insiderser.android.template.core.domain.prefs.theme.ObservableThemeUseCase
import com.insiderser.android.template.core.domain.prefs.theme.SetThemeUseCase
import com.insiderser.android.template.core.domain.prefs.theme.Theme
import com.insiderser.android.template.test.await
import com.insiderser.android.template.test.rules.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ObsoleteCoroutinesApi::class)
class SettingsViewModelTest {

    @Rule
    @JvmField
    val executorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val dispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var getAvailableThemesUseCase: GetAvailableThemesUseCase

    @RelaxedMockK
    private lateinit var observableThemeUseCase: ObservableThemeUseCase

    @RelaxedMockK
    private lateinit var setThemeUseCase: SetThemeUseCase

    private val viewModel: SettingsViewModel by lazy {
        SettingsViewModel(
            getAvailableThemesUseCase,
            observableThemeUseCase,
            setThemeUseCase
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun onThemeSettingClicked_triggersShowThemeSettingDialogEvent() {
        assertThat(viewModel.showThemeSettingDialog.value).isNull()

        viewModel.onThemeSettingClicked()

        val event = viewModel.showThemeSettingDialog.await()
        assertThat(event).isNotNull()
    }

    @Test
    fun availableThemes_returnsThemesFromGetAvailableThemesUseCase() {
        val availableThemes = listOf(Theme.DARK, Theme.LIGHT)
        every { getAvailableThemesUseCase() } returns availableThemes
        assertThat(viewModel.availableThemes).containsExactlyElementsIn(availableThemes)
    }

    @Test
    fun selectedTheme_returnsThemeFromObservableThemeUseCase() = dispatcherRule.runBlockingTest {
        val channel = ConflatedBroadcastChannel<Theme>()
        every { observableThemeUseCase() } returns channel.asFlow()

        viewModel.selectedTheme.observeForever { }

        Theme.values().forEach { theme ->
            channel.offer(theme)
            assertThat(viewModel.selectedTheme.value).isEqualTo(theme)
        }
    }

    @Test
    fun setSelectedTheme_callsSetThemeUseCase() = dispatcherRule.runBlockingTest {
        Theme.values().forEach { theme ->
            viewModel.setSelectedTheme(theme)
            coVerify(exactly = 1) { setThemeUseCase(theme) }
        }
        confirmVerified(setThemeUseCase)
    }
}
