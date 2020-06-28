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

import com.insiderser.template.buildSrc.Dependencies
import com.insiderser.template.buildSrc.sharedTestImplementation

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    `android-common`
}

android {
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf(
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
    }
}

dependencies {
    api(Dependencies.Kotlin.stdlib)
    api(Dependencies.Kotlin.Coroutines.core)
    api(Dependencies.Kotlin.Coroutines.android)

    api(Dependencies.AndroidX.coreKtx)
    api(Dependencies.AndroidX.appcompat)
    api(Dependencies.AndroidX.activity)
    api(Dependencies.AndroidX.Fragment.fragment)
    api(Dependencies.AndroidX.material)
    api(Dependencies.AndroidX.recyclerView)

    api(Dependencies.AndroidX.Lifecycle.extensions)
    api(Dependencies.AndroidX.Lifecycle.lifecycle)
    api(Dependencies.AndroidX.Lifecycle.liveData)
    api(Dependencies.AndroidX.Lifecycle.viewModel)

    api(Dependencies.timber)
    api(Dependencies.Insetter.ktx)

    api(Dependencies.Hilt.android)

    api(Dependencies.AndroidX.Room.runtime)
    api(Dependencies.AndroidX.Room.ktx)
    kapt(Dependencies.AndroidX.Room.compiler)

    sharedTestImplementation(project(":test-shared"))
    testImplementation(Dependencies.Test.mockK)

    sharedTestImplementation(Dependencies.Test.AndroidX.arch)
    sharedTestImplementation(Dependencies.Test.AndroidX.ext)
    sharedTestImplementation(Dependencies.Test.AndroidX.core)
    sharedTestImplementation(Dependencies.Test.AndroidX.rules)

    testImplementation(Dependencies.Test.Robolectric.robolectric)
    debugImplementation(Dependencies.AndroidX.Fragment.testing)
}
