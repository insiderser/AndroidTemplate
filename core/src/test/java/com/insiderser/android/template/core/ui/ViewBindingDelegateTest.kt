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

package com.insiderser.android.template.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle.State.DESTROYED
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.viewbinding.ViewBinding
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.ref.WeakReference

@RunWith(AndroidJUnit4::class)
class ViewBindingDelegateTest {

    @RelaxedMockK
    private lateinit var mockView: View

    private lateinit var bindingRef: WeakReference<ViewBinding>

    private lateinit var fragment: FakeFragmentWithViewBinding

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        val binding = ViewBinding { mockView }
        bindingRef = WeakReference(binding)

        fragment = FakeFragmentWithViewBinding(binding)
    }

    @Test
    fun whenViewIsNotCreated_getValue_throwsIllegalStateException() {
        assertThrows(IllegalStateException::class.java) {
            fragment.binding
        }
    }

    @Test
    @Suppress("UNUSED_VARIABLE")
    fun whenViewIsCreated_getValue_returnsValue() {
        val scenario = launchFragment { fragment }

        assertThat(fragment.binding).isSameInstanceAs(bindingRef.get())
        assertThat(bindingRef.get()).isNotNull()
    }

    @Test
    fun whenViewIsDestroyed_value_isGCed_and_getValue_throwsIllegalStateException() {
        val scenario = launchFragment { fragment }

        scenario.moveToState(DESTROYED)
        assertThrows(IllegalStateException::class.java) {
            fragment.binding
        }

        System.gc()
        // Check view binding is garbage collected
        assertThat(bindingRef.get()).isNull()
    }

    class FakeFragmentWithViewBinding(testBinding: ViewBinding) : Fragment() {

        private var testBinding: ViewBinding? = testBinding
        var binding: ViewBinding by viewLifecycleScoped()

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = testBinding!!
            testBinding = null
            return binding.root
        }
    }
}
