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

import com.insiderser.android.template.buildSrc.Libs
import com.insiderser.android.template.buildSrc.configureAndroidModule

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

configureAndroidModule()

dependencies {
    api(project(":model"))

    api(Libs.Kotlin.stdlib)
    api(Libs.Kotlin.Coroutines.core)
    api(Libs.Kotlin.Coroutines.android)

    api(Libs.AndroidX.coreKtx)
    api(Libs.AndroidX.appcompat)
    api(Libs.AndroidX.Lifecycle.extensions)
    api(Libs.AndroidX.Lifecycle.lifecycleKtx)
    api(Libs.AndroidX.Lifecycle.liveDataKtx)
    api(Libs.AndroidX.Lifecycle.viewModelKtx)

    api(Libs.timber)
    api(Libs.Insetter.ktx)

    api(Libs.Dagger.dagger)
    kapt(Libs.Dagger.compiler)

    implementation(Libs.AndroidX.Fragment.fragmentKtx)
    implementation(Libs.AndroidX.material)

    testImplementation(project(":test-shared"))
    testImplementation(Libs.Test.mockK)
    testImplementation(Libs.Kotlin.Coroutines.test)
    testImplementation(Libs.Test.AndroidX.arch)

    // FIXME: should be sharedTestImplementation: https://issuetracker.google.com/issues/127986458
    debugImplementation(Libs.AndroidX.Fragment.testing) {
        exclude(group = "androidx.test", module = "core")
    }
    testImplementation(Libs.Test.Robolectric.robolectric)
    testImplementation(Libs.Test.AndroidX.core)
    testImplementation(Libs.Test.AndroidX.rules)
    testImplementation(Libs.Test.AndroidX.runner)
    testImplementation(Libs.Test.AndroidX.ext)
}
