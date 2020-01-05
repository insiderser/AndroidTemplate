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

package com.insiderser.buildSrc

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.findByType

/**
 * Add dependency to both `testImplementation` and `androidTestImplementation`
 */
fun DependencyHandler.sharedTestImplementation(dependencyNotation: Any) {
    add("testImplementation", dependencyNotation)
    add("androidTestImplementation", dependencyNotation)
}

/**
 * Convenience method that loads [android extension][BaseExtension].
 */
private fun Project.android(action: BaseExtension.() -> Unit) {
    val androidExtension = extensions.findByType(BaseExtension::class)
        ?: throw AndroidExtensionNotFoundError("Should be called after applying android plugin")
    androidExtension.apply(action)
}

/**
 * Denotes that Android extension was not found. This means that `Android Gradle plugin`
 * hadn't been applied to this [project][Project].
 */
class AndroidExtensionNotFoundError(message: String) : Error(message)

/**
 * Apply default configuration to this module.
 *
 * This is a convenience method that allows us to have the configuration in one place.
 *
 * **Note**: Android plugin must be applied before calling this method.
 */
fun Project.configureAndroidModule() {
    android {
        compileSdkVersion(Versions.Sdk.compile)
        buildToolsVersion(Versions.buildToolsVersion)

        defaultConfig {
            targetSdkVersion(Versions.Sdk.target)
            minSdkVersion(Versions.Sdk.min)

            if (this@android is LibraryExtension) {
                consumerProguardFiles("consumer-rules.pro")
            }
        }

        compileOptions {
            targetCompatibility = JavaVersion.VERSION_1_8
            sourceCompatibility = JavaVersion.VERSION_1_8
        }

        lintOptions {
            isAbortOnError = true
            isCheckTestSources = false
            isCheckGeneratedSources = true
            lintConfig = rootProject.file("lint.xml")
        }
    }
}

/**
 * Apply default configuration for instrumentation testing to this module.
 *
 * This is a convenience method that allows us to have the configuration in one place.
 *
 * **Note**: Android plugin must be applied before calling this method.
 */
fun Project.configureInstrumentationTests() {
    android {
        defaultConfig {
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        sourceSets {
            findByName("test")!!.apply {
                java.srcDir("src/sharedTest/java")
            }
            findByName("androidTest")!!.apply {
                java.srcDir("src/sharedTest/java")
            }
        }

        testOptions {
            animationsDisabled = true
            unitTests.apply {
                isIncludeAndroidResources = true
            }
        }
    }
}
