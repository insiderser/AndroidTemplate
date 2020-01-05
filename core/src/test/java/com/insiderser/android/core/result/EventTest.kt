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

class EventTest {

    @Test
    fun testEvent() {
        val someClass = SimpleTestClass()
        val victim = Event(someClass)

        assertThat(victim.hasBeenHandled).isFalse()
        assertThat(victim.getContentIfNotHandled())
                .isSameInstanceAs(someClass)

        assertThat(victim.hasBeenHandled).isTrue()
        assertThat(victim.getContentIfNotHandled()).isNull()
        assertThat(victim.hasBeenHandled).isTrue()

        assertThat(victim.peekContent()).isSameInstanceAs(someClass)
    }
}
