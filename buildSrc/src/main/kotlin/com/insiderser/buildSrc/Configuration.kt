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

package com.insiderser.buildSrc

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.findByType

/**
 * Add dependency to both `testImplementation` and `androidTestImplementation`.
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
 * Apply default Android configuration to this module.
 *
 * This is a convenience method that allows us to have the configuration in one place.
 *
 * **Note**: an Android plugin must be applied before calling this method.
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

        viewBinding {
            isEnabled = true
        }

        compileOptions {
            targetCompatibility = JavaVersion.VERSION_1_8
            sourceCompatibility = JavaVersion.VERSION_1_8
        }

        lintOptions {
            // TODO: uncomment line below for new projects
            // isWarningsAsErrors = true
            isAbortOnError = true
            isCheckTestSources = false
            isCheckGeneratedSources = true
            isCheckDependencies = true
            lintConfig = rootProject.file("lint.xml")

            textReport = true
            textOutput("stdout")
        }

        testOptions {
            animationsDisabled = true
            unitTests.apply {
                isIncludeAndroidResources = true
            }
        }
    }
}
