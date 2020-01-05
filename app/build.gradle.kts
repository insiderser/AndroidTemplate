/*
 * Copyright 2020 Oleksandr Bezushko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.insiderser.buildSrc.Libs
import com.insiderser.buildSrc.Versions
import com.insiderser.buildSrc.configureAndroidModule
import com.insiderser.buildSrc.configureInstrumentationTests
import com.insiderser.buildSrc.sharedTestImplementation

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}

configureAndroidModule()
configureInstrumentationTests()

android {
    defaultConfig {
        applicationId = "com.insiderser.android.template"
        versionName = Versions.versionName
        versionCode = findProperty("app.versionCode").let {
            if (it is String) it.toInt() else 1
        }

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

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
