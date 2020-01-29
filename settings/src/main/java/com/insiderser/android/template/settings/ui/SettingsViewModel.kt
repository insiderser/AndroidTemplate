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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.insiderser.android.template.core.domain.invoke
import com.insiderser.android.template.core.result.Event
import com.insiderser.android.template.model.Theme
import com.insiderser.android.template.prefs.data.domain.theme.ObservableThemeUseCase
import com.insiderser.android.template.prefs.data.domain.theme.SetThemeUseCase
import com.insiderser.android.template.settings.domain.GetAvailableThemesUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] for [SettingsFragment] that manages preferences.
 */
internal class SettingsViewModel @Inject constructor(
    getAvailableThemesUseCase: GetAvailableThemesUseCase,
    observableThemeUseCase: ObservableThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase
) : ViewModel() {

    private val _openThemeSettingDialog = MutableLiveData<Event<Unit>>()
    /**
     * Tells the fragment when to show
     * [ThemeSettingDialogFragment][com.insiderser.android.template.settings.ui.theme.ThemeSettingDialogFragment].
     */
    val showThemeSettingDialog: LiveData<Event<Unit>> get() = _openThemeSettingDialog

    /** All themes that user can choose from. */
    val availableThemes: LiveData<List<Theme>> = getAvailableThemesUseCase()

    /** Currently selected theme by the user, or (if nothing selected) a default value. */
    val selectedTheme: LiveData<Theme> = observableThemeUseCase.observe()
        .asLiveData(viewModelScope.coroutineContext)

    /**
     * Set given [theme][Theme] as app's theme.
     */
    fun setSelectedTheme(theme: Theme) {
        setThemeUseCase(theme)
    }

    /**
     * Called when user clicks on "Choose theme" setting.
     */
    fun onThemeSettingClicked() {
        _openThemeSettingDialog.value = Event(Unit)
    }

    init {
        viewModelScope.launch {
            observableThemeUseCase()
        }
    }
}
