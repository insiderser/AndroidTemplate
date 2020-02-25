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
package com.insiderser.android.template.ui

import androidx.lifecycle.ViewModel
import com.insiderser.android.template.core.dagger.FeatureScope
import com.insiderser.android.template.core.dagger.ViewModelKey
import com.insiderser.android.template.feature1.dagger.Feature1Module
import com.insiderser.android.template.feature1.ui.Feature1Fragment
import com.insiderser.android.template.settings.dagger.SettingsModule
import com.insiderser.android.template.settings.ui.SettingsFragment
import com.insiderser.android.template.settings.ui.theme.ThemeSettingDialogFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * A Dagger module for [MainActivity].
 */
@Module
internal interface MainActivityModule {

    /**
     * Tell Dagger that we want [MainActivityViewModel] to be bound into [MainActivity]
     * using [ViewModelFactory][com.insiderser.android.template.core.dagger.ViewModelFactory].
     */
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    fun bindMainViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel

    @FeatureScope
    @ContributesAndroidInjector(modules = [Feature1Module::class])
    fun feature1Fragment(): Feature1Fragment

    @FeatureScope
    @ContributesAndroidInjector(modules = [SettingsModule::class])
    fun settingsFragment(): SettingsFragment

    // If needed, make this as subcomponent of settings subcomponent
    @FeatureScope
    @ContributesAndroidInjector(modules = [SettingsModule::class])
    fun themeSettingDialogFragment(): ThemeSettingDialogFragment
}
