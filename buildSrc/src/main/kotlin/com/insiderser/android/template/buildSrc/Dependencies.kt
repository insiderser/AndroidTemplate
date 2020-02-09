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
package com.insiderser.android.template.buildSrc

/**
 * A wrapper for high-level version config used in build scripts.
 */
object Versions {
    object Sdk {
        const val compile = 29
        const val target = 29
        const val min = 23
    }

    const val versionName = "1.0.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level

    const val jvmTarget = "1.8"

    const val buildToolsVersion = "29.0.2"

    const val ktlint = "0.36.0"
}

/**
 * A wrapper for all dependencies' notations used in the project together with their versions.
 */
object Libs {
    // Don't forget to update version in buildSrc/build.gradle.kts
    const val androidGradlePlugin = "com.android.tools.build:gradle:3.6.0-rc02"

    const val timber = "com.jakewharton.timber:timber:4.7.1"

    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.2"

    object Test {
        const val junit4 = "junit:junit:4.13"
        const val truth = "com.google.truth:truth:1.0.1"
        const val mockK = "io.mockk:mockk:1.9.3"

        object Robolectric {
            private const val version = "4.3.1"
            const val robolectric = "org.robolectric:robolectric:$version"
            const val annotations = "org.robolectric:annotations:$version"
        }

        object AndroidX {
            private const val version = "1.2.0"
            const val core = "androidx.test:core:$version"
            const val runner = "androidx.test:runner:$version"
            const val rules = "androidx.test:rules:$version"
            const val ext = "androidx.test.ext:junit:1.1.1"
            const val extTruth = "androidx.test.ext:truth:1.2.0"
            const val arch = "androidx.arch.core:core-testing:2.1.0"

            object Espresso {
                private const val version = "3.2.0"
                const val core = "androidx.test.espresso:espresso-core:$version"
                const val intents = "androidx.test.espresso:espresso-intents:$version"
            }
        }
    }

    object Kotlin {
        private const val kotlinVersion = "1.3.61"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"

        object Coroutines {
            private const val version = "1.3.3"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
        }
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.2.0"
        const val appcompat = "androidx.appcompat:appcompat:1.1.0"
        const val material = "com.google.android.material:material:1.1.0"
        const val browser = "androidx.browser:browser:1.2.0"
        const val collection = "androidx.collection:collection-ktx:1.1.0"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0"
        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.0.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta4"
        const val drawerLayout = "androidx.drawerlayout:drawerlayout:1.0.0"
        const val preference = "androidx.preference:preference:1.1.0"
        const val security = "androidx.security:security-crypto:1.0.0-alpha02"

        object Activity {
            private const val version = "1.1.0"
            const val activity = "androidx.activity:activity:$version"
            const val activityKtx = "androidx.activity:activity-ktx:$version"
        }

        object Fragment {
            private const val version = "1.2.1"
            const val fragment = "androidx.fragment:fragment:$version"
            const val fragmentKtx = "androidx.fragment:fragment-ktx:$version"
            const val testing = "androidx.fragment:fragment-testing:$version"
        }

        object Navigation {
            private const val version = "2.2.1"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
        }

        object Paging {
            private const val version = "2.1.1"
            const val common = "androidx.paging:paging-common-ktx:$version"
            const val runtime = "androidx.paging:paging-runtime-ktx:$version"
        }

        object Lifecycle {
            private const val version = "2.2.0"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val compiler = "androidx.lifecycle:lifecycle-compiler:$version"
        }

        object Room {
            private const val version = "2.2.3"
            const val common = "androidx.room:room-common:$version"
            const val runtime = "androidx.room:room-runtime:$version"
            const val compiler = "androidx.room:room-compiler:$version"
            const val ktx = "androidx.room:room-ktx:$version"
            const val testing = "androidx.room:room-testing:$version"
        }

        object Work {
            private const val version = "2.3.0"
            const val runtimeKtx = "androidx.work:work-runtime-ktx:$version"
            const val testing = "androidx.work:work-testing:$version"
        }
    }

    object Dagger {
        private const val version = "2.26"
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

    object Insetter {
        private const val version = "0.2.0"
        const val ktx = "dev.chrisbanes:insetter-ktx:$version"
    }
}
