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
package com.insiderser.android.template.settings.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.insiderser.android.template.prefs.domain.theme.DEFAULT_THEME
import com.insiderser.android.template.prefs.domain.theme.GetAvailableThemesUseCase
import com.insiderser.android.template.prefs.domain.theme.ObservableThemeUseCase
import com.insiderser.android.template.prefs.domain.theme.SetThemeUseCase
import com.insiderser.android.template.prefs.domain.theme.Theme
import com.insiderser.android.template.prefs.test.FakeAppPreferencesStorage
import com.insiderser.android.template.test.shared.util.await
import com.jraska.livedata.test
import io.mockk.MockKAnnotations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

@UseExperimental(ObsoleteCoroutinesApi::class)
class SettingsViewModelTest {

    @Rule
    @JvmField
    val executorRule = InstantTaskExecutorRule()

    private val storage = FakeAppPreferencesStorage()

    private lateinit var viewModel: SettingsViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)

        viewModel = SettingsViewModel(
            GetAvailableThemesUseCase(),
            ObservableThemeUseCase(storage),
            SetThemeUseCase(storage)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun onThemeSettingClicked_triggersShowThemeSettingDialogEvent() {
        assertThat(viewModel.showThemeSettingDialog.value).isNull()

        viewModel.onThemeSettingClicked()

        val event = viewModel.showThemeSettingDialog.await()
        assertThat(event).isNotNull()
    }

    @Test
    fun availableThemes_isCorrect() {
        val availableThemes = viewModel.availableThemes.await()
        assertThat(availableThemes).containsExactly(Theme.DARK, Theme.LIGHT, DEFAULT_THEME)
    }

    @Test
    fun setSelectedTheme_updatesPreferencesStorage() {
        val observer = viewModel.selectedTheme.test()
            // Check default value
            .awaitValue(1, TimeUnit.SECONDS)
            .assertValue(DEFAULT_THEME)

        Theme.values().forEach { theme ->
            viewModel.setSelectedTheme(theme)
            observer.awaitNextValue(1, TimeUnit.SECONDS).assertValue(theme)
            assertThat(storage.selectedTheme).isEqualTo(theme.storageKey)
        }
    }
}
