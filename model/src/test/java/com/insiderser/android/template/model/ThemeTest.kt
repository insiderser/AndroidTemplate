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
package com.insiderser.android.template.model

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class ThemeTest {

    @Test
    fun assert_storageKey_isCorrect() {
        checkStorageKey(Theme.FOLLOW_SYSTEM, "system")
        checkStorageKey(Theme.AUTO_BATTERY, "battery")
        checkStorageKey(Theme.DARK, "dark")
        checkStorageKey(Theme.LIGHT, "light")
    }

    private fun checkStorageKey(theme: Theme, storageKey: String) {
        assertThat(theme.storageKey).isEqualTo(storageKey)
    }

    @Test
    fun assert_fromStorageKey_findsTheme() {
        for (theme in Theme.values()) {
            assertThat(Theme.fromStorageKey(theme.storageKey)).isSameInstanceAs(theme)
        }

        assertThrows(NoSuchElementException::class.java) {
            Theme.fromStorageKey("")
        }
    }
}
