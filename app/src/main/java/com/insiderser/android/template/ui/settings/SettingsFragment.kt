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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.insiderser.android.template.BuildConfig
import com.insiderser.android.template.R
import com.insiderser.android.template.core.domain.prefs.theme.Theme
import com.insiderser.android.template.core.ui.NavigationHost
import com.insiderser.android.template.core.ui.viewLifecycleScoped
import com.insiderser.android.template.core.util.observeEvent
import com.insiderser.android.template.databinding.SettingsFragmentBinding
import com.insiderser.android.template.ui.settings.SettingsFragmentDirections.Companion.actionSettingsHomeToThemeSettingDialog
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
            findNavController().navigate(actionSettingsHomeToThemeSettingDialog())
        }

        viewModel.selectedTheme.observe(viewLifecycleOwner) { selectedTheme ->
            binding.chooseThemePreference.summary = getTitleForTheme(selectedTheme)
        }
    }
}

/**
 * Get short description of the given [Theme].
 */
internal fun Fragment.getTitleForTheme(theme: Theme): String = getString(
    when (theme) {
        Theme.LIGHT -> R.string.settings_theme_light
        Theme.DARK -> R.string.settings_theme_dark
        Theme.FOLLOW_SYSTEM -> R.string.settings_theme_follow_system
        Theme.AUTO_BATTERY -> R.string.settings_theme_auto_battery
    }
)
