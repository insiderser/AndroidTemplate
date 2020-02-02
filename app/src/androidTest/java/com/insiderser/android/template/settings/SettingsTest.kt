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
package com.insiderser.android.template.settings

import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.insiderser.android.template.R
import com.insiderser.android.template.model.Theme
import com.insiderser.android.template.test.rules.MainActivityRule
import com.insiderser.android.template.test.rules.TestPreferencesRule
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class SettingsTest {

    @Rule
    @JvmField
    val activityRule = MainActivityRule(R.id.settings)

    @Rule
    @JvmField
    val preferencesRule = TestPreferencesRule {
        selectedTheme = Theme.LIGHT.storageKey
    }

    @Test
    fun assert_clickingOnThemePreference_showsDialog() {
        // Check that we are in the right fragment
        onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.toolbar))))
            .check(matches(withText(R.string.settings_title)))

        onView(allOf(withId(R.id.summary), withParent(withId(R.id.choose_theme_preference))))
            .check(matches(withText(R.string.settings_theme_light)))

        onView(withText(R.string.settings_choose_theme))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withText(R.string.settings_theme_dark))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        onView(allOf(withId(R.id.summary), withParent(withId(R.id.choose_theme_preference))))
            .check(matches(withText(R.string.settings_theme_dark)))
    }
}
