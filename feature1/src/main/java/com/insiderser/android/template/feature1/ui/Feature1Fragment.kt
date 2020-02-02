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
package com.insiderser.android.template.feature1.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.insiderser.android.template.core.dagger.CoreComponentProvider
import com.insiderser.android.template.core.ui.binding.FragmentWithViewBinding
import com.insiderser.android.template.feature1.dagger.DaggerFeature1Component
import com.insiderser.android.template.feature1.databinding.Feature1FragmentBinding
import com.insiderser.android.template.navigation.NavigationHost
import dev.chrisbanes.insetter.doOnApplyWindowInsets

/**
 * Sample [Fragment] that does nothing, except injecting into itself.
 */
class Feature1Fragment : FragmentWithViewBinding<Feature1FragmentBinding>() {

    override fun onAttach(context: Context) {
        injectItself()
        super.onAttach(context)
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): Feature1FragmentBinding = Feature1FragmentBinding.inflate(inflater, container, false)

    override fun onBindingCreated(binding: Feature1FragmentBinding, savedInstanceState: Bundle?) {
        (activity as? NavigationHost)?.registerToolbarWithNavigation(binding.toolbar)

        binding.statusBar.doOnApplyWindowInsets { view, insets, _ ->
            view.updateLayoutParams<LinearLayout.LayoutParams> {
                height = insets.systemWindowInsetTop
            }
        }
    }

    private fun injectItself() {
        val coreComponentProvider = requireActivity().application as CoreComponentProvider
        val coreComponent = coreComponentProvider.coreComponent

        val feature1Component = DaggerFeature1Component.factory().create(coreComponent)
        feature1Component.inject(this)
    }
}
