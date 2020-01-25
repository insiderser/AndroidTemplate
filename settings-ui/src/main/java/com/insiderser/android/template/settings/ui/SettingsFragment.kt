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
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.insiderser.android.template.core.result.EventObserver
import com.insiderser.android.template.core.ui.binding.FragmentWithViewBinding
import com.insiderser.android.template.prefs.data.dagger.PreferencesStorageComponentProvider
import com.insiderser.android.template.settings.ui.dagger.DaggerSettingsComponent
import com.insiderser.android.template.settings.ui.databinding.SettingsFragmentBinding
import com.insiderser.android.template.settings.ui.theme.ThemeSettingDialogFragment
import javax.inject.Inject

/**
 * A [fragment][androidx.fragment.app.Fragment] that lets users to change app's preferences.
 */
class SettingsFragment : FragmentWithViewBinding<SettingsFragmentBinding>() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SettingsFragmentBinding =
        SettingsFragmentBinding.inflate(inflater, container, false)

    override fun onBindingCreated(
        binding: SettingsFragmentBinding,
        savedInstanceState: Bundle?
    ) {
        binding.chooseThemeButton.setOnClickListener {
            viewModel.onThemeSettingClicked()
        }

        viewModel.showThemeSettingDialog.observe(viewLifecycleOwner, EventObserver {
            val dialogFragment = ThemeSettingDialogFragment()
            dialogFragment.show(childFragmentManager)
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
