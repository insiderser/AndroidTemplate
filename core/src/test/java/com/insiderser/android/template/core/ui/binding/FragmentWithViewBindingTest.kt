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
package com.insiderser.android.template.core.ui.binding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle.State.DESTROYED
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.viewbinding.ViewBinding
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FragmentWithViewBindingTest {

    @RelaxedMockK
    private lateinit var mockView: View

    @MockK
    private lateinit var mockBinding: ViewBinding

    private lateinit var fragment: FakeFragmentWithViewBinding

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { mockBinding.root } returns mockView

        fragment = FakeFragmentWithViewBinding(mockBinding)
    }

    @Test
    fun assert_initialBinding_isNull() {
        assertThat(fragment.binding).isNull()
        assertThrows(IllegalStateException::class.java) {
            fragment.requireBinding()
        }
    }

    @Test
    @Suppress("UNUSED_VARIABLE")
    fun assert_bindingIsCreated() {
        val scenario = launchFragment { fragment }

        assertThat(fragment.binding).isSameInstanceAs(mockBinding)
        assertThat(fragment.requireBinding()).isSameInstanceAs(mockBinding)
        assertThat(fragment.onBindingCreatedCalled).isTrue()
        assertThat(fragment.view).isSameInstanceAs(mockView)
    }

    @Test
    fun assert_destroyedView_bindingIsNull() {
        val scenario = launchFragment { fragment }

        scenario.moveToState(DESTROYED)
        assertThat(fragment.binding).isNull()
        assertThrows(IllegalStateException::class.java) {
            fragment.requireBinding()
        }
    }

    class FakeFragmentWithViewBinding(private val testBinding: ViewBinding) :
        FragmentWithViewBinding<ViewBinding>() {

        var onBindingCreatedCalled = false

        override fun onCreateBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding =
            testBinding

        override fun onBindingCreated(binding: ViewBinding, savedInstanceState: Bundle?) {
            if (onBindingCreatedCalled) {
                throw IllegalStateException("onBindingCreated was called twice")
            }
            onBindingCreatedCalled = true
        }
    }
}
