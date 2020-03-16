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
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth.assertThat
import com.insiderser.android.template.core.data.prefs.test.FakeAppPreferencesStorage
import com.insiderser.android.template.core.domain.prefs.theme.DEFAULT_THEME
import com.insiderser.android.template.core.domain.prefs.theme.GetAvailableThemesUseCase
import com.insiderser.android.template.core.domain.prefs.theme.ObservableThemeUseCase
import com.insiderser.android.template.core.domain.prefs.theme.SetThemeUseCase
import com.insiderser.android.template.core.domain.prefs.theme.Theme
import com.insiderser.android.template.test.shared.util.await
import com.jraska.livedata.test
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
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

@OptIn(ObsoleteCoroutinesApi::class)
class SettingsViewModelTest {

    @Rule
    @JvmField
    val executorRule = InstantTaskExecutorRule()

    private val fakeAvailableThemes = listOf(Theme.DARK, Theme.LIGHT, Theme.FOLLOW_SYSTEM)

    @MockK
    private lateinit var getAvailableThemesUseCase: GetAvailableThemesUseCase

    private val storage = FakeAppPreferencesStorage()

    private lateinit var viewModel: SettingsViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)

        every { getAvailableThemesUseCase() } returns MutableLiveData(fakeAvailableThemes)

        viewModel = SettingsViewModel(
            getAvailableThemesUseCase,
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
        assertThat(availableThemes).containsExactlyElementsIn(fakeAvailableThemes)
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
