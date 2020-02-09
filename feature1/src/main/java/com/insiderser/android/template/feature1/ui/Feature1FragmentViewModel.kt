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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.insiderser.android.template.core.dagger.AssistedViewModelFactory
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

/**
 * A [ViewModel] for [Feature1Fragment] that demonstrates how to use dagger to create
 * SavedState ViewModel.
 */
internal class Feature1FragmentViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * Tells AssistedInject to generate a factory for [Feature1FragmentViewModel].
     * Must be inside of the target [ViewModel] class.
     */
    @AssistedInject.Factory
    interface Factory : AssistedViewModelFactory<Feature1FragmentViewModel> {
        // TODO remove create() override after https://github.com/square/AssistedInject/pull/121 is merged
        override fun create(savedStateHandle: SavedStateHandle): Feature1FragmentViewModel
    }
}
