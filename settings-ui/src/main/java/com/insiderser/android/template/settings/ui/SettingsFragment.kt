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

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.insiderser.android.template.core.result.EventObserver
import com.insiderser.android.template.prefs.data.dagger.PreferencesStorageComponentProvider
import com.insiderser.android.template.settings.ui.dagger.DaggerSettingsComponent
import com.insiderser.android.template.settings.ui.theme.ThemeSettingDialogFragment
import com.insiderser.android.template.settings.ui.util.consumeOnPreferenceClick
import com.insiderser.android.template.settings.ui.util.findTitleForTheme
import javax.inject.Inject

/**
 * A [fragment][androidx.fragment.app.Fragment] that lets users to change app's preferences.
 */
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        findPreference<Preference>("choose-theme")?.consumeOnPreferenceClick {
            viewModel.onThemeSettingClicked()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showThemeSettingDialog.observe(viewLifecycleOwner, EventObserver {
            val dialogFragment = ThemeSettingDialogFragment()
            dialogFragment.show(childFragmentManager)
        })

        viewModel.selectedTheme.observe(viewLifecycleOwner, Observer { selectedTheme ->
            findPreference<Preference>("choose-theme")?.summary = findTitleForTheme(selectedTheme)
        })
    }

    override fun onAttach(context: Context) {
        injectItself()
        super.onAttach(context)
    }

    private fun injectItself() {
        val preferencesProvider =
            requireActivity().application as PreferencesStorageComponentProvider
        val preferencesDataComponent = preferencesProvider.preferencesStorageComponent
        val settingsComponent = DaggerSettingsComponent.factory().create(preferencesDataComponent)
        settingsComponent.inject(this)
    }
}
