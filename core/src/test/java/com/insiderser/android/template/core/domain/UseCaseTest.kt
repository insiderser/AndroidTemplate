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
import com.insiderser.android.template.core.result.Result
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UseExperimental(ExperimentalCoroutinesApi::class)
class UseCaseTest {

    @get:Rule
    val executor = InstantTaskExecutorRule()

    @MockK
    private lateinit var fakeParam: FakeParameter

    @MockK
    private lateinit var fakeResult: FakeResult

    @RelaxedMockK
    private lateinit var exception: Exception

    private lateinit var useCaseImpl: FakeUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCaseImpl = spyk(FakeUseCase())
    }

    @Test
    fun assert_executeWasSuccessful_returnsSuccessfulResult_executeNow() = runBlocking {
        coEvery { useCaseImpl.execute(fakeParam) } returns fakeResult
        val result = useCaseImpl.executeNow(fakeParam)

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isSameInstanceAs(fakeResult)
        coVerify(exactly = 1) { useCaseImpl.execute(fakeParam) }
    }

    @Test
    fun assert_executeFailed_returnsErrorResult_executeNow() = runBlocking {
        coEvery { useCaseImpl.execute(fakeParam) } throws exception
        val result = useCaseImpl.executeNow(fakeParam)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).cause).isSameInstanceAs(exception)
        coVerify(exactly = 1) { useCaseImpl.execute(fakeParam) }
    }

    @Test
    fun assert_executeWasSuccessful_returnsSuccessfulResult_invoke() = runBlockingTest {
        coEvery { useCaseImpl.execute(fakeParam) } returns fakeResult
        val channel = Channel<Result<FakeResult>>(UNLIMITED)

        useCaseImpl(fakeParam, channel)
        coVerify(exactly = 1) { useCaseImpl.execute(fakeParam) }

        val firstValue = channel.receive()
        assertThat(firstValue).isInstanceOf(Result.Loading::class.java)

        val secondValue = channel.receive()
        assertThat(secondValue).isInstanceOf(Result.Success::class.java)
        assertThat((secondValue as Result.Success).data).isSameInstanceAs(fakeResult)
    }

    @Test
    fun assert_executeFailed_returnsErrorResult_invoke() = runBlockingTest {
        coEvery { useCaseImpl.execute(fakeParam) } throws exception
        val channel = Channel<Result<FakeResult>>(UNLIMITED)

        useCaseImpl(fakeParam, channel)
        coVerify(exactly = 1) { useCaseImpl.execute(fakeParam) }

        val firstValue = channel.receive()
        assertThat(firstValue).isInstanceOf(Result.Loading::class.java)

        val secondValue = channel.receive()
        assertThat(secondValue).isInstanceOf(Result.Error::class.java)
        assertThat((secondValue as Result.Error).cause).isSameInstanceAs(exception)
    }

    @Test
    fun assert_cancelAll_cancelsPendingJobs() = runBlockingTest {
        coEvery { useCaseImpl.execute(fakeParam) } coAnswers {
            delay(500L)
            fakeResult
        }
        val channel = Channel<Result<FakeResult>>(UNLIMITED)

        useCaseImpl(fakeParam, channel)
        useCaseImpl.cancelAll()

        withTimeoutOrNull(1000L) {
            channel.receive()
            // First value could've been offered, but second shouldn't
            channel.receive()
            fail("Job wasn't cancelled")
        }
    }

    private class FakeUseCase : UseCase<FakeParameter, FakeResult>(Dispatchers.Default) {
        public override suspend fun execute(param: FakeParameter): FakeResult = error("stub!")
    }

    private class FakeParameter
    private class FakeResult
}
