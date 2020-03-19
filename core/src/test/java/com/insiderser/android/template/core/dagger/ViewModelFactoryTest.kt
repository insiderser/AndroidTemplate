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

package com.insiderser.android.template.core.dagger

import androidx.lifecycle.ViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import javax.inject.Provider

class ViewModelFactoryTest {

    @Test
    fun givenViewModelProvider_create_returnsViewModel() {
        val provider = Provider<ViewModel> { TestViewModel() }
        val victim = ViewModelFactory(mapOf(TestViewModel::class.java to provider))

        val created = victim.create(TestViewModel::class.java)
        assertThat(created).isInstanceOf(TestViewModel::class.java)
    }

    @Test(expected = Error::class)
    fun givenNoViewModelProvider_create_fails() {
        val victim = ViewModelFactory(emptyMap())
        victim.create(TestViewModel::class.java)
    }

    private class TestViewModel : ViewModel()
}
