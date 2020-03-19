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

package com.insiderser.android.template.core.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test

class EventObserverTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testEventObserverWithLiveData() {
        val instance = Any()
        val event = Event(instance)
        val liveData = MutableLiveData(event)

        var called = false
        val victim = EventObserver<Any> {
            assertThat(called).isFalse() // Check not called multiple times
            assertThat(it).isSameInstanceAs(instance)
            called = true
        }

        liveData.observeForever(victim)
        assertThat(called).isTrue()
        assertThat(event.hasBeenHandled).isTrue()
        liveData.removeObserver(victim)

        // Check event is dispatched only once
        called = false
        liveData.observeForever(victim)
        assertThat(called).isFalse()
    }

    @Test
    fun testEventObserverWithLiveData_nullValue() {
        val liveData = MutableLiveData<Event<Any>>(null)
        liveData.observeForever(EventObserver {
            fail("Should never be called on null values")
        })
    }
}
