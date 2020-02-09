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

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.insiderser.android.template.R
import com.insiderser.android.template.databinding.ActivityMainBinding
import com.insiderser.android.template.navigation.NavigationHost
import dagger.android.support.DaggerAppCompatActivity
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import javax.inject.Inject

/**
 * The main activity and navigation point.
 */
class MainActivity : DaggerAppCompatActivity(), NavigationHost {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainActivityViewModel by viewModels { viewModelFactory }

    private lateinit var binding: ActivityMainBinding

    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(navController.graph)
    }

    private val navController: NavController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navigation_view)
            as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navigationView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        binding.root.doOnApplyWindowInsets { view, insets, initial ->
            // Avoid drawing behind navigation bar in landscape with button mode
            view.updatePadding(
                left = initial.paddings.left + insets.systemWindowInsetLeft,
                right = initial.paddings.right + insets.systemWindowInsetRight
            )
        }

        if (savedInstanceState == null && intent.hasExtra(EXTRA_DESTINATION)) {
            navigateTo(intent.getIntExtra(EXTRA_DESTINATION, 0))
        }
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        toolbar.setupWithNavController(navController, appBarConfiguration)
        toolbar.setOnMenuItemClickListener { it.onNavDestinationSelected(navController) }
    }

    private fun navigateTo(@IdRes destination: Int) {
        navController.navigate(destination)
    }

    companion object {

        const val EXTRA_DESTINATION = "extra.destination"
    }
}
