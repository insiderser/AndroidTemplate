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
import com.insiderser.buildSrc.sharedTestImplementation

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

configureAndroidModule()

dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))
    implementation(project(":core"))

    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.Coroutines.core)
    implementation(Libs.Coroutines.android)

    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.constraintlayout)
    implementation(Libs.Google.material)

    implementation(Libs.AndroidX.Lifecycle.extensions)
    implementation(Libs.AndroidX.Lifecycle.viewModelKtx)

    implementation(Libs.AndroidX.Fragment.fragment)
    implementation(Libs.AndroidX.Fragment.fragmentKtx)
    debugImplementation(Libs.AndroidX.Fragment.testing) {
        exclude(group = "androidx.test", module = "core")
    }

    implementation(Libs.timber)

    implementation(Libs.Dagger.dagger)
    kapt(Libs.Dagger.compiler)

    sharedTestImplementation(Libs.Test.junit4)
    sharedTestImplementation(Libs.Google.truth)
    sharedTestImplementation(project(":test-shared"))
    sharedTestImplementation(Libs.Coroutines.test)
    testImplementation(Libs.Test.MockK.mockK)

    sharedTestImplementation(Libs.Test.Robolectric.annotations)
    testImplementation(Libs.Test.Robolectric.robolectric)

    sharedTestImplementation(Libs.Test.AndroidX.core)
    sharedTestImplementation(Libs.Test.AndroidX.runner)
    sharedTestImplementation(Libs.Test.AndroidX.rules)
    sharedTestImplementation(Libs.Test.AndroidX.ext)
    sharedTestImplementation(Libs.Test.AndroidX.extTruth)
    sharedTestImplementation(Libs.Test.AndroidX.arch)

    sharedTestImplementation(Libs.Test.AndroidX.Espresso.espressoCore)
}
