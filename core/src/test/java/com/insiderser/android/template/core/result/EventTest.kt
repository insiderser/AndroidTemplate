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

class EventTest {

    @Test
    fun testEvent() {
        val someClass = SimpleTestClass()
        val victim = Event(someClass)

        assertThat(victim.hasBeenHandled).isFalse()
        assertThat(victim.getContentIfNotHandled()).isSameInstanceAs(someClass)

        assertThat(victim.hasBeenHandled).isTrue()
        assertThat(victim.getContentIfNotHandled()).isNull()
        assertThat(victim.hasBeenHandled).isTrue()

        assertThat(victim.peekContent()).isSameInstanceAs(someClass)
    }
}
