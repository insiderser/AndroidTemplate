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
package com.insiderser.android.common.ui.binding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle.State.DESTROYED
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.viewbinding.ViewBinding
import com.google.common.truth.Truth.assertThat
import com.insiderser.android.common.ui.dagger.DaggerFragment
import com.insiderser.android.core.dagger.AppComponent
import dagger.android.AndroidInjector
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FragmentWithViewBindingTest {

    @Test
    fun assert_initialBinding_isNull() {
        val fragment = FakeFragmentWithViewBinding()

        assertThat(fragment.binding).isNull()
        assertThrows(IllegalStateException::class.java) {
            fragment.requireBinding()
        }
    }

    @Test
    fun assert_bindingIsCreated() {
        val fragment = FakeFragmentWithViewBinding()
        @Suppress("UNUSED_VARIABLE") val scenario = launchFragment { fragment }

        // Fragment is in the RESUMED state
        assertThat(fragment.binding).isSameInstanceAs(fragment.testBinding)
        assertThat(fragment.requireBinding()).isSameInstanceAs(fragment.testBinding)
        assertThat(fragment.onBindingCreatedCalled).isTrue()
        assertThat(fragment.view).isSameInstanceAs(fragment.testBinding.root)
    }

    @Test
    fun assert_destroyedView_bindingIsNull() {
        val fragment = FakeFragmentWithViewBinding()
        val scenario = launchFragment { fragment }

        scenario.moveToState(DESTROYED)
        assertThat(fragment.binding).isNull()
        assertThrows(IllegalStateException::class.java) {
            fragment.requireBinding()
        }
    }
}

class FakeViewBinding(context: Context) : ViewBinding {
    private val view = View(context)
    override fun getRoot(): View = view
}

class FakeFragmentWithViewBinding : FragmentWithViewBinding<FakeViewBinding>() {

    val testBinding by lazy { FakeViewBinding(requireContext()) }
    var onBindingCreatedCalled = false

    override fun onCreateBinding(inflater: LayoutInflater, container: ViewGroup?): FakeViewBinding =
        testBinding

    override fun onBindingCreated(binding: FakeViewBinding, savedInstanceState: Bundle?) {
        if (onBindingCreatedCalled) {
            throw IllegalStateException("onBindingCreated was called twice")
        }
        onBindingCreatedCalled = true
    }

    override fun provideInjector(appComponent: AppComponent): AndroidInjector<out DaggerFragment> =
        AndroidInjector { }
}
