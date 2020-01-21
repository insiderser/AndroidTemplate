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
import org.junit.Assert.assertThrows
import org.junit.Test
import javax.inject.Provider

class ViewModelFactoryTest {

    @Test
    fun assert_ViewModelIsCreated_exactClass() {
        val provider = Provider<ViewModel> { TestViewModelImpl() }
        val victim = ViewModelFactory(mapOf(TestViewModelImpl::class.java to provider))

        val created = victim.create(TestViewModelImpl::class.java)
        assertThat(created).isInstanceOf(TestViewModelImpl::class.java)
    }

    @Test
    fun assert_ViewModelIsCreated_fromSubclass() {
        val provider = Provider<ViewModel> { TestViewModelImpl() }
        val victim = ViewModelFactory(mapOf(TestViewModelImpl::class.java to provider))

        val created = victim.create(TestViewModel::class.java)
        assertThat(created).isInstanceOf(TestViewModelImpl::class.java)
    }

    @Test(expected = NoVMProviderError::class)
    fun assertFails_noViewModelProvider() {
        val victim = ViewModelFactory(emptyMap())
        victim.create(TestViewModel::class.java)
    }

    @Test(expected = NoVMProviderError::class)
    fun assertFails_noViewModelProvider_getSubclassOfProvided() {
        val provider = Provider<ViewModel> { TestViewModel() }
        val victim = ViewModelFactory(mapOf(TestViewModel::class.java to provider))
        victim.create(TestViewModelImpl::class.java)
    }

    @Test
    fun assertFails_throwsException() {
        val message = "Error occurred"
        val provider = Provider<ViewModel> { throw MyException(message) }
        val victim = ViewModelFactory(mapOf(TestViewModelImpl::class.java to provider))

        val exception = assertThrows(RuntimeException::class.java) {
            victim.create(TestViewModelImpl::class.java)
        }
        assertThat(exception.cause).isInstanceOf(MyException::class.java)
        assertThat(exception.cause?.message).isEqualTo(message)
    }

    private open class TestViewModel : ViewModel()

    private class TestViewModelImpl : TestViewModel()

    private class MyException(message: String) : Exception(message)
}
