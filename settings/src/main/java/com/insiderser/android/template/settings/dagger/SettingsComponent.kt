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
package com.insiderser.android.template.settings.dagger

import androidx.annotation.VisibleForTesting
import com.insiderser.android.template.core.dagger.FeatureScope
import com.insiderser.android.template.core.dagger.ViewModelFactoryModule
import com.insiderser.android.template.prefs.data.dagger.PreferencesStorageComponent
import com.insiderser.android.template.settings.ui.SettingsFragment
import com.insiderser.android.template.settings.ui.SettingsViewModel
import com.insiderser.android.template.settings.ui.theme.ThemeSettingDialogFragment
import dagger.Component

/**
 * Dagger component for settings.
 * @see DaggerSettingsComponent.factory
 */
@Component(
    modules = [
        SettingsViewModelModule::class,
        ViewModelFactoryModule::class
    ],
    dependencies = [PreferencesStorageComponent::class]
)
@FeatureScope
internal interface SettingsComponent {

    fun inject(fragment: SettingsFragment)
    fun inject(fragment: ThemeSettingDialogFragment)

    @VisibleForTesting
    val settingsViewModel: SettingsViewModel

    @Component.Factory
    interface Factory {

        fun create(
            preferencesStorageComponent: PreferencesStorageComponent
        ): SettingsComponent
    }
}
