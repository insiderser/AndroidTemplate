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

@file:Suppress("PublicApiImplicitType")

package com.insiderser.android.template.buildSrc

import org.gradle.api.JavaVersion

object Versions {
    object Sdk {
        const val compile = 29
        const val target = 29
        const val min = 23
    }

    const val versionName = "0.1.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level
    const val versionCode = 1

    @JvmField
    val jvmTarget = JavaVersion.VERSION_1_8

    const val buildToolsVersion = "29.0.3"

    const val ktlint = "0.36.0"
}

object Libs {
    // Also used in buildSrc/build.gradle.kts
    const val androidGradlePlugin = "com.android.tools.build:gradle:4.0.0"

    const val timber = "com.jakewharton.timber:timber:4.7.1"

    object Test {
        const val junit4 = "junit:junit:4.13"
        const val truth = "com.google.truth:truth:1.0.1"
        const val mockK = "io.mockk:mockk:1.10.0"

        object Robolectric {
            private const val version = "4.3.1"
            const val robolectric = "org.robolectric:robolectric:$version"
        }

        object AndroidX {
            const val core = "androidx.test:core-ktx:1.3.0-rc01"
            const val rules = "androidx.test:rules:1.3.0-rc01"
            const val ext = "androidx.test.ext:junit-ktx:1.1.2-rc01"
            const val arch = "androidx.arch.core:core-testing:2.1.0"

            object Espresso {
                private const val version = "3.3.0-rc01"
                const val core = "androidx.test.espresso:espresso-core:$version"
            }
        }
    }

    object Kotlin {
        private const val kotlinVersion = "1.3.72"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"

        object Coroutines {
            private const val version = "1.3.7"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
        }
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.3.0"
        const val appcompat = "androidx.appcompat:appcompat:1.2.0-rc01"
        const val activity = "androidx.activity:activity-ktx:1.1.0"
        const val material = "com.google.android.material:material:1.1.0"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0"

        object Fragment {
            private const val version = "1.2.4"
            const val fragment = "androidx.fragment:fragment-ktx:$version"
            const val testing = "androidx.fragment:fragment-testing:$version"
        }

        object Navigation {
            private const val version = "2.2.2"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
        }

        object Lifecycle {
            private const val version = "2.2.0"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        }

        object Room {
            private const val version = "2.2.5"
            const val runtime = "androidx.room:room-runtime:$version"
            const val compiler = "androidx.room:room-compiler:$version"
            const val ktx = "androidx.room:room-ktx:$version"
        }
    }

    object Dagger {
        private const val version = "2.28"
        const val dagger = "com.google.dagger:dagger:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"
        const val androidSupport = "com.google.dagger:dagger-android-support:$version"
        const val androidProcessor = "com.google.dagger:dagger-android-processor:$version"
    }

    object Retrofit {
        private const val version = "2.7.1"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val mock = "com.squareup.retrofit2:retrofit-mock:$version"
        const val moshiConverter = "com.squareup.retrofit2:converter-moshi:$version"
    }

    object OkHttp {
        private const val version = "4.3.1"
        const val okHttp = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
        const val mockWebServer = "com.squareup.okhttp3:mockwebserver:$version"
    }

    object Moshi {
        private const val version = "1.9.2"
        const val moshi = "com.squareup.moshi:moshi:$version"
        const val codeGenerator = "com.squareup.moshi:moshi-kotlin-codegen:$version"
    }

    object LeakCanary {
        private const val version = "2.3"
        const val leakCanary = "com.squareup.leakcanary:leakcanary-android:$version"
        const val instrumentation =
            "com.squareup.leakcanary:leakcanary-android-instrumentation:$version"
    }

    object Insetter {
        private const val version = "0.2.2"
        const val ktx = "dev.chrisbanes:insetter-ktx:$version"
    }
}
