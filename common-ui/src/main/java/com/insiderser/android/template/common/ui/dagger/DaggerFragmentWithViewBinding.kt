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
package com.insiderser.android.template.common.ui.dagger

import android.content.Context
import androidx.viewbinding.ViewBinding
import com.insiderser.android.template.common.ui.binding.FragmentWithViewBinding
import dagger.android.AndroidInjector
import timber.log.Timber

/**
 * A [FragmentWithViewBinding] that injects its members in [onAttach].
 *
 * Differences from [dagger.android.support.DaggerFragment]:
 *   - allows subclasses to choose what dagger component should be used to inject this class
 *   - implements [FragmentWithViewBinding]
 *
 * @see FragmentWithViewBinding
 */
abstract class DaggerFragmentWithViewBinding<B : ViewBinding> : FragmentWithViewBinding<B>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.v("Trying to inject ${javaClass.canonicalName}")
        androidInjector().inject(this)
        super.onCreate(savedInstanceState)
    }

    @Suppress("UNCHECKED_CAST")
    final override fun androidInjector(): AndroidInjector<Any> {
        val application = requireActivity().application

        check(application is AppComponentProvider) {
            "Your application must implement AppComponentProvider"
        }

        val appComponent = application.appComponent()
        return provideInjector(appComponent) as AndroidInjector<Any>
    }

    /**
     * @return A dagger component that will be used to inject this class.
     */
    protected abstract fun provideInjector(appComponent: AppComponent): AndroidInjector<out DaggerFragment>
}
