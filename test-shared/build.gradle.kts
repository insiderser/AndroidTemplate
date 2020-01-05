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
import com.insiderser.buildSrc.configureAndroidModule

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}

configureAndroidModule()

dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))

    implementation(Libs.Kotlin.stdlib)

    implementation(Libs.AndroidX.Lifecycle.extensions)
}
