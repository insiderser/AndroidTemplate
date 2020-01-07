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

import com.insiderser.buildSrc.Libs
import com.insiderser.buildSrc.configureAndroidModule
import com.insiderser.buildSrc.configureInstrumentationTests
import com.insiderser.buildSrc.sharedTestImplementation

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}

configureAndroidModule()
configureInstrumentationTests()

dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))

    implementation(Libs.Kotlin.stdlib)

    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.Lifecycle.extensions)

    implementation(Libs.timber)

    implementation(Libs.Dagger.dagger)

    testImplementation(Libs.Test.junit4)
    sharedTestImplementation(Libs.Google.truth)
    sharedTestImplementation(project(":test-shared"))
    testImplementation(Libs.Test.MockK.mockK)

    testImplementation(Libs.Test.Robolectric.robolectric)
    sharedTestImplementation(Libs.Test.Robolectric.annotations)

    sharedTestImplementation(Libs.Test.AndroidX.core)
    sharedTestImplementation(Libs.Test.AndroidX.runner)
    sharedTestImplementation(Libs.Test.AndroidX.rules)
    sharedTestImplementation(Libs.Test.AndroidX.ext)
    sharedTestImplementation(Libs.Test.AndroidX.extTruth)
    sharedTestImplementation(Libs.Test.AndroidX.arch)

    sharedTestImplementation(Libs.Test.AndroidX.Espresso.espressoCore)
}
