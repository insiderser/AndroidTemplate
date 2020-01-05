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

package com.insiderser.android.core.result

import com.google.common.truth.Truth.assertThat
import com.insiderser.android.test.shared.util.SimpleTestClass
import org.junit.Test

class ResultTest {

    @Test
    fun testLoadingResult() {
        val result = Result.Loading

        assertThat(result.succeeded).isFalse()

        val fallback = SimpleTestClass()
        assertThat(result.successOr(fallback))
                .isSameInstanceAs(fallback)
    }

    @Test
    fun testErrorResult() {
        val cause = Throwable("Cannot resolve host")
        val result = Result.Error(cause)

        assertThat(result.cause).isSameInstanceAs(cause)
        assertThat(result.succeeded).isFalse()

        val fallback = SimpleTestClass()
        assertThat(result.successOr(fallback))
                .isSameInstanceAs(fallback)
    }

    @Test
    fun testSuccessResult() {
        val data = SimpleTestClass()
        val result = Result.Success(data)

        assertThat(result.data).isSameInstanceAs(data)
        assertThat(result.succeeded).isTrue()

        val fallback = SimpleTestClass()
        assertThat(result.successOr(fallback))
                .isSameInstanceAs(data)
    }

    @Test
    fun testSuccessResult_nullData() {
        val result = Result.Success<SimpleTestClass?>(null)

        assertThat(result.data).isNull()
        assertThat(result.succeeded).isFalse()

        val fallback = SimpleTestClass()
        assertThat(result.successOr(fallback))
                .isSameInstanceAs(fallback)
    }
}
