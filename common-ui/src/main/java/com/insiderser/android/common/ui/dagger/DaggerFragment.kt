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
package com.insiderser.android.common.ui.dagger

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.insiderser.android.core.dagger.AppComponent
import com.insiderser.android.core.dagger.AppComponentProvider
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber

/**
 * A [Fragment] that injects its members in [onCreate].
 */
abstract class DaggerFragment : Fragment(), HasAndroidInjector {

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
     * Returns dagger component that will be used to inject this class.
     */
    protected abstract fun provideInjector(appComponent: AppComponent): AndroidInjector<out DaggerFragment>
}
