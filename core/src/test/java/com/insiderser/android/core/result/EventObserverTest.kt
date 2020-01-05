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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth.assertThat
import com.insiderser.android.test.shared.util.SimpleTestClass
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test

class EventObserverTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testEventObserverWithLiveData() {
        val someTestClass = SimpleTestClass()
        val someEvent = Event(someTestClass)
        val liveData = MutableLiveData(someEvent)

        var called = false
        val victim = EventObserver<SimpleTestClass> {
            assertThat(called).isFalse() // Check not called multiple times
            assertThat(it).isSameInstanceAs(someTestClass)
            called = true
        }

        liveData.observeForever(victim)
        assertThat(called).isTrue()
        assertThat(someEvent.hasBeenHandled).isTrue()
        liveData.removeObserver(victim)

        // Check event is dispatched only once
        called = false
        liveData.observeForever(victim)
        assertThat(called).isFalse()
    }

    @Test
    fun testEventObserverWithLiveData_nullValue() {
        val liveData = MutableLiveData<Event<SimpleTestClass>>(null)
        liveData.observeForever(EventObserver {
            fail("Should never be called on null values")
        })
    }
}
