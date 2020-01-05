/*
 * Copyright 2020 Oleksandr Bezushko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.insiderser.android.core.dagger

import androidx.lifecycle.ViewModel
import com.google.common.truth.Truth.assertThat
import javax.inject.Provider
import org.junit.Assert
import org.junit.Test

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

        val exception = Assert.assertThrows(RuntimeException::class.java) {
            victim.create(TestViewModelImpl::class.java)
        }
        assertThat(exception)
            .hasCauseThat()
            .isInstanceOf(MyException::class.java)
        assertThat(exception)
            .hasCauseThat()
            .hasMessageThat()
            .isEqualTo(message)
    }
}

private open class TestViewModel : ViewModel()

private class TestViewModelImpl : TestViewModel()

private class MyException(message: String) : Exception(message)
