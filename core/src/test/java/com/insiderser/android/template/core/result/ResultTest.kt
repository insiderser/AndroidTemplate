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
package com.insiderser.android.template.core.result

import com.google.common.truth.Truth.assertThat
import com.insiderser.android.template.test.shared.util.SimpleTestClass
import org.junit.Test

class ResultTest {

    @Test
    fun testLoadingResult() {
        val result = Result.Loading

        assertThat(result.succeeded).isFalse()

        val fallback = SimpleTestClass()
        assertThat(result.successOr(fallback)).isSameInstanceAs(fallback)
    }

    @Test
    fun testErrorResult() {
        val cause = Throwable("Cannot resolve host")
        val result = Result.Error(cause)

        assertThat(result.cause).isSameInstanceAs(cause)
        assertThat(result.succeeded).isFalse()

        val fallback = SimpleTestClass()
        assertThat(result.successOr(fallback)).isSameInstanceAs(fallback)
    }

    @Test
    fun testSuccessResult() {
        val data = SimpleTestClass()
        val result = Result.Success(data)

        assertThat(result.data).isSameInstanceAs(data)
        assertThat(result.succeeded).isTrue()

        val fallback = SimpleTestClass()
        assertThat(result.successOr(fallback)).isSameInstanceAs(data)
    }

    @Test
    fun testSuccessResult_nullData() {
        val result = Result.Success<SimpleTestClass?>(null)

        assertThat(result.data).isNull()
        assertThat(result.succeeded).isFalse()

        val fallback = SimpleTestClass()
        assertThat(result.successOr(fallback)).isSameInstanceAs(fallback)
    }
}
