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
package com.insiderser.android.common.ui.dagger

import androidx.fragment.app.testing.launchFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.insiderser.android.core.dagger.AppComponent
import com.insiderser.android.test.shared.util.SimpleTestClass
import dagger.android.AndroidInjector
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DaggerFragmentTest {

    @Test
    fun assert_dependenciesInjected() {
        val dependency = SimpleTestClass()
        val testComponent = TestComponent(dependency)
        val fragment = TestFragment(testComponent)
        @Suppress("UNUSED_VARIABLE") val scenario = launchFragment { fragment }

        assertThat(fragment.dependency).isSameInstanceAs(dependency)
    }
}

class TestFragment(private val testComponent: TestComponent) : DaggerFragment() {

    var dependency: SimpleTestClass? = null

    override fun provideInjector(appComponent: AppComponent): AndroidInjector<out DaggerFragment> =
        testComponent
}

class TestComponent(private val dependency: SimpleTestClass) : AndroidInjector<TestFragment> {
    override fun inject(instance: TestFragment) {
        instance.dependency = dependency
    }
}
