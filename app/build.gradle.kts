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
import com.insiderser.android.template.buildSrc.Versions
import com.insiderser.android.template.buildSrc.configureAndroidModule
import com.insiderser.android.template.buildSrc.sharedTestImplementation

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}

configureAndroidModule()

val ciVersionName = findProperty("app.versionName") as String?
val ciVersionCode = findProperty("app.versionCode") as String?

android {
    defaultConfig {
        applicationId = "com.insiderser.android.template"
        versionName = ciVersionName?.takeIf { it.isNotBlank() } ?: Versions.versionName
        versionCode = ciVersionCode?.takeIf { it.isNotBlank() }?.toInt() ?: 1
    }

    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("debug.jks")
            storePassword = "android"
            keyAlias = "android"
            keyPassword = "android"
        }
    }

    sourceSets {
        findByName("test")!!.apply {
            java.srcDir("src/sharedTest/java")
        }
        findByName("androidTest")!!.apply {
            java.srcDir("src/sharedTest/java")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("debug")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":core"))
    implementation(project(":feature1"))
    implementation(project(":preferences-data"))

    implementation(Libs.AndroidX.coordinatorLayout)
    implementation(Libs.AndroidX.material)

    implementation(Libs.AndroidX.Activity.activityKtx)
    implementation(Libs.AndroidX.Fragment.fragmentKtx)

    implementation(Libs.edgeToEdge)

    implementation(Libs.AndroidX.Navigation.ui)
    implementation(Libs.AndroidX.Navigation.fragment)

    implementation(Libs.Dagger.androidSupport)
    kapt(Libs.Dagger.compiler)
    kapt(Libs.Dagger.androidProcessor)

    debugImplementation(Libs.leakCanary)

    sharedTestImplementation(project(":test-shared"))
    testImplementation(Libs.Test.mockK)

    sharedTestImplementation(Libs.Test.Robolectric.annotations)
    testImplementation(Libs.Test.Robolectric.robolectric)

    // FIXME: should be sharedTestImplementation: https://issuetracker.google.com/issues/127986458
    debugImplementation(Libs.AndroidX.Fragment.testing) {
        exclude(group = "androidx.test", module = "core")
    }

    sharedTestImplementation(Libs.Test.AndroidX.core)
    sharedTestImplementation(Libs.Test.AndroidX.runner)
    sharedTestImplementation(Libs.Test.AndroidX.rules)
    sharedTestImplementation(Libs.Test.AndroidX.ext)
    sharedTestImplementation(Libs.Test.AndroidX.arch)

    sharedTestImplementation(Libs.Test.AndroidX.Espresso.espressoCore)
}
