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
import com.insiderser.android.template.buildSrc.sharedTestImplementation

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

configureAndroidModule()

android {
    defaultConfig {
        applicationId = "com.insiderser.android.template"

        testInstrumentationRunnerArgument(
            "listener",
            "com.insiderser.android.template.test.listeners.CrashingRunListener"
        )
    }

    signingConfigs {
        named("debug") {
            storeFile = rootProject.file("debug.jks")
            storePassword = "android"
            keyAlias = "android"
            keyPassword = "android"
        }
    }

    buildTypes {
        named("debug") {
            signingConfig = signingConfigs.getByName("debug")
            applicationIdSuffix = ".debug"
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":core"))
    implementation(project(":preferences-data"))
    implementation(project(":navigation"))
    implementation(project(":feature1"))
    implementation(project(":settings"))

    implementation(Libs.AndroidX.material)
    implementation(Libs.AndroidX.Activity.activityKtx)
    implementation(Libs.AndroidX.Fragment.fragmentKtx)

    implementation(Libs.Dagger.androidSupport)
    kapt(Libs.Dagger.compiler)
    kapt(Libs.Dagger.androidProcessor)

    debugImplementation(Libs.leakCanary)

    sharedTestImplementation(project(":test-shared"))

    sharedTestImplementation(Libs.Test.AndroidX.core)
    sharedTestImplementation(Libs.Test.AndroidX.runner)
    sharedTestImplementation(Libs.Test.AndroidX.rules)
    sharedTestImplementation(Libs.Test.AndroidX.ext)
    sharedTestImplementation(Libs.Test.AndroidX.extTruth)
    sharedTestImplementation(Libs.Test.AndroidX.arch)

    androidTestImplementation(Libs.Test.AndroidX.Espresso.core)
    androidTestImplementation(Libs.Test.AndroidX.Espresso.intents)
}
