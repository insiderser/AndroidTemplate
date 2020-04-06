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

package com.insiderser.android.template.core.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UseCaseTest {

    @Rule
    @JvmField
    val executor = InstantTaskExecutorRule()

    @MockK
    private lateinit var fakeParam: FakeParameter

    @MockK
    private lateinit var fakeResult: FakeResult

    @MockK
    private lateinit var exception: Exception

    @SpyK
    private var useCaseImpl = FakeUseCaseImpl()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun whenExecuteIsSuccessful_invoke_returnsSuccess() = runBlockingTest {
        useCaseImpl.shouldReturnError = false

        val result = useCaseImpl(fakeParam)
        coVerify(exactly = 1) { useCaseImpl["execute"](fakeParam) }
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isSameInstanceAs(fakeResult)
    }

    @Test
    fun whenExecuteFails_invoke_returnsError() = runBlockingTest {
        useCaseImpl.shouldReturnError = true

        val result = useCaseImpl(fakeParam)
        coVerify(exactly = 1) { useCaseImpl["execute"](fakeParam) }

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isSameInstanceAs(exception)
    }

    private inner class FakeUseCaseImpl : UseCase<FakeParameter, FakeResult>() {

        var shouldReturnError: Boolean = false

        override suspend fun execute(param: FakeParameter): FakeResult {
            if (shouldReturnError) {
                throw exception
            } else {
                return fakeResult
            }
        }
    }

    private class FakeParameter
    private class FakeResult
}
