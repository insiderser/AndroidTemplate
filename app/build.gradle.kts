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
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}

configureAndroidModule()

val releaseKeystore = rootProject.file("release/release.jks")
val useReleaseKeystore = releaseKeystore.exists()

val ciVersionName = findProperty("app.versionName") as String?
val ciVersionCode = findProperty("app.versionCode") as String?

android {
    defaultConfig {
        applicationId = "com.insiderser.android.template"
        versionName = ciVersionName ?: "1.0.0-dev"
        versionCode = ciVersionCode?.toInt() ?: 1

        // TODO: if you use room
//        javaCompileOptions {
//            annotationProcessorOptions {
//                arguments = mapOf(
//                        "room.schemaLocation" to "$projectDir/schemas",
//                        "room.incremental" to "true",
//                        "room.expandProjection" to "true"
//                )
//            }
//        }
    }

    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("release/debug.jks")
            storePassword = "android"
            keyAlias = "android"
            keyPassword = "android"
        }

        if (releaseKeystore.exists()) {
            create("release") {
                storeFile = releaseKeystore
                storePassword = findProperty("SIGN_STORE_PASSWORD") as String?
                keyAlias = findProperty("SIGN_KEY_ALIAS") as String?
                keyPassword = findProperty("SIGN_KEY_PASSWORD") as String?
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            signingConfig =
                if (useReleaseKeystore) signingConfigs.getByName("release")
                else signingConfigs.getByName("debug")

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
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))

    implementation(project(":core"))

    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.Coroutines.core)
    implementation(Libs.Coroutines.android)

    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.coordinatorLayout)
    implementation(Libs.Google.material)

    implementation(Libs.AndroidX.Lifecycle.extensions)
    implementation(Libs.AndroidX.Lifecycle.viewModelKtx)

    implementation(Libs.AndroidX.Activity.activity)
    implementation(Libs.AndroidX.Activity.activityKtx)

    implementation(Libs.AndroidX.Fragment.fragment)
    implementation(Libs.AndroidX.Fragment.fragmentKtx)
    // FIXME: should be sharedTestImplementation: https://issuetracker.google.com/issues/127986458
    debugImplementation(Libs.AndroidX.Fragment.testing) {
        exclude(group = "androidx.test", module = "core")
    }

    implementation(Libs.AndroidX.Navigation.ui)
    implementation(Libs.AndroidX.Navigation.fragment)

    implementation(Libs.timber)

    implementation(Libs.Dagger.dagger)
    implementation(Libs.Dagger.androidSupport)
    kapt(Libs.Dagger.compiler)
    kapt(Libs.Dagger.androidProcessor)

    debugImplementation(Libs.leakCanary)

    sharedTestImplementation(Libs.Test.junit4)
    sharedTestImplementation(Libs.Google.truth)
    sharedTestImplementation(project(":test-shared"))
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
