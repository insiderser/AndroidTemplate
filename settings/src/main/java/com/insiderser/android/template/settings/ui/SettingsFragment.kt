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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.insiderser.android.template.core.result.observeEvent
import com.insiderser.android.template.core.ui.NavigationHost
import com.insiderser.android.template.core.ui.viewLifecycleScoped
import com.insiderser.android.template.settings.BuildConfig
import com.insiderser.android.template.settings.databinding.SettingsFragmentBinding
import com.insiderser.android.template.settings.ui.theme.ThemeSettingDialogFragment
import com.insiderser.android.template.settings.ui.theme.getTitleForTheme
import dagger.android.support.DaggerFragment
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import javax.inject.Inject

/**
 * A root [fragment][androidx.fragment.app.Fragment] that displays a list of preferences.
 * All preferences are stored in preferences storage.
 */
class SettingsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    private var binding: SettingsFragmentBinding by viewLifecycleScoped()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        SettingsFragmentBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as? NavigationHost)?.registerToolbarWithNavigation(binding.toolbar)

        binding.appBar.doOnApplyWindowInsets { appBar, insets, initial ->
            appBar.updatePadding(top = initial.paddings.top + insets.systemWindowInsetTop)
        }

        binding.scrollView.doOnApplyWindowInsets { scrollView, insets, initial ->
            scrollView.updatePadding(bottom = initial.paddings.bottom + insets.systemWindowInsetBottom)
        }

        binding.chooseThemePreference.setOnClickListener {
            viewModel.onThemeSettingClicked()
        }

        binding.versionName.summary = BuildConfig.VERSION_NAME

        viewModel.showThemeSettingDialog.observeEvent(viewLifecycleOwner) {
            val dialogFragment = ThemeSettingDialogFragment()
            dialogFragment.show(childFragmentManager)
        }

        viewModel.selectedTheme.observe(viewLifecycleOwner) { selectedTheme ->
            binding.chooseThemePreference.summary = getTitleForTheme(selectedTheme)
        }
    }
}
