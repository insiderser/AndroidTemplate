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
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    `android-common`
}

android {
    defaultConfig {
        applicationId = "com.insiderser.template"
        testInstrumentationRunnerArgument("listener", "leakcanary.FailTestOnLeakRunListener")
    }

    @Suppress("UnstableApiUsage")
    buildFeatures {
        viewBinding = true
        buildConfig = true
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
        }
    }

    lintOptions {
        isWarningsAsErrors = true
        isAbortOnError = true
        isCheckDependencies = true
        lintConfig = rootProject.file("lint.xml")
    }
}

dependencies {
    implementation(project(":core"))

    implementation(Dependencies.AndroidX.Navigation.fragment)
    implementation(Dependencies.AndroidX.Navigation.ui)

    // FIXME Why require Libs.Hilt.android if it's already defined in core module?
    implementation(Dependencies.Hilt.android)
    implementation(Dependencies.Hilt.AndroidX.lifecycle)
    kapt(Dependencies.Hilt.compiler)
    kapt(Dependencies.Hilt.AndroidX.compiler)

    debugImplementation(Dependencies.LeakCanary.leakCanary)
    androidTestImplementation(Dependencies.LeakCanary.instrumentation)

    sharedTestImplementation(project(":test-shared"))
    testImplementation(Dependencies.Test.mockK)

    sharedTestImplementation(Dependencies.Test.AndroidX.core)
    sharedTestImplementation(Dependencies.Test.AndroidX.rules)
    sharedTestImplementation(Dependencies.Test.AndroidX.ext)
    sharedTestImplementation(Dependencies.Test.AndroidX.arch)

    androidTestImplementation(Dependencies.Test.AndroidX.Espresso.core)
}
