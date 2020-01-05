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

package com.insiderser.android.core.util

import com.google.common.truth.Truth.assertThat
import com.insiderser.android.test.shared.util.SimpleTestClass
import org.junit.Test

class ExtensionsKtTest {

    // Jacoco currently doesn't understand Kotlin inline functions,
    // so they are marked as not-covered. See https://github.com/jacoco/jacoco/issues/654
    @Test
    fun consume() {
        var called = false
        val result = consume {
            assertThat(called).isFalse() // Check not called multiple times
            called = true
        }
        assertThat(called).isTrue()
        assertThat(result).isTrue()
    }

    @Test
    fun checkAllMatched() {
        val obj = SimpleTestClass()
        assertThat(obj.checkAllMatched).isEqualTo(obj)
    }
}
