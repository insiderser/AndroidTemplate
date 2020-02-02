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
package com.insiderser.android.template.test.rules

import androidx.test.core.app.ApplicationProvider
import com.insiderser.android.template.prefs.data.AppPreferencesStorage
import com.insiderser.android.template.prefs.data.AppPreferencesStorageImpl
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * A simple test rule that resets app preferences to default or testable.
 * For example, we don't want to show boarding screen every time we launch a test.
 * You can set custom preferences by passing configuration function to a constructor.
 */
class TestPreferencesRule(
    private val configurator: (AppPreferencesStorage.() -> Unit)? = null
) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        AppPreferencesStorageImpl(ApplicationProvider.getApplicationContext()).run {
            selectedTheme = null

            configurator?.invoke(this)
        }
    }
}
